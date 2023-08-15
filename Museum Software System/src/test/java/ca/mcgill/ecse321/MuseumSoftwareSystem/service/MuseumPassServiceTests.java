package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

/**
 * Please direct all questions and queries regarding this code to the
 * rightful owner and maintainer of this code @author muradgohar
 */

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

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.MuseumPass;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.PassType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumPassRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MuseumPassServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	MuseumPassRepository museumPassRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	MuseumPassService museumPassService;

	@Mock
	MuseumService museumService;

	@Mock
	VisitorService visitorService;
	
	//Tests the CreateMuseumPass Method

	@Test
	public void testCreateMuseumPass() {
		// MuseumPass constructor
		final MuseumPass museumpass = new MuseumPass();
		// Museum constructor
		final Museum trottier = new Museum("owner@mail.com", "password", "murad");
		// Variable declarations
		final PassType yung = PassType.Child;
		
		// Attribute Assignments for Museum
		trottier.setName("Trottier");
		trottier.setId(5);

		// Constructs a new Visitor object
		Visitor visiTHOR = new Visitor();
		
		// Attribute Assignment for Visitor
		visiTHOR.setEmailAddress("thor.orgenson@mail.com");
		visiTHOR.setMuseum(trottier);
		visiTHOR.setName("Thor");
		visiTHOR.setPassword("mjolnir");

		// Constructs a new Museum Pass Request DTO
		MuseumPassRequestDto request = new MuseumPassRequestDto();
		// Uses setter methods to ensure the right attributes are instantiated
		request.setMuseumId(trottier.getId());
		request.setType(yung);
		request.setVisitorEmailAddress(visiTHOR.getEmailAddress());

		// Tells Mock how to behave
		when(visitorService.retrieveVisitorByEmail(visiTHOR.getEmailAddress()))
				.thenAnswer((InvocationOnMock invocation) -> visiTHOR);
		when(museumService.getMuseumById(5)).thenAnswer((InvocationOnMock invocation) -> trottier);
		when(museumPassRepo.save(any(MuseumPass.class)))
				.thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

		MuseumPassResponseDto returnedMuseumPass = museumPassService.createMuseumPass(request);

		// Ensures everything was properly instantiated in the database 
		assertNotNull(returnedMuseumPass);
		assertEquals(returnedMuseumPass.getPrice(), 0);
		assertEquals(returnedMuseumPass.getVisitor(), visiTHOR);

		verify(museumPassRepo, times(1)).save(any(MuseumPass.class));
	}

	// Other tests
	

	// Tests the GetMuseumPassById method
	@Test
	public void testGetMuseumPassById() {
		// Variable Declaration
		final int id = 7;
		// Museum pass constructor
		final MuseumPass museumpass = new MuseumPass();
		// Museum Pass Attribute assignment
		museumpass.setId(id);
		// Tells mock how to behave
		when(museumPassRepo.findMuseumPassById(id)).thenAnswer((InvocationOnMock invocation) -> museumpass);
		// Retrieves Museum Pass from the database
		MuseumPass fetchedMuseumPass = museumPassService.findMuseumPassById(id);

		// Ensures everything was properly instantiated in the database
		assertNotNull(fetchedMuseumPass);
		assertEquals(id, fetchedMuseumPass.getId());

	}

	// Tests the GetMuseumPassByInvalidId method
	@Test
	public void testGetMuseumPassByInvalidId() {
		// Variable Declaration
		final int invalidId = 514;
		// Tells Mock what to do
		when(museumPassRepo.findMuseumPassById(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);

		MuseumSoftwareSystemException exceptionalexception = assertThrows(MuseumSoftwareSystemException.class,
				() -> museumPassService.findMuseumPassById(invalidId));
		// Ensures the correct response occurs
		assertEquals("Museum Pass not found.", exceptionalexception.getMessage());
		assertEquals(HttpStatus.OK, exceptionalexception.getStatus());
	}

	// Tests the GetMuseumPassByVisitor methods
	@Test
	public void testGetMuseumPassesByVisitor() {
		// Creates a new visitor object
		final Visitor visiLoki = new Visitor();
		// Variable Declaration
		final String visitoremail = "loki.odenson@mail.asgard.com";
		// Instantiates the correct attributes to the Visitor Object
		visiLoki.setEmailAddress(visitoremail);
		// Museum Pass Constructor
		final MuseumPass museumpass = new MuseumPass();
		// Assigns the Visitor to the Museum Pass
		museumpass.setVisitor(visiLoki);

		ArrayList<MuseumPass> mockedResponse = new ArrayList<MuseumPass>();
		mockedResponse.add(museumpass);

		// Tells Mock what to do 
		when(museumPassRepo.findMuseumPassesByVisitorEmailAddress(visitoremail))
				.thenAnswer((InvocationOnMock invocation) -> mockedResponse);

		// Retrieves a Museum Pass from the Database
		List<MuseumPass> returningMuseumPasses = museumPassService.findMuseumPassesByVisitor(visitoremail);

		// Ensures everything is working as it should
		assertNotNull(returningMuseumPasses);
		assertEquals(returningMuseumPasses.get(0).getVisitor(), visiLoki);

	}

	// Tests System Behavior for when an attempt is made to retrieve a museum pass using an invalid visitor
	@Test
	public void testGetMuseumPassByInvalidVisitor() {
		// Constructs a new Visitor Object
		final Visitor invalidVisitor = new Visitor();
		//  Variable Declaration
		String invalidVisitorName = "batman";
		// Attribute Assignment for visitor object
		invalidVisitor.setName(invalidVisitorName);
		// Tells Mock what to do
		MuseumSoftwareSystemException unexceptionalexception = assertThrows(MuseumSoftwareSystemException.class,
				() -> museumPassService.findMuseumPassesByVisitor(invalidVisitorName));

		// Ensures system behavior is as intended
		assertEquals("Museum Passes not found.",
				unexceptionalexception.getMessage());
		assertEquals(HttpStatus.OK, unexceptionalexception.getStatus());
	}

}
