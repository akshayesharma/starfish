package com.daddy.common;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimatedActor extends BaseActor {
    private float elapsedTime;
    private Animation<TextureRegion> activeAnimation;
    private String activeName;
    private HashMap<String, Animation<TextureRegion>> animationStorage;

    public AnimatedActor() {
        super();
        elapsedTime = 0;
        activeAnimation = null;
        activeName = null;
        animationStorage = new HashMap<String, Animation<TextureRegion>>();
    }

    public void storeAnimation(String name, Animation<TextureRegion> animation){
        animationStorage.put(name, animation);
        if(activeName == null){
            setActiveAnimation(name);
        }
    }

    public void storeAnimation(String name, Texture tex){
        TextureRegion region = new TextureRegion();
        TextureRegion[] frames = {region};
        Animation<TextureRegion> anim = new Animation<TextureRegion>(1.0f, frames);
        storeAnimation(name, anim);
     }

    public void setActiveAnimation(String name) {
        if(!animationStorage.containsKey(name) ){
            System.out.println("No animation: "+ name);
            return;
        }else if(activeName.equals(name)) {
            return;
        }else{
            elapsedTime = 0;
            activeName = name;
            activeAnimation = animationStorage.get(name);
            Texture tex = activeAnimation.getKeyFrame(0).getTexture();
            setWidth(tex.getWidth());
            setHeight(tex.getHeight());
        }
    }

    public String getAnimationName(){
        return activeName;
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        elapsedTime += dt;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        region.setRegion(activeAnimation.getKeyFrame(elapsedTime));
        super.draw(batch, parentAlpha);
    }
}
