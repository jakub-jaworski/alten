package org.example.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.example.dto.BookingDto;

public class BookingRepository {
	// TODO: implement real persistence (mongo?) and transactions
	private ConcurrentHashMap<LocalDate, List<BookingDto>> repo = new ConcurrentHashMap<>();
	
	public void save(BookingDto booking) {
		repo.putIfAbsent(booking.date(), new ArrayList<>()); // TODO: if no transactions, use concurrent collections
		List<BookingDto> list = repo.get(booking.date());
		// TODO: validate duplicates, validate if max number of simultaneous guests exceeded
		list.add(booking);
	}
	
	public List<BookingDto> getAll(LocalDate date) {
		List<BookingDto> result = repo.get(date);
		return result != null ? result : Collections.emptyList();
	}
}
