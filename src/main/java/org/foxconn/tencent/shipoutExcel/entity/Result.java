package org.foxconn.tencent.shipoutExcel.entity;

import java.util.List;

public class Result {
	String skuno;
	List<SystemModel> system;
	
	
	public String getSkuno() {
		return skuno;
	}

	public void setSkuno(String skuno) {
		this.skuno = skuno;
	}

	public List<SystemModel> getSystem() {
		return system;
	}

	public void setSystem(List<SystemModel> system) {
		this.system = system;
	}

	
}
