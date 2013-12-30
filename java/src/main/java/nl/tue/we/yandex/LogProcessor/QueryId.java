/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.LogProcessor;

/**
 *
 * @author t-jukise
 */
public class QueryId {

    private final int queryId;

    public QueryId(int queryId) {
        this.queryId = queryId;
    }

    public QueryId(final QueryId queryId) {
        this.queryId = queryId.getQueryId();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.queryId;
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
        final QueryId other = (QueryId) obj;
        if (this.queryId != other.queryId) {
            return false;
        }
        return true;
    }

    public int getQueryId() {
        return queryId;
    }

    @Override
    public String toString() {
        return "QueryId{" + "queryId=" + queryId + '}';
    }

}
