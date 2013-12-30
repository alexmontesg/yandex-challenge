package nl.tue.we.yandex.formatter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.tue.we.yandex.features.Feature;
import nl.tue.we.yandex.features.JudgedPair;
import nl.tue.we.yandex.features.LogAction;

/**
 * 
 * @author Alejandro Montes Garcia
 * 
 */
public class FeaturesToRankLib {

	private List<String> knownFeatures;

	public void format(Collection<LogAction> logActions, String outputFile)
			throws IOException {
		knownFeatures = new LinkedList<String>();
		FileWriter fw = new FileWriter(outputFile, true);
		for (LogAction logAction : logActions) {
			fw.write(actionToLine(logAction));
		}
		fw.close();
	}

	private String actionToLine(LogAction logAction) {
		Map<Integer, Float> featureMap = new HashMap<Integer, Float>();
		for (Feature feature : logAction.getFeatures()) {
			String featureName = feature.getName();
			if (!knownFeatures.contains(featureName)) {
				knownFeatures.add(featureName);
			}
			featureMap.put(knownFeatures.indexOf(featureName),
					feature.getValue());
		}
		String line = buildLine(logAction.getLabel(), featureMap,
				new TreeSet<Integer>(featureMap.keySet()));
		return line;
	}

	private String buildLine(JudgedPair pair, Map<Integer, Float> featureMap,
			SortedSet<Integer> orderedKeys) {
		StringBuilder strb = new StringBuilder();
		strb.append(pair.getJudge());
		strb.append(" qid:");
		strb.append(pair.getQueryId());
		for (Integer key : orderedKeys) {
			strb.append(" ");
			strb.append(key);
			strb.append(":");
			strb.append(featureMap.get(key));
		}
		strb.append("\n");
		return strb.toString();
	}
}
