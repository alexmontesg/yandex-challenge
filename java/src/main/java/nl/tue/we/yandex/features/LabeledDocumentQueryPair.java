/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

/**
 *
 * @author t-jukise
 */
public class LabeledDocumentQueryPair {
    
    private final int queryId;
    private final int urlId;
    private final int domainId;
    private final int label;
    
    public  LabeledDocumentQueryPair (int queryId, int urlId, int label, int domainId){
        this.queryId = queryId;
        this.urlId = urlId;
        this.label = label;
        this.domainId = domainId;
    }

    public LabeledDocumentQueryPair(final LabeledDocumentQueryPair labeledDocumentQueryPair) {
        this.queryId = labeledDocumentQueryPair.queryId;
        this.urlId = labeledDocumentQueryPair.urlId;
        this.label = labeledDocumentQueryPair.getLabel();
        this.domainId = labeledDocumentQueryPair.domainId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.queryId;
        hash = 67 * hash + this.urlId;
        hash = 67 * hash + this.label;
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
        final LabeledDocumentQueryPair other = (LabeledDocumentQueryPair) obj;
        if (this.queryId != other.queryId) {
            return false;
        }
        if (this.urlId != other.urlId) {
            return false;
        }
        if (this.label != other.label) {
            return false;
        }
        return true;
    }

    public int getQueryId() {
        return queryId;
    }

    public int getUrlId() {
        return urlId;
    }

    public int getLabel() {
        return label;
    }

    public int getDomainId() {
        return domainId;
    }
    
     
}
