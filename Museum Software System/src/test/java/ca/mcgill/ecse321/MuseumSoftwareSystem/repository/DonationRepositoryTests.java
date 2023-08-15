package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.ApprovalStatus;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Donation;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DonationRepositoryTests {
	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Method that clears database entries after each test
	 * 
	 * @author Haroun Guessous
	 */
	@AfterEach
	public void clearDatabase() {
		donationRepository.deleteAll();
		visitorRepository.deleteAll();
		museumRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	/**
	 * Test method that creates a donation entry (and its associated entries) and then
	 * saves it in the database. It then fetches it from the database, and checks
	 * whether its data has been saved.
	 * 
	 * @author Haroun Guessous
	 */
	@Test
	public void testPersistAndLoadDonation() {
//Create an owner instance and save it in the ownerrepository
		
		Owner owner = new Owner("jack@museum.com", "password456", "Jack Smith");
		ownerRepository.save(owner);

//Create a museum instance and save it in the museumrepository		
		
		Museum museum = new Museum(owner);
		museumRepository.save(museum);
//Create a Visitor instance and save it in the visitorRepository		

		Visitor visitor = new Visitor();
		visitor.setEmailAddress("Haroun Guessous");
		visitor.setName("Haroun Guessous");
		visitor.setPassword("AAAAA");
		visitor.setMuseum(museum);
		visitorRepository.save(visitor);
//Create a Donation instance and save it in the Donation and save attributes to it, namely the museum and the person/visitor making the donation		

		Donation donation = new Donation();
		donation.setDonator(visitor);
		donation.setName("Haroun Guessous");
		donation.setRequestDate(Date.valueOf(LocalDate.EPOCH));
		donation.setStatus(ApprovalStatus.Approved);
		donation.setMuseum(museum);
		Donation savedDonation = donationRepository.save(donation);
		int savedId = savedDonation.getId();
//Making sure the persistanceData works
		assertNotNull(donation.getDonator());
		assertEquals(donation.getName(), "Haroun Guessous");
		assertEquals(donation.getRequestDate(), Date.valueOf(LocalDate.EPOCH));
		assertEquals(donation.getId(), savedId);
		assertEquals(donation.getStatus(), ApprovalStatus.Approved);

		assertNotNull(donation.getDonator());
		assertNotNull(donation.getMuseum());
	}
}
