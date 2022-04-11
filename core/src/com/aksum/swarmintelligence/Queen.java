package com.aksum.swarmintelligence;

import com.badlogic.gdx.math.MathUtils;

public class Queen extends Object{
    float blueResource,redResource,greenResource;
    float storageRadius,storageWidth,storageHeight;
    float storageInitialRadius;
    float maxStorage;

    Queen() {
        radius = 20;
        width = 2*radius;
        height = 2*radius;

        storageInitialRadius = storageRadius = 15;
        storageWidth = 2*storageRadius;
        storageHeight = 2*storageRadius;

        maxStorage = 200;
        blueResource = 200;
        redResource = 200;
        greenResource = 200;

        v = 0.7F;

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

    public void getResource(String type) {
        if(type == "blue") {
            blueResource += 1;
        }
        else if(type == "green") {
            greenResource += 1;
        }
        else if(type == "red") {
            redResource += 1;
        }

        float val = Math.min(Math.min(blueResource,redResource),greenResource);
        maxStorage = Math.max(maxStorage,val);
        storageRadius = (storageInitialRadius*val)/maxStorage;
        storageWidth = 2*storageRadius;
        storageHeight = 2*storageRadius;
    }

    public boolean spendResource(){
        float k = 0.4F;
        blueResource -= k;
        redResource -= k;
        greenResource -= k;

        int shouldCreate = MathUtils.random(0,100000);
        boolean isShould = (shouldCreate <= 1);

        if(isShould){
            blueResource -= 0.2F;
            redResource -= 0.2F;
            greenResource -= 0.2F;
        }

        float val = Math.min(Math.min(blueResource,redResource),greenResource);

        maxStorage = Math.max(maxStorage,val);
        storageRadius = (storageInitialRadius*val)/maxStorage;
        storageWidth = 2*storageRadius;
        storageHeight = 2*storageRadius;

        if(val <= 0){
            isAlive = false;
            return false;
        }
        return isShould;
    }
}
