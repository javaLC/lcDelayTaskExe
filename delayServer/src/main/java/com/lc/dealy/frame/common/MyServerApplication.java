package com.lc.dealy.frame.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuchong
 * @version MyServerApplication.java, v 0.1 2020年02月21日 14:26
 */
@SpringBootApplication
@RestController
public class MyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyServerApplication.class, args);
	}


	@RequestMapping("/")
	public String host() {
		return "delayServer run ok";
	}

}
