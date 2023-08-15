package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.VisitorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VisitorIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private VisitorRepository visitorRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		visitorRepo.deleteAll();
		museumRepo.deleteAll();
	}

	/**
	 * Test makes a request to create a visitor, gets their email address, then retrieves the visitor with that email address
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testCreateAndGetVisitorByEmailAddress() {
		String emailAddress = testCreateVisitor().getEmailAddress();
		testGetVisitorByEmailAddress(emailAddress);
	}

	/**
	 * Test makes a request to create a visitor, then compares the received visitor with the one requested
	 *
	 * @return Fetched Visitor DTO Object
	 * @author Alec Tufenkjian
	 */
	private VisitorResponseDto testCreateVisitor() {

		OwnerRequestDto ownerRequest = new OwnerRequestDto("Test Owner", "test@owner.com", "abc@123456");
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest, OwnerResponseDto.class);
		MuseumRequestDto museumRequest = new MuseumRequestDto("Test Museum", ownerResponse.getBody().getEmailAddress());
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest, MuseumResponseDto.class);
		VisitorRequestDto visitorRequest = new VisitorRequestDto("Test Visitor", "test@visitor.com", "abc@123456", museumResponse.getBody().getId());
		ResponseEntity<VisitorResponseDto> visitorResponse = client.postForEntity("/visitor", visitorRequest, VisitorResponseDto.class);

		assertNotNull(visitorResponse);
		assertEquals(HttpStatus.CREATED, visitorResponse.getStatusCode(), "Response has correct status");
		assertNotNull(visitorResponse.getBody(), "Response has body");
		assertEquals(visitorRequest.getName(), visitorResponse.getBody().getName(), "Response has correct name");
		assertEquals(visitorRequest.getEmailAddress(), visitorResponse.getBody().getEmailAddress(), "Response has correct email address");
		assertEquals(visitorRequest.getPassword(), visitorResponse.getBody().getPassword(), "Response has correct password");
		assertEquals(visitorRequest.getMuseumId(), visitorResponse.getBody().getMuseum().getId(), "Response has correct museum id");
		return visitorResponse.getBody();
	}
  
	/**
	 * Test makes a request to retrieve visitor by email address, then makes sure the visitor received is the one expected
	 *
	 * @author Alec Tufenkjian
	 */
	private void testGetVisitorByEmailAddress(String emailAddress) {
		ResponseEntity<VisitorResponseDto> response = client.getForEntity("/visitor/emailAddress/" + emailAddress, VisitorResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(emailAddress, response.getBody().getEmailAddress(), "Response has correct name");
	}

	/**
	 * Test makes a request to retrieve a nonexisting visitor by email address, then makes sure the exception is handled correctly
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testGetVisitorByInvalidEmailAddress() {
		String emailAddress = "inavlid@visitor.com";
		ResponseEntity<String> response = client.getForEntity("/visitor/emailAddress/" + emailAddress, String.class);

		assertEquals("Visitor not found.", response.getBody());
	}
}
