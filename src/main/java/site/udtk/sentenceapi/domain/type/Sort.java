package site.udtk.sentenceapi.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sort {
	// 글자 별 분류
	GA1("가1"),
	GA2("가2"),
	NA("나"),
	DA("다"),
	MA("마"),
	BA("바"),
	SA1("사1"),
	SA2("사2"),
	A1("아1"),
	A2("아2"),
	JA1("자1"),
	JA2("자2"),
	CHA("차"),
	KA("카"),
	TA("타"),
	PA("파"),
	HA("하"),

	// 명언
	QUOTE("quote");

	private final String name;
}
