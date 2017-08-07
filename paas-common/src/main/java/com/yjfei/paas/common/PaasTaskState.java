package com.yjfei.paas.common;

import org.apache.mesos.Protos.TaskState;

public enum PaasTaskState {

	TASK_LAUNCHED(0, "launched", false, false), 
	TASK_STAGING(1, "staging", false, false), 
	TASK_STARTING(2, "starting",false, false), 
	TASK_RUNNING(3, "running", false, false), 
	TASK_CLEANING(4, "cleaning", false,false), 
	TASK_KILLING(5, "killing", false, false), 
	TASK_FINISHED(6, "finished", true,false), 
	TASK_FAILED(7, "failed", true, true), 
	TASK_KILLED(8, "killed", true,false), 
	TASK_LOST(9, "lost", true, false), 
	TASK_LOST_WHILE_DOWN(10, "lost", true,false), 
	TASK_ERROR(11, "error", true, false);

	private final int code;
	private final String displayName;
	private final boolean isDone;
	private final boolean isFailed;

	private PaasTaskState(int code, String displayName, boolean isDone, boolean isFailed) {
		this.displayName = displayName;
		this.isDone = isDone;
		this.isFailed = isFailed;
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean isDone() {
		return isDone;
	}

	public boolean isFailed() {
		return isFailed;
	}

	public int getCode() {
		return code;
	}

	public boolean isSuccess() {
		return this == TASK_FINISHED;
	}

	public static PaasTaskState fromTaskState(TaskState taskState) {
		switch (taskState) {
		case TASK_FAILED:
			return TASK_FAILED;
		case TASK_FINISHED:
			return TASK_FINISHED;
		case TASK_KILLED:
			return TASK_KILLED;
		case TASK_STARTING:
			return TASK_STARTING;
		case TASK_STAGING:
			return TASK_STAGING;
		case TASK_LOST:
			return TASK_LOST;
		case TASK_RUNNING:
			return TASK_RUNNING;
		default:
			throw new IllegalStateException(String.format("TaskState: %s not found", taskState));
		}
	}

	public static PaasTaskState getStateByCode(int code) {

		for (PaasTaskState type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return TASK_FINISHED;
	}
}
