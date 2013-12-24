/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author t-jukise
 */
public class Query {
    private int queryId;
    private List<Integer> terms;
    
    public Query(int queryId, List<Integer> terms){
        this.queryId = queryId;
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "Query{" + "queryId=" + queryId + ", terms=" + terms + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.queryId;
        hash = 43 * hash + Objects.hashCode(this.terms);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Query other = (Query) obj;
        if (this.queryId != other.queryId) {
            return false;
        }
        if (!Objects.equals(this.terms, other.terms)) {
            return false;
        }
        return true;
    }
    
    
}
