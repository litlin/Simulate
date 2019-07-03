package com.linlite.city;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.linlite.RandomHan;
import com.linlite.model.Army;
import com.linlite.model.City;

public class CreateCity implements Runnable {
	private Integer numberOf = 2;
	private CountDownLatch beginLatch;
	private CountDownLatch endLatch;
	private City city;

	public CreateCity(Integer i, CountDownLatch begin, CountDownLatch end) {
		this.numberOf = i;
		this.beginLatch = begin;
		this.endLatch = end;
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
			location.put(new BigDecimal(new Random().nextDouble() * 600).setScale(2, RoundingMode.HALF_UP).toString(),
					new BigDecimal(new Random().nextDouble() * 1000).setScale(2, RoundingMode.HALF_EVEN).toString());
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
			this.city = temp;
			beginLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			endLatch.countDown();
		}
	}

	public City getCity() {
		return city;
	}
}
