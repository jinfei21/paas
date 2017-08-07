package com.yjfei.paas.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.error.YAMLException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * A factory class for loading YAML configuration files, binding them to
 * configuration objects, and validating their constraints. Allows for
 * overriding configuration parameters from system properties.
 *
 * @param <T>
 *            the type of the configuration objects to produce
 */
public class YamlConfigurationFactory<T> {

	private static final Pattern ESCAPED_COMMA_PATTERN = Pattern.compile("\\\\,");
	private static final Splitter ESCAPED_COMMA_SPLITTER = Splitter.on(Pattern.compile("(?<!\\\\),")).trimResults();
	private static final Splitter DOT_SPLITTER = Splitter.on('.').trimResults();

	private final Class<T> klass;
	private final String propertyPrefix;
	private final ObjectMapper mapper;
	private final YAMLFactory yamlFactory;

	/**
	 * Creates a new configuration factory for the given class.
	 *
	 * @param klass
	 *            the configuration class
	 * @param validator
	 *            the validator to use
	 * @param objectMapper
	 *            the Jackson {@link ObjectMapper} to use
	 * @param propertyPrefix
	 *            the system property name prefix used by overrides
	 */
	public YamlConfigurationFactory(Class<T> klass,
			String propertyPrefix) {
		this.klass = klass;
		this.propertyPrefix = (propertyPrefix == null || propertyPrefix.endsWith(".")) ? propertyPrefix
				: (propertyPrefix + '.');
		// Sub-classes may choose to omit data-binding; if so, null ObjectMapper
		// passed:

		this.mapper = new ObjectMapper();
		this.yamlFactory = new YAMLFactory();

	}

	public T build(InputStream input) throws IOException {
		try {
			final JsonNode node = mapper.readTree(yamlFactory.createParser(input));

			if (node == null) {
				throw new RuntimeException("node is empty!");
			}

			return build(node, "default configuration");
		} catch (YAMLException e) {
			throw e;
		}
	}

	public T build() throws IOException {
		try {
			final JsonNode node = mapper.valueToTree(klass.newInstance());
			return build(node, "default configuration");
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(
					"Unable create an instance " + "of the configuration class: '" + klass.getCanonicalName() + "'", e);
		}
	}

	protected T build(JsonNode node, String path) throws IOException {
		for (Map.Entry<Object, Object> pref : System.getProperties().entrySet()) {
			final String prefName = (String) pref.getKey();
			if (prefName.startsWith(propertyPrefix)) {
				final String configName = prefName.substring(propertyPrefix.length());
				addOverride(node, configName, System.getProperty(prefName));
			}
		}

		try {
			final T config = mapper.readValue(new TreeTraversingParser(node), klass);
			return config;
		} catch (UnrecognizedPropertyException e) {
			throw e;
		}
	}

	protected void addOverride(JsonNode root, String name, String value) {
		JsonNode node = root;
		final String[] parts = Iterables.toArray(DOT_SPLITTER.split(name), String.class);
		for (int i = 0; i < parts.length; i++) {
			final String key = parts[i];

			if (!(node instanceof ObjectNode)) {
				throw new IllegalArgumentException("Unable to override " + name + "; it's not a valid path.");
			}
			final ObjectNode obj = (ObjectNode) node;

			final String remainingPath = Joiner.on('.').join(Arrays.copyOfRange(parts, i, parts.length));
			if (obj.has(remainingPath) && !remainingPath.equals(key)) {
				if (obj.get(remainingPath).isValueNode()) {
					obj.put(remainingPath, value);
					return;
				}
			}

			JsonNode child;
			final boolean moreParts = i < parts.length - 1;

			if (key.matches(".+\\[\\d+\\]$")) {
				final int s = key.indexOf('[');
				final int index = Integer.parseInt(key.substring(s + 1, key.length() - 1));
				child = obj.get(key.substring(0, s));
				if (child == null) {
					throw new IllegalArgumentException("Unable to override " + name + "; node with index not found.");
				}
				if (!child.isArray()) {
					throw new IllegalArgumentException(
							"Unable to override " + name + "; node with index is not an array.");
				} else if (index >= child.size()) {
					throw new ArrayIndexOutOfBoundsException(
							"Unable to override " + name + "; index is greater than size of array.");
				}
				if (moreParts) {
					child = child.get(index);
					node = child;
				} else {
					final ArrayNode array = (ArrayNode) child;
					array.set(index, TextNode.valueOf(value));
					return;
				}
			} else if (moreParts) {
				child = obj.get(key);
				if (child == null) {
					child = obj.objectNode();
					obj.set(key, child);
				}
				if (child.isArray()) {
					throw new IllegalArgumentException(
							"Unable to override " + name + "; target is an array but no index specified");
				}
				node = child;
			}

			if (!moreParts) {
				if (node.get(key) != null && node.get(key).isArray()) {
					final ArrayNode arrayNode = (ArrayNode) obj.get(key);
					arrayNode.removeAll();
					for (String val : ESCAPED_COMMA_SPLITTER.split(value)) {
						arrayNode.add(ESCAPED_COMMA_PATTERN.matcher(val).replaceAll(","));
					}
				} else {
					obj.put(key, value);
				}
			}
		}
	}

}
