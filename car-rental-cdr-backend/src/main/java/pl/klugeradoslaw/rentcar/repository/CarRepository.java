package pl.klugeradoslaw.rentcar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.klugeradoslaw.rentcar.model.Car;
import pl.klugeradoslaw.rentcar.model.CarType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByType(CarType type);

    List<Car> findAll();

    @Query("""
                SELECT c FROM Car c
                WHERE c.id NOT IN (
                    SELECT r.car.id FROM Reservation r
                    WHERE r.startDateTime < :endDate
                      AND r.endDateTime > :startDate
                )
            """)
    List<Car> findAvailableCars(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
