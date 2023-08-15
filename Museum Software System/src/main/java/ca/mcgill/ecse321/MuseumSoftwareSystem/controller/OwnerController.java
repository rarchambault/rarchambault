package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import javax.validation.Valid;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.OwnerService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/owner")
public class OwnerController {

	@Autowired
	OwnerService ownerService;

	/**
	 *
	 * Posts a new Owner with the input parameters.
	 *
	 * @author Haroun Guessous
	 *
	 * @param request
	 * @return OwnerDto of the created Owner object
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<OwnerResponseDto> createOwner(@Valid @RequestBody OwnerRequestDto request) {
		OwnerResponseDto owner = ownerService.createOwner(request);
		return new ResponseEntity<OwnerResponseDto>(owner, HttpStatus.CREATED);
	}


	/**
	 * @author Haroun Guessous
	 *
	 * Returns the Owner object associated with the specified email from the DB
	 *
	 * @param emailAddress Email address of the owner we want to find
	 *
	 * @return OwnerDto with the Owner's parameters
	 */

	@GetMapping("/emailAddress/{emailAddress}")
	public ResponseEntity<OwnerResponseDto> getOwnerByEmailAddress(@PathVariable String emailAddress) {
		Owner owner = ownerService.getOwnerByEmailAddress(emailAddress);
		return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(owner), HttpStatus.OK);
	}
}
