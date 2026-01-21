package simpleapp.demo.reservation;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;//создаем ссылку на объект
    private static final Logger log = LoggerFactory.getLogger(ReservationController.class); 

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservation(
        @RequestParam(name = "roomId", required = false) Long roomId,
        @RequestParam(name = "userId", required = false) Long userId,
        @RequestParam("pageSize") Integer pageSize,
        @RequestParam("pageNumber") Integer pageNumber
    ) {
        log.info("Called getAllReservations");
        var filter = new ReservationSearchFilter(
            roomId,
            userId,
            pageSize,
            pageNumber
        );
        log.info("data filter = {}", filter);
        return ResponseEntity.ok(reservationService.searchAllByFilter(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
        @PathVariable("id") Long id
    ) {
        log.info("Called getReservationById");
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
        @RequestBody @Valid Reservation reservationToCreate
    ){
        if (!reservationToCreate.enDate().isAfter(reservationToCreate.startDate())){
            throw new IllegalArgumentException("Start date should be earlier than endDate");
        }
        log.info("Called createReservation");
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
        @PathVariable Long id,
        @RequestBody @Valid Reservation reservationToUpdate
    ){
        if (!reservationToUpdate.enDate().isAfter(reservationToUpdate.startDate())){
            throw new IllegalArgumentException("Start date should be earlier than endDate");
        }
        log.info("Called updateReservation id={}, reservationToUpdate={}",id, reservationToUpdate);
        var updateReservation = reservationService.updateReservation(id, reservationToUpdate);
        return ResponseEntity.ok(updateReservation);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteReservation(
        @PathVariable("id") Long id
    ){
        log.info("Called deleteReservation id={}", id);
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();//без build не скомпилируется, потому что ok() возвращает BodyBuilder
        
    }

    @PostMapping("/approved/{id}")
    public ResponseEntity<Reservation> approvedReservation(
        @PathVariable("id") Long id
    ) {
        log.info("Called approvedReservation id={}",id);
        reservationService.approvedReservation(id);
        return ResponseEntity.status(200).build();
    }
}

