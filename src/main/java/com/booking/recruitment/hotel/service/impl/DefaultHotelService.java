package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.dto.CityDto;
import com.booking.recruitment.hotel.dto.HotelDto;
import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.exception.ElementNotFoundException;
import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;

    @Autowired
    DefaultHotelService(HotelRepository hotelRepository, CityRepository cityRepository) {
        this.hotelRepository = hotelRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> getHotelsByCity(Long cityId) {
        return hotelRepository.findAll().stream()
                .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Hotel createNewHotel(Hotel hotel) {
        if (hotel.getId() != null) {
            throw new BadRequestException("The ID must not be provided when creating a new Hotel");
        }

        return hotelRepository.save(hotel);
    }

    public HotelDto getHotelById(Long id) {
        return hotelRepository.findById(id).map(this::mapToDTO).orElseThrow(() -> new ElementNotFoundException("Hotel not found with id : " + id));
    }

    private HotelDto mapToDTO(Hotel hotel) {
        City city = hotel.getCity();
        CityDto cityDto = new CityDto(city.getId(), city.getName(), city.getCityCentreLatitude(), city.getCityCentreLongitude());
        return new HotelDto(hotel.getId(), hotel.getName(), hotel.getRating(), hotel.getLatitude(), hotel.getLongitude(), hotel.getAddress(), cityDto);
    }

    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()-> new ElementNotFoundException("Hotel with id " + id + " not found"));
        hotel.setDeleted(true);
        hotelRepository.save(hotel);
    }

    public List<Hotel> findTop3ClosestHotels(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(()-> new ElementNotFoundException("City with id " + cityId + " not found"));
        return hotelRepository.findByCityIdAndDeletedFalse(cityId).stream()
                .sorted(Comparator.comparingDouble(hotel -> findTop(
                        city.getCityCentreLatitude(),
                        city.getCityCentreLongitude(),
                        hotel.getLatitude(),
                        hotel.getLongitude()
                ))).limit(3).toList();
    }

    private double findTop(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians((lat2 -lat1));
        double dLon = Math.toRadians((lon2 -lon1));
        double ap = Math.sin(dLat/2)* Math.sin(dLat/2) +
                 Math.cos(Math.toRadians(lat1))* Math.cos(Math.toRadians(lat2)) *
                         Math.sin(dLon/2)* Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(ap), Math.sqrt(1-ap));
        return R*c;
    }
}
