/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

import java.util.Map;

/**
 *
 * @author t-jukise
 */
public class FeatureStoreLog {
    
    private Map<Integer, Integer> queryFreq;
    private Map<Integer, Map<Integer, Integer>> query2urls;
    private Map<Integer, Map<Integer, Integer>> query2domains;
    private Map<Integer, Map<Integer, Integer>> query2ClickedPosition;
    private Map<Integer, Map<Integer, Integer>> query2PositionInSession;
    
}
