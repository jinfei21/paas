package com.yjfei.paas.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

public class Resources {
	public static Resources add(Resources a, Resources b) {
		checkNotNull(a, "first argument of Resources.add() is null");
		checkNotNull(b, "second argument of Resources.add() is null");

		return new Resources(a.getCpus() + b.getCpus(), a.getMemoryMb() + b.getMemoryMb(),
				a.getNumPorts() + b.getNumPorts(), a.getDiskMb() + b.getDiskMb());
	}

	public static final Resources EMPTY_RESOURCES = new Resources(0, 0, 0, 0);

	private final double cpus;
	private final double memoryMb;
	private final int numPorts;
	private final double diskMb;
	private final List<String> affinity;

	public Resources(double cpus, double memoryMb, int numPorts) {
		this(cpus, memoryMb, numPorts, 0);
	}

	public Resources(double cpus, double memoryMb, int numPorts, double diskMb) {
	  this(cpus, memoryMb, numPorts, 0,Lists.newArrayList());
  }

	public Resources(double cpus, double memoryMb, int numPorts, double diskMb, List<String> affinity) {
		this.cpus = cpus;
		this.memoryMb = memoryMb;
		this.numPorts = numPorts;
		this.diskMb = diskMb;
		this.affinity = affinity;
	}

	public int getNumPorts() {
		return numPorts;
	}

	public double getCpus() {
		return cpus;
	}

	public double getMemoryMb() {
		return memoryMb;
	}

	public double getDiskMb() {
		return diskMb;
	}
	
	public List<String> getRackAffinity(){
		return this.affinity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Resources resources = (Resources) o;
		return Double.compare(resources.cpus, cpus) == 0 && Double.compare(resources.memoryMb, memoryMb) == 0
				&& numPorts == resources.numPorts && Double.compare(resources.diskMb, diskMb) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpus, memoryMb, numPorts, diskMb);
	}

	@Override
	public String toString() {
		return "Resources{" + "cpus=" + cpus + ", memoryMb=" + memoryMb + ", numPorts=" + numPorts + ", diskMb="
				+ diskMb + '}';
	}
}
