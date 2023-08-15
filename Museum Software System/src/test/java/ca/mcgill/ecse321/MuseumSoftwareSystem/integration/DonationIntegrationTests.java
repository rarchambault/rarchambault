package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DonationIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private DonationRepository donationRepo;

	@Autowired
	private VisitorRepository visitorRepo;

	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private LoanRepository loanRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		loanRepo.deleteAll();
		donationRepo.deleteAll();
		visitorRepo.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	// Annotate Test methods with @Test

	/**
	 *
	 * tests to see if creates and gets Donation by Id correctly
	 *
	 *
	 * @author Haroun Guessous
	 *
	 */
	@Test
	public void testCreateAndGetADonation() {
		int id = testCreateDonation().getId();
		testGetDonationById(id);
	}

	/**
	 *
	 * Tests if can create donation correctly
	 *
	 * @author Haroun Guessous
	 *
	 * @return Returns body of the Donation Response
	 */
	private DonationResponseDto testCreateDonation() {
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

		DonationRequestDto donationRequest = new DonationRequestDto(museumResponse.getBody().getId(),
				visitorResponse.getBody().getEmailAddress(), "Painting");
		ResponseEntity<DonationResponseDto> response = client.postForEntity("/donation", donationRequest,
				DonationResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(donationRequest.getName(), response.getBody().getName(),
				"Response has correct owner email address");
		return response.getBody();
	}

	/**
	 *
	 * tests to see if creates and gets donation by Id correctly
	 *
	 * @author Haroun Guessous
	 *
	 *
	 * @param id
	 */
	public void testGetDonationById(int id) {

		ResponseEntity<DonationResponseDto> response = client.getForEntity("/donation/id/" + id,
				DonationResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(id, response.getBody().getId(), "Response has correct name");
	}

	/**
	 *
	 * Tests if can returns proper message if Donation cannot be found by ID
	 *
	 *
	 * @author Haroun Guessous
	 *
	 */
	@Test
	public void testGetDonationByInvalidId() {
		int invalidId = -1;

		ResponseEntity<String> response = client.getForEntity("/donation/id/" + invalidId, String.class);
		assertEquals("Donation not found.", response.getBody());
	}
}
