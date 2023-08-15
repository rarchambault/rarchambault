package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArtworkRepositoryTests {
	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Clears database entries created by tests.
	 * @author Pinak Ghosh
	 */
	@AfterEach
	public void clearDatabase() {
		artworkRepository.deleteAll();
		roomRepository.deleteAll();
		visitorRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	/**
	 * This test Creates an instance of Artwork, saves it in
	 * the database, and checks if the related data has been conserved.
	 * @author Pinak Ghosh
	 */
	@Test
	public void testPersistAndLoadArtwork() {
		// Create all necessary objects

		//Creates Owner
		Owner owner = new Owner("owner@mail.com", "password", "John Smith");
		ownerRepository.save(owner);

		//creates Museum
		Museum museum = new Museum(owner);
		//sets Museum parameters
		museumRepository.save(museum);

		//creates Room
		Room room = new Room(RoomType.Large, museum);
		roomRepository.save(room);

		//creates Visitor
		Visitor visitor = new Visitor("test@visitor.com", "abc@123456", "Visitor Test", museum);
		visitorRepository.save(visitor);

		//creates Artwork
		Artwork artwork = new Artwork();
		//sets Artwork
		artwork.setMuseum(museum);
		artwork.setRoom(room);
		artwork.setVisitorOnWaitlist(visitor);
		artwork.setIsAvailableForLoan(true);
		artwork.setLoanFee(2);
		artwork.setName("bold and brash");
		artwork.setIsAvailableForLoan(false);

		//saved Artwork is the artwork instance created above, fetched artwork is the artwork found in the database
		Artwork savedArtwork = artworkRepository.save(artwork);
		int savedId = savedArtwork.getId();
		Artwork fetchedArtwork = artworkRepository.findArtworkById(savedId);

		//test cases
		assertNotNull(fetchedArtwork);
		assertEquals(savedArtwork.getName(), fetchedArtwork.getName());
		assertEquals(savedArtwork.getLoanFee(), fetchedArtwork.getLoanFee());
		assertEquals(savedArtwork.getIsAvailableForLoan(), fetchedArtwork.getIsAvailableForLoan());
		assertEquals(savedId, fetchedArtwork.getId());
		assertNotNull(savedArtwork.getRoom());
		assertNotNull(savedArtwork.getVisitorOnWaitlist());
		assertNotNull(savedArtwork.getMuseum());
	}
}
