package ca.mcgill.ecse321.MuseumSoftwareSystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Simple handler for exceptions occurring within the
 * MuseumSoftwareSystemApplication
 * 
 * @author Roxanne Archambault
 *
 */
@ControllerAdvice
public class MuseumSoftwareSystemExceptionHandler {

	@ExceptionHandler(MuseumSoftwareSystemException.class)
	public ResponseEntity<String> handleEventRegistrationException(MuseumSoftwareSystemException ex) {
		return new ResponseEntity<String>(ex.getMessage(), ex.getStatus());
	}
}
