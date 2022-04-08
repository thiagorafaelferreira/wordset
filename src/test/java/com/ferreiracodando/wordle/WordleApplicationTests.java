package com.ferreiracodando.wordle;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;


class WordleApplicationTests {

	@Test
	void contextLoads() {
		String nome = "thiago";

		nome = StringUtils.rotate(nome, 2);

		System.out.println(nome);
	}

}
