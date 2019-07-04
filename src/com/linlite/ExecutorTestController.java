package com.linlite;

import java.util.ArrayList;
import java.util.HashSet;
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

public class ExecutorTestController {
//	@Test
	public <T> void submitWithException() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<T>> resultList = new ArrayList<Future<T>>();

		// 创建10个任务并执行
		for (int i = 0; i < 10; i++) {
			// 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
			final int seq = i;
			Future<T> future = executorService.submit(new Callable<T>() {
				@Override
				public T call() throws Exception {
					System.out.println(seq + "\t线程启动");
					Thread.sleep(new Random().nextInt(10) * 100);
					if (new Random().nextBoolean()) {
						return (T) new Exception("线程 " + seq + " 故意失败.");
					}
					return (T) ("线程 " + seq + " 执行完毕");
				}
			});
			// 将任务执行结果存储到List中
			resultList.add(future);
		}
		executorService.shutdown();
		// 遍历任务的结果
		for (Future<T> fs : resultList) {
			try {
				System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				executorService.shutdownNow();
				e.printStackTrace();
				return;
			}
		}
	}

	/**
	 * cacheThreadPool
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	// @Test
	public void cachedPool() throws InterruptedException, ExecutionException {
		ExecutorService exc = Executors.newCachedThreadPool();
		Set<Future<String>> futures = new HashSet<>();
		for (int i = 1; i < 1000; i++) {
			final int seq = i;
			Callable<String> callthread = new Callable<String>() {
				public String call() throws Exception {
					return "thread\t" + seq;
				}
			};
			futures.add(exc.submit(callthread));
		}
		for (Future<String> future : futures) {
			System.out.println(future.get());
		}
		exc.shutdown();
	}

	/**
	 * submit callble方法 callble方法会返回一个指定值 runnable方法为void
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void submitCallble() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> future = executorService.submit(new Callable<String>() {
			public String call() throws Exception {
				System.out.println("Asynchronous Callable");
				return "Callable Result";
			}
		});
		System.out.println("future.get() = " + future.get());

	}

	/**
	 * submit 多线程 线程执行成功后future.get()会返回null
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@SuppressWarnings("unchecked")
	public void submitTest() throws InterruptedException, ExecutionException {
		// 开始的倒数锁
		final CountDownLatch begin = new CountDownLatch(1);

		// 结束的倒数锁
		final CountDownLatch end = new CountDownLatch(10);

		// 十名选手
		final ExecutorService exec = Executors.newFixedThreadPool(10);
		Set<Future<String>> futures = new HashSet<Future<String>>();
		for (int index = 0; index < 10; index++) {
			final int NO = index + 1;
			Runnable run = new Runnable() {
				public void run() {
					try {
						// 如果当前计数为零，则此方法立即返回。
						// 等待
						begin.await();
						Thread.sleep((long) (Math.random() * 1000));
						System.out.println("No." + NO + " arrived");
					} catch (InterruptedException e) {
					} finally {
						// 每个选手到达终点时，end就减一
						end.countDown();
					}
				}
			};
			futures.add((Future<String>) exec.submit(run));
		}
		System.out.println("Game Start");
		// begin减一，开始游戏
		begin.countDown();
		// 等待end变为0，即所有选手到达终点
		end.await();
		exec.shutdown();
		for (Future<String> future : futures) {
			System.out.println(future.get());
		}
		System.out.println("Game Over");

	}

	/**
	 * 多线程并行 execute方法
	 * 
	 * @throws InterruptedException
	 */
	// @Test
	public void executeMutlThreadTest() throws InterruptedException {
		// 开始的倒数锁
		final CountDownLatch begin = new CountDownLatch(1);

		// 结束的倒数锁
		final CountDownLatch end = new CountDownLatch(10);

		// 十名选手
		final ExecutorService exec = Executors.newFixedThreadPool(10);

		for (int index = 0; index < 10; index++) {
			final int NO = index + 1;
			Runnable run = new Runnable() {
				public void run() {
					try {
						// 如果当前计数为零，则此方法立即返回。
						// 等待
						begin.await();
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println("No." + NO + " arrived");
					} catch (InterruptedException e) {
					} finally {
						// 每个选手到达终点时，end就减一
						end.countDown();
					}
				}
			};
			exec.execute(run);
		}
		System.out.println("Game Start");
		// begin减一，开始游戏
		begin.countDown();
		// 等待end变为0，即所有选手到达终点
		end.await();
		System.out.println("Game Over");
		exec.shutdown();

	}

	/**
	 * invokeAny
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void invokeAnyTest() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Set<Callable<String>> callables = new HashSet<Callable<String>>();

		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep((long) new Random().nextInt(1000));
				return "Task 2";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep((long) new Random().nextInt(1000));
				return "Task 1";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep((long) new Random().nextInt(1000));
				return "Task 3";
			}
		});

		String result = executorService.invokeAny(callables);
		System.out.println("result = " + result);
		executorService.shutdown();
	}

	/**
	 * 测试invokeAll
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
//	@Test
	public void invokeAllTest() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Set<Callable<String>> callables = new HashSet<Callable<String>>();
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep((long) new Random().nextInt(1000));
				return "Task 1";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep((long) new Random().nextInt(1000));
				return "Task 2";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep((long) new Random().nextInt(1000));
				return "Task 3";
			}
		});
		List<Future<String>> futures = executorService.invokeAll(callables);
		for (Future<String> future : futures) {
			System.out.println("future.get = " + future.get());
		}
		executorService.shutdown();

	}
}
