/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nl.tue.we.yandex.LogProcessor.*;

/**
 *
 * @author t-jukise
 */
public class FeatureProcessorSession extends YandexLogProcessor {

    private Integer currentSessionId = null;
    private Integer lastSessionId = null;
    private static final int LABEL_BAD = 0;
    private static final int LABEL_POOR = 1;
    private static final int LABEL_GOOD = 2;
    private static final int LABEL_EXCELENT = 3;
    private final Map<QueryId, List<Feature>> storedFeatures;

    public FeatureProcessorSession(String inputFile, String outputFile) throws IOException {
        FeatureStoreLog storeFeatures = new FeatureStoreLog();
        storeFeatures.convertInputToOutput(inputFile, outputFile);
        this.storedFeatures = storeFeatures.returnQueryFeature();
    }

    public LabelQuery2Url assightBadLabel(final Map<Integer, List<Integer>> impressions, LabelQuery2Url label, final QueryRequest query) {
        for (List<Integer> urlIds : impressions.values()) {
            for (Integer urlId : urlIds) {
                label = new LabelQuery2Url(LABEL_BAD, query.getQuery().getQueryId(), urlId);
            }
        }
        return label;
    }

    @Override
    protected void parseLine(String line, String lastLine) throws IOException {
        final String[] fields = line.split("\t");
        final Map<Integer, List<Integer>> serpIs2showedUlrs = new HashMap<>();
        currentSessionId = Integer.parseInt(fields[0]);
        List<QueryRequest> queries = new LinkedList<>();
        Clicks clicks = new Clicks();
        if (currentSessionId != lastSessionId) {
            final Session session = new Session(queries, clicks);
            LabelQuery2Url labels = labelSession(session);
            updateRankLibFile (labels);
            queries = new LinkedList<>();
            clicks = new Clicks();
            lastSessionId = currentSessionId;
        }
        if (isQuery(fields)) {
            final Integer serpId = Integer.parseInt(fields[3]);
            final QueryId queryId = new QueryId(Integer.parseInt(fields[4]));
            final List<Term> terms = readQueryTerms(fields);
            final Query query = new Query(queryId, terms);
            final List<Integer> urls = readSerp(fields);
            serpIs2showedUlrs.put(serpId, urls);
            QueryRequest queryRequest = new QueryRequest(query, serpId, urls);
            queries.add(queryRequest);
        } else if (isClick(fields)) {
            int serpId = Integer.parseInt(fields[3]);
            int urlId = Integer.parseInt(fields[4]);
            clicks.updateClicks(serpId, urlId);
        }
    }

    private LabelQuery2Url labelSession(final Session session) {
        LabelQuery2Url label = null;
        final Map<Integer, List<Integer>> clicks = session.getClicks().getSerpId2ClickedUrl();
        final List<QueryRequest> queries = session.getQueries();
        for (final QueryRequest query : queries) {
            final Map<Integer, List<Integer>> impressions = query.getSerpId2ShowedUrls();
            if (clicks == null) {
                label = assightBadLabel(impressions, label, query);
            } else {
                for (Integer serpIdShowed : impressions.keySet()) {
                    final List<Integer> clickedUrls = clicks.get(serpIdShowed);
                    if (clickedUrls == null) {
                        label = assightBadLabel(impressions, label, query);
                    } else if (clickedUrls.size() == 1) {
                        label = new LabelQuery2Url(LABEL_GOOD, query.getQuery().getQueryId(), clickedUrls.get(0));
                    } else {
                        int size = clickedUrls.size();
                        for (int i = 0; i < size; i++) {
                            if (i == size - 1) {
                                label = new LabelQuery2Url(LABEL_GOOD, query.getQuery().getQueryId(), clickedUrls.get(i));
                            } else {
                                label = new LabelQuery2Url(LABEL_POOR, query.getQuery().getQueryId(), clickedUrls.get(i));
                            }
                        }
                    }

                }
            }
        }
        return label;
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

    private void updateRankLibFile(final LabelQuery2Url labels) {
      Map<QueryId, Map<Integer, Integer>> map = labels.getQuery2urls2labels();
      for(QueryId queryId: map.keySet()){
      List<Feature> feature = storedFeatures.get(queryId);
      Map<Integer, Integer> url2label = map.get(queryId);
      for(Integer url: url2label.keySet()){
          Integer label = url2label.get(url);
      StringBuilder strb = new StringBuilder();
		strb.append(label);
		strb.append(" qid:");
		strb.append(queryId);
		for (Feature feature1 : feature) {
			strb.append(" ");
			strb.append(feature1.getFeatureId());
			strb.append(":");
			strb.append(feature1.getValue());
		}
		strb.append("\n");
        }
      }
    }
}
