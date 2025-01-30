package com.zosh.mapper;

import com.zosh.modal.Salon;
import com.zosh.payload.dto.SalonDTO;

public class SalonMapper {

    public static SalonDTO mapToDTO(Salon salon){
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setCity(salon.getCity());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setImages(salon.getImages());
        salonDTO.setName(salon.getName());
        salonDTO.setCloseTime(salon.getCloseTime());
        salonDTO.setOpenTime(salon.getOpenTime());
        salonDTO.setOwnerId(salon.getOwnerId());

        return salonDTO;

    }
}
