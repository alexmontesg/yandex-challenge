/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tue.we.yandex.LogProcessor.QueryId;
import nl.tue.we.yandex.LogProcessor.YandexLogProcessor;

/**
 *
 * @author t-jukise
 */
public class FeatureProcessorSession extends YandexLogProcessor {

    private Integer currentSessionId = null;
    private Integer lastSessionId = null;

    @Override
    protected void parseLine(String line, String lastLine) throws IOException {
        final String[] fields = line.split("\t");
        final Map<Integer, List<Integer>> serpIs2showedUlrs = new HashMap<>();
        currentSessionId = Integer.parseInt(fields[0]);
        if (currentSessionId != lastSessionId) {
            processSession();
            lastSessionId = currentSessionId;
        }
        if (isQuery(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            final QueryId queryId = new QueryId(Integer.parseInt(fields[4]));
            final List<Integer> urls = readSerp(fields);
            serpIs2showedUlrs.put(serpId, urls);
            
        } else if (isClick(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            int urlId = Integer.parseInt(fields[4]);
            //updateMap2List(serpId, urlId, serpId2ListClickedUrls);
        }
    }

    private void processSession() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void parseMetadata(String[] fields, String[] lastFields) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void parseClick(String[] fields, String[] lastFields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void parseQuery(String[] fields, String[] lastFields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
