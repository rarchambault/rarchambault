package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTests {

	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	OwnerRepository ownerRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	OwnerService ownerService;

	/**
	 *
	 * Test method that creates an Owner object from pre-determined parameters
	 *
	 * @author Haroun Guessous
	 */
	@Test
	public void testCreateOwner() {
		final Owner owner = new Owner("test@owner.com", "password", "Test Owner");

		when(ownerRepo.save(any(Owner.class))).thenAnswer((InvocationOnMock invocation) -> owner);

		OwnerRequestDto request = new OwnerRequestDto(owner.getName(), owner.getEmailAddress(), owner.getPassword());
		OwnerResponseDto returnedOwner = ownerService.createOwner(request);

		assertNotNull(returnedOwner);
		assertEquals(owner.getName(), returnedOwner.getName());
		assertEquals(owner.getEmailAddress(), returnedOwner.getEmailAddress());

		verify(ownerRepo, times(1)).save(any(Owner.class));
	}

	/**
	 *
	 * Test method that attempts to retrieve a Owner object from a given email address
	 *
	 * @author Haroun Guessous
	 */
	@Test
	public void testGetOwnerByEmailAddress() {

		final Owner owner = new Owner("test@visitor.com", "abc@123456", "Test Visitor");

		when(ownerRepo.findOwnerByEmailAddress(owner.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> owner);

		Owner returnedOwner = ownerService.getOwnerByEmailAddress(owner.getEmailAddress());

		assertNotNull(returnedOwner);
		assertEquals(owner.getName(), returnedOwner.getName());
		assertEquals(owner.getEmailAddress(), returnedOwner.getEmailAddress());
		assertEquals(owner.getPassword(), returnedOwner.getPassword());
	}

	/**
	 *
	 * Test method that attempts to retrieve a Room object with an invalid email address
	 *
	 * @author Haroun Guessous
	 */
	@Test
	public void testGetOwnerByInvalidEmailAddress() {

		final String ownerEmail = "test@fake.com";

		when(ownerRepo.findOwnerByEmailAddress(ownerEmail))
				.thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> ownerService.getOwnerByEmailAddress(ownerEmail));

		assertEquals("Owner not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

}
