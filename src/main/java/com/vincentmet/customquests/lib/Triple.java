package com.vincentmet.customquests.lib;

public class Triple<L, M, R>{
    private L l;
    private M m;
    private R r;

    public Triple(L _l, M _m, R _r){
        l = _l;
        m = _m;
        r = _r;
    }

    public L getLeft() {
        return l;
    }

    public M getMiddle() {
        return m;
    }

    public R getRight() {
        return r;
    }

    @Override
    public String toString() {
        return "L: " + l + " M: " + m + " R: " + r;
    }
}
