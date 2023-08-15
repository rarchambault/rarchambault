package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.controller.TOAssignment;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class ViewAssignmentsController {

	@FXML
	private TableView<TOAssignment> assignmentTable;

	@FXML
	private Button AssignmentButton;

	@FXML
	private Button initiateAssignmentButton;

	@FXML
	private Label AssignedLabel;

	@FXML
	private Label PaidLabel;

	@FXML
	private Label StartedLabel;

	@FXML
	private Label FinishedLabel;

	@FXML
	private Label CancelledLabel;

	@FXML
	private Label CancelledBanLabel;

	@FXML
	/**
	 * Initializes the columns of the ViewAssignments table as well as the colors
	 * for the Color Codes table.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 */
	public void initialize() {
		// initialize the overview table by adding new columns
		if (assignmentTable != null) {
			assignmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			var memberNameColumn = createTableColumn("Member Name", "memberName");
			assignmentTable.getColumns().add(memberNameColumn);

			var memberEmailColumn = createTableColumn("Member Email", "memberEmail");
			assignmentTable.getColumns().add(memberEmailColumn);

			var guideNameColumn = createTableColumn("Guide Name", "guideName");
			assignmentTable.getColumns().add(guideNameColumn);

			var guideEmailColumn = createTableColumn("Guide Email", "guideEmail");
			assignmentTable.getColumns().add(guideEmailColumn);

			var hotelColumn = createTableColumn("Hotel", "hotelName");
			assignmentTable.getColumns().add(hotelColumn);
			hotelColumn.setMaxWidth(3000);

			var startDayColumn = createTableColumn("Start Day", "startDay");
			assignmentTable.getColumns().add(startDayColumn);
			startDayColumn.setMaxWidth(3000);
			var endDayColumn = createTableColumn("End Day", "endDay");
			assignmentTable.getColumns().add(endDayColumn);
			endDayColumn.setMaxWidth(3000);

			var guideCostColumn = createTableColumn("Guide Cost", "totalCostForGuide");
			assignmentTable.getColumns().add(guideCostColumn);
			guideCostColumn.setMaxWidth(3000);

			var equipmentCostColumn = createTableColumn("Equipment Cost", "totalCostForEquipment");
			assignmentTable.getColumns().add(equipmentCostColumn);
			equipmentCostColumn.setMaxWidth(5000);

			AssignedLabel.setTextFill(Color.BLACK);
			PaidLabel.setTextFill(Color.BLUE);
			StartedLabel.setTextFill(Color.GREEN);
			FinishedLabel.setTextFill(Color.PURPLE);
			CancelledLabel.setTextFill(Color.RED);
			CancelledBanLabel.setTextFill(Color.RED);

			// change the color of the cells based on the value in the TODailyOverview
			memberNameColumn.setCellFactory(col -> new TableCell<>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					var row = getTableRow();
					setText(item);
					setTextFill(Color.BLACK);
					if (row.getItem() != null && row.getItem().getMemberStatus().equals("Banned")) {
						setText(item + " (banned)");
					}
					if (row.getItem() != null && row.getItem().getAssignmentStatus().equals("Paid")) {
						setTextFill(Color.BLUE);
					}
					if (row.getItem() != null && row.getItem().getAssignmentStatus().equals("Started")) {
						setTextFill(Color.GREEN);
					}
					if (row.getItem() != null && row.getItem().getAssignmentStatus().equals("Finished")) {
						setTextFill(Color.PURPLE);
					}
					if (row.getItem() != null && row.getItem().getAssignmentStatus().equals("Cancelled")) {
						setTextFill(Color.RED);
					}
				}
			});

			// overview table if a refreshable element
			assignmentTable.addEventHandler(DiveSafeFxmlView.REFRESH_EVENT,
					e -> assignmentTable.setItems(getOverviewItems()));

			// register refreshable nodes
			DiveSafeFxmlView.getInstance().registerRefreshEvent(assignmentTable);
		}
	}

	@FXML
	/**
	 * Called when the initiateAssignmentButton is clicked and initiates
	 * assignments.
	 * 
	 * @author Murad Gohar
	 * @param event
	 */
	public void initiateAssignmentsClicked(ActionEvent event) {
		AssignmentController.initiateAssignment();
	}

	/**
	 * Returns an observableList of the current TOAssignment objects in DiveSafe.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 * @return
	 */
	public ObservableList<TOAssignment> getOverviewItems() {
		return FXCollections.observableList(AssignmentController.getAssignments());
	}

	// the table column will automatically display the string value of the property
	// for each instance
	// in the table
	/**
	 * Displays parameters of the TOAssignment objects in the right columns.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 * @param header
	 * @param propertyName
	 * @return
	 */
	public static TableColumn<TOAssignment, String> createTableColumn(String header, String propertyName) {
		TableColumn<TOAssignment, String> column = new TableColumn<>(header);
		column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
		return column;
	}

	// Event Listener on Button[#AssignmentButton].onAction
	/**
	 * Called when AssignmentButton is clicked and displays all current assignments.
	 * 
	 * @author rarchambault (Roxanne Archambault)
	 * @param event
	 */
	@FXML
	public void AssignmentButtonClicked(ActionEvent event) {
		DiveSafeFxmlView.getInstance().refresh();
	}
}
