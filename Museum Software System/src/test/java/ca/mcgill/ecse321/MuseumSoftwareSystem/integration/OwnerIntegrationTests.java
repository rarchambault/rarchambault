package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private RoomRepository roomRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		roomRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	/**
	 * @author Haroun Guessous
	 *
	 * tests to see if creates and gets Owner by email address correctly
	 *
	 */
	@Test
	public void testCreateAndGetOwnerByEmailAddress() {
		String emailAddress = testCreateOwner().getEmailAddress();
		testGetOwnerByEmailAddress(emailAddress);
	}

	/**
	 *
	 * @author Haroun Guessous
	 *
	 * Tests if can create Owner correctly
	 *
	 * @return Returns body of the Owner Response
	 */
	private OwnerResponseDto testCreateOwner() {

		OwnerRequestDto ownerRequest = new OwnerRequestDto("Test Owner", "test@owner.com", "abc@123456");
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest, OwnerResponseDto.class);

		assertNotNull(ownerResponse);
		assertNotNull(ownerResponse.getBody(), "Response has body");
		assertEquals(ownerRequest.getName(), ownerResponse.getBody().getName(), "Response has correct name");
		assertEquals(ownerRequest.getEmailAddress(), ownerResponse.getBody().getEmailAddress(), "Response has correct email address");

		return ownerResponse.getBody();
	}

	/**
	 *
	 * @author Haroun Guessous
	 *
	 * Tests if can get Owner by email address correctly
	 *
	 * @param emailAddress
	 */
	private void testGetOwnerByEmailAddress(String emailAddress) {
		ResponseEntity<OwnerResponseDto> response = client.getForEntity("/owner/emailAddress/" + emailAddress, OwnerResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(emailAddress, response.getBody().getEmailAddress(), "Response has correct name");
	}

	/**
	 *
	 * Tests if can returns proper message if Owner cannot be found by email address
	 *
	 * @author Haroun Guessous
	 *
	 */
	@Test
	public void testGetOwnerByInvalidEmailAddress(){
		String invalidEmailAddress = "invalid@owner.com";

		ResponseEntity<String> response = client.getForEntity("/owner/emailAddress/" + invalidEmailAddress, String.class);

		assertEquals("Owner not found.", response.getBody());
	}

}
