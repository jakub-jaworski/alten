package org.example;

import static org.example.Constants.PATH;
import static org.example.Constants.PORT;

import javax.ws.rs.core.Response;

import org.example.resource.BookingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import io.muserver.rest.RestHandlerBuilder;

public class BookingApplication {
	private static final Logger log = LoggerFactory.getLogger(BookingApplication.class);
	
	public void run() {
		ObjectMapper mapper = JsonMapper.builder()
				.findAndAddModules()
				.build();
		BookingResource res = new BookingResource();
		MuServer server = MuServerBuilder.httpServer()
				.withHttpPort(PORT)
				.addHandler(RestHandlerBuilder.restHandler(res)
						.addExceptionMapper(ValueInstantiationException.class,
								e -> Response.status(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage())
										.build())
						.addExceptionMapper(IllegalArgumentException.class,
								e -> Response.status(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage())
										.build())
						.addCustomWriter(new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS))
						.addCustomReader(new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS)))
				.start();
		log.info("Endpoint: " + server.uri()
				.resolve(PATH));
	}
}
