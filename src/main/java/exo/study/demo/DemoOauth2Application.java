package exo.study.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoOauth2Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(DemoOauth2Application.class, args);
		} catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
	}

}
