package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;


import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Room;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;

public class RoomResponseDto {

    private int id;
    private RoomType type;
    private MuseumResponseDto museum;

    public RoomResponseDto (Room room) {
        this.id = room.getId();
        this.type =room.getType();
        this.museum = new MuseumResponseDto(room.getMuseum());
    }

    public RoomResponseDto () {
    }

    public int getId() {
        return this.id;
    }

    public MuseumResponseDto getMuseum() {return museum;}

    public RoomType getType() {
        return this.type;
    }


}
