package com.zosh.service;

import com.zosh.domain.BookingStatus;
import com.zosh.dto.BookingRequest;
import com.zosh.dto.SalonDTO;
import com.zosh.dto.ServiceDTO;
import com.zosh.dto.UserDTO;
import com.zosh.modal.Booking;
import com.zosh.modal.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest booking,
                          SalonDTO salonDTO,
                          UserDTO userDTO,
                          Set<ServiceDTO> serviceDTOSet) throws Exception;


    List<Booking> getBookingsByCustomer(Long customerId);
    List<Booking> getBookingsBySalon(Long salonId);
    Booking getBookingById(long id) throws Exception;
    Booking updateBooking(long bookingId, BookingStatus status) throws Exception;
    List <Booking> getBookingsByDate(LocalDate date, Long salonId);

    SalonReport getSalonReport(Long salonId);



}
