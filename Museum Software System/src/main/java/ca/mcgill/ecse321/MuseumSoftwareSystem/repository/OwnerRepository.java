package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {
	public Owner findOwnerByEmailAddress(String emailAddress);
}