package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.LoanResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.MuseumResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.RoomResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Loan;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    /**
     * Gets all rooms saved in the database
     * @return Iterable of all rooms from the database
     *
     * @author Roxanne Archambault
     */
    @GetMapping()
    public ResponseEntity<Iterable<RoomResponseDto>> getAllRooms() {
        Iterable<Room> rooms = roomService.getAllRooms();

        ArrayList<RoomResponseDto> roomResponses = new ArrayList<RoomResponseDto>();

        for (var room : rooms) {
            roomResponses.add(new RoomResponseDto(room));
        }

        return new ResponseEntity<Iterable<RoomResponseDto>>(roomResponses, HttpStatus.OK);
    }

    /**
     *
     * Returns the Room object associated with the specified id from the DB
     *
     * @author Pinak Ghosh
     *
     * @param id
     * @return  RoomDto with the Room's parameters
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<RoomResponseDto> findRoomById(@PathVariable int id){
        Room room = roomService.findRoomById(id);
        return new ResponseEntity<RoomResponseDto>(new RoomResponseDto(room), HttpStatus.OK);
    }


    /**
     *
     *
     * Posts a new Room with the input parameters.
     *
     * @author Pinak Ghosh
     *
     * @param request RoomDto of the created Room object
     * @return RoomDto with the Room's parameters
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody RoomRequestDto request) {
        RoomResponseDto room = roomService.createRoom(request);
        return new ResponseEntity<RoomResponseDto>(room, HttpStatus.CREATED);
    }





}
