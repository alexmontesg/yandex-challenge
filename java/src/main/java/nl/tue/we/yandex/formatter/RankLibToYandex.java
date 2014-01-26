package nl.tue.we.yandex.formatter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RankLibToYandex {

	private FileWriter fw;
	private String currentQueryID;
	private static RankLibToYandex instance;
	private Map<String, String> queryToSession;

	private class ValueComparator implements Comparator<String> {

		Map<String, Double> base;

		public ValueComparator(Map<String, Double> base) {
			this.base = base;
		}

		public int compare(String a, String b) {
			return base.get(a) < base.get(b) ? -1 : 1;
		}
	}

	private RankLibToYandex() {
	}

	public static RankLibToYandex getInstance() {
		if (instance == null) {
			instance = new RankLibToYandex();
			instance.fw = null;
		}
		return instance;
	}

	/**
	 * 
	 * @param inputFile
	 *            Input ranklib file
	 * @param outputFile
	 *            Output yandex file
	 * @param queryToSession
	 *            Map that takes a query id and converts it into a session id
	 * @throws IOException
	 */
	public void format(String inputFile, String outputFile,
			Map<String, String> queryToSession) throws IOException {
		if (fw == null) {
			this.queryToSession = queryToSession;
			BufferedReader br = openStreams(inputFile, outputFile);
			readFile(br);
			closeStreams(br);
		}
	}

	private BufferedReader openStreams(String inputFile, String outputFile)
			throws FileNotFoundException, IOException {
		InputStream fis = new FileInputStream(inputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis,
				Charset.forName("UTF-8")));
		fw = new FileWriter(outputFile, true);
		return br;
	}

	private void readFile(BufferedReader br) throws IOException {
		Map<String, Double> scores = new HashMap<String, Double>();
		while (br.ready()) {
			String line = br.readLine();
			String[] fields = line.split("\t");
			String queryID = fields[0];
			if (queryID.equalsIgnoreCase(currentQueryID)) {
				scores.put(fields[1], Double.parseDouble(fields[2]));
			} else {
				if (!scores.isEmpty()) {
					writeSession(scores);
					scores.clear();
				}
				currentQueryID = queryID;
				scores.put(fields[1], Double.parseDouble(fields[2]));
			}
		}
		writeSession(scores);
	}

	private void writeSession(Map<String, Double> scores) throws IOException {
		TreeMap<String, Double> sortedScores = new TreeMap<String, Double>(
				new ValueComparator(scores));
		String sessionID = queryToSession.get(currentQueryID);
		StringBuilder strb = new StringBuilder();
		for(Map.Entry<String, Double> entry : sortedScores.entrySet()) {
			strb.append(sessionID);
			strb.append(",");
			strb.append(entry.getKey());
			strb.append("\n");
		}
		fw.write(strb.toString());
	}

	private void closeStreams(BufferedReader br) throws IOException {
		fw.close();
		fw = null;
		br.close();
	}

}
