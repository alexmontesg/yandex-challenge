/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;

import java.io.IOException;
import java.util.*;
import nl.tue.we.yandex.LogProcessor.QueryId;
import nl.tue.we.yandex.LogProcessor.YandexLogProcessor;
import nl.tue.we.yandex.LogProcessor.Query;

/**
 *
 * @author t-jukise
 */
public class FeatureStoreLog extends YandexLogProcessor {

    private static final int AMOUNT_QUERIES = 65172853;

    private final Map<QueryId, Integer> query2Freq = new HashMap<QueryId, Integer>();
    private final Map<QueryId, Map<Integer, Integer>> query2Urls = new HashMap<QueryId, Map<Integer, Integer>>();
    private final Map<QueryId, Map<Integer, Integer>> query2PositionInSession = new HashMap<QueryId, Map<Integer, Integer>>();
    private final Map<QueryId, Map<Integer, Integer>> query2ClickedPosition = new HashMap<QueryId, Map<Integer, Integer>>();
    private final Map<QueryId, Float> query2ProbReform = new HashMap<>();
    private final Map<QueryId, Integer> query2Reformulation = new HashMap<>();

    //private final Map<QueryId, Map<Integer, Integer>> query2domains = new HashMap<QueryId, Map<Integer, Integer>>();
    //temp storages make  sure it's empties when new session comes
    private Map<Integer, List<Integer>> serpId2ListClickedUrls = new HashMap<Integer, List<Integer>>();
    private Map<Integer, List<Integer>> serpId2ShowedUrls = new HashMap<Integer, List<Integer>>();
    private Map<Integer, QueryId> serpId2QueryId = new HashMap<Integer, QueryId>();
    private Map<QueryId, List<Integer>> queryId2PositionInSession = new HashMap<QueryId, List<Integer>>();
    //
    private Integer currentSessionId = null;
    private Integer lastSessionId = null;
    private int positionQueryInSession = 0;

    protected Map<QueryId, List<Feature>> returnQueryFeature() {
        final Map<QueryId, List<Feature>> query2Features = new HashMap<>();
        for (final QueryId queryId : query2Freq.keySet()) {
            final List<Feature> features = new LinkedList<>();
            final Feature queryFreq = new Feature("query_freq", (float) query2Freq.get(queryId) / AMOUNT_QUERIES, 1);
            features.add(queryFreq);
            final Feature queryPositionInSession = new Feature("query_position_session", getAvgMap(query2PositionInSession.get(queryId)), 2);
            features.add(queryPositionInSession);
            final Feature queryClickUrlEntropy = new Feature("query_click_url_entropy", calculateQueryClickUrlEntropy(query2Urls.get(queryId)),3);
            features.add(queryClickUrlEntropy);
            final Feature queryClickDomainEntropy = new Feature("query_click_url_entropy", calculateQueryClickDomainEntropy(query2Urls.get(queryId)),4);
            features.add(queryClickDomainEntropy);
            final Feature queryCtr = new Feature("query_ctr", calculateQueryCTR(queryId), 5);
            features.add(queryCtr);
            calculateAvg(query2Reformulation);
            final Feature refProb = new Feature("query_ref_prob", query2ProbReform.get(queryId),6);
            features.add(refProb);
            query2Features.put(queryId, features);
        }
        return query2Features;
    }
   

    @Override
    protected void parseLine(String line, String lastLine) throws IOException {
        final String[] fields = line.split("\t");
        Query prevQuery = null ;
        Query currentQuery = null;
        currentSessionId = Integer.parseInt(fields[0]);
        if (currentSessionId != lastSessionId) {
            pushSessionToStorage();
            lastSessionId = currentSessionId;
            positionQueryInSession = 0;
            prevQuery = null;
        }
        if (isQuery(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            final QueryId queryId = new QueryId(Integer.parseInt(fields[4]));
            positionQueryInSession++;
            updateMap2List(queryId, positionQueryInSession, queryId2PositionInSession);
            serpId2QueryId.put(serpId, queryId);
            updateMapQueryId2Freq(queryId, query2Freq);
            final List<Integer> urls = readSerp(fields);
            serpId2ShowedUrls.put(serpId, urls);
            final List<Integer> currentTerms = new LinkedList<>();
            currentQuery = new Query(queryId, currentTerms,0);
            for(String elem: fields[4].split(",")){
                currentTerms.add(Integer.parseInt(elem));
            }
            if(prevQuery!= null && Query2IssuedTime.isQueriesSimilar(prevQuery.getTerms(), currentQuery.getTerms())){
                updateMapQueryId2Freq(queryId, query2Reformulation);
            }
        } else if (isClick(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            int urlId = Integer.parseInt(fields[4]);
            updateMap2List(serpId, urlId, serpId2ListClickedUrls);
        }
        prevQuery = currentQuery;
        lastSessionId = currentSessionId;
    }

    private void pushSessionToStorage() {
        for (final Integer serpId : serpId2QueryId.keySet()) {
            final QueryId queryId = serpId2QueryId.get(serpId);
            final List<Integer> clickedUrls = serpId2ListClickedUrls.get(serpId);
            procesFreq(queryId, clickedUrls, query2Urls);
            final List<Integer> positionsInSession = queryId2PositionInSession.get(queryId);
            procesFreq(queryId, positionsInSession, query2PositionInSession);
            final List<Integer> urls = serpId2ShowedUrls.get(serpId);
            final List<Integer> positionClicks = new LinkedList<>();
            for (Integer clickedUrl : clickedUrls) {
                int position = urls.indexOf(clickedUrl);
                positionClicks.add(position);
            }
            procesFreq(queryId, positionClicks, query2ClickedPosition);
        }
        serpId2ListClickedUrls = new HashMap<>();
        serpId2QueryId = new HashMap<>();
        queryId2PositionInSession = new HashMap<>();
        serpId2ShowedUrls = new HashMap<>();
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

    public void updateMap2List(final QueryId key, final Integer value, final Map<QueryId, List<Integer>> map) {
        List<Integer> list = map.get(key);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(value);
        map.put(key, list);
    }

    public void updateMap2List(final Integer key, final Integer value, final Map<Integer, List<Integer>> map) {
        List<Integer> list = map.get(key);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(value);
        map.put(key, list);
    }

    public void updateMapQueryId2Freq(final QueryId key, final Map<QueryId, Integer> map) {
        Integer value = map.get(key);
        if (value == null) {
            value = 1;
        } else {
            value++;
        }
        map.put(key, value);
    }

    public void updateMapInt2Freq(final Integer key, final Map<Integer, Integer> map) {
        Integer value = map.get(key);
        if (value == null) {
            value = 1;
        } else {
            value++;
        }
        map.put(key, value);
    }

    public void procesFreq(final QueryId key, final List<Integer> list, final Map<QueryId, Map<Integer, Integer>> output) {
        final Map<Integer, Integer> value2Freq = new HashMap<>();
        for (Integer value : list) {
            updateMapInt2Freq(value, value2Freq);
        }
        output.put(key, value2Freq);
    }

    private float getAvgList(final List<Integer> list) {
        int sum = 0;
        for (final Integer integer : list) {
            sum = sum + integer;
        }
        return (float) sum / list.size();
    }

    private float getAvgMap(final Map<Integer, Integer> map) {
        int sum = 0;
        int size = 0;
        for (final Integer key : map.keySet()) {
            int value = map.get(key);
            size = size + value;
            sum = sum + key * value;
        }
        return (float) sum / size;
    }

    private float calculateQueryClickUrlEntropy(final Map<Integer, Integer> map) {
        int sum = 0;
        for (final Integer value : map.values()) {
            sum = sum + value;
        }
        float entropy = calculateEntropy(map, sum);
        return -entropy;
    }

    private float calculateEntropy(final Map<Integer, Integer> map, int sum) {
        float entropy = 0;
        for (final Integer key : map.keySet()) {
            float prob = (float) key / sum;
            entropy = (float) (entropy + prob * Math.log(prob));
        }
        return entropy;
    }

    private float calculateQueryClickDomainEntropy(final Map<Integer, Integer> map) {
        int sum = 0;
        final Map<Integer, Integer> domain2Freq = new HashMap<>();
        for (final Integer key : map.keySet()) {
            final Integer domain = super.urls2domains.get(key);
            updateMapInt2Freq(domain, domain2Freq);
            sum = sum + map.get(key);
        }
        float entropy = calculateEntropy(domain2Freq, sum);
        return -entropy;
    }

    private float calculateQueryCTR(final QueryId queryId) {
        int freq = this.query2Freq.get(queryId);
        int clicks = 0;
        for (final Integer value : query2Urls.get(queryId).values()) {
            clicks = clicks + value;
        }
        return (float) clicks / freq;
    }

    private void calculateAvg(final Map<QueryId, Integer> query2Reformulation) {
        int numberreformulations = 0;
        for(QueryId queryId: query2Reformulation.keySet()){
            numberreformulations = numberreformulations+ query2Reformulation.get(queryId);
        }
        for(QueryId queryId: query2Reformulation.keySet()){
            query2ProbReform.put(queryId, (float) query2Reformulation.get(queryId)/numberreformulations);
        }
    }

}
