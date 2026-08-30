package pl.klugeradoslaw.rentcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class CarRentalCdrApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalCdrApplication.class, args);
    }

}
