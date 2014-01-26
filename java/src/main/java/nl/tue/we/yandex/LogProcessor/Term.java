/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.LogProcessor;

import java.util.Objects;

/**
 *
 * @author Julia
 */
public class Term {
    private final Integer termId;

        public Term(final Integer termId) {
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
