package hello;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "helo")
@EnableProcessApplication("app-bpm")
public class FirstApp {
    public static void main(String[] args) {
        SpringApplication.run(FirstApp.class, args);
    }
}