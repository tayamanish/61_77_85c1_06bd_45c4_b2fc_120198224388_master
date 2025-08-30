package com.booking.recruitment.hotel.service;

import com.booking.recruitment.hotel.dto.HotelDto;
import com.booking.recruitment.hotel.model.Hotel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface HotelService {
  List<Hotel> getAllHotels();

  List<Hotel> getHotelsByCity(Long cityId);

  Hotel createNewHotel(Hotel hotel);

  HotelDto getHotelById(Long id);

  void deleteHotelById(Long id);

  List<Hotel> findTop3ClosestHotels(Long cityId);
}
