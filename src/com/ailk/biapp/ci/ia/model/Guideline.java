package com.ailk.biapp.ci.ia.model;

public class Guideline {
    private String name;
    private int valueAxisIndex = 0;

    public Guideline(String name) {
        this.name = name;
    }

    public Guideline(String name, int valueAxisIndex) {
        this.name = name;
        this.valueAxisIndex = valueAxisIndex;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValueAxisIndex() {
        return this.valueAxisIndex;
    }

    public void setValueAxisIndex(int valueAxisIndex) {
        this.valueAxisIndex = valueAxisIndex;
    }
}
