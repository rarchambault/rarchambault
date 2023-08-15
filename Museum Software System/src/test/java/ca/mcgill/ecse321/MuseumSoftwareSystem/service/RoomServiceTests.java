package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {


	@Mock
	MuseumService museumService;
	@Mock
	RoomRepository roomRepo;
	// Get a service that uses the mock repository
	@InjectMocks
	RoomService roomService;

	/**
	 *
	 *Test method that attempts to retrieve a Room object from a given ID
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testFindRoomById() {
		Museum museum = new Museum("Owner@mail.ca", "Rich Homie Quan", "Owner");
		RoomType type = RoomType.Large;

		final int id = 1;
		final Museum aMuseum = museum;
		final RoomType aType = type;
		final Room aRoom = new Room(type, museum);


		when(roomRepo.findRoomById(id)).thenAnswer((InvocationOnMock invocation) -> aRoom);

		Room room = roomService.findRoomById(id);

		assertNotNull(room);
		assertEquals(museum, room.getMuseum());
		assertEquals(type, room.getType());

	}

	/**
	 *Test method that attempts to retrieve a Room object with an invalid ID
	 *
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testFindInvalidRoomById() {

		final int invalidId = 99;
		when(roomRepo.findRoomById(invalidId)).thenAnswer((InvocationOnMock innvocation) -> null);

		MuseumSoftwareSystemException ex = assertThrows(MuseumSoftwareSystemException.class,
				() -> roomService.findRoomById(invalidId));

		assertEquals("Room Not Found.", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	/**
	 *
	 *
	 * Test method that creates a Room object from pre-determined parameters
	 *
	 * @author Pinak Ghosh
	 */
	@Test
	public void testCreateRoom() {

		final String museumEmail = "pinak@mail.com";
		final String museumPass = "pass123";
		final String museumName = "Pinak";
		final RoomType type = RoomType.Large;
		final Museum museum = new Museum(museumEmail,museumPass,museumName);
		museum.setName(museumName);
		final Room room = new Room();
		room.setMuseum(museum);
		room.setType(type);

		RoomRequestDto request = new RoomRequestDto();
		request.setType(type);
		request.setMuseumId(museum.getId());

		when(museumService.getMuseumById(museum.getId())).thenAnswer((InvocationOnMock invocation) -> museum);

		when(roomRepo.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> room);

		RoomResponseDto returnedRoom = roomService.createRoom(request);


		assertNotNull(returnedRoom);
		assertEquals(type, returnedRoom.getType());
		assertEquals(museumName, returnedRoom.getMuseum().getName());
		verify(roomRepo, times(1)).save(any(Room.class));

	}
}
