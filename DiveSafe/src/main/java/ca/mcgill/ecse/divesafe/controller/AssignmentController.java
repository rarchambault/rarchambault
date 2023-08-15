package ca.mcgill.ecse.divesafe.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Hotel;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.Member.MemberStatus;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;

public class AssignmentController {
	private static DiveSafe diveSafe = DiveSafeApplication.getDiveSafe();

	private AssignmentController() {
	}

	public static List<TOAssignment> getAssignments() {
		List<TOAssignment> assignments = new ArrayList<>();

		for (var assignment : diveSafe.getAssignments()) {
			var newTOAssignment = wrapAssignment(assignment);
			assignments.add(newTOAssignment);
		}

		return assignments;
	}

	/**
	 * This method is used by the administrator to initiate all the member/guide
	 * assignments at the beginning of the season.
	 * 
	 * @author Pinak Ghosh
	 * @return error string ("" if there was no error)
	 */
	public static String initiateAssignment() {

		String error = "";
		int hotelIndex = 0;

		for (var guide : diveSafe.getGuides()) {

			boolean allDays[];

			allDays = new boolean[diveSafe.getNumDays()];
			int timeset1 = 1;

			for (var member : diveSafe.getMembers()) {

				if (!member.hasAssignment()) {
					int triplength = member.getNumDays();

					if (member.isGuideRequired()) {

						int timeset2 = timeset1 + triplength - 1;

						int availabilityLength = allDays.length - timeset1;

						if (availabilityLength >= triplength - 1) {

							var assignment = diveSafe.addAssignment(timeset1, timeset2, member);
							guide.addAssignment(assignment);
							assignment.setTotalCostForGuide(15);

							timeset1 = timeset1 + timeset2;
						}

					} else {

						int independenttimeset1 = 1;
						int independenttimeset2 = independenttimeset1 + triplength - 1;

						var assignment = diveSafe.addAssignment(independenttimeset1, independenttimeset2, member);
						assignment.setTotalCostForGuide(0);

					}
				}

				if (member.hasAssignment()) {
					if (member.isHotelRequired()) {
						List<Hotel> hotels = diveSafe.getHotels();
						if (hotels.size() != 0) {
							if (hotelIndex >= hotels.size())
								hotelIndex = 0;
							member.getAssignment().setHotel(hotels.get(hotelIndex));
							hotelIndex++;
						}
					}
				}
			}

		}

		for (var member : diveSafe.getMembers()) {
			if (!member.hasAssignment())
				error = "Assignments could not be completed for all members";
		}
		if (error.equals(""))
			DiveSafePersistence.save();
		return error;
	}

	/**
	 * This method is used by the administrator to cancel the trip of a specific
	 * member (set it to the state "Cancelled").
	 * 
	 * @author rarchambault [Roxanne Archambault]
	 * @param userEmail : Email of the user for which we want to cancel the
	 *                  assignment
	 * @return error string ("" if there was no error)
	 */
	public static String cancelTrip(String userEmail) {
		String error = "";
		Member currentMember = Member.getWithEmail(userEmail);
		if (currentMember != null) {
			if (currentMember.getMemberStatus().equals(MemberStatus.Banned))
				error = "Cannot cancel the trip due to a ban";
			else
				try {
					currentMember.getAssignment().cancel(userEmail);
				} catch (Exception e) {
					error = e.getMessage();
				}

			if (error.equals("")) {
				if (currentMember.getAssignment().getGuide() != null) {
					currentMember.getAssignment().getGuide().removeAssignment(currentMember.getAssignment());
				}
			}
		} else
			error = "Member with email address " + userEmail + " does not exist";
		if (error.equals(""))
			DiveSafePersistence.save();
		return error;
	}

	/**
	 * This method is used by the administrator to set the assignment of a certain
	 * member to the state "Finished".
	 * 
	 * @author rarchambault [Roxanne Archambault]
	 * @param userEmail: Email of the user for which we want to finish the trip
	 * @return error string ("" if there was no error)
	 */
	public static String finishTrip(String userEmail) {
		String error = "";
		Member currentMember = Member.getWithEmail(userEmail);
		if (currentMember != null) {
			if (currentMember.getMemberStatus().equals(MemberStatus.Banned))
				error = "Cannot finish the trip due to a ban";
			else
				try {
					currentMember.getAssignment().finish(userEmail);
				} catch (Exception e) {
					error = e.getMessage();
				}
		} else
			error = "Member with email address " + userEmail + " does not exist";
		if (error.equals(""))
			DiveSafePersistence.save();
		return error;
	}

	/**
	 * This method is used by the administrator to start all the trips for a given
	 * day (set the assignments to status "Started").
	 * 
	 * @author rarchambault [Roxanne Archambault]
	 * @param day : current day
	 * @return error string ("" if there was no error)
	 */
	public static String startTripsForDay(int day) {
		String error = "";
		boolean found = false;
//		List<Assignment> assignments = new ArrayList<Assignment>();
//		assignments = diveSafe.getAssignments();
		for (var assignment : diveSafe.getAssignments()) {
			if (day == assignment.getStartDay()) {
				found = true;
				if (assignment.getMember().getMemberStatus().equals(MemberStatus.Banned))
					error = "Cannot start the trip due to a ban";
				else {
					try {
						assignment.start(assignment.getMember().getEmail());
					} catch (Exception e) {
						error = e.getMessage();
					}
				}
			}
		}
		if (!found)
			error = "There are no assignments starting on day " + day;
		if (error.equals(""))
			DiveSafePersistence.save();
		return error;
	}

	/**
	 * This method is used by the administrator to confirm the payment of a certain
	 * member (set their assignment to status "Paid").
	 * 
	 * @author rarchambault [Roxanne Archambault]
	 * @param userEmail         : Email of the user for whom we want to confirm
	 *                          payment
	 * @param authorizationCode : Code the user has to enter to confirm payment
	 * @return error string ("" if there was no error)
	 */
	public static String confirmPayment(String userEmail, String authorizationCode) {
		String error = "";
		Member currentMember = Member.getWithEmail(userEmail);
		if (currentMember != null) {
			Assignment currentAssignment = currentMember.getAssignment();
			if (authorizationCode.equals(""))
				error = "Invalid authorization code";
			else if (currentMember.getMemberStatus().equals(MemberStatus.Banned))
				error = "Cannot pay for the trip due to a ban";
			else
				try {
					currentAssignment.pay(userEmail, authorizationCode);
				} catch (Exception e) {
					error = e.getMessage();
				}
		} else
			error = "Member with email address " + userEmail + " does not exist";
		if (error.equals(""))
			DiveSafePersistence.save();
		return error;
	}

	/**
	 * This method is used by the administrator to ban a certain member (set their
	 * status to "Banned").
	 * 
	 * @author rarchambault [Roxanne Archambault]
	 * @param userEmail : Email of the user for whom we want to toggle ban
	 * @return error string ("" if there was no error)
	 */
	public static String toggleBan(String userEmail) {
		String error = "";
		Member currentMember = Member.getWithEmail(userEmail);
		if (currentMember != null) {
			try {
				currentMember.toggleMemberStatus();
			} catch (Exception e) {
				error = e.getMessage();
			}
		} else
			error = "Member with email address " + userEmail + " does not exist";
		if (error.equals(""))
			DiveSafePersistence.save();
		return error;
	}

	/**
	 * Helper method used to wrap the information in an <code>Assignment</code>
	 * instance in an instance of <code>TOAssignment</code>.
	 *
	 * @author Harrison Wang Oct 19, 2021
	 * @param assignment - The <code>Assignment</code> instance to transfer the
	 *                   information from.
	 * @return A <code>TOAssignment</code> instance containing the information in
	 *         the <code>Assignment</code> parameter.
	 */
	private static TOAssignment wrapAssignment(Assignment assignment) {
		var member = assignment.getMember();

		// Initialize values for all necessary parameters.
		String memberEmail = member.getEmail();
		String memberName = member.getName();
		String guideEmail = assignment.hasGuide() ? assignment.getGuide().getEmail() : "";
		String guideName = assignment.hasGuide() ? assignment.getGuide().getName() : "";
		String hotelName = assignment.hasHotel() ? assignment.getHotel().getName() : "";

		int numDays = member.getNumDays();
		int startDay = assignment.getStartDay();
		int endDay = assignment.getEndDay();
		int totalCostForGuide = assignment.hasGuide() ? numDays * diveSafe.getPriceOfGuidePerDay() : 0;
		/*
		 * Calculate the totalCostForEquipment.
		 *
		 * Sum the costs of all booked items depending on if they are an Equipment or
		 * EquipmentBundle instance to get the equipmentCostPerDay for this assignment.
		 *
		 * Multiply equipmentCostPerDay by nrOfDays to get totalCostForEquipment.
		 */
		int equipmentCostPerDay = 0;
		for (var bookedItem : member.getItemBookings()) {
			Item item = bookedItem.getItem();
			if (item instanceof Equipment equipment) {
				equipmentCostPerDay += equipment.getPricePerDay() * bookedItem.getQuantity();
			} else if (item instanceof EquipmentBundle bundle) {
				int bundleCost = 0;
				for (var bundledItem : bundle.getBundleItems()) {
					bundleCost += bundledItem.getEquipment().getPricePerDay() * bundledItem.getQuantity();
				}
				// Discount only applicable if assignment includes guide, so check for that
				// before applying discount
				if (assignment.hasGuide()) {
					bundleCost = (int) (bundleCost * ((100.0 - bundle.getDiscount()) / 100.0));
				}
				equipmentCostPerDay += bundleCost * bookedItem.getQuantity();
			}
		}
		int totalCostForEquipment = equipmentCostPerDay * numDays;

		return new TOAssignment(memberEmail, memberName, guideEmail, guideName, hotelName,
				assignment.getAssignmentStatusFullName(), member.getMemberStatusFullName(), startDay, endDay,
				totalCostForGuide, totalCostForEquipment);
	}

}
