package sugaryo.t4jboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
//@EnableScheduling
@SpringBootApplication
public class T4jBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(T4jBootApplication.class, args);
	}

}
