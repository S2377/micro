package com.zosh.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zosh.domain.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BookingDTO {

    private Long id;

    private Long salonId;  // here we will send actual Salon object when we will integrate feign client

    private Long customerId; // here we will send actual Customer object when we will integrate feign client

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds;  // here we will send actual Service object when we will integrate feign client

    private BookingStatus status = BookingStatus.PENDING;

    private Integer totalPrice;

}
