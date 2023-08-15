package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.MuseumService;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.OwnerService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/museum")
public class MuseumController {

	@Autowired
	MuseumService museumService;

	@Autowired
	OwnerService ownerService;

	/**
	 * Posts a new Museum with the input parameters.
	 * 
	 * @param name              Museum's name
	 * @param ownerEmailAddress Email address of the Owner object to be associated
	 *                          to the museum
	 * @return MuseumDto of the created Museum object
	 * 
	 * @author Roxanne Archambault
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<MuseumResponseDto> createMuseum(@Valid @RequestBody MuseumRequestDto request) {
		MuseumResponseDto museum = museumService.createMuseum(request);
		return new ResponseEntity<MuseumResponseDto>(museum, HttpStatus.CREATED);
	}

	/**
	 * Returns the Museum object associated with the specified id from the DB
	 * 
	 * @param id The requested museum's id
	 * @return MuseumDto with the museum's parameters
	 * 
	 * @author Roxanne Archambault
	 */
	@GetMapping("/id/{id}")
	public ResponseEntity<MuseumResponseDto> getMuseumById(@PathVariable int id) {
		Museum museum = museumService.getMuseumById(id);
		return new ResponseEntity<MuseumResponseDto>(new MuseumResponseDto(museum), HttpStatus.OK);
	}

	/**
	 * Retrieves a museum object from the database with its name
	 * 
	 * @param name The name of the museum we want to retrieve
	 * @return Retrieved museum (MuseumResponseDto object)
	 * 
	 * @author Roxanne Archambault
	 */
	@GetMapping("/name/{name}")
	public ResponseEntity<MuseumResponseDto> getMuseumByName(@PathVariable String name) {
		Museum museum = museumService.getMuseumByName(name);
		return new ResponseEntity<MuseumResponseDto>(new MuseumResponseDto(museum), HttpStatus.OK);
	}

	/**
	 * Retrieves a museum object from the database using its owner's email address
	 * 
	 * @param emailAddress Email address we want to use to retrieve the museum
	 * @return Retrieved museum (MuseumResponseDto object)
	 * 
	 * @author Roxanne Archambault
	 */
	@GetMapping("/ownerEmailAddress/{emailAddress}")
	public ResponseEntity<MuseumResponseDto> getMuseumByOwnerEmailAddress(@PathVariable String emailAddress) {
		Museum museum = museumService.getMuseumByOwnerEmailAddress(emailAddress);
		return new ResponseEntity<MuseumResponseDto>(new MuseumResponseDto(museum), HttpStatus.OK);
	}
}
