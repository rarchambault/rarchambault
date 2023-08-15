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

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DayRepositoryTests {
	@Autowired
	private DayRepository dayRepository;

	@Autowired
	private MuseumRepository museumRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Method that clears database entries after each test
	 * 
	 * @author Mohammed Elsayed
	 */
	@AfterEach
	public void clearDatabase() {
		dayRepository.deleteAll(); // delete the day entry
		museumRepository.deleteAll(); // delete the museum entry
		ownerRepository.deleteAll(); // delete the owner entry
	}

	/**
	 * Test method that creates a day entry (and its associated entries) and then
	 * saves it in the database. It then fetches it from the database, and checks
	 * whether its data has been saved.
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testPersistAndLoadDay() {
		// create an owner object and set its attributes
		Owner owner = new Owner("owner@mail.com", "password", "John Smith");
		ownerRepository.save(owner); // save the owner entry before saving the day

		// create a museum object and set its attributes
		Museum museum = new Museum(owner);
		museumRepository.save(museum); // save the museum entry before saving the day

		// create a day object and set its attributes
		Day day = new Day();
		int openingHour = 8;
		int closingHour = 16;
		Date date = Date.valueOf("2022-11-10");
		boolean isHoliday = false;
		day.setOpeningHour(openingHour);
		day.setClosingHour(closingHour);
		day.setDate(date);
		day.setIsHoliday(isHoliday);
		day.setMuseum(museum);

		// save the day entry
		Day newDay = dayRepository.save(day);
		Date newDate = newDay.getDate();

		// fetch the saved day object from the database
		Day fetchedDay = dayRepository.findDayByDate(newDate);

		// assert that all saved entries have the correct attributes
		assertNotNull(fetchedDay);
		assertEquals(newDate, fetchedDay.getDate());
		assertEquals(newDay.getOpeningHour(), fetchedDay.getOpeningHour());
		assertEquals(newDay.getClosingHour(), fetchedDay.getClosingHour());
		assertEquals(newDay.getIsHoliday(), fetchedDay.getIsHoliday());
		assertEquals(newDay.getMuseum().getId(), fetchedDay.getMuseum().getId());
		assertEquals(newDay.getMuseum().getOwner().getEmailAddress(),
				fetchedDay.getMuseum().getOwner().getEmailAddress());
		assertEquals(newDay.getMuseum().getOwner().getName(), fetchedDay.getMuseum().getOwner().getName());
		assertEquals(newDay.getMuseum().getOwner().getPassword(), fetchedDay.getMuseum().getOwner().getPassword());

	}
}
