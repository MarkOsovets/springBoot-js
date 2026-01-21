package simpleapp.demo.reservation;


import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationMapper mapper;

    public ReservationService(
        ReservationRepository repository,
        ReservationMapper mapper
    ){
        this.repository = repository;
        this.mapper = mapper;
    }

    public Reservation getReservationById(
        Long id
    ) 
    {
        ReservationEntity reservationEntity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            "Not found reservation by id = " + id
        ));
        return mapper.toDomainReservation(reservationEntity);
    }

    
    public List<Reservation> searchAllByFilter(
        ReservationSearchFilter filter
    ) {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        log.info("pageable = {}", pageable);

        List<ReservationEntity> allEntities = repository.searchAllByFilter(
            filter.roomId(),
            filter.userId(),
            pageable
        );

        log.info("filtered reservations = {}", allEntities);

        return allEntities.stream().map(mapper::toDomainReservation).toList();
    }


    public Reservation createReservation(Reservation reservationToCreate){
        if (reservationToCreate.id() != null){
            throw new IllegalArgumentException("id should be empty");
        }
        if (reservationToCreate.status() != null){
            throw new IllegalArgumentException("status should be empty");
        }
        var reservationToSave = mapper.toEntity(reservationToCreate);
        
        var createdReservation = repository.save(reservationToSave);
        return mapper.toDomainReservation(createdReservation);
    }   
    
    public Reservation updateReservation(Long id, Reservation reservationToUpdate){
        var reservationEntity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id =" + id));


        var reservationToSave = mapper.toEntity(reservationToUpdate);
        reservationToSave.setId(reservationEntity.getId());
        reservationToSave.setStatus(ReservationStatus.PENDING);
        
        var updatedReservation = repository.save(reservationToSave);
        return mapper.toDomainReservation(updatedReservation);
    }

    @Transactional
    public void cancelReservation(Long id){
        var reservation = repository.getById(id);
        if (!repository.existsById(id)){
            throw new IllegalArgumentException("Not found reservation by id = " + id);
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED){
            throw new IllegalStateException("Cannot cancel the reservation. Reservation was already cancelled");
        }
        
        if (reservation.getStatus() == ReservationStatus.APPROVED){
            throw new IllegalStateException("Cannot cancel the approved reservation. Please contact with manager");
        }
        repository.setStatus(id, ReservationStatus.CANCELLED);
        log.info("Succesfuly cancelled reservation: id={}", id);
    }

    public Reservation approvedReservation(Long id){
        var reservationEntity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id =" + id));
        

        if (reservationEntity.getStatus() != ReservationStatus.PENDING && reservationEntity.getStatus() != null){
            throw new IllegalArgumentException("Cannot approve reservation: status =" + reservationEntity.getStatus());
        }
        
        var isConflict = isConflictReservation(reservationEntity.getRoomId(),
        reservationEntity.getStartDate(),
        reservationEntity.getEnDate()
        ); 

        if (isConflict) {
            throw new IllegalArgumentException("Reservation has a conflict");
        }

        reservationEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(reservationEntity);

        return mapper.toDomainReservation(reservationEntity);
    } 

    public boolean isConflictReservation(
        Long roomId,
        LocalDate startDate,
        LocalDate enDate
    ){
        List <Long> conflictIds = repository.findConflictReservation(
            roomId,
            startDate,
            enDate,
            ReservationStatus.APPROVED
        );
        if (conflictIds.isEmpty()){
            return false;
        }
        log.info("Conflicting with: ids={}", conflictIds);
        return true;

    }
}
