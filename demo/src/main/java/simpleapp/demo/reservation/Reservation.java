package simpleapp.demo.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record Reservation (
    @Null
    Long id,
    @NotNull
    Long userId,
    @NotNull
    Long roomId,
    @FutureOrPresent
    @NotNull
    LocalDate startDate,
    @FutureOrPresent
    @NotNull
    LocalDate enDate,
    ReservationStatus status
){

}