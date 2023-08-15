package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The methods in this class were adapted from the BTMS version of the ViewUtils
 * class (provided in tutorials)
 * 
 * @author rarchambault (Roxanne Archambault)
 *
 */
public class ViewUtils {
	/** Calls the controller and shows an error, if applicable. */
	public static boolean callController(String result) {
		if (result.isEmpty()) {
			DiveSafeFxmlView.getInstance().refresh();
			return true;
		}
		showError(result);
		return false;
	}

	/**
	 * Calls the controller and returns true on success. This method is included for
	 * readability.
	 */
	public static boolean successful(String controllerResult) {
		return callController(controllerResult);
	}

	/**
	 * Creates a popup window.
	 *
	 * @param title:   title of the popup window
	 * @param message: message to display
	 */
	public static void makePopupWindow(String title, String message) {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		VBox dialogPane = new VBox();

		// create UI elements
		Text text = new Text(message);
		Button okButton = new Button("OK");
		okButton.setOnAction(a -> dialog.close());

		// display the popup window
		int innerPadding = 10; // inner padding/spacing
		int outerPadding = 100; // outer padding
		dialogPane.setSpacing(innerPadding);
		dialogPane.setAlignment(Pos.CENTER);
		dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
		dialogPane.getChildren().addAll(text, okButton);
		Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
		dialog.setScene(dialogScene);
		dialog.setTitle(title);
		dialog.show();
	}

	public static void showError(String message) {
		makePopupWindow("Error", message);
	}

	/**
	 * @author Mona Kalaoun
	 * @return
	 */
	public static ObservableList<String> guidesYN() {
		List<String> yn = Arrays.asList("Yes", "No");

		return FXCollections.observableList(yn);

	}

	/**
	 * @author Mona Kalaoun
	 * @return
	 */
	public static ObservableList<String> hotelYN() {
		List<String> yn = Arrays.asList("Yes", "No");

		return FXCollections.observableList(yn);

	}
}
