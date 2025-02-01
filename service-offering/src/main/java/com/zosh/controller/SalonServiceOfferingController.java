package com.zosh.controller;

import com.zosh.dto.CategoryDTO;
import com.zosh.dto.SalonDTO;
import com.zosh.dto.ServiceDTO;
import com.zosh.modal.ServiceOffering;
import com.zosh.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service/salon-owner")
public class SalonServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

//    @PostMapping("/salon/{salonId}/category/{categoryId}")
    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO) {

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        CategoryDTO categoryDTO = new CategoryDTO();
        System.out.println("value printed"+serviceDTO.getCategory());
        categoryDTO.setId(serviceDTO.getCategory());

        ServiceOffering createdService = serviceOfferingService.createService(
                salonDTO,
                serviceDTO,
                categoryDTO
        );
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> updateService(
            @PathVariable Long serviceId,
            @RequestBody ServiceOffering service) throws Exception {

        ServiceOffering updatedService = serviceOfferingService.updateService(serviceId, service);
        return ResponseEntity.ok(updatedService);
    }
}
