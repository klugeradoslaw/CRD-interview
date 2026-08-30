package pl.klugeradoslaw.rentcar.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.klugeradoslaw.rentcar.model.Car;
import pl.klugeradoslaw.rentcar.model.Reservation;
import pl.klugeradoslaw.rentcar.repository.CarRepository;
import pl.klugeradoslaw.rentcar.repository.ReservationRepository;
import pl.klugeradoslaw.rentcar.dto.CreateReservationRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        LocalDateTime start = request.getStartDateTime();
        LocalDateTime end = request.getEndDateTime();

        if (end.isBefore(start) || end.isEqual(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with id " + request.getCarId() + " not found"));

        boolean isOverlapping = reservationRepository.existsOverlappingReservation(car.getId(), start, end);
        if (isOverlapping) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Car is already reserved for the selected period");
        }

        Reservation reservation = new Reservation(null, car, start, end);
        return reservationRepository.save(reservation);
    }
}
