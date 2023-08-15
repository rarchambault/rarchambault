package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

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

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.VisitorRepository;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	VisitorRepository visitorRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	VisitorService visitorService;

	@Mock
	MuseumService museumService;

	/**
	 * Test that creates a visitor, mock saves it, then compares the saved visitor with original visitor
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testCreateVisitor() {
		final Owner owner = new Owner("test@owner.com", "password", "Test Owner");
		final Museum museum = new Museum(owner, "Test Museum");
		final Visitor visitor = new Visitor("test@visitor.com","abc@123456" , "Test Visitor", museum);
		museum.setId(1);

		when(museumService.getMuseumById(visitor.getMuseum().getId())).thenAnswer((InvocationOnMock invocation) -> museum);
		when(visitorRepo.save(any(Visitor.class))).thenAnswer((InvocationOnMock invocation) -> visitor);

		VisitorRequestDto request = new VisitorRequestDto(visitor.getName(), visitor.getEmailAddress(), visitor.getPassword(), visitor.getMuseum().getId());
		VisitorResponseDto returnedVisitor = visitorService.createVisitor(request);

		assertNotNull(returnedVisitor);
		assertEquals(visitor.getName(), returnedVisitor.getName());
		assertEquals(visitor.getEmailAddress(), returnedVisitor.getEmailAddress());
		assertEquals(visitor.getPassword(), returnedVisitor.getPassword());
		assertEquals(visitor.getMuseum().getId(), returnedVisitor.getMuseum().getId());

		verify(visitorRepo, times(1)).save(any(Visitor.class));
	}

	/**
	 * Creates a visitor, then fetches for it by emailAddress, then compares it with original visitor
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testGetVisitorByEmailAddress() {

		final Visitor visitor = new Visitor("test@visitor.com", "abc@123456", "Test Visitor", new Museum());

		when(visitorRepo.findVisitorByEmailAddress(visitor.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> visitor);

		Visitor returnedVisitor = visitorService.retrieveVisitorByEmail(visitor.getEmailAddress());

		assertNotNull(returnedVisitor);
		assertEquals(visitor.getName(), returnedVisitor.getName());
		assertEquals(visitor.getEmailAddress(), returnedVisitor.getEmailAddress());
		assertEquals(visitor.getPassword(), returnedVisitor.getPassword());
	}

	/**
	 * Test that fetches for a nonexistent visitor, and validates exception behaviour
	 *
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testGetVisitorByInvalidEmailAddress() {

		final String visitorEmail = "test@fake.com";

		when(visitorRepo.findVisitorByEmailAddress(visitorEmail))
				.thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> visitorService.retrieveVisitorByEmail(visitorEmail));

		assertEquals("Visitor not found.", ex.getMessage());
		assertEquals(HttpStatus.OK, ex.getStatus());
	}
}
