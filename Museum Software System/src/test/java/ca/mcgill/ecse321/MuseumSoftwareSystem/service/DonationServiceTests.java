package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DonationResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.DonationRepository;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	DonationRepository donationRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	DonationService donationService;

	@Mock
	MuseumService museumService;

	@Mock
	VisitorService visitorService;

	/**
	 *
	 * Test method that creates a Donation object from pre-determined parameters
	 *
	 * @author Haroun Guessous
	 *
	 *
	 */
	@Test
	public void testCreateDonation() {
		final Owner owner = new Owner("test@owner.com", "password", "Test Owner");
		final Museum museum = new Museum(owner, "Test Museum");
		final Visitor donator = new Visitor("test@donator.com","abc@123456" , "Test Donator", museum);
		museum.setId(1);
		final Donation donation = new Donation("Painting", new Date(), ApprovalStatus.Approved, museum, donator);

		when(museumService.getMuseumById(donation.getMuseum().getId())).thenAnswer((InvocationOnMock invocation) -> museum);
		when(visitorService.retrieveVisitorByEmail(donator.getEmailAddress())).thenAnswer((InvocationOnMock invocation) -> donator);
		when(donationRepo.save(any(Donation.class))).thenAnswer((InvocationOnMock invocation) -> donation);

		DonationRequestDto request = new DonationRequestDto(museum.getId(), donator.getEmailAddress(), donation.getName());
		DonationResponseDto returnedDonation = donationService.createDonation(request);

		assertNotNull(returnedDonation);
		assertEquals(donation.getName(), returnedDonation.getName());
		assertEquals(donation.getDonator().getEmailAddress(), returnedDonation.getDonator().getEmailAddress());
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-mm-dd");
		assertEquals(dt1.format(donation.getRequestDate()), returnedDonation.getRequestDate());
		assertEquals(donation.getMuseum().getId(), returnedDonation.getMuseum().getId());

		verify(donationRepo, times(1)).save(any(Donation.class));
	}

	/**
	 *
	 * Test method that attempts to retrieve a Donation object from a given ID
	 *
	 *
	 * @author Haroun Guessous
	 *
	 */
	@Test
	public void testGetDonationById() {
		final Owner owner = new Owner("test@owner.com", "password", "Test Owner");
		final Museum museum = new Museum(owner, "Test Museum");
		final Visitor donator = new Visitor("test@donator.com","abc@123456" , "Test Donator", museum);
		museum.setId(1);
		final Donation donation = new Donation("Painting", new Date(), ApprovalStatus.Approved, museum, donator);

		when(donationRepo.findDonationById(donation.getId()))
				.thenAnswer((InvocationOnMock invocation) -> donation);


		Donation returnedDonation = donationService.getDonationById(donation.getId());

		assertNotNull(returnedDonation);
		assertEquals(donation.getName(), returnedDonation.getName());
		assertEquals(donation.getDonator().getEmailAddress(), returnedDonation.getDonator().getEmailAddress());
		assertEquals(donation.getRequestDate(), returnedDonation.getRequestDate());
		assertEquals(donation.getMuseum().getId(), returnedDonation.getMuseum().getId());
	}

	/**
	 *
	 * Test method that attempts to retrieve a Donation object with an invalid ID
	 *
	 *
	 * @author Haroun Guessous
	 */
	@Test
	public void testGetDonationByInvalidId() {
		final int invalidDonationId = -1;

		when(donationRepo.findDonationById(invalidDonationId))
				.thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> donationService.getDonationById(invalidDonationId));

		assertEquals("Donation not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}


}
