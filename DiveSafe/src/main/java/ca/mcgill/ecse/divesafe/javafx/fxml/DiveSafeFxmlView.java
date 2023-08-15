package ca.mcgill.ecse.divesafe.javafx.fxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DiveSafeFxmlView extends Application {

	public static final EventType<Event> REFRESH_EVENT = new EventType<>("REFRESH");
	private static DiveSafeFxmlView instance;
	private List<Node> refreshableNodes = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) {
		instance = this;
		try {
			var root = (Pane) FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			root.setStyle(DiveSafeApplication.BLUE_MODE ? "-fx-base: rgba(69, 161, 255, 1);" : "");
			var scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			primaryStage.setTitle("DiveSafe");
			primaryStage.show();
			refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Register the node for receiving refresh events
	public void registerRefreshEvent(Node node) {
		refreshableNodes.add(node);
	}

	// Register multiple nodes for receiving refresh events
	public void registerRefreshEvent(Node... nodes) {
		for (var node : nodes) {
			refreshableNodes.add(node);
		}
	}

	// remove the node from receiving refresh events
	public void removeRefreshableNode(Node node) {
		refreshableNodes.remove(node);
	}

	// fire the refresh event to all registered nodes
	public void refresh() {
		for (Node node : refreshableNodes) {
			node.fireEvent(new Event(REFRESH_EVENT));
		}
	}

	public static DiveSafeFxmlView getInstance() {
		return instance;
	}

}
