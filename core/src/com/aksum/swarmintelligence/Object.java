package com.aksum.swarmintelligence;

import com.badlogic.gdx.math.MathUtils;

public class Object {
    float x,y;
    float radius,width,height;
    boolean isAlive = true;
    float vx,vy,v;
    float a; // angle of movement

    Object() {
        a = MathUtils.random(0,359);
    }

    void move() {
        x += vx;
        y += vy;
        if(x <= 0 || x > Main.SCR_WIDTH - width/2) a = 180 - a;
        if(y <= 0 || y > Main.SCR_HEIGHT - height/2) a = -a;
    }

    boolean overlaps(Object o){
        float len1 = (x-o.x)*(x-o.x) + (y-o.y)*(y-o.y);
        float len2 = (radius + o.radius)*(radius + o.radius);
        return (len2 >= len1);
    }

}
