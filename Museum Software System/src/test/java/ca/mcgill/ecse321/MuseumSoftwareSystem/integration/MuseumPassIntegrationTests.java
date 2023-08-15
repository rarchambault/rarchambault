package ca.mcgill.ecse321.MuseumSoftwareSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

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

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.MuseumPass;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.PassType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MuseumPassIntegrationTests {

	/**
	 * Please direct all questions and queries regarding this code to the
	 * rightful owner and maintainer of this code @author muradgohar
	 */
	
	@Autowired
	private TestRestTemplate client;

	@Autowired
	private MuseumPassRepository museumPassRepo;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private VisitorRepository visitorRepo;
	
	@Autowired
	private MuseumRepository museumRepo;

	@Autowired
	private OwnerRepository ownerRepo;
	
	//Deletes all information saved in the Repository's associated with the tests in this section

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		museumPassRepo.deleteAll();
		visitorRepo.deleteAll();
		roomRepository.deleteAll();
		museumRepo.deleteAll();
		ownerRepo.deleteAll();
	}

	// Annotate Test methods with @Test
	
	// Tests the tests detailed below 
	@Test 
	public void testTests() {
		String email = "thor.odenson@mail.asgard.ca";
		int id = testCreateMuseumPass().getId();
		testFindMuseumPassById(id);
		testFindMuseumPassesByVisitor(email);
	}
	
	// Tests the Create Museum Pass method
	@Test
	private MuseumPassResponseDto testCreateMuseumPass() {
		//Variable declarations for Museum
		String museumName = "Marwan's museum";
		String ownerName = "Marwan";
		String ownerEmailAddress = "marwan@mail.com";
		String ownerPassword = "museum123";
		Date date = Date.valueOf("2022-12-01");

		//Variable Assignments to OwnerRequest 
		OwnerRequestDto ownerRequest = new OwnerRequestDto();
		ownerRequest.setEmailAddress(ownerEmailAddress);
		ownerRequest.setName(ownerName);
		ownerRequest.setPassword(ownerPassword);

		// Post Owner to create the museum
		ResponseEntity<OwnerResponseDto> ownerResponse = client.postForEntity("/owner", ownerRequest,
				OwnerResponseDto.class);

		//Variable Assignments to MuseumRequest
		MuseumRequestDto museumRequest = new MuseumRequestDto();
		museumRequest.setName(museumName);
		museumRequest.setOwnerEmailAddress(ownerEmailAddress);
		
		assertEquals(HttpStatus.CREATED, ownerResponse.getStatusCode(), "Response has correct status! Yay!");
		
		
		ResponseEntity<MuseumResponseDto> museumResponse = client.postForEntity("/museum", museumRequest,
				MuseumResponseDto.class); 
		
		assertEquals(HttpStatus.CREATED, museumResponse.getStatusCode(), "Response has correct status! Yay!");
		
		//Variable Declarations and Assignments for Visitor
		 PassType yung = PassType.Child;
		 int id = 7;
		 Visitor visiTHOR = new Visitor();
		 String name = "Thor";
		 String password = "mjnolir";
		 String email = "thor.odenson@mail.asgard.ca";
		 visiTHOR.setName(name);
		 visiTHOR.setEmailAddress(email);
		 visiTHOR.setPassword(password);
		 
		 // MuseumPassRequest creation and assignments to give it the right attributes
		MuseumPassRequestDto museumPassRequest = new MuseumPassRequestDto();
		museumPassRequest.setType(yung);
		museumPassRequest.setVisitorEmailAddress(email);
		museumPassRequest.setMuseumId(museumResponse.getBody().getId());
		museumPassRequest.setDate(date);
		
		// VisitorRequest creations and assignments to give it the right attributes 
		VisitorRequestDto visitorRequest = new VisitorRequestDto();
		visitorRequest.setName(name);
		visitorRequest.setPassword(password);
		visitorRequest.setEmailAddress(email);
		visitorRequest.setMuseumId(museumResponse.getBody().getId());
	
		
		ResponseEntity<VisitorResponseDto> visitorResponse = client.postForEntity("/visitor", visitorRequest,
				VisitorResponseDto.class);
		
		assertEquals(HttpStatus.CREATED, visitorResponse.getStatusCode(), "Response has correct status! Yay!");
		
		ResponseEntity<MuseumPassResponseDto> response = client.postForEntity("/museumPass", museumPassRequest,
				MuseumPassResponseDto.class);
		
		
		//Checks everything is correct and as it should be 
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status! Yay!");
		assertNotNull(response.getBody(), "Response has body! Yay!");
		assertEquals(0, response.getBody().getPrice(), "Response has correct price! Yay!");
		assertEquals(email, response.getBody().getVisitor().getEmailAddress(), "Response has correct visitor! Yay!");
		assertEquals(date, response.getBody().getDate(), "Response has correct date! Yay!");

		return response.getBody();
		
	}
	
	// Tests the FindMuseumPassById method
	private void testFindMuseumPassById (int id) {
		ResponseEntity<MuseumPassResponseDto> response = client.getForEntity("/museumPass/id/" + id, MuseumPassResponseDto.class);

		//Checks everything is correct and as it should be
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status! Yay!");
		assertNotNull(response.getBody(), "Response has body! Yay!");
		assertEquals(id , response.getBody().getId(), "Response has correct visitor! Yay!");
	}

	// Tests that the correct behavior is observed when trying to find a museum pass using an invalid id
	@Test
	public void testFindMuseumPassByInvalidId() {
		int id = -1;
		ResponseEntity<String> response = client.getForEntity("/museumPass/id/" + id, String.class);

		assertEquals("Museum Pass not found.", response.getBody());
	}

	// Tests the FindMuseumPassesByVisitor method
	private void testFindMuseumPassesByVisitor (String email) {
		ResponseEntity<MuseumPassResponseDto[]> response = client.getForEntity("/museumPass/visitor/" + email, MuseumPassResponseDto[].class);
	
		//Checks everything is correct and as it should be 
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status! Yay!");
		assertNotNull(response.getBody(), "Response has body! Yay!");
		assertEquals(email, response.getBody()[0].getVisitor().getEmailAddress(), "Response has correct visitor! Yay!");
	}

	// Tests that the correct behavior is observed when attempting to find a museum pass using an invalid visitor 
	@Test
	public void testFindMuseumPassesByInvalidVisitor() {
		String invalidVisitor = "inavlid@visitor.com";
		ResponseEntity<String> response = client.getForEntity("/museumPass/visitor/" + invalidVisitor, String.class);

		assertEquals("Museum Passes not found.", response.getBody());
	}
}