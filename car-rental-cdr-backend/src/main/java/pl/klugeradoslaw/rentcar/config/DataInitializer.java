package pl.klugeradoslaw.rentcar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import pl.klugeradoslaw.rentcar.model.Car;
import pl.klugeradoslaw.rentcar.model.CarType;
import pl.klugeradoslaw.rentcar.model.Reservation;
import pl.klugeradoslaw.rentcar.repository.CarRepository;
import pl.klugeradoslaw.rentcar.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {
        if (carRepository.count() > 0) {
            return;
        }
        // cars
        Car sedan1 = new Car(null, CarType.SEDAN, "Toyota", "Camry", 5);
        Car sedan2 = new Car(null, CarType.SEDAN, "Skoda", "Superb", 5);
        Car sedan3 = new Car(null, CarType.SEDAN, "BMW", "Seria 3", 5);
        Car sedan4 = new Car(null, CarType.SEDAN, "Audi", "A4", 5);
        Car sedan5 = new Car(null, CarType.SEDAN, "Volkswagen", "Passat", 5);

        Car suv1 = new Car(null, CarType.SUV, "Volvo", "XC60", 5);
        Car suv2 = new Car(null, CarType.SUV, "Toyota", "RAV4", 5);
        Car suv3 = new Car(null, CarType.SUV, "Kia", "Sportage", 5);

        Car van1 = new Car(null, CarType.VAN, "Mercedes-Benz", "V-Class", 7);
        Car van2 = new Car(null, CarType.VAN, "Volkswagen", "Multivan", 7);

        List<Car> savedCars = carRepository.saveAll(List.of(
                sedan1, sedan2, sedan3, sedan4, sedan5,
                suv1, suv2, suv3,
                van1, van2
        ));


        // reservations
        LocalDateTime now = LocalDateTime.now();

        Reservation resSedan1 = new Reservation(
                null,
                savedCars.get(0),
                now.plusDays(1).withHour(10),
                now.plusDays(3).withHour(18)
        );

        Reservation resSedan2 = new Reservation(
                null,
                savedCars.get(1),
                now.plusDays(2).withHour(12),
                now.plusDays(5).withHour(12)
        );

        Reservation resSuv = new Reservation(
                null,
                savedCars.get(5),
                now.plusDays(1).withHour(9),
                now.plusDays(4).withHour(17)
        );

        Reservation resVan = new Reservation(
                null,
                savedCars.get(8), // Mercedes-Benz V-Class (VAN)
                now.plusDays(3).withHour(8),
                now.plusDays(7).withHour(20)
        );

        reservationRepository.saveAll(List.of(resSedan1, resSedan2, resSuv, resVan));
    }
}