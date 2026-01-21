package simpleapp.demo.reservation;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="reservations")
@Entity
public class ReservationEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="userId", nullable = false)
    private Long userId;

    @Column(name="roomId",nullable = false)
    private Long roomId;

    @Column(name="startDate",nullable = false)
    private LocalDate startDate;

    @Column(name="enDate", nullable = false)
    private LocalDate enDate;  // Оставляем как было - enDate
    
    @Enumerated(EnumType.STRING)
    @Column(name="ReservationStatus")
    private ReservationStatus status;

    // Конструктор по умолчанию (без параметров)
    public ReservationEntity() {
    }

    // Конструктор со всеми полями
    public ReservationEntity(Long id, Long userId, Long roomId, LocalDate startDate, 
                           LocalDate enDate, ReservationStatus status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.enDate = enDate;
        this.status = status;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEnDate() {
        return enDate;
    }

    public void setEnDate(LocalDate enDate) {
        this.enDate = enDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}