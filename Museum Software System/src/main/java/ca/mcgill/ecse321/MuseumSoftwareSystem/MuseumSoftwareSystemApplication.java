package ca.mcgill.ecse321.MuseumSoftwareSystem;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import ca.mcgill.ecse321.MuseumSoftwareSystem.controller.MuseumController;
import ca.mcgill.ecse321.MuseumSoftwareSystem.controller.OwnerController;
import ca.mcgill.ecse321.MuseumSoftwareSystem.controller.RoomController;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.*;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MuseumSoftwareSystemApplication {

	@Autowired
	OwnerController ownerController;

	@Autowired
	MuseumController museumController;

	@Autowired
	RoomController roomController;

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		try{
			// Check if the museum was created
			museumController.getMuseumByName("Marwan's Fun House");
		}
		catch (MuseumSoftwareSystemException e){
			// If museum was not found, create the owner, the museum and all 11 rooms
			ResponseEntity<OwnerResponseDto> ownerResponse = ownerController.createOwner(new OwnerRequestDto("Marwan Kanaan", "marwan.kanaan@mcgill.ca", "museum123"));
			ResponseEntity<MuseumResponseDto> museumResponse = museumController.createMuseum(new MuseumRequestDto("Marwan's Fun House", ownerResponse.getBody().getEmailAddress()));
			int museumId = museumResponse.getBody().getId();

			// Create 5 large rooms and 5 small rooms
			for (int i = 0; i < 5; i++){
				roomController.createRoom(new RoomRequestDto(museumId, RoomType.Large));
				roomController.createRoom(new RoomRequestDto(museumId, RoomType.Small));
			}

			// Create a storage room
			roomController.createRoom(new RoomRequestDto(museumId, RoomType.Storage));
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(MuseumSoftwareSystemApplication.class, args);
	}
}
