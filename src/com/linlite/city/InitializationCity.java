package com.linlite.city;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.linlite.RandomHan;
import com.linlite.model.Army;
import com.linlite.model.City;

public class InitializationCity implements Runnable {
	private Integer numberOf = 2;
	private CountDownLatch beginLatch;
	private CountDownLatch endLatch;
	private Hashtable<Integer, City> cities = new Hashtable<>();
	private Integer width = 1;
	private Integer height = 1;
	volatile boolean keepCheck = true;

	public InitializationCity(Integer forces, CountDownLatch begin, CountDownLatch end, Hashtable<Integer, City> cities,
			Hashtable<Integer, Integer> size) {
		this.numberOf = forces;
		this.beginLatch = begin;
		this.endLatch = end;
		this.cities = cities;
		this.width = size.keys().nextElement();
		this.height = size.values().iterator().next();
	}

	@Override
	public void run() {
		try {

			City temp = new City();
			Integer i = new Random().nextInt(numberOf);
			temp.setBelongTo(i.byteValue());
			temp.setName(String.valueOf(RandomHan.getRandomHan()));
			temp.setDefence(new Random().nextInt(5000) + 1000);
			temp.setGold(new Random().nextInt(10000) + 500);
			Hashtable<String, String> location = new Hashtable<>();
			final Object someObject = new Object();
			synchronized (someObject) {
				double lw = 0d;
				double lh = 0d;
				while (keepCheck) {
					lw = new Random().nextDouble() * width;
					lh = new Random().nextDouble() * height;
					keepCheck = false;
					if ((width - lw) < 20d || lw < 20d || (height - lh) < 20d || lh < 20d) {
						keepCheck = true;
					} else {
						Enumeration<City> ces = cities.elements();
						while (ces.hasMoreElements()) {
							City c = (City) ces.nextElement();
							if (Math.abs(Double.parseDouble(c.getLocation().keys().nextElement()) - lw) <= 15 || Math
									.abs(Double.parseDouble(c.getLocation().values().iterator().next()) - lh) <= 15) {
								keepCheck = true;
								break;
							}
						}

					}
				}
				location.put(String.valueOf(new BigDecimal(lw).setScale(2, RoundingMode.HALF_UP)),
						String.valueOf(new BigDecimal(lh).setScale(2, RoundingMode.HALF_EVEN)));
			}
			temp.setLocation(location);
			List<Army> troops = new ArrayList<>();
			i = new Random().nextInt(3) + 1;
			for (int ii = 0; ii < i; ii++) {
				Army army = new Army();
				army.setClasses((byte) (new Random().nextInt(4) + 1));
				army.setExp(new Random().nextInt(300) + 1);
				army.setNumber(new Random().nextInt(3000) + 1000);
				army.setMorale(new Random().nextInt(80) + 20);
				troops.add(army);
			}
			temp.setTroops(troops);

			synchronized (someObject) {
				int id = cities.keySet().size();
				temp.setId(id);
				cities.put(id, temp);
			}
			beginLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			endLatch.countDown();
		}
	}

}
