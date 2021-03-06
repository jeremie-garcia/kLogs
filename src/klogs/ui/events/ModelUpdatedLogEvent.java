package klogs.ui.events;

import java.util.ArrayList;

import javafx.scene.Node;
import klogs.trainingdata.model.ModelProcessor;
import klogs.trainingdata.model.TrainingDataSet;
import klogs.trainingdata.ui.TrainingVizKNN;
import klogs.utils.KLogFileUtils;
import logs.model.LogEvent;

public class ModelUpdatedLogEvent extends LogEvent {

	private TrainingDataSet dataSet;
	private TrainingVizKNN inspectorNode;

	public ModelUpdatedLogEvent(String label, long timeStamps, long duration, ArrayList<String> args, String source) {
		super(label, timeStamps, duration, args, source);
		String fileName = KLogFileUtils.getFileNameForModelFromEvent(this);
		this.dataSet = ModelProcessor.extractDataSetFromKNNModelFile(fileName);
	}

	// TODO: put back true when bugs are resolved
	@Override
	public boolean hasInspectorNode() {
		return true;
	}

	@Override
	public Node getInspectorNode() {
		if (inspectorNode == null) {
			inspectorNode = new TrainingVizKNN(this);
		}
		return inspectorNode;
	}

	public TrainingDataSet getTrainingDataSet() {
		return dataSet;
	}
}
