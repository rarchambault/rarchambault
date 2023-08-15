package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;



import javax.validation.Valid;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ArtworkResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.MuseumPass;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.MuseumPassService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/museumPass")
public class MuseumPassController {
	
	/**WARNING!!!
	 * THIS CODE IS THE EXCLUSIVE INTELLECTUAL PROPERTY OF 
	 * @author muradgohar
	 * ANY EFFORTS TO TAMPER WITH, COPY, PLAGARIZE, TEST OR READ MAY RESULT IN LEGAL ACTION
	 */
	
	
	
	//Gotta Autowire it for it to work 
	@Autowired
	MuseumPassService museumpassservice;
	
	//Creates a new museum pass for when a museum pass needs to be created 
	@RequestMapping (method = RequestMethod.POST) 
	public ResponseEntity <MuseumPassResponseDto> createMuseumPass (@Valid @RequestBody MuseumPassRequestDto request ){
		MuseumPassResponseDto museumpass1 = museumpassservice.createMuseumPass(request);
		return new ResponseEntity<MuseumPassResponseDto>(museumpass1, HttpStatus.CREATED);
	}
	
	//Returns the museum pass associated with the correct id in the database
	@GetMapping ("/id/{id}")
	public ResponseEntity<MuseumPassResponseDto> findMuseumPassById(@PathVariable int id){
		MuseumPass museumpass = museumpassservice.findMuseumPassById(id);
		return new ResponseEntity<MuseumPassResponseDto>(new MuseumPassResponseDto(museumpass),HttpStatus.OK);
		
	}
	
	//Returns the museum pass associate with the correct visitor in the database
	@GetMapping ("visitor/{visitorEmailAddress}")
	public ResponseEntity<Iterable<MuseumPassResponseDto>> findMuseumPassesByVisitor (@PathVariable String visitorEmailAddress){
		List<MuseumPass> museumpasses = museumpassservice.findMuseumPassesByVisitor(visitorEmailAddress);
		ArrayList<MuseumPassResponseDto> museumpassResponses = new ArrayList<MuseumPassResponseDto>();
		for (var museumpass : museumpasses) {
			museumpassResponses.add(new MuseumPassResponseDto(museumpass));
		}
		return new ResponseEntity<Iterable<MuseumPassResponseDto>>(museumpassResponses, HttpStatus.OK);
	}
	
	/**
	 * Deletes the existing shift object
	 *
	 *
	 * @author Murad G.
	 */
	@DeleteMapping("/id/{id}")
	public ResponseEntity<MuseumPassResponseDto> deleteMuseumPass(@PathVariable int id) {
		MuseumPass museumpass = museumpassservice.deleteMuseumPassById(id);
		MuseumPassResponseDto response = new MuseumPassResponseDto(museumpass);
		return new ResponseEntity<MuseumPassResponseDto>(response, HttpStatus.OK);

	}

	

}
