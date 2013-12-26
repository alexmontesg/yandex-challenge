/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.LogProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author t-jukise
 */
public class Query {
    
    private final QueryId queryId;
    private final List<Term> terms;

    public Query(QueryId queryId, List<Term> terms) {
        this.queryId = queryId;
        this.terms = terms;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        if (!Objects.equals(this.queryId, other.queryId)) {
            return false;
        }
        if (!Objects.equals(this.terms, other.terms)) {
            return false;
        }
        return true;
    }

    public QueryId getQueryId() {
        return queryId;
    }

    public List<Term> getTerms() {
        return new ArrayList<Term>(terms);
    }
    
    public class Term{
        private final Integer termId;

        public Term(Integer termId) {
            this.termId = termId;
        }

        public Integer getTermId() {
            return termId;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.termId);
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
            final Term other = (Term) obj;
            if (!Objects.equals(this.termId, other.termId)) {
                return false;
            }
            return true;
        }
        
    }
    
}
