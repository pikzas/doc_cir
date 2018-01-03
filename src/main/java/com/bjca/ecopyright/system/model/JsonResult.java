package com.bjca.ecopyright.system.model;

public class JsonResult {
	private boolean state;
	private String msg;
	private String data;

	public JsonResult() {
	}

	public JsonResult(boolean state, String data,String msg) {
		this.state = state;
		this.data = data;
		this.msg = msg;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		//return "[{\"state\":"+ state + ",\"data\":\"" + data + "\"}]";
		return "{\"state\":"+state+",\"msg\":\""+msg+"\",\"data\":\""+data+"\"}";
	}
}
