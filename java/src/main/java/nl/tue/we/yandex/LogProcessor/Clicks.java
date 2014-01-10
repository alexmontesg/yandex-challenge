/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.LogProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author t-jukise
 */
public class QueryClicks {

    private final Map<Integer, List<Integer>> serpId2ClickedUrl = new HashMap<>();

    public QueryClicks() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.serpId2ClickedUrl);
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
        final QueryClicks other = (QueryClicks) obj;
        if (!Objects.equals(this.serpId2ClickedUrl, other.serpId2ClickedUrl)) {
            return false;
        }
        return true;
    }

    public Map<Integer, List<Integer>> getSerpId2ClickedUrl() {
        return new HashMap<>(serpId2ClickedUrl);
    }
}
