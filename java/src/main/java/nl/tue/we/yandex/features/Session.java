/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import nl.tue.we.yandex.LogProcessor.Clicks;
import nl.tue.we.yandex.LogProcessor.QueryRequest;
/**
 *
 * @author t-jukise
 */
public class Session {

    private final Clicks clicks;
    private final List<QueryRequest> queries;

    public Session(final List<QueryRequest> queries, final Clicks clicks) {
        this.clicks = clicks;
        this.queries = queries;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.clicks);
        hash = 43 * hash + Objects.hashCode(this.queries);
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
        final Session other = (Session) obj;
        if (!Objects.equals(this.clicks, other.clicks)) {
            return false;
        }
        if (!Objects.equals(this.queries, other.queries)) {
            return false;
        }
        return true;
    }

    public Clicks getClicks() {
        return new Clicks(clicks);
    }

    public List<QueryRequest> getQueries() {
        return new LinkedList<>(queries);
    }

}
