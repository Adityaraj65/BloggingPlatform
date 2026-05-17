package com.inkwell.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class PostServiceApplicationTests {

	@Test
	void applicationClassIsSpringBootApplication() {
		assertThat(PostServiceApplication.class).hasAnnotation(SpringBootApplication.class);
	}

}
