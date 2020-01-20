package com.vincentmet.customquests.lib;

import javax.annotation.Nonnull;

@Nonnull
public class Vec2i{
    private int x = 0;
    private int y = 0;

    public Vec2i(){

    }

    public Vec2i(int xy){
        this.x = xy;
        this.y = xy;
    }

    public Vec2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vec2i add(int x, int y){
        this.x+=x;
        this.y+=y;
        return this;
    }

    public Vec2i add(int xy){
        this.x+=xy;
        this.y+=xy;
        return this;
    }

    public Vec2i sub(int x, int y){
        this.x-=x;
        this.y-=y;
        return this;
    }

    public Vec2i sub(int xy){
        this.x-=xy;
        this.y-=xy;
        return this;
    }

    public int getSum(){
        return x+y;
    }

    public int getMulptiplication(){
        return x*y;
    }

    public int getXDivByY(){
        return y!=0?x/y:0;
    }

    public int getYDivByX(){
        return x!=0?y/x:0;
    }

    public int getXSubByY(){
        return x-y;
    }

    public int getYSubByX(){
        return y-x;
    }

    public int getXModY(){
        return x % y;
    }

    public int getYModX(){
        return y % x;
    }
}
