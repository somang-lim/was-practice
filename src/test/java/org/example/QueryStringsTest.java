package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class QueryStringsTest {

	@Test
	void createTest() {
		QueryStrings queryStrings = new QueryStrings("operand1=11&operator=*&operand2=55"); // List<QueryString>

		assertThat(queryStrings).isNotNull();
	}
}
