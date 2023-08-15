package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumRepository;

/**
 * Service layer implementation for business logic behind controller calls for
 * the Museum class
 * 
 * @author Roxanne Archambault
 *
 */

@Service
public class MuseumService {

	@Autowired
	MuseumRepository museumRepo;

	@Autowired
	OwnerService ownerService;

	/**
	 * Gets a Museum object given its id
	 * 
	 * @param id ID number for the museum we want to find
	 * @return Fetched Museum object
	 * 
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Museum getMuseumById(int id) {
		Museum museum = museumRepo.findMuseumById(id);
		if (museum == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Museum not found.");
		}
		return museum;
	}

	/**
	 * Gets a Museum object given its name
	 * 
	 * @param name Museum's name
	 * @return Fetched Museum object
	 * 
	 * @author Roxanne Archambault
	 */
	@Transactional
	public Museum getMuseumByName(String name) {
		Museum museum = museumRepo.findMuseumByName(name);
		if (museum == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Museum not found.");
		}
		return museum;
	}

	/**
	 * Gets a Museum object given its owner's email address
	 * 
	 * @param ownerEmailAddress Owner's email address
	 * @return Fetched Museum object
	 */
	@Transactional
	public Museum getMuseumByOwnerEmailAddress(String ownerEmailAddress) {
		Museum museum = museumRepo.findMuseumByOwnerEmailAddress(ownerEmailAddress);
		if (museum == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Museum not found.");
		}
		return museum;
	}

	/**
	 * Creates a new Museum object in the database
	 * 
	 * @param museumRequest Museum we want to save
	 * @return saved Museum
	 * 
	 * @author Roxanne Archambault
	 */
	@Transactional
	public MuseumResponseDto createMuseum(MuseumRequestDto museumRequest) {
		Owner owner = ownerService.getOwnerByEmailAddress(museumRequest.getOwnerEmailAddress());

		Museum museum = new Museum();
		museum.setName(museumRequest.getName());
		museum.setOwner(owner);

		// Save new object and return it
		museum = museumRepo.save(museum);
		return new MuseumResponseDto(museum);
	}
	
	
}
