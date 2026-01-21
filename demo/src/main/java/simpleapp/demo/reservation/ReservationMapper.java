package simpleapp.demo.reservation;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation toDomainReservation(
    ReservationEntity reservation
    ){
        return new Reservation(
            reservation.getId(),
            reservation.getUserId(),
            reservation.getRoomId(),
            reservation.getStartDate(),
            reservation.getEnDate(),
            reservation.getStatus()
        );
    }

    public ReservationEntity toEntity(
    Reservation reservation
    ){
        return new ReservationEntity(
            reservation.id(),
            reservation.userId(),
            reservation.roomId(),
            reservation.startDate(),
            reservation.enDate(),
            reservation.status()
        );
    }


}
