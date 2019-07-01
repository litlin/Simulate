package com.linlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;
import com.linlite.city.CreateCity;
import com.linlite.model.City;

public class MainController {

	@Test
	public void mainTest() throws InterruptedException {

		Integer poolSize = 10;
		Integer Coutries = 6;
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		CountDownLatch beginLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(poolSize);
		Set<Runnable> pool = new HashSet<>();
		for (int i = 0; i < poolSize; i++) {
			CreateCity createCity = new CreateCity(Coutries, beginLatch, endLatch);
			executor.execute(createCity);
			pool.add(createCity);
		}
		beginLatch.countDown();
		endLatch.await();
		Iterator<Runnable> cityIterator = pool.iterator();
		HashMap<Byte, List<City>> cities = new HashMap<Byte, List<City>>();
		for (int i = 0; i < Coutries; i++) {
			cities.put((byte) i, new ArrayList<>());
		}
		while (cityIterator.hasNext()) {
			CreateCity source = (CreateCity) cityIterator.next();
			City city = source.getCity();
			List<City> tempList = cities.get(city.getBelongTo());
			if (tempList.add(city))
				cities.put(city.getBelongTo(), tempList);
			// System.out.println(city.getName() + "\t" + city.getBelongTo() + "\t" +
			// city.getDefence() + "\t"
			// + city.getGold() + "\t" + city.getLocation() + "\t" +
			// city.getTroops().size());
		}

		System.out.println(cities);
	}

}
