package com.linlite;

import java.util.Random;

public class RandomHan {
	private final static int delta = 0x9fa5 - 0x4e00 + 1;

	public final static char getRandomHan() {
		return (char) (0x4e00 + new Random().nextInt(delta));
	}
}
