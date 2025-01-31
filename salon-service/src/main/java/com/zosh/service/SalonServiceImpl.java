package com.zosh.service;

import com.zosh.modal.Salon;
import com.zosh.payload.dto.SalonDTO;
import com.zosh.payload.dto.UserDTO;
import com.zosh.repository.SalonRepository;
import com.zosh.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/////////////////////////////////////// Business logic //////////////////////////////////////

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    // creating the instance of salonRepository;
    private final SalonRepository salonRepository;

        @Override
        public Salon createSalon(SalonDTO req, UserDTO user) {
            Salon salon = new Salon();
            salon.setName(req.getName());
            salon.setAddress(req.getAddress());
            salon.setEmail(req.getEmail());
            salon.setCity(req.getCity());
            salon.setImages(req.getImages());
            salon.setOwnerId(user.getId());
            salon.setOpenTime(req.getOpenTime());
            salon.setCloseTime(req.getCloseTime());
            salon.setPhoneNumber(req.getPhoneNumber());
            // Save and return the persisted entity
            Salon savedSalon = salonRepository.save(salon);
            return savedSalon;
        }

    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);

        if(existingSalon != null){
            existingSalon.setCity(salon.getCity());
            existingSalon.setName(salon.getName());
            existingSalon.setAddress(salon.getAddress());
            existingSalon.setEmail(salon.getEmail());
            existingSalon.setImages(salon.getImages());
            existingSalon.setOpenTime(salon.getOpenTime());
            existingSalon.setCloseTime(salon.getCloseTime());
            existingSalon.setOwnerId(user.getId());
            existingSalon.setPhoneNumber(salon.getPhoneNumber());
            return salonRepository.save(existingSalon);
        }
        throw new Exception("salon not exist");
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
        Salon salon =  salonRepository.findById(salonId).orElse(null);

        if(salon == null){
            throw new Exception("salon not exist");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        Salon salon = salonRepository.findByOwnerId(ownerId);
        return salon;

    }

    @Override
    public List<Salon> searchSalonByCityName(String city) {
        return salonRepository.searchSalons(city);
    }
}
