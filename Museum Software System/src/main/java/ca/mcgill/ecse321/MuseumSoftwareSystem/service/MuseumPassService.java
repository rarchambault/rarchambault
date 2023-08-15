package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumPassResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.MuseumPassRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Please direct all questions and queries regarding this code to the
 * rightful owner and maintainer of this code @author muradgohar
 */

@Service
public class MuseumPassService {

	@Autowired
	MuseumPassRepository museumPassRepo;

	@Autowired
	MuseumService museumService;

	@Autowired
	VisitorService visitorService;

	@Autowired
	DayService dayService;

	// Creates a new MuseumPass in the Database 
	@Transactional
	public MuseumPassResponseDto createMuseumPass(MuseumPassRequestDto museumpassRequest) {
		Visitor visitor = visitorService.retrieveVisitorByEmail(museumpassRequest.getVisitorEmailAddress());
		Museum museum = museumService.getMuseumById(museumpassRequest.getMuseumId());
		MuseumPass museumpass = new MuseumPass();
		museumpass.setVisitor(visitor);
		museumpass.setMuseum(museum);

		double price = 0;
		switch(museumpassRequest.getType())
		{
			case Child -> price = 0;
			case Regular -> price = 20;
			case Senior -> price = 15;
			case Student -> price = 15;
			default -> price = 20;
		};

		museumpass.setPrice(price);
		museumpass.setType(museumpassRequest.getType());
		museumpass.setDate(museumpassRequest.getDate());
		museumpass = museumPassRepo.save(museumpass);

		return new MuseumPassResponseDto(museumpass);
	}

	//finds a MuseumPass in the database using its associated id
	@Transactional
	public MuseumPass findMuseumPassById(int id) {
		MuseumPass museumpass = museumPassRepo.findMuseumPassById(id);
		if (museumpass == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.OK,
					"Museum Pass not found.");
		}
		return museumpass;
	}

	//finds a MuseumPass in the database by its associated visitor's email address 
	@Transactional
	public List<MuseumPass> findMuseumPassesByVisitor(String email) {
		List<MuseumPass> museumpasses = museumPassRepo.findMuseumPassesByVisitorEmailAddress(email);
		if (museumpasses == null || museumpasses.isEmpty()) {
			throw new MuseumSoftwareSystemException(HttpStatus.OK,
					"Museum Passes not found.");
		}
		return museumpasses;
	}
	
	/**
	 * deletes an existing museumpass object
	 *

	 *
	 * @author Murad G.
	 */
	@Transactional
	public MuseumPass deleteMuseumPassById(int id) {
		MuseumPass museumpass = museumPassRepo.findMuseumPassById(id);
		if (museumpass != null) {
			museumPassRepo.delete(museumpass);
		}
		return museumpass;
	}
}