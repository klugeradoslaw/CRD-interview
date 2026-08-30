package pl.klugeradoslaw.rentcar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.klugeradoslaw.rentcar.dto.CreateReservationRequest;
import pl.klugeradoslaw.rentcar.model.Car;
import pl.klugeradoslaw.rentcar.model.CarType;
import pl.klugeradoslaw.rentcar.model.Reservation;
import pl.klugeradoslaw.rentcar.repository.CarRepository;
import pl.klugeradoslaw.rentcar.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Car sampleCar;

    @BeforeEach
    void setUp() {
        sampleCar = new Car(1L, CarType.SEDAN, "Toyota", "Camry", 5);
    }

    @Test
    @DisplayName("Should successfully create a reservation when car is available and dates are valid")
    void shouldCreateReservationSuccessfully() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        CreateReservationRequest request = new CreateReservationRequest(1L, start, end);

        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));
        when(reservationRepository.existsOverlappingReservation(1L, start, end)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> {
                    Reservation r = invocation.getArgument(0);
                    r.setId(100L);
                    return r;
                });

        // when
        Reservation result = reservationService.createReservation(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getCar()).isEqualTo(sampleCar);
        assertThat(result.getStartDateTime()).isEqualTo(start);
        assertThat(result.getEndDateTime()).isEqualTo(end);

        verify(carRepository).findById(1L);
        verify(reservationRepository).existsOverlappingReservation(1L, start, end);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw 400 Bad Request when end date is before start date")
    void shouldThrowBadRequestWhenEndDateIsBeforeStartDate() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        CreateReservationRequest request = new CreateReservationRequest(1L, start, end);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseStatusEx = (ResponseStatusException) ex;
                    assertThat(responseStatusEx.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                });

        verifyNoInteractions(carRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should throw 404 Not Found when car does not exist")
    void shouldThrowNotFoundWhenCarDoesNotExist() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        CreateReservationRequest request = new CreateReservationRequest(999L, start, end);

        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseStatusEx = (ResponseStatusException) ex;
                    assertThat(responseStatusEx.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });

        verify(carRepository).findById(999L);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw 409 Conflict when car is already reserved for given timeframe")
    void shouldThrowConflictWhenCarAlreadyReserved() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        CreateReservationRequest request = new CreateReservationRequest(1L, start, end);

        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));
        when(reservationRepository.existsOverlappingReservation(1L, start, end)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseStatusEx = (ResponseStatusException) ex;
                    assertThat(responseStatusEx.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                });

        verify(carRepository).findById(1L);
        verify(reservationRepository).existsOverlappingReservation(1L, start, end);
        verify(reservationRepository, never()).save(any());
    }
}