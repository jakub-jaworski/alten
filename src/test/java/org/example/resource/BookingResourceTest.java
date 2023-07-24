package org.example.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNSUPPORTED_MEDIA_TYPE;
import static org.example.Constants.PATH;
import static org.example.Constants.PORT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import org.example.dto.BookingDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@TestMethodOrder(MethodOrderer.MethodName.class)
class BookingResourceTest {
	private static ObjectMapper mapper = JsonMapper.builder()
			.findAndAddModules()
			.build();
	
	private static LocalDate now = LocalDate.now(); // TODO: what if tests are ran at midnight?
	
	private static LocalDate nowPlusMonth = now.plusMonths(1);
	
	private static String nowPlusMonthString = asString(nowPlusMonth);
	
	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = PORT;
	}
	
	@Test
	public void test10_getNoContentType() {
		RestAssured.given()
				.body(nowPlusMonthString)
				.get(PATH)
				.then()
				.statusCode(UNSUPPORTED_MEDIA_TYPE.getStatusCode());
	}
	
	@Test
	public void test11_getNoBody() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.get(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test15_getIsEmpty() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body(nowPlusMonthString)
				.get(PATH)
				.then()
				.statusCode(OK.getStatusCode())
				.body("", equalTo(Collections.emptyList()));
	}
	
	@Test
	public void test20_postNoContentType() {
		RestAssured.given()
				.body("{ \"name\": \"Adam\", \"size\": 7, \"date\": " + nowPlusMonthString + ", \"time\": " + asString(LocalTime.now())
						+ "}")
				.post(PATH)
				.then()
				.statusCode(UNSUPPORTED_MEDIA_TYPE.getStatusCode());
	}
	
	@Test
	public void test21_postNoBody() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.post(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test22_postBadBodyNoName() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{ \"size\": 7, \"date\": " + nowPlusMonthString + ", \"time\": " + asString(LocalTime.now()) + "}")
				.post(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test23_postBadBodyNoSize() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{ \"name\": \"Adam\", \"date\": " + nowPlusMonthString + ", \"time\": " + asString(LocalTime.now()) + "}")
				.post(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test24_postBadBodyNoDate() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{ \"name\": \"Adam\", \"size\": 7, \"time\": " + asString(LocalTime.now()) + "}")
				.post(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test25_postBadBodyOldDate() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{ \"name\": \"Adam\", \"size\": 7, \"date\": " + asString(now.minusDays(1)) + ", \"time\": " + asString(
						LocalTime.now()) + "}")
				.post(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test26_postBadBodyNoTime() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{ \"name\": \"Adam\", \"size\": 7, \"date\": " + nowPlusMonthString + "}")
				.post(PATH)
				.then()
				.statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void test30_postA() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body(new BookingDto("Adam", 6, nowPlusMonth, LocalTime.now()))
				.post(PATH)
				.then()
				.statusCode(OK.getStatusCode()) // TODO: shall be 201
				.body("name", equalTo("Adam"));
	}
	
	@Test
	public void test31_postB() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body(new BookingDto("Barbara", 10, nowPlusMonth, LocalTime.now()))
				.post(PATH)
				.then()
				.statusCode(OK.getStatusCode()) // TODO: shall be 201
				.body("name", equalTo("Barbara"));
	}
	
	@Test
	public void test32_getIsNotEmpty() {
		RestAssured.given()
				.contentType(ContentType.JSON)
				.body(nowPlusMonthString)
				.get(PATH)
				.then()
				.statusCode(OK.getStatusCode())
				.body("size()", is(2));
	}
	
	// TODO: tests for max guests exceeded and for concurrent requests
	
	private static String asString(LocalDate date) {
		try {
			return mapper.writeValueAsString(date);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String asString(LocalTime time) {
		try {
			return mapper.writeValueAsString(time);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
