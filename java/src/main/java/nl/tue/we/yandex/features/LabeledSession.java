/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.we.yandex.features;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Julia
 */
public class LabeledSession {
    
    private final LabelQuery2Url label;
    private final List<Feature> features;

    public LabeledSession(LabelQuery2Url label, List<Feature> features) {
        this.label = label;
        this.features = features;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LabeledSession other = (LabeledSession) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        if (!Objects.equals(this.features, other.features)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.label);
        hash = 59 * hash + Objects.hashCode(this.features);
        return hash;
    }

    public List<Feature> getFeatures() {
        return new LinkedList<>(features);
    }

    public LabelQuery2Url getLabel() {
        return new LabelQuery2Url(label);
    }

    
}
