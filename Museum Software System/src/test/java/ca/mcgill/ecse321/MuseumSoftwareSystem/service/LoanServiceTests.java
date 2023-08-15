package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.IdDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.LoanRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.LoanResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.PayRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.LoanRepository;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTests {
	// Replace the repositories and services used my this service with a "mock" that
	// exposes the same interface
	@Mock
	LoanRepository loanRepo;

	@Mock
	VisitorService visitorService;

	@Mock
	ArtworkService artworkService;

	@Mock
	MuseumService museumService;

	// Get a service that uses the mock repository and services
	@InjectMocks
	LoanService loanService;

	/**
	 * Test method which attempts to create a Loan object with some predetermined
	 * parameters
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testCreateLoan() {
		// Create required objects
		final int id = 1;
		Museum museum = new Museum("owner@mail.com", "password", "Owner");
		museum.setId(id);
		Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		Artwork artwork = new Artwork("Mona Lisa", 100, true, true, room, null, museum);
		artwork.setId(id);

		Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);
		LoanRequestDto loanRequest = new LoanRequestDto("visitor@mail.com", 1, 1);

		// Create loan to be returned
		Loan expectedLoan = new Loan(null, null, 0, ApprovalStatus.InReview, false, 0, false, loanee, artwork, museum);
		expectedLoan.setId(1);

		when(artworkService.findArtworkById(id)).thenAnswer((InvocationOnMock invocation) -> artwork);
		when(museumService.getMuseumById(id)).thenAnswer((InvocationOnMock invocation) -> museum);
		when(visitorService.retrieveVisitorByEmail("visitor@mail.com"))
				.thenAnswer((InvocationOnMock invocation) -> loanee);

		// Just return the Loan with no modification
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> expectedLoan);

		LoanResponseDto response = loanService.createLoan(loanRequest);

		// Verify that the parameters are valid
		assertNotNull(response);
		assertTrue(response.getId() > 0);
		assertFalse(response.getIsFeePaid());
		assertFalse(response.getIsLate());
		assertEquals(response.getLateFee(), 0);
		assertNull(response.getStartDate());
		assertNull(response.getEndDate());
		assertEquals(response.getNumberOfRenewals(), 0);
		assertEquals(response.getStatus(), ApprovalStatus.InReview);
		assertEquals(response.getMuseum().getId(), museum.getId());
		assertEquals(response.getArtwork().getId(), artwork.getId());
		assertEquals(response.getLoanee().getEmailAddress(), loanee.getEmailAddress());

		// Check that the service actually saved the person
		verify(loanRepo, times(1)).save(any(Loan.class));
	}

	/**
	 * Test method which attempts to create a Loan object with some predetermined
	 * parameters, but the artwork is not in the museum
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testCreateLoanArtworkNotInMuseum() {
		// Create required objects
		final int id = 1;
		Museum museum = new Museum("owner@mail.com", "password", "Owner");
		museum.setId(id);
		Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		Artwork artwork = new Artwork("Mona Lisa", 100, false, true, room, null, museum);
		artwork.setId(id);

		Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);
		LoanRequestDto loanRequest = new LoanRequestDto("visitor@mail.com", 1, 1);

		// Create loan to be returned
		Loan expectedLoan = new Loan(null, null, 0, ApprovalStatus.InReview, false, 0, false, loanee, artwork, museum);
		expectedLoan.setId(1);

		when(artworkService.findArtworkById(id)).thenAnswer((InvocationOnMock invocation) -> artwork);
		when(museumService.getMuseumById(id)).thenAnswer((InvocationOnMock invocation) -> museum);
		when(visitorService.retrieveVisitorByEmail("visitor@mail.com"))
				.thenAnswer((InvocationOnMock invocation) -> loanee);

		LoanResponseDto response = loanService.createLoan(loanRequest);

		// Verify that the response is null, meaning the loanee was just added to the
		// artwork's waitlist
		assertNull(response);
	}

	/**
	 * Test method that attempts to retrieve a Loan object with a given id
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetLoanById() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.getLoanById(id);

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(isFeePaid, fetchedLoan.getIsFeePaid());
		assertEquals(isLate, fetchedLoan.getIsLate());
		assertEquals(lateFee, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(status, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artwork, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
	}

	/**
	 * Test method that tries to retrieve all loans for a given email address
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetLoansByLoaneeEmailAddress() {
		// Tell the mocked repository how to behave
		final String loaneeEmailAddress = "visitor@mail.com";
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		final Room room = new Room(RoomType.Large, museum);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		List<Loan> loans = new ArrayList<Loan>();
		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		Loan loan2 = new Loan(null, null, 0, ApprovalStatus.InReview, false, 0, false, loanee, artwork, museum);
		Loan loan3 = new Loan(startDate, endDate, numberOfRenewals + 1, ApprovalStatus.Approved, false, 0, true, loanee,
				artwork, museum);

		loans.add(loan);
		loans.add(loan2);
		loans.add(loan3);

		when(loanRepo.findLoansByLoaneeEmailAddress(loaneeEmailAddress))
				.thenAnswer((InvocationOnMock invocation) -> loans);

		// Test that the service behaves properly
		List<Loan> fetchedLoans = loanService.getLoansByLoaneeEmailAddress(loaneeEmailAddress);

		// Check that all parameters are valid
		assertNotNull(fetchedLoans);
		assertEquals(3, fetchedLoans.size());

		// Check that each fetched loan has the right loanee email address
		for (int i = 0; i < fetchedLoans.size(); i++) {
			assertEquals(loaneeEmailAddress, fetchedLoans.get(i).getLoanee().getEmailAddress());
		}
	}

	/**
	 * Test method that tries to retrieve all loans for a given artworkId
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetLoansByArtworkId() {
		// Tell the mocked repository how to behave
		final int artworkId = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		final Room room = new Room(RoomType.Large, museum);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(artworkId);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		List<Loan> loans = new ArrayList<Loan>();
		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		Loan loan2 = new Loan(null, null, 0, ApprovalStatus.InReview, false, 0, false, loanee, artwork, museum);
		Loan loan3 = new Loan(startDate, endDate, numberOfRenewals + 1, ApprovalStatus.Approved, false, 0, true, loanee,
				artwork, museum);

		loans.add(loan);
		loans.add(loan2);
		loans.add(loan3);

		when(loanRepo.findLoansByArtworkId(artworkId)).thenAnswer((InvocationOnMock invocation) -> loans);

		// Test that the service behaves properly
		List<Loan> fetchedLoans = loanService.getLoansByArtworkId(artworkId);

		// Check that all parameters are valid
		assertNotNull(fetchedLoans);
		assertEquals(3, fetchedLoans.size());

		// Check that each fetched loan has the right loanee email address
		for (int i = 0; i < fetchedLoans.size(); i++) {
			assertEquals(artworkId, fetchedLoans.get(i).getArtwork().getId());
		}
	}

	/**
	 * Test method that tries to approve a loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testApproveLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.InReview;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		Artwork artworkToReturn = new Artwork("Mona Lisa", 100, false, false, room, null, museum);

		when(artworkService.setArtworkIsInMuseum(id, false))
				.thenAnswer((InvocationOnMock invocation) -> artworkToReturn);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.approveLoan(new IdDto(id));

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(isFeePaid, fetchedLoan.getIsFeePaid());
		assertEquals(isLate, fetchedLoan.getIsLate());
		assertEquals(lateFee, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(ApprovalStatus.Approved, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artworkToReturn, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
		assertFalse(fetchedLoan.getArtwork().getIsInMuseum());
	}

	/**
	 * Test method that tries to reject a loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testRejectLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.InReview;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.rejectLoan(new IdDto(id));

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(isFeePaid, fetchedLoan.getIsFeePaid());
		assertEquals(isLate, fetchedLoan.getIsLate());
		assertEquals(lateFee, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(ApprovalStatus.Rejected, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artwork, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
	}

	/**
	 * Test method that tries to renew a loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testRenewLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, false, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		double inputAmount = 100;

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.renewLoan(new PayRequestDto(id, inputAmount));

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(isFeePaid, fetchedLoan.getIsFeePaid());
		assertEquals(isLate, fetchedLoan.getIsLate());
		assertEquals(lateFee, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(Date.valueOf(endDate.toLocalDate().plusWeeks(2)), fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals + 1, fetchedLoan.getNumberOfRenewals());
		assertEquals(status, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artwork, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
		assertFalse(fetchedLoan.getArtwork().getIsInMuseum());
	}

	/**
	 * Test method that tries to set a loan to "late"
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testSetLoanLate() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-11-01");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, false, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.setLoanLate(id);

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(false, fetchedLoan.getIsFeePaid());
		assertEquals(true, fetchedLoan.getIsLate());
		assertEquals(fetchedLoan.getArtwork().getLoanFee() / 10, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(status, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artwork, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
		assertFalse(fetchedLoan.getArtwork().getIsInMuseum());
	}

	/**
	 * Test method that tries to pay fees for a loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testPayLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = false;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-11-01");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		double inputAmount = 100;

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.payLoan(new PayRequestDto(id, inputAmount));

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(true, fetchedLoan.getIsFeePaid());
		assertEquals(isLate, fetchedLoan.getIsLate());
		assertEquals(lateFee, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(status, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artwork, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
	}

	/**
	 * Test method that tries to return a loan
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testReturnLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, false, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		Artwork artworkToReturn = new Artwork("Mona Lisa", 100, true, false, room, null, museum);

		when(artworkService.setArtworkIsInMuseum(id, true))
				.thenAnswer((InvocationOnMock invocation) -> artworkToReturn);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.returnLoan(new PayRequestDto(id, 0));

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(isFeePaid, fetchedLoan.getIsFeePaid());
		assertEquals(isLate, fetchedLoan.getIsLate());
		assertEquals(lateFee, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(ApprovalStatus.Returned, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artworkToReturn, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
		assertTrue(fetchedLoan.getArtwork().getIsInMuseum());
	}

	/**
	 * Test method that tries to return a loan after its end date
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testReturnLoanLate() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-10-12");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, false, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		Artwork artworkToReturn = new Artwork("Mona Lisa", 100, true, false, room, null, museum);

		when(artworkService.setArtworkIsInMuseum(id, true))
				.thenAnswer((InvocationOnMock invocation) -> artworkToReturn);

		// Return the saved loan when trying to save it
		when(loanRepo.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		double lateFeeAdded = 10;

		// Test that the service behaves properly
		Loan fetchedLoan = loanService.returnLoan(new PayRequestDto(id, lateFeeAdded));

		// Check that all parameters are valid
		assertNotNull(fetchedLoan);
		assertEquals(id, fetchedLoan.getId());
		assertEquals(true, fetchedLoan.getIsFeePaid());
		assertEquals(true, fetchedLoan.getIsLate());
		assertEquals(lateFeeAdded, fetchedLoan.getLateFee());
		assertEquals(startDate, fetchedLoan.getStartDate());
		assertEquals(endDate, fetchedLoan.getEndDate());
		assertEquals(numberOfRenewals, fetchedLoan.getNumberOfRenewals());
		assertEquals(ApprovalStatus.Returned, fetchedLoan.getStatus());
		assertEquals(museum, fetchedLoan.getMuseum());
		assertEquals(artworkToReturn, fetchedLoan.getArtwork());
		assertEquals(loanee, fetchedLoan.getLoanee());
		assertTrue(fetchedLoan.getArtwork().getIsInMuseum());
	}

	/**
	 * Test method that attempts to retrieve a loan with an invalid ID
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetLoanByInvalidId() {
		final int invalidId = 99;
		when(loanRepo.findLoanById(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.getLoanById(invalidId));

		assertEquals("Loan not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that attempts to retrieve a list of loans with an invalid loanee
	 * email address
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetLoansByInvalidLoaneeEmailAddress() {
		final String invalidLoaneeEmailAddress = "invalid@mail.com";
		when(loanRepo.findLoansByLoaneeEmailAddress(invalidLoaneeEmailAddress))
				.thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.getLoansByLoaneeEmailAddress(invalidLoaneeEmailAddress));

		assertEquals("Loans not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that attempts to retrieve a list of loans with an invalid artwork
	 * ID
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetLoansByInvalidArtworkId() {
		final int invalidArtworkId = 69;
		when(loanRepo.findLoansByArtworkId(invalidArtworkId)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.getLoansByArtworkId(invalidArtworkId));

		assertEquals("Loans not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that attempts to approve a loan that has not been paid
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testApproveUnpaidLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = false;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf(LocalDate.now());
		final Date endDate = Date.valueOf(LocalDate.now().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.InReview;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		// Return the previously created loan when we query the repo
		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);

		// Check that the right exception is thrown
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.approveLoan(new IdDto(id)));

		assertEquals("Cannot approve a loan that has not been paid for yet.", ex.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}

	/**
	 * Test method that tries to renew a loan that has not been approved
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testRenewUnapprovedLoan() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = false;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = null;
		final Date endDate = null;
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.InReview;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);
		double inputAmount = 100;

		// Check that the right exception is thrown
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.renewLoan(new PayRequestDto(id, inputAmount)));

		assertEquals("Cannot renew a loan that has not been approved.", ex.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}

	/**
	 * Test method that tries to renew a loan when there is a visitor on the
	 * waitlist for the artwork
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testRenewLoanVisitorOnWaitlist() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = false;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-01-13");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Visitor visitorOnWaitlist = new Visitor("waitlistee@mail.com", "password", "Johnny", museum);
		final Artwork artwork = new Artwork("Mona Lisa", 100, false, false, room, visitorOnWaitlist, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);
		double inputAmount = 100;

		// Check that the right exception is thrown
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.renewLoan(new PayRequestDto(id, inputAmount)));

		assertEquals("Cannot renew loan because there is a visitor on the waitlist for this artwork", ex.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}

	/**
	 * Test method that tries to renew a loan with not enough money to cover the
	 * renewal fee
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testRenewLoanNotEnoughFunds() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = false;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-01-13");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);
		double inputAmount = 50;

		// Check that the right exception is thrown
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.renewLoan(new PayRequestDto(id, inputAmount)));

		assertEquals("Input amount $" + inputAmount + " does not cover the total price of the loan which is $" + 100.0,
				ex.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}

	/**
	 * Test method that tries to pay a loan with not enough money to cover the loan
	 * fee
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testPayLoanNotEnoughFunds() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = false;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-01-13");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 2;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);
		double inputAmount = 50;

		// Check that the right exception is thrown
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.payLoan(new PayRequestDto(id, inputAmount)));

		assertEquals("Input amount $" + inputAmount + " does not cover the total price of the loan which is $" + 100.0,
				ex.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}

	/**
	 * Test method that tries to return a loan with not enough money to cover the
	 * late fee
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testReturnLoanNotEnoughFunds() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final boolean isFeePaid = true;
		final boolean isLate = false;
		final double lateFee = 0;
		final Date startDate = Date.valueOf("2022-01-13");
		final Date endDate = Date.valueOf(startDate.toLocalDate().plusWeeks(2));
		final int numberOfRenewals = 0;
		final ApprovalStatus status = ApprovalStatus.Approved;
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setId(id);
		final Room room = new Room(RoomType.Large, museum);
		room.setId(id);
		final Artwork artwork = new Artwork("Mona Lisa", 100, true, false, room, null, museum);
		artwork.setId(id);
		final Visitor loanee = new Visitor("visitor@mail.com", "password", "Visitor", museum);

		Loan loan = new Loan(startDate, endDate, numberOfRenewals, status, isLate, lateFee, isFeePaid, loanee, artwork,
				museum);
		loan.setId(id);

		when(loanRepo.findLoanById(id)).thenAnswer((InvocationOnMock invocation) -> loan);
		double inputAmount = 0;

		// Check that the right exception is thrown
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> loanService.returnLoan(new PayRequestDto(id, inputAmount)));

		assertEquals("Input amount $" + inputAmount + " does not cover the late fee for the loan which is $" + 10.0,
				ex.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}
}
