package com.product.managemet;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductManagementApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
	}

}
