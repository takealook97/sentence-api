package site.udtk.sentenceapi.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class RandomIdsGenerator {
	private static final int SIZE = 4758;

	public static List<Long> generateRandomIds(int count) {
		Set<Long> randomIds = new HashSet<>();

		while (randomIds.size() < count) {
			randomIds.add((long)((Math.random() * SIZE) + 1));
		}
		return randomIds.stream().toList();
	}
}
