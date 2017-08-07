package com.yjfei.paas.configuration;

import lombok.Data;

@Data
public class UIConfiguration {

	private String title = "PPD-PaaS";

	private String baseUrl;

	private String runningTaskLogPath = "stdout";

	private String finishedTaskLogPath = "stdout";

}
