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

/**
 *
 * @author t-jukise
 */
public abstract class YandexLogProcessor {
    
   protected static FileWriter fw;
    
    public void convert(String inputFile, String outputFile) throws IOException {
		BufferedReader br = openStreams(inputFile, outputFile);
		readFile(br);
		closeStreams(br);
	}
    
    private static void closeStreams(BufferedReader br) throws IOException {
		fw.close();
		br.close();
	}
    protected abstract void readFile(BufferedReader br) throws IOException;

	private static BufferedReader openStreams(String inputFile, String outputFile)
			throws FileNotFoundException, IOException {
		InputStream fis = new FileInputStream(inputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis,
				Charset.forName("UTF-8")));
		fw = new FileWriter(outputFile,true);
		return br;
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

    protected abstract void parseLine(String line, String lastLine) throws IOException;
    protected abstract void parseMetadata(String[] fields, String[] lastFields) throws IOException;
    protected abstract void parseClick(String[] fields, String[] lastFields);
    protected abstract void parseQuery(String[] fields, String[] lastFields);

}
