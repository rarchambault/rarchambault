package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VisitorRepositoryTests {
	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Method that clears database entries made by the test methods
	 * 
	 * @author Alec Tufenkjian
	 */
	@AfterEach
	public void clearDatabase() {
		visitorRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	/**
	 * Test method that creates a visitor, persists it, loads it, and checks whether
	 * related data has been conserved.
	 * 
	 * @author Alec Tufenkjian
	 */
	@Test
	public void testPersistAndLoadVisitor() {

		// Create a test owner for the museum
		Owner owner = new Owner();
		owner.setEmailAddress("owner@test.com");
		owner.setName("Test Owner");
		owner.setPassword("abc@123456");
		ownerRepository.save(owner);

		Museum museum = new Museum();
		museum.setOwner(owner);
		museumRepository.save(museum);

		Visitor testVisitor = new Visitor("test@visitor.com", "abc@123456", "Visitor Test", museum);
		Visitor savedVisitor = visitorRepository.save(testVisitor);
		Visitor fetchedVisitor = visitorRepository.findVisitorByEmailAddress(savedVisitor.getEmailAddress());

		assertEquals(savedVisitor.getEmailAddress(), fetchedVisitor.getEmailAddress());
		assertEquals(savedVisitor.getPassword(), fetchedVisitor.getPassword());
		assertEquals(savedVisitor.getName(), fetchedVisitor.getName());
		assertNotNull(fetchedVisitor.getMuseum());
	}
}
