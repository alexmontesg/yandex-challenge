package nl.tue.we.yandex.reranking14;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nl.tue.we.yandex.LogProcessor.YandexLogProcessor;

public class YandexLogToMahout extends YandexLogProcessor {

    private static String currentUID;
    private static Map<String, Integer> currentSessionScore;

    private YandexLogToMahout() {
    }

    @Override
    protected void readFile(BufferedReader br) throws IOException {
        String lastLine = "";
        while (br.ready()) {
            String line = br.readLine();
            parseLine(line, lastLine);
            lastLine = line;
        }
        String[] lastFields = lastLine.split("\t");
        if (isClick(lastFields)) {
            currentSessionScore.put(lastFields[4], 2);
        }
        writeCurrentSessionScore();
    }

    @Override
    protected void parseLine(String line, String lastLine) throws IOException {
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

    @Override
    protected void parseClick(String[] fields, String[] lastFields) {
        if (isClick(lastFields)) {
            int value = evaluateLastClickRelevance(fields, lastFields);
            currentSessionScore.put(lastFields[4], value);
        }
    }

    @Override
    protected void parseQuery(String[] fields, String[] lastFields) {
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

    @Override
    protected void parseMetadata(String[] fields, String[] lastFields) throws IOException {
        if (isClick(lastFields)) {
            currentSessionScore.put(lastFields[4], 2);
        }
        if (currentSessionScore != null) {
            writeCurrentSessionScore();
        }
        currentSessionScore = null;
        currentUID = fields[3].trim();
    }

    private static void writeCurrentSessionScore() throws IOException {
        for (Map.Entry<String, Integer> entry : currentSessionScore.entrySet()) {
            fw.write(currentUID + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");
        }
    }

}
