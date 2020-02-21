package com.lc.dealy.frame.delayclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuchong
 * @version MyClientApplication.java, v 0.1 2020年02月21日 14:26
 */
@SpringBootApplication
@RestController
public class MyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyClientApplication.class, args);
	}


	@RequestMapping("/")
	public String host() {
		return "delayClient run ok";
	}

}
