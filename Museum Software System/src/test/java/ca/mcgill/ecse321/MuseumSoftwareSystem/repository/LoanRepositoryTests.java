package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoanRepositoryTests {
	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private RoomRepository roomRepository;

	@AfterEach
	/**
	 * Clears database entries created by tests.
	 * @author Pinak Ghosh
	 */
	public void clearDatabase() {
		loanRepository.deleteAll();
		artworkRepository.deleteAll();
		roomRepository.deleteAll();
		visitorRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	/**
	 * This test Creates an instance of Loan, saves it in
	 * the database, and checks if the related data has been conserved.
	 * @author Pinak Ghosh
	 */
	@Test
	public void testPersistAndLoadLoan() {

		// Create all necessary objects

		/*
		Owner, Museum, Room, Artwork and Visitor are created
		because those are parameters for lean.
		 */

		//Creates Owner
		Owner owner = new Owner("james@museum.com", "password456", "James Smith");
		ownerRepository.save(owner);

		//creates Museum
		Museum museum = new Museum(owner);
		//sets Museum parameters
		museumRepository.save(museum);

		//creates Room
		Room artworkRoom = new Room();
		//sets room parameters
		artworkRoom.setType(RoomType.Small);
		artworkRoom.setMuseum(museum);
		roomRepository.save(artworkRoom);

		//creates Artwork
		Artwork artwork = new Artwork();
		//sets Artwork
		artwork.setName("Mona Lisa");
		artwork.setIsAvailableForLoan(false);
		artwork.setRoom(artworkRoom);
		artwork.setMuseum(museum);
		artworkRepository.save(artwork);

		//creates Visitor
		Visitor loaner = new Visitor("monaLisaLover@gmail.com", "ilovemuseums123", "Jacques Cousteau", museum);
		visitorRepository.save(loaner);

		//creates Loan
		Loan loan = new Loan();
		//sets loan parameters
		loan.setArtwork(artwork);
		loan.setLoanee(loaner);
		loan.setMuseum(museum);

		loan.setStartDate(Date.valueOf("2021-02-10"));
		loan.setEndDate(Date.valueOf("2022-02-10"));
		loan.setNumberOfRenewals(2);
		loan.setStatus(ApprovalStatus.Approved);
		loan.setIsLate(true);
		loan.setLateFee(420.69);
		loan.setIsFeePaid(true);

		//saved loan is the loan instance created above, fetched loan is the loan found in the database
		Loan savedLoan = loanRepository.save(loan);
		Loan fetchedLoan = loanRepository.findLoanById(savedLoan.getId());


		//test cases
		assertNotNull(fetchedLoan);
		assertEquals(fetchedLoan.getStartDate(), Date.valueOf("2021-02-10"));
		assertEquals(fetchedLoan.getEndDate(), Date.valueOf("2022-02-10"));
		assertEquals(fetchedLoan.getNumberOfRenewals(), 2);
		assertEquals(fetchedLoan.getStatus(), ApprovalStatus.Approved);
		assertEquals(fetchedLoan.getIsLate(), true);
		assertEquals(fetchedLoan.getLateFee(), 420.69);
		assertEquals(fetchedLoan.getIsFeePaid(), true);
		assertNotNull(fetchedLoan.getArtwork());
		assertNotNull(fetchedLoan.getLoanee());
		assertNotNull(fetchedLoan.getMuseum());
	}
}
