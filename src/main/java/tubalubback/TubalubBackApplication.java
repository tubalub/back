package tubalubback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("tubalubback")
public class TubalubBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubalubBackApplication.class, args);
	}

}
