package com.aksum.swarmintelligence;

import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

public class Boid extends Object{
    int distanceA,distanceB;
    boolean isNeedA;
    boolean isNeedB;
    float innerRadius, innerWidth,innerHeight; // сам боид

    Boid() {
        innerRadius = 4;
        innerWidth = 2*innerRadius;
        innerHeight = 2*innerRadius;

        radius = 30;
        width = 2*radius;
        height = 2*radius;

        v = MathUtils.random(2,5);

        distanceA = 0;
        distanceB = 0;
        isNeedA = true;
        isNeedB = true;

        x = MathUtils.random(0, Main.SCR_WIDTH - innerWidth);
        y = MathUtils.random(0, Main.SCR_HEIGHT - innerHeight);
    }

    @Override
    void move() {
        int shouldChange = MathUtils.random(0,100);
        if(shouldChange < 10){
            a = MathUtils.random(a - 20,a + 20);
        }
        vx = v*MathUtils.cosDeg(a);
        vy = v*MathUtils.sinDeg(a);
        super.move();

        distanceB += 1;
        distanceA += 1;
    }

    boolean overlapsZone(Object o){
        float len1 = (x-o.x)*(x-o.x) + (y-o.y)*(y-o.y);
        float len2 = (radius + o.radius)*(radius + o.radius);
        return (len2 >= len1);
    }

    @Override
    boolean overlaps(Object o){
        float len1 = (x-o.x)*(x-o.x) + (y-o.y)*(y-o.y);
        float len2 = (innerRadius + o.radius)*(innerRadius + o.radius);
        return (len2 >= len1 && (distanceA > o.radius && distanceB > o.radius));
    }
}
