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
public class ClickQueryUrl {
    private final int queryId;
    private final int urlId;

    public ClickQueryUrl(int queryId, int urlId) {
        this.queryId = queryId;
        this.urlId = urlId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.queryId;
        hash = 37 * hash + this.urlId;
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
        final ClickQueryUrl other = (ClickQueryUrl) obj;
        if (this.queryId != other.queryId) {
            return false;
        }
        if (this.urlId != other.urlId) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
