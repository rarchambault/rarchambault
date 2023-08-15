package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
	public Room findRoomById(int id);
}