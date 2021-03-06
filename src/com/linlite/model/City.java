package com.linlite.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class City {
	private Integer id;
	private byte belongTo = 0;
	private String name;
	private Hashtable<String, String> location = new Hashtable<>();
	private Integer gold = 0;
	private Integer defence = 0;
	private List<Army> troops = new ArrayList<>();

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setBelongTo(byte belongTo) {
		this.belongTo = belongTo;
	}

	public byte getBelongTo() {
		return belongTo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLocation(Hashtable<String, String> location) {
		this.location = location;
	}

	public Hashtable<String, String> getLocation() {
		return location;
	}

	public void setGold(int i) {
		this.gold = i;
	}

	public Integer getGold() {
		return gold;
	}

	public void setDefence(Integer defence) {
		this.defence = defence;
	}

	public Integer getDefence() {
		return defence;
	}

	public void setTroops(List<Army> troops) {
		this.troops = troops;
	}

	public List<Army> getTroops() {
		return troops;
	}
}
