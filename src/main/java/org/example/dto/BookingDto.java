package org.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;

public record BookingDto(String name, Integer size, LocalDate date, LocalTime time) {
	public BookingDto {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Illegal name: " + name);
		}
		
		if (size == null || size < 1) {
			throw new IllegalArgumentException("Illegal size: " + size);
		}
		
		// TODO: validate how many days ahead of now booking can be made
		if (date == null || date.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Illegal date: " + date);
		}
		
		// TODO: validate opening hours
		if (time == null) {
			throw new IllegalArgumentException("Illegal time: " + time);
		}
	}
	
}
