package logs.ui;

import java.util.Date;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import logs.model.LogEvent;

/**
 * LogEventInspector class that display the data available for the event. It
 * shows the date, the type and the arguments. Extra views can be added for
 * specific event Types.
 *
 * @author jeremiegarcia
 *
 */
public class EventInspector extends VBox {

	public static EventInspector singleton = null;

	private TextField dateTxt = new TextField();
	private TextField typeTxt = new TextField();
	private TextArea argsTxt = new TextArea();

	private LogEvent currentEvent = null;
	private Node eventNode = null;

	private EventInspector() {
		super();
		this.setPrefSize(200, 0);

		Label title = new Label("Inspector");

		Label date = new Label("Date");
		dateTxt.setMaxSize(200, 40);
		dateTxt.setEditable(false);

		Label type = new Label("Type");
		typeTxt.setMaxSize(200, 40);
		typeTxt.setEditable(false);

		Label args = new Label("Args");
		argsTxt.setEditable(false);

		this.setPadding(new Insets(5, 10, 5, 20));

		this.getChildren().addAll(title, date, dateTxt, type, typeTxt, args, argsTxt);

	}

	public static EventInspector getInstance() {
		if (singleton == null)
			singleton = new EventInspector();
		return singleton;
	}

	public void update(LogEvent event) {

		if (event != currentEvent) {
			this.dateTxt.setText(new Date(event.getTimeStamp()).toString());
			this.typeTxt.setText(event.getLabel());
			this.argsTxt.setText(event.getArgs().toString());
			currentEvent = event;

			if (this.eventNode != null) {
				this.getChildren().remove(eventNode);
				this.eventNode = null;
			}

			if (event.hasInspectorNode()) {
				this.eventNode = event.getInspectorNode();
				this.getChildren().add(this.eventNode);
				// fit node to available space
				double width = this.getWidth();
				double height = this.getHeight() - (this.argsTxt.getLayoutY() + this.argsTxt.getHeight());

				double nodeWidth = this.eventNode.getBoundsInParent().getWidth();
				double nodeHeight = this.eventNode.getBoundsInParent().getHeight();

				double widthRatio = width / nodeWidth;
				double heightRatio = height / nodeHeight;

				double minRatio = (widthRatio < heightRatio) ? widthRatio : heightRatio;
				this.eventNode.getTransforms().add(new Scale(minRatio, minRatio));

			}
		}

	}
}
