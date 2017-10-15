package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class ReturnMsgModel implements Serializable {
	private static final long serialVersionUID = 6015538502977379761L;
	private boolean success;
	private String msg;

	public ReturnMsgModel() {
	}

	public String toString() {
		return "MsgModel [success=" + this.success + ", msg=" + this.msg + "]";
	}

	public ReturnMsgModel(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
