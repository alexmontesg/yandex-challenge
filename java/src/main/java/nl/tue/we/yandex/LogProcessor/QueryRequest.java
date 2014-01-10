/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.LogProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nl.tue.we.yandex.features.Query;
/**
 *
 * @author t-jukise
 */
public class QueryRequest {
    
    private final Map<Integer,List<Integer>> serpId2ShowedUrls = new HashMap<>();
    private final Query query;


    public QueryRequest(final Query query, final Integer serpId, final List<Integer> urls) {
        this.query = query;
        serpId2ShowedUrls.put(serpId, urls);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QueryRequest other = (QueryRequest) obj;
        if (!Objects.equals(this.serpId2ShowedUrls, other.serpId2ShowedUrls)) {
            return false;
        }
        if (!Objects.equals(this.query, other.query)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.serpId2ShowedUrls);
        hash = 89 * hash + Objects.hashCode(this.query);
        return hash;
    }

    @Override
    public String toString() {
        return "QueryRequest{" + "serpId2ShowedUrls=" + serpId2ShowedUrls + ", query=" + query + '}';
    }

    public Query getQuery() {
        return new Query(query);
    }

    public Map<Integer, List<Integer>> getSerpId2ShowedUrls() {
        return serpId2ShowedUrls;
    }
    
    
   
}
