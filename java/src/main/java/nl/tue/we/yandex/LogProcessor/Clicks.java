/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.LogProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author t-jukise
 */
public class Clicks {

    private Map<Integer, List<Integer>> serpId2ClickedUrl = new HashMap<>();

    public Clicks() {
    }

    public Clicks(Clicks clicks) {
        serpId2ClickedUrl = clicks.getSerpId2ClickedUrl();
    }
    
    public void updateClicks (int serpId, int clickedUrlId){
       List<Integer> clickedUrls = serpId2ClickedUrl.get(serpId);
        if(clickedUrls == null){
            clickedUrls = new ArrayList<>();
        }
        clickedUrls.add(clickedUrlId);
        serpId2ClickedUrl.put(serpId, clickedUrls);
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
        final Clicks other = (Clicks) obj;
        if (!Objects.equals(this.serpId2ClickedUrl, other.serpId2ClickedUrl)) {
            return false;
        }
        return true;
    }

    public Map<Integer, List<Integer>> getSerpId2ClickedUrl() {
        return new HashMap<>(serpId2ClickedUrl);
    }
}
