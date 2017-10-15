package com.ailk.biapp.ci.task;

public abstract class AbstractJob {
	private String dataTime = null;

	public AbstractJob() {
	}

	public String getDataTime() {
		return this.dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public abstract void work();
}
