package com.linlite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import com.linlite.city.CreateCity;
import com.linlite.city.InitializationCity;
import com.linlite.model.City;

public class MainController {

	// @Test
	public void mainTest() throws InterruptedException {

		Integer poolSize = 10;
		Integer Coutries = 6;
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		CountDownLatch beginLatch;
		CountDownLatch endLatch;
		Hashtable<Integer, City> cities = new Hashtable<>();
		Hashtable<Integer, Integer> mapSize = new Hashtable<Integer, Integer>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put(1000, 800);
			}
		};
		do {
			cities.clear();
			beginLatch = new CountDownLatch(1);
			endLatch = new CountDownLatch(poolSize);
			for (int i = 0; i < poolSize; i++) {
				// InitializationCity initCity =new InitializationCity(Coutries, beginLatch,
				// endLatch, cities, mapSize) ;
				executor.execute(new InitializationCity(Coutries, beginLatch, endLatch, cities, mapSize));
				// executor.submit(initCity);
			}
			beginLatch.countDown();// 开始latch计数归零，所有线程开始工作
			endLatch.await();// 等待全部子线程执行完毕
		} while (cities.size() != poolSize);

		for (int i = 0; i < cities.size(); i++) {
			City city = cities.get(i);
			System.out
					.println(city.getId() + "\t" + city.getName() + "\t" + city.getBelongTo() + "\t" + city.getDefence()
							+ "\t" + city.getGold() + "\t" + city.getLocation() + "\t" + city.getTroops().size());
		}

	}

	public void old() throws InterruptedException {
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
			System.out.println(city.getName() + "\t" + city.getBelongTo() + "\t" + city.getDefence() + "\t"
					+ city.getGold() + "\t" + city.getLocation() + "\t" + city.getTroops().size());
		}
	}

	// @Test
	public void testAccuracy() {
		BigDecimal decimal = new BigDecimal(new Random().nextDouble() * 1000);
		decimal = decimal.setScale(2, RoundingMode.HALF_UP);
		System.out.println(decimal.doubleValue());
	}
}
