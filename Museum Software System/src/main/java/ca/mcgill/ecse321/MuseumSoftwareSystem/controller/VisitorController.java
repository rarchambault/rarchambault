package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import javax.validation.Valid;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.VisitorService;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/visitor")
public class VisitorController {
	@Autowired
	VisitorService visitorService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<VisitorResponseDto> createVisitor(@Valid @RequestBody VisitorRequestDto request) {
		VisitorResponseDto visitor = visitorService.createVisitor(request);
		return new ResponseEntity<VisitorResponseDto>(visitor, HttpStatus.CREATED);
	}

	@GetMapping("/emailAddress/{emailAddress}")
	public ResponseEntity<VisitorResponseDto> getVisitorByEmailAddress(@PathVariable String emailAddress) {
		Visitor visitor = visitorService.retrieveVisitorByEmail(emailAddress);
		return new ResponseEntity<VisitorResponseDto>(new VisitorResponseDto(visitor), HttpStatus.OK);
	}

}
