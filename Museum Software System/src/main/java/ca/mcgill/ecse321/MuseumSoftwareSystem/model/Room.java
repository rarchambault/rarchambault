package ca.mcgill.ecse321.MuseumSoftwareSystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Room {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Room Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private RoomType type;

	@ManyToOne(optional = false)
	private Museum museum;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Room() {
	}

	public Room(RoomType type, Museum museum) {
		this.type = type;
		this.museum = museum;
	}

	public boolean setType(RoomType aType) {
		boolean wasSet = false;
		type = aType;
		wasSet = true;
		return wasSet;
	}

	public RoomType getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public Museum getMuseum() {
		return museum;
	}

	public boolean setMuseum(Museum aMuseum) {
		boolean wasSet = false;
		if (aMuseum == null) {
			return wasSet;
		}

		museum = aMuseum;
		wasSet = true;
		return wasSet;
	}

	public boolean setId(int aId) {
		id = aId;
		return true;
	}
}
