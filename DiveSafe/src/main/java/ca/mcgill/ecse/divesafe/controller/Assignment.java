/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;

import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;

// line 1 "../../../../../DiveSafeStates.ump"
// line 121 "../../../../../DiveSafe.ump"
// line 223 "../../../../../DiveSafe.ump"
public class Assignment {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Assignment Attributes
	private int startDay;
	private int endDay;

	// Assignment State Machines
	public enum AssignmentStatus {
		Assigned, Paid, Started, Finished, Cancelled
	}

	private AssignmentStatus assignmentStatus;

	// Assignment Associations
	private Member member;
	private Guide guide;
	private Hotel hotel;
	private DiveSafe diveSafe;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Assignment(int aStartDay, int aEndDay, Member aMember, DiveSafe aDiveSafe) {
		startDay = aStartDay;
		endDay = aEndDay;
		boolean didAddMember = setMember(aMember);
		if (!didAddMember) {
			throw new RuntimeException(
					"Unable to create assignment due to member. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		}
		boolean didAddDiveSafe = setDiveSafe(aDiveSafe);
		if (!didAddDiveSafe) {
			throw new RuntimeException(
					"Unable to create assignment due to diveSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		}
		setAssignmentStatus(AssignmentStatus.Assigned);
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public boolean setStartDay(int aStartDay) {
		boolean wasSet = false;
		startDay = aStartDay;
		wasSet = true;
		return wasSet;
	}

	public boolean setEndDay(int aEndDay) {
		boolean wasSet = false;
		endDay = aEndDay;
		wasSet = true;
		return wasSet;
	}

	public int getStartDay() {
		return startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public String getAssignmentStatusFullName() {
		String answer = assignmentStatus.toString();
		return answer;
	}

	public AssignmentStatus getAssignmentStatus() {
		return assignmentStatus;
	}

	public boolean pay(String email, String authorizationCode) {
		boolean wasEventProcessed = false;

		AssignmentStatus aAssignmentStatus = assignmentStatus;
		switch (aAssignmentStatus) {
		case Assigned:
			// line 5 "../../../../../DiveSafeStates.ump"
			acceptPay();
			setAssignmentStatus(AssignmentStatus.Paid);
			wasEventProcessed = true;
			break;
		case Paid:
			// line 30 "../../../../../DiveSafeStates.ump"
			rejectPayAlreadyPaid();
			setAssignmentStatus(AssignmentStatus.Paid);
			wasEventProcessed = true;
			break;
		case Started:
			// line 36 "../../../../../DiveSafeStates.ump"
			rejectPayAlreadyPaid();
			setAssignmentStatus(AssignmentStatus.Started);
			wasEventProcessed = true;
			break;
		case Finished:
			// line 48 "../../../../../DiveSafeStates.ump"
			rejectPayFinished();
			setAssignmentStatus(AssignmentStatus.Finished);
			wasEventProcessed = true;
			break;
		case Cancelled:
			// line 59 "../../../../../DiveSafeStates.ump"
			rejectPayCancelled();
			setAssignmentStatus(AssignmentStatus.Cancelled);
			wasEventProcessed = true;
			break;
		default:
			// Other states do respond to this event
		}

		return wasEventProcessed;
	}

	public boolean cancel(String email) {
		boolean wasEventProcessed = false;

		AssignmentStatus aAssignmentStatus = assignmentStatus;
		switch (aAssignmentStatus) {
		case Assigned:
			// line 11 "../../../../../DiveSafeStates.ump"
			cancelAssignment();
			setAssignmentStatus(AssignmentStatus.Cancelled);
			wasEventProcessed = true;
			break;
		case Paid:
			// line 24 "../../../../../DiveSafeStates.ump"
			cancelAssignment();
			setAssignmentStatus(AssignmentStatus.Cancelled);
			wasEventProcessed = true;
			break;
		case Started:
			// line 42 "../../../../../DiveSafeStates.ump"
			cancelAssignment();
			setAssignmentStatus(AssignmentStatus.Cancelled);
			wasEventProcessed = true;
			break;
		case Finished:
			// line 54 "../../../../../DiveSafeStates.ump"
			rejectCancelFinished();
			setAssignmentStatus(AssignmentStatus.Finished);
			wasEventProcessed = true;
			break;
		default:
			// Other states do respond to this event
		}

		return wasEventProcessed;
	}

	public boolean finish(String email) {
		boolean wasEventProcessed = false;

		AssignmentStatus aAssignmentStatus = assignmentStatus;
		switch (aAssignmentStatus) {
		case Assigned:
			// line 14 "../../../../../DiveSafeStates.ump"
			rejectFinishNotStarted();
			setAssignmentStatus(AssignmentStatus.Assigned);
			wasEventProcessed = true;
			break;
		case Paid:
			// line 27 "../../../../../DiveSafeStates.ump"
			rejectFinishNotStarted();
			setAssignmentStatus(AssignmentStatus.Paid);
			wasEventProcessed = true;
			break;
		case Started:
			// line 39 "../../../../../DiveSafeStates.ump"
			finishTrip();
			setAssignmentStatus(AssignmentStatus.Finished);
			wasEventProcessed = true;
			break;
		case Cancelled:
			// line 65 "../../../../../DiveSafeStates.ump"
			rejectFinishCancelled();
			setAssignmentStatus(AssignmentStatus.Cancelled);
			wasEventProcessed = true;
			break;
		default:
			// Other states do respond to this event
		}

		return wasEventProcessed;
	}

	public boolean start(String email) {
		boolean wasEventProcessed = false;

		AssignmentStatus aAssignmentStatus = assignmentStatus;
		switch (aAssignmentStatus) {
		case Paid:
			// line 21 "../../../../../DiveSafeStates.ump"
			startAssignment();
			setAssignmentStatus(AssignmentStatus.Started);
			wasEventProcessed = true;
			break;
		case Finished:
			// line 51 "../../../../../DiveSafeStates.ump"
			rejectStartFinished();
			setAssignmentStatus(AssignmentStatus.Finished);
			wasEventProcessed = true;
			break;
		case Cancelled:
			// line 62 "../../../../../DiveSafeStates.ump"
			rejectStartCancelled();
			setAssignmentStatus(AssignmentStatus.Cancelled);
			wasEventProcessed = true;
			break;
		default:
			// Other states do respond to this event
		}

		return wasEventProcessed;
	}

	private void setAssignmentStatus(AssignmentStatus aAssignmentStatus) {
		assignmentStatus = aAssignmentStatus;
	}

	/* Code from template association_GetOne */
	public Member getMember() {
		return member;
	}

	/* Code from template association_GetOne */
	public Guide getGuide() {
		return guide;
	}

	public boolean hasGuide() {
		boolean has = guide != null;
		return has;
	}

	/* Code from template association_GetOne */
	public Hotel getHotel() {
		return hotel;
	}

	public boolean hasHotel() {
		boolean has = hotel != null;
		return has;
	}

	/* Code from template association_GetOne */
	public DiveSafe getDiveSafe() {
		return diveSafe;
	}

	/* Code from template association_SetOneToOptionalOne */
	public boolean setMember(Member aNewMember) {
		boolean wasSet = false;
		if (aNewMember == null) {
			// Unable to setMember to null, as assignment must always be associated to a
			// member
			return wasSet;
		}

		Assignment existingAssignment = aNewMember.getAssignment();
		if (existingAssignment != null && !equals(existingAssignment)) {
			// Unable to setMember, the current member already has a assignment, which would
			// be orphaned if it were re-assigned
			return wasSet;
		}

		Member anOldMember = member;
		member = aNewMember;
		member.setAssignment(this);

		if (anOldMember != null) {
			anOldMember.setAssignment(null);
		}
		wasSet = true;
		return wasSet;
	}

	/* Code from template association_SetOptionalOneToMany */
	public boolean setGuide(Guide aGuide) {
		boolean wasSet = false;
		Guide existingGuide = guide;
		guide = aGuide;
		if (existingGuide != null && !existingGuide.equals(aGuide)) {
			existingGuide.removeAssignment(this);
		}
		if (aGuide != null) {
			aGuide.addAssignment(this);
		}
		wasSet = true;
		return wasSet;
	}

	/* Code from template association_SetOptionalOneToMany */
	public boolean setHotel(Hotel aHotel) {
		boolean wasSet = false;
		Hotel existingHotel = hotel;
		hotel = aHotel;
		if (existingHotel != null && !existingHotel.equals(aHotel)) {
			existingHotel.removeAssignment(this);
		}
		if (aHotel != null) {
			aHotel.addAssignment(this);
		}
		wasSet = true;
		return wasSet;
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
			existingDiveSafe.removeAssignment(this);
		}
		diveSafe.addAssignment(this);
		wasSet = true;
		return wasSet;
	}

	public void delete() {
		Member existingMember = member;
		member = null;
		if (existingMember != null) {
			existingMember.setAssignment(null);
		}
		if (guide != null) {
			Guide placeholderGuide = guide;
			this.guide = null;
			placeholderGuide.removeAssignment(this);
		}
		if (hotel != null) {
			Hotel placeholderHotel = hotel;
			this.hotel = null;
			placeholderHotel.removeAssignment(this);
		}
		DiveSafe placeholderDiveSafe = diveSafe;
		this.diveSafe = null;
		if (placeholderDiveSafe != null) {
			placeholderDiveSafe.removeAssignment(this);
		}
		DiveSafePersistence.save();
	}

	// line 73 "../../../../../DiveSafeStates.ump"
	public void acceptPay() {

	}

	// line 76 "../../../../../DiveSafeStates.ump"
	public void cancelAssignment() {

	}

	// line 79 "../../../../../DiveSafeStates.ump"
	public void rejectFinishNotStarted() {
		throw new RuntimeException("Cannot finish a trip which has not started");
	}

	// line 83 "../../../../../DiveSafeStates.ump"
	public void startAssignment() {

	}

	// line 86 "../../../../../DiveSafeStates.ump"
	public void rejectPayAlreadyPaid() {
		throw new RuntimeException("Trip has already been paid for");
	}

	// line 90 "../../../../../DiveSafeStates.ump"
	public void finishTrip() {

	}

	// line 93 "../../../../../DiveSafeStates.ump"
	public void rejectPayFinished() {
		throw new RuntimeException("Cannot pay for a trip which has finished");
	}

	// line 97 "../../../../../DiveSafeStates.ump"
	public void rejectStartFinished() {
		throw new RuntimeException("Cannot start a trip which has finished");
	}

	// line 101 "../../../../../DiveSafeStates.ump"
	public void rejectCancelFinished() {
		throw new RuntimeException("Cannot cancel a trip which has finished");
	}

	// line 105 "../../../../../DiveSafeStates.ump"
	public void rejectPayCancelled() {
		throw new RuntimeException("Cannot pay for a trip which has been cancelled");
	}

	// line 109 "../../../../../DiveSafeStates.ump"
	public void rejectStartCancelled() {
		throw new RuntimeException("Cannot start a trip which has been cancelled");
	}

	// line 113 "../../../../../DiveSafeStates.ump"
	public void rejectFinishCancelled() {
		throw new RuntimeException("Cannot finish a trip which has been cancelled");
	}

	@Override
	public String toString() {
		return super.toString() + "[" + "startDay" + ":" + getStartDay() + "," + "endDay" + ":" + getEndDay() + "]"
				+ System.getProperties().getProperty("line.separator") + "  " + "member = "
				+ (getMember() != null ? Integer.toHexString(System.identityHashCode(getMember())) : "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "guide = "
				+ (getGuide() != null ? Integer.toHexString(System.identityHashCode(getGuide())) : "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "hotel = "
				+ (getHotel() != null ? Integer.toHexString(System.identityHashCode(getHotel())) : "null")
				+ System.getProperties().getProperty("line.separator") + "  " + "diveSafe = "
				+ (getDiveSafe() != null ? Integer.toHexString(System.identityHashCode(getDiveSafe())) : "null");
	}
}