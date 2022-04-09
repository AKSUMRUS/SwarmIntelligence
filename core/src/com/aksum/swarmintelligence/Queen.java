package com.aksum.swarmintelligence;

import com.badlogic.gdx.math.MathUtils;

public class Queen extends Object{

    Queen() {
        radius = 30;
        width = 2*radius;
        height = 2*radius;

        v = 1;

        x = MathUtils.random(0, Main.SCR_WIDTH - width);
        y = MathUtils.random(0, Main.SCR_HEIGHT - height);
    }

    @Override
    void move() {
        int shouldChange = MathUtils.random(0,100);
        if(shouldChange < 10){
            a = MathUtils.random(a - 30,a + 30);
        }
        vx = v*MathUtils.cosDeg(a);
        vy = v*MathUtils.sinDeg(a);
        super.move();
    }
}
