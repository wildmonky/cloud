package org.lizhao.authority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lizhao
 */
@SpringBootApplication(scanBasePackages = "org.lizhao.authority")
public class AuthorityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorityApplication.class, args);
	}

}
