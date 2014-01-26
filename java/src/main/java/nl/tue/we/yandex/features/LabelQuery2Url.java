/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import nl.tue.we.yandex.LogProcessor.QueryId;

/**
 *
 * @author Julia
 */
public class LabelQuery2Url {

    private Map<QueryId, Map<Integer, Integer>> query2urls2labels;

    public LabelQuery2Url(int label, QueryId queryId, int urlId) {
        Map<Integer, Integer> urls2lables = query2urls2labels.get(queryId);
        if (urls2lables == null) {
            urls2lables = new HashMap<>();
        }
        urls2lables.put(urlId, label);
        query2urls2labels.put(queryId, urls2lables);
    }

    public LabelQuery2Url(final LabelQuery2Url label) {
        query2urls2labels = label.getQuery2urls2labels();
    }

    public Map<QueryId, Map<Integer, Integer>> getQuery2urls2labels() {
        return query2urls2labels;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LabelQuery2Url other = (LabelQuery2Url) obj;
        if (!Objects.equals(this.query2urls2labels, other.query2urls2labels)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.query2urls2labels);
        return hash;
    }

    @Override
    public String toString() {
        return "Label{" + "query2urls2labels=" + query2urls2labels + '}';
    }
}
