package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.RoomRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		roomRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	/**
	 *
	 * tests to see if creates and gets room by Id correctly
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testCreateAndGetRoom() {
		int id = testCreateRoom().getId();
		testGetRoomById(id);
	}

	/**
	 *Tests if can create Room correctly
	 *
	 * @author Pinak Ghosh
	 * @return Returns body of the Room Response
	 */
	private RoomResponseDto testCreateRoom() {

		String name = "Marwan's museum";
		String ownerName = "Marwan";
		String ownerEmailAddress = "marwan@mail.com";
		String ownerPassword = "museum123";

		OwnerRequestDto ownerRequest = new OwnerRequestDto();
		ownerRequest.setEmailAddress(ownerEmailAddress);
		ownerRequest.setName(ownerName);
		ownerRequest.setPassword(ownerPassword);

		// Post Owner so we can create the museum
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		MuseumRequestDto museumRequest = new MuseumRequestDto();
		museumRequest.setName(name);
		museumRequest.setOwnerEmailAddress(ownerEmailAddress);

		// Post Museum so we can create the Room
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class);

		RoomRequestDto roomRequest = new RoomRequestDto(museumResponse.getBody().getId(), RoomType.Large);

		ResponseEntity<RoomResponseDto> response = client.postForEntity("/room", roomRequest, RoomResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");

		return response.getBody();
	}

	/**
	 *
	 * Tests if can get Room by Id correctly
	 *
	 * @author Pinak Ghosh
	 *
	 * @param id
	 */
	public void testGetRoomById(int id) {

		ResponseEntity<RoomResponseDto> response = client.getForEntity("/room/id/" + id, RoomResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("Marwan's museum", response.getBody().getMuseum().getName(), "Response has correct name");
		assertTrue(response.getBody().getId() == id, "Response has correct ID");

	}


	/**
	 *
	 * Tests if can returns proper message if Room cannot be found by ID
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testGetArtworkByInvalidId() {
		int id = -1;
		ResponseEntity<String> response = client.getForEntity("/room/id/"+ id, String.class);

		assertEquals("Room Not Found.", response.getBody());
	}


}
