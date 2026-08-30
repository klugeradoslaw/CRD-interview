package pl.klugeradoslaw.rentcar.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.klugeradoslaw.rentcar.model.Car;
import pl.klugeradoslaw.rentcar.repository.CarRepository;
import pl.klugeradoslaw.rentcar.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }

        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        return carRepository.findAvailableCars(startDate, endDate);
    }


}