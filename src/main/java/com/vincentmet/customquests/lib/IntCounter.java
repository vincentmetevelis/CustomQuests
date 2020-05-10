package com.vincentmet.customquests.lib;

public class IntCounter {
    private int value = 0;
    private int incrementer = 1;

    public IntCounter(){

    }

    public IntCounter(int startingValue){
        this.value = startingValue;
    }

    public IntCounter(int startingValue, int incrementer){
        this.value = startingValue;
        this.incrementer = incrementer;
    }

    public IntCounter count(){
        this.value += this.incrementer;
        return this;
    }

    public int getValue(){
        return value;
    }

    public IntCounter add(int value){
        this.value += value;
        return this;
    }

    @Override
    public String toString() {
        return "" + getValue();
    }
}
