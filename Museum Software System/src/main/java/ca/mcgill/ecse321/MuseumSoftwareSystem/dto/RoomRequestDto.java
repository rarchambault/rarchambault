package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.RoomType;

public class RoomRequestDto {

	@Positive
	private int museumId;

	@NotNull
	private RoomType type;

	public RoomRequestDto(int aMuseumId, RoomType aType) {
		this.museumId = aMuseumId;
		this.type = aType;
	}

	public RoomRequestDto() {
	}

	public void setMuseumId(int museumId) {
		this.museumId = museumId;
	}

	public int getMuseumId() {
		return this.museumId;
	}

	public void setType(RoomType type) {
		this.type = type;
	}

	public RoomType getType() {
		return this.type;
	}

}
