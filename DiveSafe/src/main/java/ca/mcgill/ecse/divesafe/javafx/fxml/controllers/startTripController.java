package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.successful;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class startTripController {

	@FXML
	private Button StartTripsButton;
	@FXML
	private TextField MemberEmailText;
	@FXML
	private Button FinishTripButton;
	@FXML
	private Button CancelTripButton;

	@FXML
	private ChoiceBox<String> dateChoiceBox;

	@FXML
	/**
	 * Initializes the choices for the start day.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 */
	public void initialize() {
		int numDays = DiveSafeApplication.getDiveSafe().getNumDays();
		String[] dateChoices = new String[numDays + 1];
		for (int i = 1; i <= numDays; i++) {
			dateChoices[i] = String.valueOf(i);
		}
		dateChoiceBox.getItems().addAll(dateChoices);
	}

	// Event Listener on Button[#StartTripsButton].onAction
	/**
	 * Called when StartTripsButton is clicked and starts trips for this day.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 * @param event
	 */
	@FXML
	public void StartTripsClicked(ActionEvent event) {
		int startDay = Integer.valueOf(dateChoiceBox.getValue());
		String error = AssignmentController.startTripsForDay(startDay);
		if (successful(error)) {
			dateChoiceBox.setValue(null);
		} else {
			ViewUtils.showError(error);
		}
	}

	// Event Listener on Button[#FinishTripButton].onAction
	/**
	 * Called when FinishTripButton is clicked and finished the trip of the member
	 * whose email is written.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 * @param event
	 */
	@FXML
	public void FinishTripClicked(ActionEvent event) {
		String email = MemberEmailText.getText();
		String error = AssignmentController.finishTrip(email);
		if (successful(error)) {
			MemberEmailText.setText("");
		} else {
			ViewUtils.showError(error);
		}
	}

	// Event Listener on Button[#CancelTripButton].onAction
	/**
	 * Called when CancelTripButton is clicked and cancels the trip of the member
	 * whose email is written.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 * @param event
	 */
	@FXML
	public void CancelTripClicked(ActionEvent event) {
		String email = MemberEmailText.getText();
		String error = AssignmentController.cancelTrip(email);
		if (successful(error)) {
			MemberEmailText.setText("");
		} else {
			ViewUtils.showError(error);
		}
	}
}
