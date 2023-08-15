package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.ArtworkRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.LoanRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.RoomRepository;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.VisitorRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LoanIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private LoanRepository loanRepo;

	@Autowired
	private VisitorRepository visitorRepo;

	@Autowired
	private ArtworkRepository artworkRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	private int id;
	private double loanFee;
	private String loaneeEmailAddress;
	private int artworkId;

	@BeforeAll
	@AfterAll
	public void clearDatabase() {
		loanRepo.deleteAll();
		artworkRepo.deleteAll();
		visitorRepo.deleteAll();
		roomRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	/**
	 * Test method that creates a loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T01TestCreateLoan() {
		// Create
		LoanResponseDto response = createLoan();
		this.id = response.getId();
		this.loanFee = response.getArtwork().getLoanFee();
		this.loaneeEmailAddress = response.getLoanee().getEmailAddress();
		this.artworkId = response.getArtwork().getId();
	}

	/**
	 * Test method that retrieves the created loan by its ID
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T02TestGetLoanById() {
		ResponseEntity<LoanResponseDto> response = client.getForEntity("/loan/id/" + id, LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertTrue(response.getBody().getId() == id, "Response has correct ID");
	}

	/**
	 * Test method that retrieves the created loan by its loanee's email address
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T03TestGetLoansByLoaneeEmailAddress() {
		ResponseEntity<LoanResponseDto[]> response = client
				.getForEntity("/loan/loaneeEmailAddress/" + loaneeEmailAddress, LoanResponseDto[].class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody()[0].getLoanee().getEmailAddress(), loaneeEmailAddress,
				"Response has correct ID");
	}

	/**
	 * Test method that retrieves the created loan by its artwork ID
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T04TestGetLoansByArtworkId() {
		ResponseEntity<LoanResponseDto[]> response = client.getForEntity("/loan/artworkId/" + artworkId,
				LoanResponseDto[].class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody()[0].getArtwork().getId(), artworkId, "Response has correct ID");
	}

	/**
	 * Test method that rejects the created loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T05testRejectLoan() {
		ResponseEntity<LoanResponseDto> response = client.postForEntity("/loan/reject/", new IdDto(id), LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody().getId(), id, "Response has correct ID");
		assertEquals(response.getBody().getStatus(), ApprovalStatus.Rejected);
	}

	/**
	 * Test method that attempts to pay for a loan without inputting money
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T06testPayLoanNoMoney() {
		ResponseEntity<String> response = client.postForEntity("/loan/pay/", new PayRequestDto(id, 0), String.class);

		assertEquals("Input amount $" + 0.0 + " does not cover the total price of the loan which is $" + loanFee,
				response.getBody());
	}

	/**
	 * Test method that attempts to approve the loan when it has not been paid yet
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T07testApproveLoanNotPaid() {
		ResponseEntity<String> response = client.postForEntity("/loan/approve/", new IdDto(id), String.class);

		assertEquals("Cannot approve a loan that has not been paid for yet.", response.getBody());
	}

	/**
	 * Test method that attempts to renew the loan when it has not been approved
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T08testRenewLoanNotApproved() {
		ResponseEntity<String> response = client.postForEntity("/loan/renew/", new PayRequestDto(id, loanFee),
				String.class);

		assertEquals("Cannot renew a loan that has not been approved.", response.getBody());
	}

	/**
	 * Test method that pays for the loan fees
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T09testPayLoan() {
		ResponseEntity<LoanResponseDto> response = client.postForEntity("/loan/pay/", new PayRequestDto(id, loanFee),
				LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody().getId(), id, "Response has correct ID");
		assertTrue(response.getBody().getIsFeePaid());
	}

	/**
	 * Test method that approves the created loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T10testApproveLoan() {
		ResponseEntity<LoanResponseDto> response = client.postForEntity("/loan/approve/", id, LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody().getId(), id, "Response has correct ID");
		assertEquals(ApprovalStatus.Approved, response.getBody().getStatus());
		assertEquals(Date.valueOf(LocalDate.now()), response.getBody().getStartDate());
		assertEquals(Date.valueOf(LocalDate.now().plusWeeks(2)), response.getBody().getEndDate());
		assertFalse(response.getBody().getArtwork().getIsInMuseum());
	}

	/**
	 * Test method that renews the created loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T11testRenewLoan() {
		ResponseEntity<LoanResponseDto> response = client.postForEntity("/loan/renew/", new PayRequestDto(id, loanFee),
				LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody().getId(), id, "Response has correct ID");
		assertEquals(ApprovalStatus.Approved, response.getBody().getStatus());
		assertEquals(Date.valueOf(LocalDate.now()), response.getBody().getStartDate());
		assertEquals(Date.valueOf(LocalDate.now().plusWeeks(4)), response.getBody().getEndDate());
	}

	/**
	 * Test method that sets the created loan to "late"
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T12testSetLoanLate() {
		ResponseEntity<LoanResponseDto> response = client.postForEntity("/loan/setLate/", id, LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody().getId(), id, "Response has correct ID");
		assertTrue(response.getBody().getIsLate());
		assertFalse(response.getBody().getIsFeePaid());
		assertEquals(loanFee / 10, response.getBody().getLateFee());
	}

	/**
	 * Test method that attemps to return the loan without paying the late fees
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T13testReturnLoanNoMoney() {
		ResponseEntity<String> response = client.postForEntity("/loan/return/", new PayRequestDto(id, 0), String.class);

		assertEquals("Input amount $" + 0.0 + " does not cover the late fee for the loan which is $" + loanFee / 10,
				response.getBody());
	}

	/**
	 * Test method that returns the loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void T14testReturnLoan() {
		ResponseEntity<LoanResponseDto> response = client.postForEntity("/loan/return/",
				new PayRequestDto(id, loanFee / 10), LoanResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(response.getBody().getId(), id, "Response has correct ID");
		assertTrue(response.getBody().getIsLate());
		assertTrue(response.getBody().getIsFeePaid());
		assertEquals(loanFee / 10, response.getBody().getLateFee());
		assertTrue(response.getBody().getArtwork().getIsInMuseum());
	}

	private LoanResponseDto createLoan() {
		// Need to first post owner, museum, room, artwork, visitor

		// Create Owner
		OwnerRequestDto ownerRequest = new OwnerRequestDto("Marwan", "marwan@mail.com", "museum123");
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		assertEquals(HttpStatus.CREATED, ownerResponse.getStatusCode(), "Response has correct status");

		// Create Museum
		MuseumRequestDto museumRequest = new MuseumRequestDto("Marwan's museum",
				ownerResponse.getBody().getEmailAddress());
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class);

		assertEquals(HttpStatus.CREATED, museumResponse.getStatusCode(), "Response has correct status");

		int museumId = museumResponse.getBody().getId();

		// Create Room
		RoomRequestDto roomRequest = new RoomRequestDto(museumId, RoomType.Small);
		ResponseEntity<RoomResponseDto> roomResponse = client.postForEntity("/room", roomRequest,
				RoomResponseDto.class);

		assertEquals(HttpStatus.CREATED, roomResponse.getStatusCode(), "Response has correct status");

		int roomId = roomResponse.getBody().getId();

		// Create Artwork
		ArtworkRequestDto artworkRequest = new ArtworkRequestDto(museumId, roomId, "Mona Lisa", 100, true, true, null);
		ResponseEntity<ArtworkResponseDto> artworkResponse = client.postForEntity("/artwork", artworkRequest,
				ArtworkResponseDto.class);

		assertEquals(HttpStatus.CREATED, artworkResponse.getStatusCode(), "Response has correct status");

		int artworkId = artworkResponse.getBody().getId();

		// Create Visitor
		VisitorRequestDto visitorRequest = new VisitorRequestDto("Visitor", "visitor@mail.com", "password", museumId);
		ResponseEntity<VisitorResponseDto> visitorResponse = client.postForEntity("/visitor", visitorRequest,
				VisitorResponseDto.class);

		assertEquals(HttpStatus.CREATED, visitorResponse.getStatusCode(), "Response has correct status");

		String visitorEmailAddress = visitorResponse.getBody().getEmailAddress();

		// Create Loan
		LoanRequestDto loanRequest = new LoanRequestDto(visitorEmailAddress, artworkId, museumId);
		ResponseEntity<LoanResponseDto> loanResponse = client.postForEntity("/loan", loanRequest,
				LoanResponseDto.class);

		assertNotNull(loanResponse);
		assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode(), "Response has correct status");
		assertNotNull(loanResponse.getBody(), "Response has body");

		assertFalse(loanResponse.getBody().getIsFeePaid());
		assertFalse(loanResponse.getBody().getIsLate());
		assertEquals(0, loanResponse.getBody().getLateFee());
		assertNull(loanResponse.getBody().getStartDate());
		assertNull(loanResponse.getBody().getEndDate());
		assertEquals(0, loanResponse.getBody().getNumberOfRenewals());
		assertEquals(ApprovalStatus.InReview, loanResponse.getBody().getStatus());
		assertEquals(museumId, loanResponse.getBody().getMuseum().getId());
		assertEquals(artworkId, loanResponse.getBody().getArtwork().getId());
		assertEquals(visitorEmailAddress, loanResponse.getBody().getLoanee().getEmailAddress());
		assertTrue(loanResponse.getBody().getId() > 0, "Response has valid ID");

		return loanResponse.getBody();
	}
}
