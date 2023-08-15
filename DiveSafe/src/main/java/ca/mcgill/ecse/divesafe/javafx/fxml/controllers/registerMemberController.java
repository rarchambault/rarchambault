package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.successful;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
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

public class registerMemberController {
	@FXML
	private TextField memberNameTextField;
	@FXML
	private TextField memberEmailTextField;
	@FXML
	private TextField emergencyContactTextField;

	@FXML
	private Button registerButton;

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
	private Spinner<Integer> numDaysSpinner;

	@FXML
	private Button addItemButton;

	@FXML
	private ListView<String> itemView;
	@FXML
	private ListView<Integer> quantView;

	@FXML
	/**
	 * @author Mona Kalaoun (m-kln)
	 */
	public void initialize() {
		itemChoice.getItems().addAll(itemChoices);
		SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
		SpinnerValueFactory<Integer> value2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
				DiveSafeApplication.getDiveSafe().getNumDays());
		value.setValue(0);
		value2.setValue(1);
		quantitySpinner.setValueFactory(value);
		numDaysSpinner.setValueFactory(value2);
	}

	@FXML
	/**
	 * Register member
	 * 
	 * @author Mona Kalaoun (m-kln)
	 * @param event
	 */
	public void addMemberClicked(ActionEvent event) {
		String name = memberNameTextField.getText();
		String email = memberEmailTextField.getText();
		String emergencyContact = emergencyContactTextField.getText();

		int numDay = numDaysSpinner.getValue();

		List<String> itemNames = new ArrayList<String>();
		List<Integer> itemQuant = new ArrayList<Integer>();

		// guide bool
		boolean guideYN = false;
		if (guideCheck.isSelected())
			guideYN = true;
		else
			guideYN = false;

		// hotel bool

		boolean hotelYN = false;
		if (hotelCheck.isSelected())
			hotelYN = true;
		else
			hotelYN = false;

		String password = passPasswordField.getText();

		itemNames = itemView.getItems();
		itemQuant = quantView.getItems();

		String error = MemberController.registerMember(email, password, name, emergencyContact, numDay, guideYN,
				hotelYN, itemNames, itemQuant);

		if (successful(error)) {
			memberNameTextField.setText("");
			memberEmailTextField.setText("");
			emergencyContactTextField.setText("");

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