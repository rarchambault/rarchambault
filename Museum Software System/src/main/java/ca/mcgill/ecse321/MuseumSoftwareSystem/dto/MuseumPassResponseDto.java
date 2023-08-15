package ca.mcgill.ecse321.MuseumSoftwareSystem.dto;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.MuseumPass;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.PassType;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Visitor;

import java.sql.Date;

/**
 * Please direct all questions and queries regarding this code to the
 * rightful owner and maintainer of this code @author muradgohar
 */

public class MuseumPassResponseDto {
	
	//variable declarations 
	private int id;
	private double price;
	private Visitor visitor;
	private PassType type;
	public Date date;
	
	//MuseumPassResponseDto constructor
	public MuseumPassResponseDto(MuseumPass museumpass) {
		this.id = museumpass.getId();
		this.visitor = museumpass.getVisitor();
		this.price = museumpass.getPrice();
		this.type = museumpass.getType();
		this.date = museumpass.getDate();
	}
	
	public MuseumPassResponseDto() {
	}

	//getter methods 
	public int getId() {
		return id;
	}
	
	public Visitor getVisitor(){
		return visitor;
	}

	public Double getPrice() {
		return price;
	}

	public PassType getType() {
		return type;
	}

	public Date getDate() {
		return this.date;
	}
}
