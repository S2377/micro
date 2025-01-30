package com.zosh.controller;

import com.zosh.mapper.SalonMapper;
import com.zosh.modal.Salon;
import com.zosh.payload.dto.SalonDTO;
import com.zosh.payload.dto.UserDTO;
import com.zosh.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/salons")  // base api url
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    // http://localhost:5002/api/salons
    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        // Creating a default user with ID 1
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        // Creating the salon entity
        Salon salon = salonService.createSalon(salonDTO, userDTO);

        // Mapping salon entity to DTO
        SalonDTO responseDTO = SalonMapper.mapToDTO(salon);

        // Returning response
        return ResponseEntity.ok(responseDTO);
    }

    // http://localhost:5002/api/salons/2
    @PatchMapping("/{id}")
    public ResponseEntity<SalonDTO> updateSalon(
            @PathVariable("id") Long salonId,
            @RequestBody SalonDTO salonDTO) throws Exception {

        // Creating a default user with ID 1, later will be provoided by client side JWT token
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        // Updating the salon entity
        Salon salon = salonService.updateSalon(salonDTO, userDTO, salonId);

        // Mapping salon entity to DTO
        SalonDTO responseDTO = SalonMapper.mapToDTO(salon);

        // Returning response
        return ResponseEntity.ok(responseDTO);
    }

    // http://localhost:5002/api/salons
    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalons() throws Exception {
        // Fetching all salons
        List<Salon> salons = salonService.getAllSalons();

        // Mapping salon entities to DTOs using a lambda expression
        List<SalonDTO> salonDTOs = salons.stream().map(salon -> {
            SalonDTO salonDTO = SalonMapper.mapToDTO(salon);
            return salonDTO;
        }).toList();

        // Returning response
        return ResponseEntity.ok(salonDTOs);
    }


    // http://localhost:5002/api/salons/2
    @GetMapping("/{id}")
    public ResponseEntity<SalonDTO> getSalonsById(@PathVariable Long id) throws Exception {
        // Fetching  salon
        Salon salon = salonService.getSalonById(id);
        // Mapping salon entity to DTO
        SalonDTO responseDTO = SalonMapper.mapToDTO(salon);

        // Returning response
        return ResponseEntity.ok(responseDTO);

    }


    // http://localhost:5002/api/salons/owner
    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonsByOwnerId(@PathVariable Long ownerId) throws Exception {

        // Creating a default user with ID 1 later on user will be grabbed throw JWT token
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        // Fetching  salon
        Salon salon = salonService.getSalonByOwnerId(userDTO.getId());

        // Mapping salon entity to DTO
        SalonDTO responseDTO = SalonMapper.mapToDTO(salon);

        // Returning response
        return ResponseEntity.ok(responseDTO);

    }

    // http://localhost:5002/api/salons/search?city=mumbai
    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam String city) throws Exception {
        // Fetching all salons
        List<Salon> salons = salonService.searchSalonByCityName(city);

        // Mapping salon entities to DTOs using a lambda expression
        List<SalonDTO> salonDTOs = salons.stream().map(salon -> {
            SalonDTO salonDTO = SalonMapper.mapToDTO(salon);
            return salonDTO;
        }).toList();

        // Returning response
        return ResponseEntity.ok(salonDTOs);
    }

}
