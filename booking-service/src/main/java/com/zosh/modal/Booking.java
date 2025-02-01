package com.zosh.modal;

import com.zosh.domain.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long salonId;

    private Long customerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ElementCollection       // will create separate table for service ids
    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING;

    private Integer totalPrice;

}
