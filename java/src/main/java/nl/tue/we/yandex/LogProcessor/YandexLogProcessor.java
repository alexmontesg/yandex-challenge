/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.LogProcessor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author t-jukise
 */
public abstract class YandexLogProcessor {

    protected static FileWriter fw;
    protected final Map<Integer, Integer> urls2domains = new HashMap<Integer, Integer>();

    public void convert(String inputFile, String outputFile) throws IOException {
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
        fw = new FileWriter(outputFile, true);
        return br;
    }

    protected void readFile(final BufferedReader br) throws IOException {
        String lastLine = "";
        while (br.ready()) {
            final String line = br.readLine();
            parseLine(line, lastLine);
            lastLine = line;
        }
    }

    protected static boolean isMetadata(String[] fields) {
        return fields.length == 4;
    }

    protected static boolean isClick(String[] fields) {
        return fields.length == 5;
    }

    protected static boolean isQuery(String[] fields) {
        return fields.length > 5;
    }

    protected List<Integer> readSerp(final String[] fields) throws NumberFormatException {
        final List<Integer> urls = new ArrayList<>();
        for (int i = 6; i < 16; i++) {
            final String[] url2domain = fields[i].split(",");
            final Integer url = Integer.valueOf(url2domain[0]);
            final Integer domain = Integer.valueOf(url2domain[1]);
            urls.add(url);
            urls2domains.put(url, domain);
        }
        return urls;
    }

    protected abstract void parseLine(String line, String lastLine) throws IOException;

    protected abstract void parseMetadata(String[] fields, String[] lastFields) throws IOException;

    protected abstract void parseClick(String[] fields, String[] lastFields);

    protected abstract void parseQuery(String[] fields, String[] lastFields);

}
