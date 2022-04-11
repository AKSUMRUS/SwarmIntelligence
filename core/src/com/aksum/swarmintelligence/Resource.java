package com.aksum.swarmintelligence;

import com.badlogic.gdx.math.MathUtils;

public class Resource extends Object{
    float amount,initialAmount;
    float initialRadius;

    Resource() {
        initialRadius = radius = 20;
        width = 2*radius;
        height = 2*radius;

        initialAmount = amount = 1000;

        v = 1;

        x = MathUtils.random(0, Main.SCR_WIDTH - width);
        y = MathUtils.random(0, Main.SCR_HEIGHT - height);
    }

    @Override
    void move() {
        int shouldChange = MathUtils.random(0,100);
        if(shouldChange < 10){
            a = MathUtils.random(a - 10,a + 10);
        }
        vx = v*MathUtils.cosDeg(a);
        vy = v*MathUtils.sinDeg(a);
        super.move();
    }

    void giveResource(){
        if(!isAlive) return;

        amount -= 1;

        if(amount <= 0){
            isAlive = false;
        }
        else{
            radius = (initialRadius * Math.min(initialAmount,amount + 500))/initialAmount;
            width = 2*radius;
            height = 2*radius;
        }
    }

}
