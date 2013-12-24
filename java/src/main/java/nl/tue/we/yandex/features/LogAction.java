/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author t-jukise
 */
public class LogAction {
    
    private final JudgedPair label;
    private final List<Feature> features;

    public LogAction(final JudgedPair label, final List<Feature> features) {
        this.label = label;
        this.features = features;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.label);
        hash = 17 * hash + Objects.hashCode(this.features);
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
        final LogAction other = (LogAction) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        if (!Objects.equals(this.features, other.features)) {
            return false;
        }
        return true;
    }

    public JudgedPair getLabel() {
        return new JudgedPair(label);
    }

    public List<Feature> getFeatures() {
        return new ArrayList<>(features);
    }
    
    
    
    
    
}
