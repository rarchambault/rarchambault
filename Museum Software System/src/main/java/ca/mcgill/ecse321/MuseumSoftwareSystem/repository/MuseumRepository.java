package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;

public interface MuseumRepository extends CrudRepository<Museum, Integer> {
	public Museum findMuseumById(int id);

	public Museum findMuseumByName(String name);

	public Museum findMuseumByOwnerEmailAddress(String ownerEmailAddress);
}