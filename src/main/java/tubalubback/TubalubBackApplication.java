package tubalubback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@ComponentScan("tubalubback")
public class TubalubBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubalubBackApplication.class, args);
	}

}
