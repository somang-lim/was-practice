package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class QueryStringTest {

	// operand1=11
	@Test
	void createTest() {
		QueryString queryString = new QueryString("operand1", "11");

		assertThat(queryString).isNotNull();
	}
}
