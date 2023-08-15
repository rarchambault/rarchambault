package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

public interface VisitorRepository extends CrudRepository<Visitor, String> {
	public Visitor findVisitorByEmailAddress(String emailAddress);
}