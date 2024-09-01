package site.udtk.sentenceapi.util;

import static site.udtk.sentenceapi.domain.type.Threshold.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class RandomIdsGenerator {
	public static List<Long> generateRandomIds(int count) {
		Set<Long> randomIds = new HashSet<>();

		while (randomIds.size() < count) {
			randomIds.add((long)(Math.random() * DATA_SIZE.getNum()) + 1);
		}
		return randomIds.stream().toList();
	}
}
