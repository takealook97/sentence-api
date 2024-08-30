package site.udtk.sentenceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SentenceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentenceApiApplication.class, args);
	}

}
