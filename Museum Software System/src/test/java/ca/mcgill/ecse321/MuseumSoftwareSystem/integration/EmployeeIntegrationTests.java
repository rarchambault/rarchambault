package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Employee;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ArtworkRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private ArtworkRepository artworkRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		artworkRepo.deleteAll();
		employeeRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	/**
	 * Test makes a request to create an employee, gets it's email address, then retrieves the employee with that email address
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testCreateAndGetEmployeeByEmailAddress() {
		String emailAddress = testCreateEmployee().getEmailAddress();
		testGetEmployeeByEmailAddress(emailAddress);
	}

	/**
	 * Test makes a request to create an employee, then compares the received employee with the one requested
	 *
	 * @return Fetched Employee DTO Object
	 * @author Alec Tufenkjian
	 */

	private EmployeeResponseDto testCreateEmployee() {

		OwnerRequestDto ownerRequest = new OwnerRequestDto("Test Owner", "test@owner.com", "abc@123456");
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest, OwnerResponseDto.class);

		MuseumRequestDto museumRequest = new MuseumRequestDto("Test Museum", ownerResponse.getBody().getEmailAddress());
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest, MuseumResponseDto.class);

		EmployeeRequestDto employeeRequest = new EmployeeRequestDto("Test Employee", "test@employee.com", "abc@123456", museumResponse.getBody().getId());
		ResponseEntity<EmployeeResponseDto> employeeResponse = client.postForEntity("/employee", employeeRequest, EmployeeResponseDto.class);

		assertNotNull(employeeResponse);
		assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode(), "Response has correct status");
		assertNotNull(employeeResponse.getBody(), "Response has body");
		assertEquals(employeeRequest.getName(), employeeResponse.getBody().getName(), "Response has correct name");
		assertEquals(employeeRequest.getEmailAddress(), employeeResponse.getBody().getEmailAddress(), "Response has correct email address");
		assertEquals(employeeRequest.getPassword(), employeeResponse.getBody().getPassword(), "Response has correct password");
		assertEquals(employeeRequest.getMuseumId(), employeeResponse.getBody().getMuseum().getId(), "Response has correct museum id");

		return employeeResponse.getBody();
	}

	/**
	 * Test makes a request to retrieve employee by email address, then makes sure the employee received is the one expected
	 *
	 * @author Alec Tufenkjian
	 */
	private void testGetEmployeeByEmailAddress(String emailAddress) {
		ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/employee/emailAddress/" + emailAddress, EmployeeResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(emailAddress, response.getBody().getEmailAddress(), "Response has correct name");
	}

	/**
	 * Test makes a request to retrieve a nonexisting employee by email address, then makes sure the exception is handled correctly
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testGetEmployeeByInvalidEmailAddress() {
		String emailAddress = "inavlid@employee.com";
		ResponseEntity<String> response = client.getForEntity("/employee/emailAddress/" + emailAddress, String.class);

		assertEquals("Employee not found.", response.getBody());
	}

	/**
	 * Test makes a request to delete an employee by email address, then makes sure the employee does not exist anymore
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testDeleteEmployeeById(){
		String emailAddress = testCreateEmployee().getEmailAddress();

		client.delete("/employee/emailAddress/"+ emailAddress);

		ResponseEntity<String> response = client.getForEntity("/employee/emailAddress/"+ emailAddress, String.class);

		assertEquals("Employee not found.", response.getBody());
	}
}
