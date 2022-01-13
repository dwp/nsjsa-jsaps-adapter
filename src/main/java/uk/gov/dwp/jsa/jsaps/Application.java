package uk.gov.dwp.jsa.jsaps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(value = "uk.gov.dwp.jsa")
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
