package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static java.lang.Integer.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumPassRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MuseumIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private MuseumPassRepository museumPassRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		museumPassRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	@Test
	public void testCreateAndGetMuseumById() {
		int id = testCreateMuseum().getId();
		testGetMuseumById(id);
	}

	private MuseumResponseDto testCreateMuseum() {
		String museumName = "Marwan's museum";
		String ownerName = "Marwan";
		String ownerEmailAddress = "marwan@mail.com";
		String ownerPassword = "museum123";

		OwnerRequestDto ownerRequest = new OwnerRequestDto(ownerName, ownerEmailAddress, ownerPassword);

		// Post Owner so we can create the museum
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		MuseumRequestDto museumRequest = new MuseumRequestDto(museumName, ownerResponse.getBody().getEmailAddress());

		ResponseEntity<MuseumResponseDto> response = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("Marwan's museum", response.getBody().getName(), "Response has correct name");
		assertEquals("marwan@mail.com", response.getBody().getOwner().getEmailAddress(),
				"Response has correct owner email address");
		assertTrue(response.getBody().getId() > 0, "Response has valid ID");

		return response.getBody();
	}

	private void testGetMuseumById(int id) {
		ResponseEntity<MuseumResponseDto> response = client.getForEntity("/museum/id/" + id, MuseumResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("Marwan's museum", response.getBody().getName(), "Response has correct name");
		assertTrue(response.getBody().getId() == id, "Response has correct ID");
	}

	@Test
	public void testGetMuseumByInvalidId() {
		int id = MAX_VALUE;
		ResponseEntity<String> response = client.getForEntity("/museum/id/" + id, String.class);
		assertEquals("Museum not found.", response.getBody());

	}


}