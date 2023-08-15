package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ArtworkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.DayRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.DayService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DayIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private DayRepository dayRepo;
	
	@Autowired
	private MuseumRepository museumRepo;
	
	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private ArtworkRepository artworkRepo;
	
	@Autowired
	DayService dayService;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		artworkRepo.deleteAll();
		dayRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	/**
	 * Test that makes  a request to create a day, gets its date, the retrieves the day with that date
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testCreateAndGetDay(){
		Date date = testCreateDay().getDate();
		testGetDayByDate(date);
	}
	
	/**
	 * Test that makes a request to create a day, then compares the received day with the one requested
	 * 
	 * @return Fetched Day DTO object
	 * @author Mohammed Elsayed
	 */
	private DayResponseDto testCreateDay() {
		
		String name = "Marwan's museum";
		String ownerName = "Marwan";
		String ownerEmailAddress = "marwan@mail.com";
		String ownerPassword = "museum123";
		
		OwnerRequestDto ownerRequest = new OwnerRequestDto();
		ownerRequest.setEmailAddress(ownerEmailAddress);
		ownerRequest.setName(ownerName);
		ownerRequest.setPassword(ownerPassword);
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest, OwnerResponseDto.class);
		
		MuseumRequestDto museumRequest = new MuseumRequestDto();
		museumRequest.setName(name);
		museumRequest.setOwnerEmailAddress(ownerEmailAddress);
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest, MuseumResponseDto.class);
		
		int openingHour = 8;
		int closingHour = 16;
		Date date =Date.valueOf("2022-11-09");
		boolean isHoliday = false;
		DayRequestDto dayRequest = new DayRequestDto();
		dayRequest.setMuseumId(museumResponse.getBody().getId());
		dayRequest.setOpeningHour(openingHour);
		dayRequest.setClosingHour(closingHour);
		dayRequest.setDate(date);
		dayRequest.setIsHoliday(isHoliday);
		
		ResponseEntity<DayResponseDto> response = client.postForEntity("/day", dayRequest, DayResponseDto.class);
		
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(date, response.getBody().getDate(), "Response has correct date");
		assertTrue((response.getBody().getDate().compareTo(Date.valueOf("2022-11-10"))) < 0,"Response has valid date");
		return response.getBody();
	}
	
	/**
	 * Test that makes a request to retrieve a day by date, then makes sure the day received is the one expected
	 *  
	 * @param date
	 * @author Mohammed Elsayed
	 */
	public void testGetDayByDate(Date date) {
		ResponseEntity<DayResponseDto> response = client.getForEntity("/day/date/" + date.toString(), DayResponseDto.class);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("Marwan's museum", response.getBody().getMuseum().getName(), "Response has correct status");
		Date aDate = Date.valueOf("2022-11-09");
		assertEquals(aDate, response.getBody().getDate(), "Response has correct date");
		assertTrue(response.getBody().getDate().equals(date), "Response has correct date");
	}
	
	/*
	 * Test that makes a request to retrieve a non-existing day by date, then makes sure the exception is handed correctly
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetDayByInvalidDate() {
		Date date = Date.valueOf("2022-12-13");
		ResponseEntity<String> response = client.getForEntity("/day/date/" + date, String.class);
		assertEquals("Day Not Found.", response.getBody());
	}
	
	/*
	 * Test that creates a day, deletes it, then makes sure the exception is handled
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testDeleteDay() {
	Date date = testCreateDay().getDate();
	client.delete("/day/date/" + date);
	ResponseEntity<String> response = client.getForEntity("/day/date/" + date, String.class);
	assertEquals("Day Not Found.", response.getBody());		
	}

}
