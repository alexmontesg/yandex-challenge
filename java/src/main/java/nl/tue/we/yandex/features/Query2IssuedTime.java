/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

import java.util.List;

/**
 *
 * @author t-jukise
 */
public class Query2IssuedTime extends Query{
    
    private static final float SIM_THRESHOLD = (float) 0.35;
    private int timeIssued;
    
    public Query2IssuedTime(int queryId, List<Integer> terms) {
        super(queryId, terms);
    }
    public Query2IssuedTime(int queryId, List<Integer> terms, int timeIssued) {
        super(queryId, terms);
        this.timeIssued = timeIssued;
    }
     
    //for now just next query
    public boolean isReformulation (final Query q2){
     boolean reformulation = false;
     int denominator;
     int inCommon = 0;
     List<Integer> terms2 = q2.getTerms();
     if (super.getTerms().size() > terms2.size()){
         denominator = super.getTerms().size();
         for (Integer term1: super.getTerms()){
             for(Integer term2 : terms2){
                 if(term1 == term2){
                     inCommon++;
                 }
             }
         }
     }else{
         denominator = terms2.size();
          for (Integer term2 : terms2){
             for(Integer term1: super.getTerms()){
                 if(term1 == term2){
                     inCommon++;
                 }
             }
         }
     }
     if((float)inCommon/denominator >= SIM_THRESHOLD){
         reformulation = true;
     }
     return reformulation;
    }

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
