/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

import java.util.List;
import nl.tue.we.yandex.LogProcessor.QueryId;
import nl.tue.we.yandex.LogProcessor.Term;
import nl.tue.we.yandex.LogProcessor.Query;;

/**
 *
 * @author t-jukise
 */
public class Query2IssuedTime extends Query{
    
    private int timeIssued;

    public Query2IssuedTime(int timeIssued, QueryId queryId, List<Integer> currentTerms, int fake) {
        super(queryId, currentTerms, fake);
        this.timeIssued = timeIssued;
    }

    public Query2IssuedTime(int timeIssued, QueryId queryId, List<Term> terms) {
        super(queryId, terms);
        this.timeIssued = timeIssued;
    }
     
    //for now just next query
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.timeIssued;
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
        final Query2IssuedTime other = (Query2IssuedTime) obj;
        if (this.timeIssued != other.timeIssued) {
            return false;
        }
        return true;
    }

    public int getTimeIssued() {
        return timeIssued;
    }

    public void setTimeIssued(int timeIssued) {
        this.timeIssued = timeIssued;
    }

  
}
