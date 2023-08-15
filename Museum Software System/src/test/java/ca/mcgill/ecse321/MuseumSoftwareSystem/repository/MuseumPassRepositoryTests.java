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
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.MuseumPass;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.PassType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MuseumPassRepositoryTests {
	@Autowired
	private MuseumPassRepository museumPassRepository;
	@Autowired
	private VisitorRepository visitorRepository;
	@Autowired
	private OwnerRepository ownerRepository;
	@Autowired
	private MuseumRepository museumRepository;

	/**
	 * Method that clears database entries made by the test methods
	 * 
	 * @author Murad Gohar
	 */
	@AfterEach
	public void clearDatabase() {
		museumPassRepository.deleteAll();
		visitorRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	/**
	 * Test method that creates a museum pass, persists it, loads it, and checks
	 * whether related data has been conserved.
	 * 
	 * @author Murad Gohar
	 */
	@Test
	public void testPersistAndLoadMuseumPass() {
		// Create Object

		// Variables needed for Museum Pass
		double price = 420.69;
		PassType old = PassType.Senior;

		// For Visitor - Anakin
		String name = "Anakin";
		String email = "anakin.skywalker@mail.mcgill.ca";
		String password = "password";

		// For Owner - Palpatine
		String name2 = "Palpatine";
		String email2 = "palpatine@mcgill.ca";
		String password2 = "password2";

		// Owner Constructor
		Owner palpatine = new Owner();
		palpatine.setName(name2);
		palpatine.setEmailAddress(email2);
		palpatine.setPassword(password2);
		ownerRepository.save(palpatine);

		// Museum Constructor
		Museum clubhouse = new Museum();
		clubhouse.setOwner(palpatine);
		museumRepository.save(clubhouse);

		// Visitor Constructor
		Visitor anakin = new Visitor();
		anakin.setName(name);
		anakin.setEmailAddress(email);
		anakin.setPassword(password);
		anakin.setMuseum(clubhouse);
		visitorRepository.save(anakin);
		// Museum Pass Constructor
		MuseumPass test = new MuseumPass();
		test.setPrice(price);
		test.setType(old);
		test.setVisitor(anakin);
		test.setMuseum(clubhouse);
		museumPassRepository.save(test);

		int id = test.getId();

		// Read Object from database
		test = museumPassRepository.findMuseumPassById(id);

		// Assert that object has correct attributes and relations
		assertNotNull(test);
		assertEquals(price, test.getPrice());
		assertEquals(old, test.getType());
		assertNotNull(test.getMuseum());
		assertNotNull(test.getVisitor());
	}
}
