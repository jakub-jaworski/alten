package org.example.resource;

import static org.example.Constants.PATH;

import java.time.LocalDate;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.example.dto.BookingDto;
import org.example.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path(PATH)
public class BookingResource {
	private static final Logger log = LoggerFactory.getLogger(BookingResource.class);
	
	private BookingRepository repository = new BookingRepository();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BookingDto post(BookingDto booking) {
		log.info("post " + booking);
		if (booking == null) {
			throw new IllegalArgumentException("Illegal booking");
		}
		repository.save(booking);
		return booking; // TODO: status '201 created'
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<BookingDto> getAll(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("Illegal date");
		}
		List<BookingDto> result = repository.getAll(date);
		log.info("getAll, size = " + result.size());
		return result;
	}
}
