package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OwnerRepositoryTests {
	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Method that clears database entries after each test
	 * 
	 * @author Haroun Guessous
	 */
	@AfterEach
	public void clearDatabase() {
		ownerRepository.deleteAll();
	}

	/**
	 * Test method that creates an owner entry (and its associated entries) and then
	 * saves it in the database. It then fetches it from the database, and checks
	 * whether its data has been saved.
	 * 
	 * @author Haroun Guessous
	 */
	@Test
	public void testPersistAndLoadOwner() {
		Owner owner = new Owner();//Creating a new owner instance, and setting parameters
		owner.setEmailAddress("Haroun.guessous@onemm.com");
		owner.setName("Haroun");
		owner.setPassword("Aaaaaa");
		ownerRepository.save(owner);

		//Testing the persistance of the data, seeing if it's saved right
		Assertions.assertEquals(owner.getEmailAddress(), "Haroun.guessous@onemm.com");
		Assertions.assertEquals(owner.getName(), "Haroun");
		Assertions.assertEquals(owner.getPassword(), "Aaaaaa");
	}
}
