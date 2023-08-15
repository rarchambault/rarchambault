package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.OwnerRepository;

@Service
public class OwnerService {

	@Autowired
	OwnerRepository ownerRepo;

	// Annotate methods with @Transactional

	/**
	 * Gets an Owner object given its email address
	 *
	 * @author Haroun Guessous
	 *
	 * @param emailAddress Email address for the owner we want to find
	 * @return Fetched Owner object
	 */
	@Transactional
	public Owner getOwnerByEmailAddress(String emailAddress) {
		Owner owner = ownerRepo.findOwnerByEmailAddress(emailAddress);
		if (owner == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Owner not found.");
		}
		return owner;
	}

	/**
	 * Creates an Owner in the database from the input request
	 *
	 * @author Haroun Guessous
	 *
	 * @param ownerRequest Owner
	 * @return The saved Owner object wrapped in a ReponseDto object
	 */
	@Transactional
	public OwnerResponseDto createOwner(OwnerRequestDto ownerRequest) {
		Owner owner = new Owner();
		owner.setEmailAddress(ownerRequest.getEmailAddress());
		owner.setName(ownerRequest.getName());
		owner.setPassword(ownerRequest.getPassword());

		// Save new object and return it
		owner = ownerRepo.save(owner);
		return new OwnerResponseDto(owner);
	}
}
