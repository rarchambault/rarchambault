/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;

// line 119 "../../../../../DiveSafeStates.ump"
// line 36 "../../../../../DiveSafe.ump"
// line 179 "../../../../../DiveSafe.ump"
public class Guide extends NamedUser {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Guide State Machines
	public enum GuideStatus {
		Available, Unavailable
	}

	private GuideStatus guideStatus;

	// Guide Associations
	private DiveSafe diveSafe;
	private List<Assignment> assignments;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Guide(String aEmail, String aPassword, String aName, String aEmergencyContact, DiveSafe aDiveSafe) {
		super(aEmail, aPassword, aName, aEmergencyContact);
		boolean didAddDiveSafe = setDiveSafe(aDiveSafe);
		if (!didAddDiveSafe) {
			throw new RuntimeException(
					"Unable to create guide due to diveSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		}
		assignments = new ArrayList<Assignment>();
		setGuideStatus(GuideStatus.Available);
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public String getGuideStatusFullName() {
		String answer = guideStatus.toString();
		return answer;
	}

	public GuideStatus getGuideStatus() {
		return guideStatus;
	}

	public boolean assignMember(Member member, int day) {
		boolean wasEventProcessed = false;

		GuideStatus aGuideStatus = guideStatus;
		switch (aGuideStatus) {
		case Available:
			setGuideStatus(GuideStatus.Unavailable);
			wasEventProcessed = true;
			break;
		default:
			// Other states do respond to this event
		}

		return wasEventProcessed;
	}

	public boolean cancelTrip() {
		boolean wasEventProcessed = false;

		GuideStatus aGuideStatus = guideStatus;
		switch (aGuideStatus) {
		case Unavailable:
			setGuideStatus(GuideStatus.Available);
			wasEventProcessed = true;
			break;
		default:
			// Other states do respond to this event
		}

		return wasEventProcessed;
	}

	private void setGuideStatus(GuideStatus aGuideStatus) {
		guideStatus = aGuideStatus;
	}

	/* Code from template association_GetOne */
	public DiveSafe getDiveSafe() {
		return diveSafe;
	}

	/* Code from template association_GetMany */
	public Assignment getAssignment(int index) {
		Assignment aAssignment = assignments.get(index);
		return aAssignment;
	}

	public List<Assignment> getAssignments() {
		List<Assignment> newAssignments = Collections.unmodifiableList(assignments);
		return newAssignments;
	}

	public int numberOfAssignments() {
		int number = assignments.size();
		return number;
	}

	public boolean hasAssignments() {
		boolean has = assignments.size() > 0;
		return has;
	}

	public int indexOfAssignment(Assignment aAssignment) {
		int index = assignments.indexOf(aAssignment);
		return index;
	}

	/* Code from template association_SetOneToMany */
	public boolean setDiveSafe(DiveSafe aDiveSafe) {
		boolean wasSet = false;
		if (aDiveSafe == null) {
			return wasSet;
		}

		DiveSafe existingDiveSafe = diveSafe;
		diveSafe = aDiveSafe;
		if (existingDiveSafe != null && !existingDiveSafe.equals(aDiveSafe)) {
			existingDiveSafe.removeGuide(this);
		}
		diveSafe.addGuide(this);
		wasSet = true;
		return wasSet;
	}

	/* Code from template association_MinimumNumberOfMethod */
	public static int minimumNumberOfAssignments() {
		return 0;
	}

	/* Code from template association_AddManyToOptionalOne */
	public boolean addAssignment(Assignment aAssignment) {
		boolean wasAdded = false;
		if (assignments.contains(aAssignment)) {
			return false;
		}
		Guide existingGuide = aAssignment.getGuide();
		if (existingGuide == null) {
			aAssignment.setGuide(this);
		} else if (!this.equals(existingGuide)) {
			existingGuide.removeAssignment(aAssignment);
			addAssignment(aAssignment);
		} else {
			assignments.add(aAssignment);
		}
		wasAdded = true;
		return wasAdded;
	}

	public boolean removeAssignment(Assignment aAssignment) {
		boolean wasRemoved = false;
		if (assignments.contains(aAssignment)) {
			assignments.remove(aAssignment);
			aAssignment.setGuide(null);
			wasRemoved = true;
		}
		return wasRemoved;
	}

	/* Code from template association_AddIndexControlFunctions */
	public boolean addAssignmentAt(Assignment aAssignment, int index) {
		boolean wasAdded = false;
		if (addAssignment(aAssignment)) {
			if (index < 0) {
				index = 0;
			}
			if (index > numberOfAssignments()) {
				index = numberOfAssignments() - 1;
			}
			assignments.remove(aAssignment);
			assignments.add(index, aAssignment);
			wasAdded = true;
		}
		return wasAdded;
	}

	public boolean addOrMoveAssignmentAt(Assignment aAssignment, int index) {
		boolean wasAdded = false;
		if (assignments.contains(aAssignment)) {
			if (index < 0) {
				index = 0;
			}
			if (index > numberOfAssignments()) {
				index = numberOfAssignments() - 1;
			}
			assignments.remove(aAssignment);
			assignments.add(index, aAssignment);
			wasAdded = true;
		} else {
			wasAdded = addAssignmentAt(aAssignment, index);
		}
		return wasAdded;
	}

	@Override
	public void delete() {
		DiveSafe placeholderDiveSafe = diveSafe;
		this.diveSafe = null;
		if (placeholderDiveSafe != null) {
			placeholderDiveSafe.removeGuide(this);
		}
		while (!assignments.isEmpty()) {
			assignments.get(0).setGuide(null);
		}
		super.delete();
		DiveSafePersistence.save();
	}

	// line 40 "../../../../../DiveSafe.ump"
	public static Guide getWithEmail(String email) {
		if (User.getWithEmail(email) instanceof Guide guide) {
			return guide;
		}
		return null;
	}

	// line 47 "../../../../../DiveSafe.ump"
	public static boolean hasWithEmail(String email) {
		return getWithEmail(email) != null;
	}

}