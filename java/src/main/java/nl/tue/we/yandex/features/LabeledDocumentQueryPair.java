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
public class JudgedPair {
    
    private final int queryId;
    private final int urlId;
    private final int domainId;
    private final int judge;
    
    public  JudgedPair (int queryId, int urlId, int judge, int domainId){
        this.queryId = queryId;
        this.urlId = urlId;
        this.judge = judge;
        this.domainId = domainId;
    }

    public JudgedPair(final JudgedPair judgedPair) {
        this.queryId = judgedPair.queryId;
        this.urlId = judgedPair.urlId;
        this.judge = judgedPair.getJudge();
        this.domainId = judgedPair.domainId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.queryId;
        hash = 67 * hash + this.urlId;
        hash = 67 * hash + this.judge;
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
        final JudgedPair other = (JudgedPair) obj;
        if (this.queryId != other.queryId) {
            return false;
        }
        if (this.urlId != other.urlId) {
            return false;
        }
        if (this.judge != other.judge) {
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

    public int getJudge() {
        return judge;
    }

    public int getDomainId() {
        return domainId;
    }
    
     
}
