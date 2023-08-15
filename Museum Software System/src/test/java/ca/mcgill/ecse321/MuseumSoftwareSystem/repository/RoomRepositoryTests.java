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
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomRepositoryTests {
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private OwnerRepository ownerRepository;
	@Autowired
	private MuseumRepository museumRepository;

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

	/**
	 * Test method that creates a room, persists it, loads it, and checks whether
	 * related data has been conserved.
	 * 
	 * @author Roxanne Archambault
	 */
	@Test
	public void testPersistAndLoadRoom() {
		// Create object
		Owner owner = new Owner("owner@mail.com", "password", "John Smith");
		ownerRepository.save(owner);

		Museum museum = new Museum(owner);
		museumRepository.save(museum);

		Room room = new Room(RoomType.Large, museum);

		// Store object
		Room savedRoom = roomRepository.save(room);
		int savedRoomId = savedRoom.getId();

		// Read from the DB
		Room fetchedRoom = roomRepository.findRoomById(savedRoomId);

		// Check everything is still there
		assertNotNull(fetchedRoom);
		assertEquals(savedRoomId, fetchedRoom.getId());
		assertEquals(room.getType(), fetchedRoom.getType());
		assertNotNull(room.getMuseum());
	}
}
