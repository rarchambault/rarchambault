package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.RoomRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *  Service layer implementation for business logic behind controller calls for
 *  the Room class
 *
 * @author Pinak Ghosh
 */
@Service
public class RoomService {

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	MuseumService museumService;

	/**
	 * Finds Room object given its Id
	 *
	 * @param id
	 * @return Room Object
	 *
	 * @author Pinak Ghosh
	 */
	@Transactional
	public Room findRoomById (int id){
		Room room = roomRepo.findRoomById(id);

		if (room == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Room Not Found.");
		}

		return room;
	}

	@Transactional
	public Iterable<Room> getAllRooms() {

		Iterable<Room> rooms = roomRepo.findAll();

		if (rooms == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Rooms not found.");
		}

		return rooms;
	}

	/**
	 * Creates a new Room object in the database
	 *
	 * @param roomRequest
	 * @return saved Room
	 *
	 * @author Pinak Ghosh
	 *
	 */
	@Transactional
	public RoomResponseDto createRoom(RoomRequestDto roomRequest) {
		Museum museum = museumService.getMuseumById(roomRequest.getMuseumId());

		Room room = new Room();
		room.setType(roomRequest.getType());
		room.setMuseum(museum);

		// Save new object and return it
		room = roomRepo.save(room);
		return new RoomResponseDto(room);
	}
	// Annotate methods with @Transactional
}
