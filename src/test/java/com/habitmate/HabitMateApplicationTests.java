package com.habitmate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HabitMateApplicationTests {

	@Test
	@DisplayName("스프링 부트 애플리케이션이 정상적으로 실행된다")
	void contextLoads() {
		assertThatCode(() -> HabitMateApplication.main(new String[]{}))
				.doesNotThrowAnyException();
	}


}

