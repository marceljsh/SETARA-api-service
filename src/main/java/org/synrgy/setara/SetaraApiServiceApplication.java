package org.synrgy.setara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SetaraApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SetaraApiServiceApplication.class, args);
	}

}
