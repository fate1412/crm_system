package com.fate1412.crmSystem.utils;

import javafx.util.Pair;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PairList<X, Y> extends ArrayList<Pair<X,Y>> {
    private final boolean reiterated;
    
    public PairList() {
        this.reiterated = true;
    }
    
    public PairList(boolean reiterated) {
        this.reiterated = reiterated;
    }
    
    public int addPair(Pair<X, Y> pair) {
        if (!reiterated && this.size() > 0) {
            for (Pair<X,Y> p : this) {
                X x = p.getKey();
                Y y = p.getValue();
                if (pair.getKey().equals(x) || pair.getValue().equals(y)) {
                    throw new RuntimeException("x or y is reiterated");
                }
            }
        }
        this.add(pair);
        return this.size() - 1;
    }
    
    public int addPair(X x, Y y) {
        return addPair(new Pair<>(x, y));
    }
    
    public Pair<X,Y> getPair(int index) {
        return this.get(index);
    }
    
    public Y getY(X x) {
        for (Pair<X,Y> pair : this) {
            if (pair.getKey().equals(x)) {
                return pair.getValue();
            }
        }
        return null;
    }
    
    public X getX(Y y) {
        for (Pair<X,Y> pair : this) {
            if (pair.getValue().equals(y)) {
                return pair.getKey();
            }
        }
        return null;
    }
    
}
