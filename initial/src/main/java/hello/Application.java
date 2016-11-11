package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class Application {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}
	
	@Scheduled(fixedRate = 1000)
	public void getQuote (){
		Quote quote = restTemplate.getForObject(
				"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		log.info(quote.toString());
	}
}