package com.vincentmet.customquests.lib;

public class IntCounter {
    private int value = 0;
    private int incrementor = 1;

    public IntCounter(){

    }

    public IntCounter(int startingValue){
        this.value = startingValue;
    }

    public IntCounter(int startingValue, int incrementor){
        this.value = startingValue;
        this.incrementor = incrementor;
    }

    public IntCounter count(){
        this.value += this.incrementor;
        return this;
    }

    public int getValue(){
        return value;
    }

    public IntCounter add(int value){
        this.value += value;
        return this;
    }
}
