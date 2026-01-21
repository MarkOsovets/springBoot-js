package simpleapp.demo.reservation;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Modifying
    @Query("""
            update ReservationEntity r
            set r.status=:status
            where r.id= :id
            """)
    void setStatus(
        @Param("id") Long id,
        @Param("status") ReservationStatus reservationStatus
    );


    @Query("""
        SELECT r.id from ReservationEntity r
            WHERE r.roomId = :roomId
            AND :startDate < r.enDate
            AND r.startDate < :enDate
            AND r.status = :status
            """)
    List<Long> findConflictReservation(
        @Param("roomId") Long roomId,
        @Param("startDate") LocalDate startDate,
        @Param("enDate") LocalDate enDate,
        @Param("status") ReservationStatus status
    );


    @Query("""
        SELECT r from ReservationEntity r
            WHERE (:roomId IS NULL OR r.roomId = :roomId)
            AND (:userId IS NULL OR r.userId = :userId)
            """)
    List<ReservationEntity> searchAllByFilter(
        @Param("roomId") Long roomId,
        @Param("userId") Long userId,
        Pageable pageable
    );
}
