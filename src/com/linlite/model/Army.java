package com.linlite.model;

public class Army {
	private byte classes;
	private Integer number;
	private Integer exp;
	private Integer morale;

	public void setClasses(byte classes) {
		this.classes = classes;
	}

	public byte getClasses() {
		return classes;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getNumber() {
		return number;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public Integer getExp() {
		return exp;
	}

	public void setMorale(Integer morale) {
		this.morale = morale;
	}

	public Integer getMorale() {
		return morale;
	}
}
