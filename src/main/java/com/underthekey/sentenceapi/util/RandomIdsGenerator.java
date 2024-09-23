package com.underthekey.sentenceapi.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.underthekey.sentenceapi.domain.type.Threshold;

public class RandomIdsGenerator {
	public static List<Long> generateRandomIds(int count) {
		Set<Long> randomIds = new HashSet<>();

		while (randomIds.size() < count) {
			randomIds.add((long)(Math.random() * Threshold.DATA_SIZE.getNum()) + 1);
		}
		return randomIds.stream().toList();
	}
}
