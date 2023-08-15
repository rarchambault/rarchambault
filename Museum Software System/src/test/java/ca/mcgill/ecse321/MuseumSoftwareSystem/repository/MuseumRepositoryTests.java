package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MuseumRepositoryTests {
	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private RoomRepository roomRepository;

	private Museum savedMuseum;

	/**
	 * Method that clears database entries made by the test methods
	 * 
	 * @author Roxanne Archambault
	 */
	@AfterEach
	public void clearDatabase() {
		roomRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	@BeforeEach
	public void createAndSaveMuseum() {
		// Create object
		String emailAddress = "owner@museum.com";
		String password = "password123";
		String name = "Owner John";
		Owner owner = new Owner();
		owner.setEmailAddress(emailAddress);
		owner.setPassword(password);
		owner.setName(name);

		ownerRepository.save(owner);

		Museum museum = new Museum();
		museum.setName("John's museum");
		museum.setOwner(owner);

		// Store object
		savedMuseum = museumRepository.save(museum);
	}

	/**
	 * Test method that creates a museum, persists it, loads it, and checks whether
	 * related data has been conserved.
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testFindMuseumById() {
		// Read from the DB
		Museum fetchedMuseum = museumRepository.findMuseumById(savedMuseum.getId());

		// Check everything is still there
		assertNotNull(fetchedMuseum);
		assertEquals(savedMuseum.getId(), fetchedMuseum.getId());
		assertEquals(savedMuseum.getName(), fetchedMuseum.getName());
		assertEquals(savedMuseum.getOwner().getEmailAddress(), fetchedMuseum.getOwner().getEmailAddress());
		assertEquals(savedMuseum.getOwner().getPassword(), fetchedMuseum.getOwner().getPassword());
		assertEquals(savedMuseum.getOwner().getName(), fetchedMuseum.getOwner().getName());
	}

	@Test
	public void testFindMuseumByName() {
		// Read from the DB
		Museum fetchedMuseum = museumRepository.findMuseumByName(savedMuseum.getName());

		// Check everything is still there
		assertNotNull(fetchedMuseum);
		assertEquals(savedMuseum.getId(), fetchedMuseum.getId());
		assertEquals(savedMuseum.getName(), fetchedMuseum.getName());
		assertEquals(savedMuseum.getOwner().getEmailAddress(), fetchedMuseum.getOwner().getEmailAddress());
		assertEquals(savedMuseum.getOwner().getPassword(), fetchedMuseum.getOwner().getPassword());
		assertEquals(savedMuseum.getOwner().getName(), fetchedMuseum.getOwner().getName());
	}

	@Test
	public void testFindMuseumByOwnerEmailAddress() {
		// Read from the DB
		Museum fetchedMuseum = museumRepository.findMuseumByOwnerEmailAddress(savedMuseum.getOwner().getEmailAddress());

		// Check everything is still there
		assertNotNull(fetchedMuseum);
		assertEquals(savedMuseum.getId(), fetchedMuseum.getId());
		assertEquals(savedMuseum.getName(), fetchedMuseum.getName());
		assertEquals(savedMuseum.getOwner().getEmailAddress(), fetchedMuseum.getOwner().getEmailAddress());
		assertEquals(savedMuseum.getOwner().getPassword(), fetchedMuseum.getOwner().getPassword());
		assertEquals(savedMuseum.getOwner().getName(), fetchedMuseum.getOwner().getName());
	}
}
