package com.company.my.galaxy;

public class Time {
    static private long currentTime = System.nanoTime();
    static public float deltaTime;

    //-----------------------------
    // deltaTime 계산
    //-----------------------------
    static public void update() {
        deltaTime = (System.nanoTime() - currentTime) / 1000000000f;
        currentTime = System.nanoTime();
    }

} // Time