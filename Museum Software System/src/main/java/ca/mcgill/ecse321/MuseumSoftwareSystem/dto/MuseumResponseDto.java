package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;

public class MuseumResponseDto {

	private int id;
	private String name;
	private OwnerResponseDto owner;

	public MuseumResponseDto(Museum museum) {
		this.id = museum.getId();
		this.name = museum.getName();
		this.owner = new OwnerResponseDto(museum.getOwner());
	}

	public MuseumResponseDto() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public OwnerResponseDto getOwner() {
		return owner;
	}
}
