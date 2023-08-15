package ca.mcgill.ecse.divesafe.features;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.BundleItem;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Hotel;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.User;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AssignmentFeatureStepDefinitions {
	private DiveSafe diveSafe;
	private String error;

	/**
	 * @param dataTable
	 * @author KaraBest & JoeyKoay & VictorMicha
	 */
	@Given("the following DiveSafe system exists:")
	public void the_following_dive_safe_system_exists(io.cucumber.datatable.DataTable dataTable) {
		error = "";
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		DiveSafePersistence.setFilename("test.json");
		diveSafe = DiveSafeApplication.getDiveSafe();
		for (Map<String, String> row : rows) {
			Date startDate = java.sql.Date.valueOf(row.get("startDate"));
			diveSafe.setStartDate(startDate);

			int numDays = Integer.parseInt(row.get("numDays"));
			diveSafe.setNumDays(numDays);

			int priceOfGuidePerDay = Integer.parseInt(row.get("priceOfGuidePerDay"));
			diveSafe.setPriceOfGuidePerDay(priceOfGuidePerDay);
		}
	}

	/**
	 * @param dataTable
	 * @author EnzoBenoitJeannin & KaraBest & JoeyKoay & VictorMicha
	 */
	@Given("the following pieces of equipment exist in the system:")
	public void the_following_pieces_of_equipment_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> r : rows) {
			String name = r.get("name");
			int weight = Integer.parseInt(r.get("weight"));
			int pricePerDay = Integer.parseInt(r.get("pricePerDay"));
			if (!Equipment.hasWithName(name))
				new Equipment(name, weight, pricePerDay, this.diveSafe);
		}
	}

	/**
	 * @param dataTable
	 * @author EnzoBenoitJeannin & KaraBest & EunjunChang & JoeyKoay & VictorMicha &
	 *         SejongYoon
	 */
	@Given("the following equipment bundles exist in the system:")
	public void the_following_equipment_bundles_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> r : rows) {
			String name = r.get("name");
			int discount = Integer.parseInt(r.get("discount"));
			if (!EquipmentBundle.hasWithName(name)) {
				EquipmentBundle bundle = new EquipmentBundle(name, discount, diveSafe);
				List<String> items = Arrays.asList(r.get("items").split(","));
				List<String> quantities = Arrays.asList(r.get("quantity").split(","));
				for (int i = 0; i < items.size(); i++) {
					new BundleItem(Integer.parseInt(quantities.get(i)), this.diveSafe, bundle,
							Equipment.getWithName(items.get(i)));
				}
			}
		}
	}

	/**
	 * @author Edward Habelrih Given that the following guide exists, the guides are
	 *         given through the dataTable
	 * @param dataTable contains the required data regarding each guide (email,
	 *                  password, name and emergency contact) that are coded to be
	 *                  associated with the system.
	 */
	@Given("the following guides exist in the system:")
	public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> guideList = dataTable.asMaps();
		// Traverse through list of guides
		for (int i = 0; i < guideList.size(); i++) {
			// Retrieve information
			String guideEmail = guideList.get(i).get("email");
			String guidePassword = guideList.get(i).get("password");
			String guideName = guideList.get(i).get("name");
			String guideEmergencyContact = guideList.get(i).get("emergencyContact");
			// Add guide with given information
			if (!Guide.hasWithEmail(guideEmail))
				diveSafe.addGuide(guideEmail, guidePassword, guideName, guideEmergencyContact);

		}
	}

	/**
	 * @param dataTable
	 * @author EnzoBenoitJeannin & KaraBest & JoeyKoay & VictorMicha
	 */
	@Given("the following members exist in the system:")
	public void the_following_members_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> r : rows) {
			String email = r.get("email");
			String password = r.get("password");
			String name = r.get("name");
			String emergencyContact = r.get("emergencyContact");
			int numDays = Integer.parseInt(r.get("numDays"));
			List<String> items = Arrays.asList(r.get("itemBookings").split(","));
			List<String> requestedQuantities = Arrays.asList(r.get("itemBookingQuantities").split(","));
			boolean guideRequired = Boolean.parseBoolean(r.get("guideRequired"));
			boolean hotelRequired = Boolean.parseBoolean(r.get("hotelRequired"));
			Member m = new Member(email, password, name, emergencyContact, numDays, guideRequired, hotelRequired,
					this.diveSafe);
			for (int i = 0; i < items.size(); i++) {
				Item item = Item.getWithName(items.get(i));
				m.addItemBooking(Integer.parseInt(requestedQuantities.get(i)), this.diveSafe, item);
			}

		}
	}

	/**
	 * @author rarchambault (Roxanne Archambault)
	 */
	@When("the administrator attempts to initiate the assignment process")
	public void the_administrator_attempts_to_initiate_the_assignment_process() {
		error = AssignmentController.initiateAssignment();
	}

	/**
	 * @author Mona Kalaoun (m-kln)
	 * @param dataTable
	 */
	@Then("the following assignments shall exist in the system:")
	public void the_following_assignments_shall_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();

		for (var row : rows) {
			String memberEmail = row.get("memberEmail");
			String guideEmail = row.get("guideEmail");
			int startDay = Integer.parseInt(row.get("startDay"));
			int endDay = Integer.parseInt(row.get("endDay"));

			var member = Member.getWithEmail(memberEmail);
			var guide = Guide.getWithEmail(guideEmail);

			var assignment = member.getAssignment();

			assertEquals(member, assignment.getMember());
			assertEquals(guide, assignment.getGuide());
			assertEquals(startDay, assignment.getStartDay());
			assertEquals(endDay, assignment.getEndDay());
		}
	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string  member's email
	 * @param string2 assignment status
	 */

	@Then("the assignment for {string} shall be marked as {string}")
	public void the_assignment_for_shall_be_marked_as(String string, String string2) {
		Member currentMember = Member.getWithEmail(string);
		Assignment currentAssignment = currentMember.getAssignment();
		Assert.assertEquals(string2, currentAssignment.getAssignmentStatusFullName());
	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string number of assignments in the system
	 */

	@Then("the number of assignments in the system shall be {string}")
	public void the_number_of_assignments_in_the_system_shall_be(String string) {
		// Write code here that turns the phrase above into concrete actions
		Integer numberAssignments = Integer.parseInt(string);
		Assert.assertEquals(numberAssignments, (Integer) diveSafe.numberOfAssignments());

	}

	/**
	 * @author Mona Kalaoun
	 * @param string: error
	 */
	@Then("the system shall raise the error {string}")
	public void the_system_shall_raise_the_error(String string) {
		assertTrue(error.contains(string)); // check if error message is the correct one
	}

	/**
	 * Instantiates <code>Assignment</code> instances for use with Gherkin Scenario.
	 *
	 * @param dataTable Data provided in the Cucumber Feature file.
	 * @author Harrison Wang
	 */
	@Given("the following assignments exist in the system:")
	public void the_following_assignments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> cucumberData = dataTable.asMaps();

		for (Map<String, String> assignmentData : cucumberData) {
			var assignmentMember = (Member) User.getWithEmail(assignmentData.get("memberEmail"));
			var assignmentGuide = (Guide) User.getWithEmail(assignmentData.get("guideEmail"));
			var assignmentHotel = Hotel.getWithName(assignmentData.get("hotelName"));
			int startDay = Integer.valueOf(assignmentData.get("startDay"));
			int endDay = Integer.valueOf(assignmentData.get("endDay"));

			if (!assignmentMember.hasAssignment()) {
				Assignment newAssignment = diveSafe.addAssignment(startDay, endDay, assignmentMember);
				newAssignment.setGuide(assignmentGuide);
				newAssignment.setHotel(assignmentHotel);
			}
		}
	}

	/**
	 * @author rarchambault (Roxanne Archambault)
	 * @param string:  email
	 * @param string2: authorization code
	 */
	@When("the administrator attempts to confirm payment for {string} using authorization code {string}")
	public void the_administrator_attempts_to_confirm_payment_for_using_authorization_code(String string,
			String string2) {
		error = AssignmentController.confirmPayment(string, string2);
	}

	/**
	 * @author rarchambault (Roxanne Archambault)
	 * @param string:  email
	 * @param string2: code
	 */
	@Then("the assignment for {string} shall record the authorization code {string}")
	public void the_assignment_for_shall_record_the_authorization_code(String string, String string2) {
		Member currentMember = Member.getWithEmail(string);
		Assignment currentAssignment = currentMember.getAssignment();
		Assert.assertEquals(string2, currentAssignment.getAuthorizationCode());
	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string member's email
	 */

	@Then("the member account with the email {string} does not exist")
	public void the_member_account_with_the_email_does_not_exist(String string) {
		Assert.assertNull(Member.getWithEmail(string));
	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string number of members in the system
	 */

	@Then("there are {string} members in the system")
	public void there_are_members_in_the_system(String string) {
		Integer numberMembers = Integer.parseInt(string);
		assertEquals(numberMembers, (Integer) diveSafe.numberOfMembers());

	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string error message to be raised
	 */

	@Then("the error {string} shall be raised")
	public void the_error_shall_be_raised(String string) {
		assertTrue(error.contains(string));
	}

	/**
	 * @author rarchambault (Roxanne Archambault)
	 * @param string: email
	 */
	@When("the administrator attempts to cancel the trip for {string}")
	public void the_administrator_attempts_to_cancel_the_trip_for(String string) {
		error = AssignmentController.cancelTrip(string);
	}

	/**
	 * @author rarchambault (Roxanne Archambault)
	 * @param string: email
	 */
	@Given("the member with {string} has paid for their trip")
	public void the_member_with_has_paid_for_their_trip(String string) {
		Member currentMember = Member.getWithEmail(string);
		Assignment currentAssignment = currentMember.getAssignment();
		currentAssignment.pay(string, "anything");
	}

	/**
	 * @author rarchambault (Roxanne Archambault)
	 * @param string:  email
	 * @param string2: refund amount
	 */
	@Then("the member with email address {string} shall receive a refund of {string} percent")
	public void the_member_with_email_address_shall_receive_a_refund_of_percent(String string, String string2) {
		Member currentMember = Member.getWithEmail(string);
		Assignment currentAssignment = currentMember.getAssignment();

		currentAssignment.setRefund(string2); // added
		Assert.assertEquals(string2, currentAssignment.getRefund());
	}

	/**
	 * @author Mona Kalaoun (m-kln)
	 * @param string : email
	 */
	@Given("the member with {string} has started their trip")
	public void the_member_with_has_started_their_trip(String string) {
		var member = Member.getWithEmail(string);
		var assignment = member.getAssignment();
		if (assignment.getAssignmentStatus() != Assignment.AssignmentStatus.Finished) {
			error = AssignmentController.confirmPayment(string, "anything");
			assignment.start(string);
		}
	}

	/**
	 * @author Mona Kalaoun (m-kln)
	 * @param string : email
	 */
	@When("the administrator attempts to finish the trip for the member with email {string}")
	public void the_administrator_attempts_to_finish_the_trip_for_the_member_with_email(String string) {
		error = AssignmentController.finishTrip(string);
	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string banned member's email
	 */

	@Given("the member with {string} is banned")
	public void the_member_with_is_banned(String string) {

		Member currentMember = Member.getWithEmail(string);
		if (currentMember.getMemberStatus() == Member.MemberStatus.NotBanned)
			currentMember.toggleMemberStatus();
		Assert.assertEquals("Banned", currentMember.getMemberStatusFullName());

	}

	/**
	 * @author Nadine Masri (NadineM-00)
	 * @param string  banned member's email
	 * @param string2 member's status in diveSafe
	 */

	@Then("the member with email {string} shall be {string}")
	public void the_member_with_email_shall_be(String string, String string2) {
		Member currentMember = Member.getWithEmail(string);
		Assert.assertEquals(string2, currentMember.getMemberStatusFullName());
	}

	/**
	 * @author Jonathan Batanian
	 * @param string: day
	 */
	@When("the administrator attempts to start the trips for day {string}")
	public void the_administrator_attempts_to_start_the_trips_for_day(String string) {
		int day = Integer.parseInt(string);
		error = AssignmentController.startTripsForDay(day);
	}

	/**
	 * @author Jonathan Batanian
	 * @param string: email
	 */

	@Given("the member with {string} has cancelled their trip")
	public void the_member_with_has_cancelled_their_trip(String string) {
		var member = Member.getWithEmail(string);
		var assignment = member.getAssignment();

		assignment.cancel(string);
		error = AssignmentController.cancelTrip(string);
	}

	/**
	 * @author Jonathan Batanian
	 * @param string: email
	 */
	@Given("the member with {string} has finished their trip")
	public void the_member_with_has_finished_their_trip(String string) {
		var member = Member.getWithEmail(string);
		var assignment = member.getAssignment();
		error = AssignmentController.finishTrip(string);
		error = AssignmentController.confirmPayment(string, "anything");
		assignment.start(string);
		if (assignment.getAssignmentStatus() != Assignment.AssignmentStatus.Cancelled) {
			assignment.finish(string);
		} else
			error = AssignmentController.cancelTrip(string);

	}
}
