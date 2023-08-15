package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.VisitorResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.VisitorRepository;

@Service
public class VisitorService {

	@Autowired
	VisitorRepository visitorRepository;

	@Autowired
	MuseumService museumService;

	/**
	 * Method that retrieves visitor by email address
	 *
	 * @param emailAddress
	 * @return visitor
	 *
	 * @author Alec Tufenkjian
	 */
	@Transactional
	public Visitor retrieveVisitorByEmail(String emailAddress) {

		if (emailAddress == null) {
			return null;
		}

		Visitor visitor = visitorRepository.findVisitorByEmailAddress(emailAddress);

		if (visitor == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.OK, "Visitor not found.");
		}

		return visitor;
	}

	/**
	 * Method that creates visitor from request
	 *
	 * @param visitorRequest
	 * @return Visitor DTO Object
	 *
	 * @author Alec Tufenkjian
	 */
	@Transactional
	public VisitorResponseDto createVisitor(VisitorRequestDto visitorRequest) {
		Museum museum = museumService.getMuseumById(visitorRequest.getMuseumId());

		Visitor visitor = new Visitor();
		visitor.setMuseum(museum);
		visitor.setName(visitorRequest.getName());
		visitor.setEmailAddress(visitorRequest.getEmailAddress());
		visitor.setPassword(visitorRequest.getPassword());

		visitor = visitorRepository.save(visitor);
		return new VisitorResponseDto(visitor);
	}
}
