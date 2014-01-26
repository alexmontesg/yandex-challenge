/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.LogProcessor;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import nl.tue.we.yandex.LogProcessor.QueryId;
import nl.tue.we.yandex.LogProcessor.Term;
/**
 *
 * @author Julia
 */
public class Query {
    
    private static final float SIM_THRESHOLD = (float) 0.35;
   
    private final QueryId queryId;
    private final List<Term> terms;
    
    public Query(final Query query){
        this.queryId = query.getQueryId();
        this.terms = query.getTerms();
    }
    
    public Query(final QueryId queryId, final List<Term> terms) {
        this.queryId = queryId;
        this.terms = terms;
    }

    public Query(final QueryId queryId, final List<Integer> currentTerms, int fake) {
        this.queryId = queryId;
        this.terms = new LinkedList<>();
        for (Integer termInt: currentTerms){
           Term term = new Term(termInt);
           this.terms.add(term);
        }
    }
    public boolean isReformulation (final Query q2){
     List<Term> terms2 = q2.getTerms();
     List<Term> terms1 = this.getTerms();
     return isQueriesSimilar(terms1, terms2);
    }

    public static boolean isQueriesSimilar(final List<Term> terms1, final List<Term> terms2) {
     int inCommon = 0;
     boolean reformulation = false;
        int denominator;
        if (terms1.size() > terms2.size()){
            denominator = terms1.size();
            for (Term term1: terms1) {
                for(Term term2 : terms2){
                    if(term1 == term2){
                        inCommon++;
                    }
                }
            }
        }else{
            denominator = terms2.size();
             for (Term term2 : terms2){
                for(Term term1: terms1){
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
    public String toString() {
        return "Query{" + "queryId=" + queryId + ", terms=" + terms + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.queryId);
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

    public QueryId getQueryId() {
        return new QueryId(queryId);
    }

    public List<Term> getTerms() {
        return new LinkedList<>(terms);
    }
}
