package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalTime;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.DayRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ShiftRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShiftIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private ShiftRepository shiftRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private DayRepository dayRepo;

	@Autowired
	private EmployeeRepository employeeRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		shiftRepo.deleteAll(); // delete shift entry
		dayRepo.deleteAll(); // delete day entry
		employeeRepo.deleteAll(); // delete employee entry
		museumRepo.deleteAll(); // delete museum entry
		ownerRepo.deleteAll(); // delete owner entry
	}

	/**
	 * Test that makes a request to create a shift, gets its id, employee, and date,
	 * the retrieves the shift with these endpoints
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testCreateandGetShift() {
		ShiftResponseDto response = testCreateShift();
	}

	/**
	 * Test that makes a request to create a shift, then compares the received shift
	 * with the one requested
	 * 
	 * @return Fetched Shift DTO object
	 * @author Mohammed Elsayed
	 */
	private ShiftResponseDto testCreateShift() {
		String name = "Marwan's museum";
		String ownerName = "Marwan";
		String ownerEmailAddress = "marwan@mail.com";
		String ownerPassword = "museum123";

		OwnerRequestDto ownerRequest = new OwnerRequestDto(ownerName, ownerEmailAddress, ownerPassword);
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		MuseumRequestDto museumRequest = new MuseumRequestDto(name, ownerResponse.getBody().getEmailAddress());
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class);

		int museumId = museumResponse.getBody().getId();

		int openingHour = 8;
		int closingHour = 16;
		Date date = Date.valueOf("2022-11-09");
		boolean isHoliday = false;
		DayRequestDto dayRequest = new DayRequestDto(museumId, openingHour, closingHour, date, isHoliday);
		ResponseEntity<DayResponseDto> dayResponse = client.postForEntity("/day", dayRequest, DayResponseDto.class);

		Date dateFromResponse = dayResponse.getBody().getDate();

		String employeeName = "Mohammed";
		String employeeEmailAddress = "elsayedmo@mail.com";
		String employeePassword = "sheeesh";

		EmployeeRequestDto employeeRequest = new EmployeeRequestDto(employeeName, employeeEmailAddress,
				employeePassword, museumId);
		ResponseEntity<EmployeeResponseDto> employeeResponse = client.postForEntity("/employee", employeeRequest,
				EmployeeResponseDto.class);

		String employeeEmailFromResponse = employeeResponse.getBody().getEmailAddress();

		LocalTime startTime = LocalTime.of(8, 0, 0, 000000000);
		LocalTime endTime = LocalTime.of(15, 0, 0, 000000000);
		ShiftRequestDto shiftRequest = new ShiftRequestDto(startTime, endTime, employeeEmailFromResponse,
				dateFromResponse, museumId);
		ResponseEntity<ShiftResponseDto> response = client.postForEntity("/shift", shiftRequest,
				ShiftResponseDto.class);

		assertNotNull(response);
		assertEquals(response.getStatusCode(), response.getStatusCode(), "Response has correct status");
		return response.getBody();
	}

	/**
	 * Test that makes a request to retrieve a shift by id, then makes sure the
	 * shift received is the one expected
	 * 
	 * @param id
	 * @author Mohammed Elsayed
	 */

	private void testGetShiftById(int id) {
		ResponseEntity<ShiftResponseDto> response = client.getForEntity("/shift/id/" + id, ShiftResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(id, response.getBody().getId());
		assertTrue(response.getBody().getId() == id, "Response has correct id");
	}

	/*
	 * Test that makes a request to retrieve a non-existing shift by an invalid id,
	 * then makes sure the exception is handed correctly
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftByInvalidId() {
		int id = -1;
		ResponseEntity<String> response = client.getForEntity("/shift/id/" + id, String.class);

		assertEquals("Shift not found.", response.getBody());
	}

	/**
	 * Test that makes a request to retrieve a shift by employee email, then makes
	 * sure the shift received is the one expected
	 * 
	 * @param email
	 * @author Mohammed Elsayed
	 */
	private void testGetShiftByEmployee(String email) {
		ResponseEntity<ShiftResponseDto> response = client.getForEntity("/shift/employeeEmailAddress/" + email,
				ShiftResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(email, response.getBody().getEmployee().getEmailAddress(), "Response has correct employee");
	}

	/*
	 * Test that makes a request to retrieve a non-existing shift by an invalid
	 * employee email, then makes sure the exception is handed correctly
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftsByInvalidEmployee() {
		String invalidEmployeeEmail = "loser@gmail.com";
		ResponseEntity<String> response = client.getForEntity("/shift/employeeEmailAddress/" + invalidEmployeeEmail,
				String.class);
		assertEquals("Shifts not found.", response.getBody());
	}

	/**
	 * Test that makes a request to retrieve a shift by date, then makes sure the
	 * shift received is the one expected
	 * 
	 * @param date
	 * @author Mohammed Elsayed
	 */
	private void testGetShiftByDay(Date date) {
		ResponseEntity<ShiftResponseDto> response = client.getForEntity("/shift/dayDate" + date,
				ShiftResponseDto.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(date, response.getBody().getDay().getDate(), "Response has correct day");
	}

	/*
	 * Test that makes a request to retrieve a non-existing shift by an invalid day,
	 * then makes sure the exception is handed correctly
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testGetShiftByInvalidDay() {
		Date invalidDate = Date.valueOf("2022-12-13");
		ResponseEntity<String> response = client.getForEntity("/shift/dayDate/" + invalidDate, String.class);
		assertEquals("Shift not found.", response.getBody());
	}

}
