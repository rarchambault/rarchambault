package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.successful;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.divesafe.controller.MemberController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class DeleteUpdateMemberController {
	@FXML
	private TextField memberNameTextField;
	@FXML
	private TextField memberEmailTextField;
	@FXML
	private TextField emergencyContactTextField;
	@FXML
	private TextField nbrOfDaysTextField;
	@FXML
	private Button updateButton;

	@FXML
	private CheckBox guideCheck;

	@FXML
	private CheckBox hotelCheck;

	@FXML
	private PasswordField passPasswordField;

	@FXML
	private ChoiceBox<String> itemChoice;
	private String[] itemChoices = { "BCD", "dive boots", "net", "snorkel", "goggles", "fins", "rope", "oxygen tank" };

	@FXML
	private Spinner<Integer> quantitySpinner;

	@FXML
	private Button addItemButton;

	@FXML
	private ListView<String> itemView;
	@FXML
	private ListView<Integer> quantView;
	@FXML
	private Button deleteButton;

	@FXML
	/**
	 * @author Mona Kalaoun (m-kln)
	 */
	public void initialize() {
		itemChoice.getItems().addAll(itemChoices);
		SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
		value.setValue(0);
		quantitySpinner.setValueFactory(value);
	}

	/**
	 * @author Mona Kalaoun (m-kln)
	 * @param event when the delete button is clicked
	 */

	public void deleteMemberClicked(ActionEvent event) {
		String email = memberEmailTextField.getText();
		String error = MemberController.deleteMember(email);

		List<String> itemNames = new ArrayList<String>();
		List<Integer> itemQuant = new ArrayList<Integer>();
		itemNames = itemView.getItems();
		itemQuant = quantView.getItems();

		if (successful(error)) {
			memberEmailTextField.setText("");
			memberNameTextField.setText("");
			emergencyContactTextField.setText("");
			nbrOfDaysTextField.setText("");
			itemView.getItems().removeAll(itemNames);
			quantView.getItems().removeAll(itemQuant);
			// guide
			guideCheck.setSelected(false);

			// hotel
			hotelCheck.setSelected(false);

			passPasswordField.setText("");
		} else {
			ViewUtils.showError(error);
		}
	}

	/**
	 * @author Nadine Masri
	 * @param event when the update button is clicked
	 */

	public void updateMemberClicked(ActionEvent event) {
		String newName = memberNameTextField.getText();
		String newEmail = memberEmailTextField.getText();
		String newEmergencyContact = emergencyContactTextField.getText();
		int newNbrDays = Integer.parseInt(nbrOfDaysTextField.getText());

		// new list of items

		List<String> itemNames = new ArrayList<String>();
		List<Integer> itemQuant = new ArrayList<Integer>();
		//
		boolean guideYN = false;
		if (guideCheck.isSelected())
			guideYN = true;
		else
			guideYN = false;

		boolean hotelYN = false;

		if (hotelCheck.isSelected())
			hotelYN = true;
		else
			hotelYN = false;

		String password = passPasswordField.getText();

		itemNames = itemView.getItems();
		itemQuant = quantView.getItems();

		String error = MemberController.updateMember(newEmail, password, newName, newEmergencyContact, newNbrDays,
				guideYN, hotelYN, itemNames, itemQuant);

		// if member is updated, reset the textfields to blank
		if (successful(error)) {
			memberNameTextField.setText("");
			memberEmailTextField.setText("");
			emergencyContactTextField.setText("");
			nbrOfDaysTextField.setText("");
			itemView.getItems().removeAll(itemNames);
			quantView.getItems().removeAll(itemQuant);
			// guide
			guideCheck.setSelected(false);
			// guideChoiceBox.setValue(null);
			// hotel
			hotelCheck.setSelected(false);
			// hotelChoiceBox.setValue(null);

			passPasswordField.setText("");
		} else {
			ViewUtils.showError(error);
		}

	}

	/**
	 * Add requested items to the list view
	 * 
	 * @author Mona Kalaoun (m-kln)
	 * @param event
	 */
	public void addItemClicked(ActionEvent event) {

		String item = itemChoice.getValue();
		int quant = quantitySpinner.getValue();

		itemView.getItems().addAll(item);
		quantView.getItems().addAll(quant);

		itemChoice.setValue(null);
		SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
		value.setValue(0);
		quantitySpinner.setValueFactory(value);

	}
}