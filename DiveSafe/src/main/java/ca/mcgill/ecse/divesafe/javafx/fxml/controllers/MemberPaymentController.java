package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.successful;


import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;




/**
 * 
 * @author jonat (Jonathan Batanian)
 *
 */






public class MemberPaymentController {
	@FXML
	private TextField authorizationCodetxt;
	@FXML
	private TextField userEmailtxt;
	@FXML
	private Button payButton;

	// Event Listener on Button[#payButton].onAction
	@FXML
	public void payButtonClicked(ActionEvent event) {
		String userEmail = userEmailtxt.getText();
		String auCode = authorizationCodetxt.getText();

		String error = AssignmentController.confirmPayment(userEmail, auCode);

		if (successful(error)) {

			userEmailtxt.setText("");
			authorizationCodetxt.setText("");

		} else {

			ViewUtils.showError(error);
		}

	}
}
