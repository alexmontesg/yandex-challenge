package nl.tue.we.yandex.reranking14;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class YandexLogToMahout {

	private static String currentUID;
	private static Map<String, Integer> currentSessionScore;
	private static FileWriter fw;
	
	private YandexLogToMahout(){}

	public static void convert(String inputFile, String outputFile) throws IOException {
		BufferedReader br = openStreams(inputFile, outputFile);
		readFile(br);
		closeStreams(br);
	}

	private static void closeStreams(BufferedReader br) throws IOException {
		fw.close();
		br.close();
	}

	private static BufferedReader openStreams(String inputFile, String outputFile)
			throws FileNotFoundException, IOException {
		InputStream fis = new FileInputStream(inputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis,
				Charset.forName("UTF-8")));
		fw = new FileWriter(outputFile,true);
		return br;
	}

	private static void readFile(BufferedReader br) throws IOException {
		String lastLine = "";
		while (br.ready()) {
			String line = br.readLine();
			parseLine(line, lastLine);
			lastLine = line;
		}
		String[] lastFields = lastLine.split("\t");
		if(isClick(lastFields)) {
			currentSessionScore.put(lastFields[4], 2);
		}
		writeCurrentSessionScore();
	}

	private static void parseLine(String line, String lastLine) throws IOException {
		String[] fields = line.split("\t");
		String[] lastFields = lastLine.split("\t");
		if (isMetadata(fields)) {
			parseMetadata(fields, lastFields);
		} else if (isClick(fields)) {
			parseClick(fields, lastFields);
		} else {
			parseQuery(fields, lastFields);
		}
	}

	private static void parseClick(String[] fields, String[] lastFields) {
		if (isClick(lastFields)) {
			int value = evaluateLastClickRelevance(fields, lastFields);
			currentSessionScore.put(lastFields[4], value);
		}
	}

	private static void parseQuery(String[] fields, String[] lastFields) {
		if (isClick(lastFields)) {
			int value = evaluateLastClickRelevance(fields, lastFields);
			currentSessionScore.put(lastFields[4], value);
		}
		if (currentSessionScore == null) {
			currentSessionScore = new HashMap<String, Integer>();
		}
		for (int i = 6; i < fields.length; i++) {
			currentSessionScore.put(fields[i].split(",")[0].trim(), 0);
		}
	}

	private static int evaluateLastClickRelevance(String[] fields, String[] lastFields) {
		int timeSpentInLastClick = Integer.parseInt(fields[1].trim())
				- Integer.parseInt(lastFields[1].trim());
		int value = 2;
		if (timeSpentInLastClick < 50) {
			value = 0;
		} else if (timeSpentInLastClick < 400) {
			value = 1;
		}
		return value;
	}

	private static void parseMetadata(String[] fields, String[] lastFields) throws IOException {
		if (isClick(lastFields)) {
			currentSessionScore.put(lastFields[4], 2);
		}
		if(currentSessionScore != null) {
			writeCurrentSessionScore();
		}
		currentSessionScore = null;
		currentUID = fields[3].trim();
	}

	private static void writeCurrentSessionScore() throws IOException {
		for(Map.Entry<String, Integer> entry : currentSessionScore.entrySet()) {
			fw.write(currentUID + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");
		}
	}
	
	private static boolean isMetadata(String[] fields) {
		return fields.length == 4;
	}

	private static boolean isClick(String[] fields) {
		return fields.length == 5;
	}

}
