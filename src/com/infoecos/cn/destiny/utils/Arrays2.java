package com.infoecos.cn.destiny.utils;

public class Arrays2 {
	public static float[] copyOf(float[] original, int newLength) {
		float[] copy = new float[newLength];
		System.arraycopy(original, 0, copy, 0,
				Math.min(original.length, newLength));
		return copy;
	}
}
