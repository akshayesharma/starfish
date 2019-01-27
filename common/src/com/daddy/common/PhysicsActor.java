package com.daddy.common;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PhysicsActor extends AnimatedActor {
    public static final int MAX_SPEED = 9999;
    private Vector2 velocity;
    private Vector2 acceleration;

    private float maxSpeed;
    private float deceleration;

    private boolean autoAngle;

    public PhysicsActor() {
        velocity = new Vector2();
        acceleration = new Vector2();
        maxSpeed = MAX_SPEED;
        deceleration = 0;
        autoAngle = false;
    }

    public void setVelocityXY(float vx, float vy){
        velocity.set(vx, vy);
    }

    public void addVelocity(float vx, float vy){
        velocity.add(vx, vy);
    }

    public void setVelocityAS(float angleDeg, float speed){
        velocity.x = speed * MathUtils.cosDeg(angleDeg);
        velocity.y = speed * MathUtils.sinDeg(angleDeg);
    }

    public void setAccelerationXY(float ax, float ay){
        acceleration.set(ax, ay);
    }

    public void addAccelerationXY(float ax, float ay){
        acceleration.add(ax, ay);
    }

    public void setAccelerationAS(float angleDeg, float speed){
        acceleration.x = speed * MathUtils.cosDeg(angleDeg);
        acceleration.y = speed * MathUtils.sinDeg(angleDeg);
    }

    public void setDeceleration(float d){
        deceleration = d;
    }

    public float getSpeed(){
        return velocity.len();
    }

    public void setSpeed(float s){
        velocity.setLength(s);
    }

    public void setMaxSpeed(float ms){
        maxSpeed = ms;
    }

    public float getMotionAngle(){
        return MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees;
    }

    public void setAutoAngle(boolean b){
        autoAngle = b;
    }

    public void accelerateForward(float speed){
        setAccelerationAS(getRotation(), speed);
    }

    public void act(float dt){
        super.act(dt);

        // apply acceleration.
        velocity.add(acceleration.x * dt, acceleration.y * dt);

        // decrease velocity when not accelerating.
        if(acceleration.len() < 0.01){
            float decelerateAmount = deceleration * dt;
            if(getSpeed()  < decelerateAmount){
                setSpeed(0);
            }else{
                setSpeed(getSpeed() - decelerateAmount);
            }
        }

        if(getSpeed() > maxSpeed){
            setSpeed(maxSpeed);
        }

        moveBy(velocity.x * dt, velocity.y * dt);

        if(autoAngle && getSpeed() > 0.1){
            setRotation(getMotionAngle());
        }
    }

}
