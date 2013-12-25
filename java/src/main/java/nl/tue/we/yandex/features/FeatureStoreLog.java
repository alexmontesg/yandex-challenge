/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;

import com.google.common.collect.HashBiMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nl.tue.we.yandex.LogProcessor.YandexLogProcessor;

/**
 *
 * @author t-jukise
 */
public class FeatureStoreLog extends YandexLogProcessor {
    
    private static final int AMOUNT_QUERIES = 65172853;

    private Map<Integer, Integer> queryFreq = new HashMap<Integer, Integer>();
    private Map<Integer, Map<Integer, Integer>> query2urls = new HashMap<Integer, Map<Integer, Integer>>();
    private Map<Integer, Map<Integer, Integer>> query2domains = new HashMap<Integer, Map<Integer, Integer>>();
    private Map<Integer, Map<Integer, Integer>> query2PositionInSession = new HashMap<Integer, Map<Integer, Integer>>();
    private Map<Integer, Map<Integer, Integer>> query2ClickedPosition = new HashMap<Integer, Map<Integer, Integer>>();
    private Map<Integer, Integer> urls2domains = new HashMap<Integer, Integer>();

    //temp storages make  sure it's empties when new session comes
    private Map<Integer, List<Integer>> serpId2ListClickedUrls = new HashMap<Integer, List<Integer>>();
    private Map<Integer, List<Integer>> serpIs2showedUrls = new HashMap<Integer, List<Integer>>();
    private Map<Integer, Integer> serpId2QueryId = new HashMap<Integer, Integer>();
    private Map<Integer, List<Integer>> queryId2PositionInSession = new HashMap<Integer, List<Integer>>();
    //
    private Integer currentSessionId = null;
    private Integer lastSessionId = null;
    private int positionQueryInSession = 0;

    @Override
    protected void readFile(final BufferedReader br) throws IOException {
        String lastLine = "";
        while (br.ready()) {
            final String line = br.readLine();
            parseLine(line, lastLine);
            lastLine = line;
        }
    }

    @Override
    protected void parseLine(String line, String lastLine) throws IOException {
        final String[] fields = line.split("\t");
        final String[] lastFields = lastLine.split("\t");
        currentSessionId = Integer.parseInt(fields[0]);
        if (currentSessionId != lastSessionId) {
            pushSessionToStorage();
            lastSessionId = currentSessionId;
            positionQueryInSession = 0;
        }
        if (isQuery(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            int queryId = Integer.parseInt(fields[4]);
            positionQueryInSession ++;
            updateMap2List(queryId, positionQueryInSession, queryId2PositionInSession);
            serpId2QueryId.put(serpId, queryId);
            updateMapInt2Freq(queryId, queryFreq);
            final List<Integer> urls = readSerp(fields);  
            serpIs2showedUrls.put(serpId, urls);
            
        } else if (isClick(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            int urlId = Integer.parseInt(fields[4]);
            updateMap2List(serpId, urlId, serpId2ListClickedUrls);
        }
    }

    private  List<Integer> readSerp(final String[] fields) throws NumberFormatException {
        final List<Integer> urls = new ArrayList<>();
        for(int i=6; i<16; i++){
            final String [] url2domain = fields[i].split(",");
            final Integer url = Integer.valueOf(url2domain[0]);
            final Integer domain = Integer.valueOf(url2domain[1]);
            urls.add(url);
            urls2domains.put(url,domain);
        }
        return urls;
    }

    private void pushSessionToStorage() {
        for(final Integer serpId: serpId2QueryId.keySet()){
            final Integer queryId = serpId2QueryId.get(serpId);
            final List<Integer> clickedUrls = serpId2ListClickedUrls.get(serpId);
            procesFreq(queryId, clickedUrls, query2urls);
            final List<Integer> positionsInSession = queryId2PositionInSession.get(queryId);
            procesFreq(queryId, positionsInSession, query2PositionInSession);
            final List<Integer> urls = serpIs2showedUrls.get(serpId);
            final List<Integer> positionClicks = new LinkedList<>();
            for(Integer clickedUrl : clickedUrls ){
                int position = urls.indexOf(clickedUrl);
                positionClicks.add(position);
            }
            procesFreq(queryId, positionClicks, query2ClickedPosition);
        }
        serpId2ListClickedUrls = new HashMap<>();
        serpId2QueryId = new HashMap<>();
        queryId2PositionInSession = new HashMap<>();
        serpIs2showedUrls = new HashMap<>();
    }

    @Override
    protected void parseMetadata(final String[] fields, final String[] lastFields) throws IOException {
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

    public void updateMap2List(final Integer key, final Integer value, final Map<Integer, List<Integer>> map) {
        List<Integer> list = map.get(key);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(value);
        map.put(key, list);
    }
    
     public void updateMapInt2Freq(final Integer key, final Map<Integer, Integer> map) {
        Integer value = map.get(key);
        if (value == null) {
            value = 1;
        } else{
            value++;
        }
        map.put(key, value);
    }
     
     public void procesFreq (final Integer key, final List<Integer> list, final Map<Integer, Map<Integer,Integer>> output){
             final Map<Integer, Integer> value2Freq = new HashMap<>();
             for(Integer value: list){
                 updateMapInt2Freq(value, value2Freq);
             }
             output.put(key, value2Freq);
     }

}
