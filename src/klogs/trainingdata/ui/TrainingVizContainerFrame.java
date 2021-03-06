package klogs.trainingdata.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;
import klogs.trainingdata.model.ModelProcessor;
import klogs.trainingdata.model.TrainingDataSet;
import klogs.utils.KLogFileUtils;
import logs.model.LogEvent;
import logs.utils.JavaFXUtils;

/**
 * This class is the main container for comparison tool of a single exercise
 * input strategies Needs: way to evaluate the classifier for the exercise for
 * each trial.
 *
 * @author jeremiegarcia
 *
 */
public class TrainingVizContainerFrame {

	String type;
	ArrayList<LogEvent> events;

	ArrayList<TrainingDataSet> dataSets;

	public int[] globalInputMinValues;
	public int[] globalInputMaxValues;
	public int[] globalInputRangeValues;
	HashMap<Integer, Color> colorMap;

	public TrainingVizContainerFrame(String type, ArrayList<LogEvent> events, double grade) {
		this.type = type;
		this.events = events;
		this.dataSets = new ArrayList<TrainingDataSet>();

		String dir = null;
		for (LogEvent logEvent : this.events) {
			String fileName = KLogFileUtils.getFileNameForModelFromEvent(logEvent);
			if (dir == null) {
				dir = new File(fileName).getParent();
			}
			TrainingDataSet data = ModelProcessor.extractDataSetFromKNNModelFile(fileName);
			data.setTimeStamp(logEvent.getTimeStamp());
			data.grade = grade;
			// global grade for now but should be individualized
			this.dataSets.add(data);
		}

		this.updateMinMaxValues();
		this.updateColorScale();
	}

	private void updateColorScale() {
		this.colorMap = new HashMap<Integer, Color>();
		for (TrainingDataSet set : this.dataSets) {
			for (int out : set.possibleOutputs) {
				if (!colorMap.containsKey(out)) {
					colorMap.put(out, JavaFXUtils.getColorWithGoldenRationByIndex(out));
				}
			}
		}
	}

	private void updateMinMaxValues() {
		if (dataSets.size() > 0) {
			int size = this.dataSets.get(0).numInputs;

			// initialize to first element
			this.globalInputMinValues = this.dataSets.get(0).inputMinValues;
			this.globalInputMaxValues = this.dataSets.get(0).inputMaxValues;

			// then iterate over all dataSets to pudate the min max values

			for (TrainingDataSet set : this.dataSets) {
				for (int k = 0; k < size; k++) {
					if (set.inputMinValues[k] < this.globalInputMinValues[k]) {
						this.globalInputMinValues[k] = set.inputMinValues[k];
					}

					if (set.inputMaxValues[k] > this.globalInputMaxValues[k]) {
						this.globalInputMaxValues[k] = set.inputMaxValues[k];
					}
				}
			}

			globalInputRangeValues = new int[size];
			for (int k = 0; k < size; k++) {
				globalInputRangeValues[k] = globalInputMaxValues[k] - globalInputMinValues[k];
			}
		}
	}

}
