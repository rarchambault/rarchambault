package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArtworkIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private ArtworkRepository artworkRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private VisitorRepository visitorRepo;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private LoanRepository loanRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		loanRepo.deleteAll();
		artworkRepo.deleteAll();
		roomRepo.deleteAll();
		visitorRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	/**
	 * tests to see if creates and gets museum by Id correctly
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testCreateAndGetMuseumById() {
		int id = testCreateArtwork().getId();
		testGetArtworkById(id);
	}

	/**
	 * Tests if can create artwork correctly
	 *
	 * @author Pinak Ghosh
	 * @return Returns body of the Artwork Response
	 */
	private ArtworkResponseDto testCreateArtwork() {

		OwnerRequestDto ownerRequest = new OwnerRequestDto("Test Owner", "test@owner.com", "abc@123456");
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		MuseumRequestDto museumRequest = new MuseumRequestDto("Test Museum", ownerResponse.getBody().getEmailAddress());
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class);

		VisitorRequestDto visitorRequest = new VisitorRequestDto("Test Visitor", "test@visitor.com", "abc@123456",
				museumResponse.getBody().getId());
		ResponseEntity<VisitorResponseDto> visitorResponse = client.postForEntity("/visitor", visitorRequest,
				VisitorResponseDto.class);

		RoomRequestDto roomRequest = new RoomRequestDto();
		roomRequest.setMuseumId(museumResponse.getBody().getId());
		RoomType type = RoomType.Large;
		roomRequest.setType(type);
		ResponseEntity<RoomResponseDto> roomResponse = client.postForEntity("/room", roomRequest,
				RoomResponseDto.class);

		ArtworkRequestDto artworkRequest = new ArtworkRequestDto();
		artworkRequest.setName("Test Artwork");
		artworkRequest.setLoanFee(1.00);
		artworkRequest.setMuseumId(museumResponse.getBody().getId());
		artworkRequest.setIsAvailableForLoan(true);
		artworkRequest.setVisitorOnWaitingListEmail(visitorResponse.getBody().getEmailAddress());
		artworkRequest.setRoomId(roomResponse.getBody().getId());

		ResponseEntity<ArtworkResponseDto> artworkResponse = client.postForEntity("/artwork", artworkRequest,
				ArtworkResponseDto.class);

		assertNotNull(artworkResponse);
		assertEquals(HttpStatus.CREATED, artworkResponse.getStatusCode(), "Response has correct status");
		assertNotNull(artworkResponse.getBody(), "Response has body");

		return artworkResponse.getBody();
	}

	/**
	 *
	 * Tests if can get artwork by Id correctly
	 *
	 * @author Pinak Ghosh
	 *
	 * @param id
	 */
	private void testGetArtworkById(int id) {
		ResponseEntity<ArtworkResponseDto> response = client.getForEntity("/artwork/id/" + id,
				ArtworkResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertTrue(response.getBody().getId() == id, "Response has correct ID");
	}

	/**
	 *
	 * Tests if can returns proper message if artwork cannot be found by ID
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testGetArtworkByInvalidId() {
		int id = -1;
		ResponseEntity<String> response = client.getForEntity("/artwork/id/" + id, String.class);

		assertEquals("Artwork Not Found.", response.getBody());
	}

	/**
	 *
	 * Test to see if deletes artwork properly
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testDeleteArtwork() {
		int id = testCreateArtwork().getId();

		client.delete("/artwork/id/" + id);

		ResponseEntity<String> response = client.getForEntity("/artwork/id/" + id, String.class);

		assertEquals("Artwork Not Found.", response.getBody());
	}

	/**
	 *
	 * test to see if adds visitor to waitlist properly
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testAddVisitorToWaitList() {
		OwnerRequestDto ownerRequest = new OwnerRequestDto("Test Owner", "test@owner.com", "abc@123456");
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		MuseumRequestDto museumRequest = new MuseumRequestDto("Test Museum", ownerResponse.getBody().getEmailAddress());
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class);

		VisitorRequestDto visitorRequest = new VisitorRequestDto("Test Visitor", "test@visitor.com", "abc@123456",
				museumResponse.getBody().getId());
		ResponseEntity<VisitorResponseDto> visitorResponse = client.postForEntity("/visitor", visitorRequest,
				VisitorResponseDto.class);

		RoomRequestDto roomRequest = new RoomRequestDto(museumResponse.getBody().getId(), RoomType.Large);
		ResponseEntity<RoomResponseDto> roomResponse = client.postForEntity("/room", roomRequest,
				RoomResponseDto.class);

		ArtworkUpdateRequestDto artworkRequest = new ArtworkUpdateRequestDto(museumResponse.getBody().getId(),
				roomResponse.getBody().getId(), "name", 2.0, false, true, null);
		ResponseEntity<ArtworkResponseDto> returnedArtwork = client.postForEntity("/artwork", artworkRequest,
				ArtworkResponseDto.class);

		ArtworkUpdateRequestDto updateRequest = new ArtworkUpdateRequestDto();
		updateRequest.setId(returnedArtwork.getBody().getId());
		updateRequest.setVisitorOnWaitingListEmail(visitorResponse.getBody().getEmailAddress());
		ResponseEntity<ArtworkResponseDto> updatedArtwork = client.postForEntity("/artwork/addVisitorToWaitList",
				updateRequest, ArtworkResponseDto.class);

		assertEquals(visitorRequest.getEmailAddress(),
				updatedArtwork.getBody().getVisitorOnWaitlist().getEmailAddress());
	}
}
