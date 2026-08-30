package pl.klugeradoslaw.rentcar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.klugeradoslaw.rentcar.model.Car;
import pl.klugeradoslaw.rentcar.model.CarType;
import pl.klugeradoslaw.rentcar.repository.CarRepository;
import pl.klugeradoslaw.rentcar.service.CarService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    @DisplayName("Should return available cars for valid date range")
    void shouldReturnAvailableCars() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);

        List<Car> availableCars = List.of(
                new Car(1L, CarType.SEDAN, "Toyota", "Camry", 5),
                new Car(2L, CarType.SUV, "Volvo", "XC60", 5)
        );

        when(carRepository.findAvailableCars(start, end)).thenReturn(availableCars);

        // when
        List<Car> result = carService.getAvailableCars(start, end);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(availableCars);
        verify(carRepository).findAvailableCars(start, end);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when end date is before or equal to start date")
    void shouldThrowExceptionWhenEndDateBeforeStartDate() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(() -> carService.getAvailableCars(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("End date must be after start date");

        verifyNoInteractions(carRepository);
    }
}