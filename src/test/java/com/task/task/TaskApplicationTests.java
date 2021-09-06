package com.task.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Default spring boot test case")
class TaskApplicationTests {

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	@DisplayName("Test 1+1")
	void addition(){
		assertEquals(2, 1 + 1);
	}

	@Test
	@DisplayName("Windows only")
	@EnabledOnOs({OS.WINDOWS})
	@Order(3)
	void onWindows(){}

	@Test
	@DisplayName("On Java8")
	@EnabledOnJre(JRE.JAVA_8)
	@Order(5)
	void onJava8(){}

	@Test
	@DisplayName("On Java11")
	@EnabledOnJre(JRE.JAVA_11)
	@Order(4)
	void onJava11(){}

}
