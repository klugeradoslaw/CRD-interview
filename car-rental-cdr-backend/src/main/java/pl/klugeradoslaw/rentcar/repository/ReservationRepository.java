package pl.klugeradoslaw.rentcar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.klugeradoslaw.rentcar.model.Reservation;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        SELECT COUNT(r) > 0 FROM Reservation r
        WHERE r.car.id = :carId
          AND r.startDateTime < :endDateTime
          AND r.endDateTime > :startDateTime
    """)
    boolean existsOverlappingReservation(
            @Param("carId") Long carId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
