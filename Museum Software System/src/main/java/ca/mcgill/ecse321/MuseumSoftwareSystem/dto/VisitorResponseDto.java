package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

public class VisitorResponseDto {

	private String name;
	private String emailAddress;
	private String password;
	private MuseumResponseDto museum;

	public VisitorResponseDto(Visitor visitor) {
		this.name = visitor.getName();
		this.emailAddress = visitor.getEmailAddress();
		this.password = visitor.getPassword();
		this.museum = new MuseumResponseDto(visitor.getMuseum());
	}
	
	public VisitorResponseDto() { }

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getName() {
		return name;
	}

	public String getPassword() { return password; }

	public MuseumResponseDto getMuseum() {
		return museum;
	}
}
