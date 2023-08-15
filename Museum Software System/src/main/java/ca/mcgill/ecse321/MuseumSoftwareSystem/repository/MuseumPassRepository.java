package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.MuseumPass;

import java.sql.Date;
import java.util.List;

public interface MuseumPassRepository extends CrudRepository<MuseumPass, Integer> {
	public MuseumPass findMuseumPassById(int id);

	public List<MuseumPass> findMuseumPassesByVisitorEmailAddress(String visitorEmailAddress);
}