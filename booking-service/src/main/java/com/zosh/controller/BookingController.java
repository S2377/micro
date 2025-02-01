package com.zosh.controller;

import com.zosh.domain.BookingStatus;
import com.zosh.dto.*;
import com.zosh.mapper.BookingMapper;
import com.zosh.modal.Booking;
import com.zosh.modal.SalonReport;
import com.zosh.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody BookingRequest request,
            @RequestParam(required = false) Long salonId) throws Exception {

        // for the time being it is static,but later on using JWT
        UserDTO user = new UserDTO();
        user.setId(1L);

        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        // here we will call service offering  microservice using OpenFeign
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setName("hair cut for men");
        serviceDTO.setPrice(50);
        serviceDTO.setDuration(5);


        serviceDTOSet.add(serviceDTO);

        // here we will call salon microservice using OpenFeign
        SalonDTO salon = new SalonDTO();
        salon.setId(1L);
        salon.setOpenTime(LocalTime.now());
        salon.setCloseTime(LocalTime.now().plusHours(12));

        Booking booking = bookingService.createBooking(
                request,
                salon,
                user,
                serviceDTOSet);

        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }


    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getCustomerBookings() {
        List<Booking> bookings = bookingService.getBookingsByCustomer(1L);
        return ResponseEntity.ok(getBookingDTOs(bookings));
    }


    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getSalonBookings() {
        List<Booking> bookings = bookingService.getBookingsBySalon(1L);
        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    // will work for rightNow ,but when we integrate oprn feign client then we need to create for user,service,salon dto
    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toSet());
    }


//    @GetMapping("/salon/{salonId}")
//    public ResponseEntity<List<Booking>> getSalonBookings(
//            @PathVariable Long salonId) {
//        return ResponseEntity.ok(bookingService.getBookingsBySalon(salonId));
//    }
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable Long bookingId) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(BookingMapper.toDTO(booking));
    }


//    @PutMapping("/{bookingId}/status")
//    public ResponseEntity<Booking> updateBookingStatus(
//            @PathVariable Long bookingId,
//            @RequestParam BookingStatus status) throws Exception {
//        return ResponseEntity.ok(bookingService.updateBooking(bookingId, status));
//    }
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingById(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status) throws Exception {

        Booking booking = bookingService.updateBooking(bookingId,status);
        return ResponseEntity.ok(BookingMapper.toDTO(booking));
    }


//    @GetMapping("/salon/{salonId}/date")
//    public ResponseEntity<List<Booking>> getBookingsByDate(
//            @PathVariable Long salonId,
//            @RequestParam(required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        return ResponseEntity.ok(bookingService.getBookingsByDate(date, salonId));
//    }
    @GetMapping("/slots/slon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedslot(
            @PathVariable Long salonId,
            @RequestParam LocalDate date) throws Exception {
        List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);

        List<BookingSlotDTO> slotDTOS = bookings.stream()
                .map(booking -> {
                    BookingSlotDTO bookingSlotDTO = new BookingSlotDTO();
                    bookingSlotDTO.setStartTime(booking.getStartTime());
                    bookingSlotDTO.setEndTime(booking.getEndTime());
                    return  bookingSlotDTO;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(slotDTOS);
    }

//    @GetMapping("/salon/{salonId}/report")
//    public ResponseEntity<SalonReport> getSalonReport(
//            @PathVariable Long salonId) {
//        return ResponseEntity.ok(bookingService.getSalonReport(salonId));
//    }
    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(
            @PathVariable Long salonId,
            @RequestParam(required = false) LocalDate date) throws Exception {
        SalonReport report = bookingService.getSalonReport(1L);

        return ResponseEntity.ok(report);
    }

}