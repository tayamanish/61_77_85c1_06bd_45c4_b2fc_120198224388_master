package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.dto.HotelDto;
import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
    if(id <= 0) {
      throw new BadRequestException("Hotel id must be positive");
    }
    HotelDto hotel = hotelService.getHotelById(id);
    return ResponseEntity.ok(hotel);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) {
    if(id <= 0) {
      throw new BadRequestException("Hotel id must be positive");
    }
    hotelService.deleteHotelById(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/search/{cityId}")
  public ResponseEntity<List<Hotel>> getClosestHotels(@PathVariable Long cityId, @RequestParam(defaultValue = "distance") String sortBy) {
    if(cityId <= 0) {
      throw new BadRequestException("Hotel id must be positive");
    }
    List<Hotel> hotels = hotelService.findTop3ClosestHotels(cityId);
    return ResponseEntity.ok(hotels);
  }
}
