package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;

@ExtendWith(MockitoExtension.class)
public class MuseumServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	MuseumRepository museumRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	MuseumService museumService;

	@Mock
	OwnerService ownerService;

	/**
	 * Test method that creates a Museum object from pre-determined parameters
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testCreateMuseum() {
		final String museumName = "Roxanne's museum";
		final String ownerEmail = "roxanne@gmail.com";
		final String ownerPassword = "password";
		final String ownerName = "Roxanne";
		final Owner owner = new Owner(ownerEmail, ownerPassword, ownerName);
		final Museum museum = new Museum();
		museum.setName(museumName);
		museum.setOwner(owner);

		MuseumRequestDto request = new MuseumRequestDto();
		request.setName(museumName);
		request.setOwnerEmailAddress(ownerEmail);

		// Return owner when trying to find an owner with the specified email
		when(ownerService.getOwnerByEmailAddress(ownerEmail)).thenAnswer((InvocationOnMock invocation) -> owner);

		// Just return the Museum with no modification
		when(museumRepo.save(any(Museum.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		MuseumResponseDto returnedMuseum = museumService.createMuseum(request);

		assertNotNull(returnedMuseum);
		assertEquals(museumName, returnedMuseum.getName());
		assertEquals(ownerEmail, returnedMuseum.getOwner().getEmailAddress());

		// Check that the service actually saved the person
		verify(museumRepo, times(1)).save(any(Museum.class));
	}

	/**
	 * Test method that attempts to retrieve a Museum object from a given ID
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetMuseumById() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final String name = "Marwan's museum";
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setName(name);
		when(museumRepo.findMuseumById(id)).thenAnswer((InvocationOnMock invocation) -> museum);

		// Test that the service behaves properly
		Museum fetchedMuseum = museumService.getMuseumById(id);

		assertNotNull(fetchedMuseum);
		assertEquals(name, fetchedMuseum.getName());
		assertEquals("Marwan", fetchedMuseum.getOwner().getName());
		assertEquals("marwan@hotmail.com", fetchedMuseum.getOwner().getEmailAddress());
		assertEquals("museum123", fetchedMuseum.getOwner().getPassword());
	}

	/**
	 * Test method that attempts to retrieve a Museum object by name
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetMuseumByName() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final String name = "Marwan's museum";
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setName(name);
		when(museumRepo.findMuseumByName(name)).thenAnswer((InvocationOnMock invocation) -> museum);

		// Test that the service behaves properly
		Museum fetchedMuseum = museumService.getMuseumByName(name);

		assertNotNull(fetchedMuseum);
		assertEquals(name, fetchedMuseum.getName());
		assertEquals("Marwan", fetchedMuseum.getOwner().getName());
		assertEquals("marwan@hotmail.com", fetchedMuseum.getOwner().getEmailAddress());
		assertEquals("museum123", fetchedMuseum.getOwner().getPassword());
	}

	/**
	 * Test method that attempts to retrieve a Museum object by its owner's email
	 * address
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetMuseumByOwnerEmailAddress() {
		// Tell the mocked repository how to behave
		final int id = 1;
		final String name = "Marwan's museum";
		final Museum museum = new Museum("marwan@hotmail.com", "museum123", "Marwan");
		museum.setName(name);
		when(museumRepo.findMuseumByOwnerEmailAddress("marwan@hotmail.com"))
				.thenAnswer((InvocationOnMock invocation) -> museum);

		// Test that the service behaves properly
		Museum fetchedMuseum = museumService.getMuseumByOwnerEmailAddress("marwan@hotmail.com");

		assertNotNull(fetchedMuseum);
		assertEquals(name, fetchedMuseum.getName());
		assertEquals("Marwan", fetchedMuseum.getOwner().getName());
		assertEquals("marwan@hotmail.com", fetchedMuseum.getOwner().getEmailAddress());
		assertEquals("museum123", fetchedMuseum.getOwner().getPassword());
	}

	/**
	 * Test method that attempts to retrieve a Museum object with an invalid ID
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetMuseumByInvalidId() {
		final int invalidId = 99;
		when(museumRepo.findMuseumById(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> museumService.getMuseumById(invalidId));

		assertEquals("Museum not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that attempts to retrieve a museum with an invalid name
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetMuseumByInvalidName() {
		final String invalidName = "Fake museum";
		when(museumRepo.findMuseumByName(invalidName)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> museumService.getMuseumByName(invalidName));

		assertEquals("Museum not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 * Test method that attempts to retrieve a Museum object with an invalid owner
	 * email address
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testGetMuseumByInvalidEmailAdress() {
		final String invalidEmail = "fake@gmail.com";
		when(museumRepo.findMuseumByOwnerEmailAddress(invalidEmail)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> museumService.getMuseumByOwnerEmailAddress(invalidEmail));

		assertEquals("Museum not found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}
}
