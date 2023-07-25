package org.example.repository;

import static org.example.Constants.BOOKING_HOURS;
import static org.example.Constants.MAX_SEATS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.example.dto.BookingDto;

public class BookingRepository {
	// TODO: implement real persistence (mongo?)
	private ConcurrentHashMap<LocalDate, List<BookingDto>> repo = new ConcurrentHashMap<>();
	
	public void save(BookingDto booking) {
		repo.putIfAbsent(booking.date(), new ArrayList<>());
		List<BookingDto> list = repo.get(booking.date());
		synchronized (list) {
			Integer bookedSeats = list.stream().filter(b ->
					(b.time().plusHours(BOOKING_HOURS).compareTo(booking.time()) >= 0 && b.time().compareTo(booking.time()) <= 0)
							|| (b.time().compareTo(booking.time().plusHours(BOOKING_HOURS)) <= 0 && b.time().compareTo(booking.time()) >= 0))
					.map(b -> b.size())
					.reduce(0, Integer::sum);
			if (bookedSeats + booking.size() > MAX_SEATS) {
				throw new IllegalArgumentException("Illegal time");
			}
			list.add(booking);
		}
	}
	
	public List<BookingDto> getAll(LocalDate date) {
		List<BookingDto> result = repo.get(date);
		return result != null ? result : Collections.emptyList();
	}
}
