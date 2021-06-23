package com.example.cinema.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: Correspondens
 * @description:
 **/
public class Correspondens {
    private List<Integer> index = new LinkedList<Integer>();

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
    }

    public void add(int i) {
        this.index.add(i);
    }
}
