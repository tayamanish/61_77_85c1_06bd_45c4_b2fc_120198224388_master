package com.booking.recruitment.hotel.dto;

import com.booking.recruitment.hotel.model.City;

public record HotelDto(Long id,
                      String name,
                      Double rating,
                      Double latitude,
                      Double longitude, String address, CityDto city) {
}
