package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.DayRepository;

@ExtendWith(MockitoExtension.class)
public class DayServiceTests {
	// Replace the repository with a "mock" that exposes the same interface
	@Mock
	DayRepository dayRepo;

	// Get a service that uses the mock repository
	@InjectMocks
	DayService dayService;

	@Mock
	MuseumService museumService;
	
	@Mock
	OwnerService ownerService;
	
	@Test
	
	/**
	 * Test method that creates a day object from pre-determined parameters
	 * 
	 * @author Mohammed Elsayed
	 */
	public void testCreateDay() {

		final String museumName = "Mohammed's museum";
		final String ownerEmail = "momo@hotmail.com";
		final String ownerPassword = "password";
		final String ownerName = "Momo";
		final Owner owner = new Owner(ownerEmail, ownerPassword, ownerName);
		final Museum museum = new Museum();
		museum.setName(museumName);
		museum.setOwner(owner);
		
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final Day aDay = new Day();
		aDay.setMuseum(museum);
		aDay.setOpeningHour(openingHour);
		aDay.setClosingHour(closingHour);
		aDay.setDate(date);
		aDay.setIsHoliday(isHoliday);
		
		DayRequestDto request = new DayRequestDto();
		request.setMuseumId(openingHour);
		request.setOpeningHour(openingHour);
		request.setClosingHour(closingHour);
		request.setDate(date);
		request.setIsHoliday(isHoliday);
		
		when(dayRepo.save(any(Day.class))).thenAnswer((InvocationOnMock invocation) -> aDay);
		
		DayResponseDto returnedDay = dayService.createDay(request);
		
		assertNotNull(returnedDay);
		assertEquals(ownerEmail, returnedDay.getMuseum().getOwner().getEmailAddress());
		assertEquals(museumName, returnedDay.getMuseum().getName());
		assertEquals(openingHour, returnedDay.getOpeningHour());
		assertEquals(closingHour, returnedDay.getClosingHour());
		assertEquals(date, returnedDay.getDate());
		assertEquals(isHoliday, returnedDay.getIsHoliday());
		verify(dayRepo,times(1)).save(any(Day.class));
	}
	
	/**
	 * Test method that attempts to retrieve a day object with an invalid date
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testFindDayByInvalidDate(){

		final Date invalidDate = new Date(13-13-2022);
		when(dayRepo.findDayByDate(invalidDate)).thenAnswer((InvocationOnMock innvocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class, () -> dayService.getDayByDate(invalidDate));

		assertEquals("Day Not Found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}
	
	/**
	 * Test method that attempts to retrieve a day object by date
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testFindDayByDate(){
		// Tell the mocked repository how to behave
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = new Date(11-11-2022);
		final boolean isHoliday = false;
		Museum museum = new Museum("Owner2@gmail.ca","password123","Mr.Owner2");
		final Day aDay = new Day();
		aDay.setOpeningHour(openingHour);
		aDay.setClosingHour(closingHour);
		aDay.setDate(date);
		aDay.setIsHoliday(isHoliday);
		aDay.setMuseum(museum);
		
		when(dayRepo.findDayByDate(date)).thenAnswer((InvocationOnMock invocation) -> aDay);
		
		// Test that the service behaves properly
		Day fetchedDay = dayService.getDayByDate(date);
		
		assertNotNull(aDay);
		assertEquals(openingHour, fetchedDay.getOpeningHour());
		assertEquals(closingHour, fetchedDay.getClosingHour());
		assertEquals(date, fetchedDay.getDate());
		assertEquals(isHoliday, fetchedDay.getIsHoliday());
		assertEquals(museum, fetchedDay.getMuseum());
		
	}
	
	/**
	 * Test method that creates a shift, deletes it, and then handle the exception when finding it
	 * 
	 * @author Mohammed Elsayed
	 */
	@Test
	public void testDeleteShift() {
		Museum museum = new Museum("Owner@gmail.ca","password123","Mr.Owner");
		
		final Museum aMuseum = museum;
		final int openingHour = 8;
		final int closingHour = 16;
		final Date date = Date.valueOf("2022-11-10");
		final boolean isHoliday = false;
		final Day aDay = new Day();
		aDay.setMuseum(aMuseum);
		aDay.setOpeningHour(openingHour);
		aDay.setClosingHour(closingHour);
		aDay.setDate(date);
		aDay.setIsHoliday(isHoliday);
		
		dayRepo.delete(aDay);
		
		when(dayRepo.findDayByDate(date)).thenAnswer((InvocationOnMock innvocation) -> null);
		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class, () -> dayService.getDayByDate(date));

		assertEquals("Day Not Found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

}
