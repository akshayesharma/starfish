package com.daddy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.daddy.common.BaseActor;
import com.daddy.common.BaseScreen;
import com.daddy.common.PhysicsActor;

import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.input;

public class TurtleLevel extends BaseScreen {
    private BaseActor ocean;
    private ArrayList<BaseActor> rockList;
    private ArrayList<BaseActor> startFishList;
    private PhysicsActor turtle;


    public TurtleLevel(Game game) {
        super(game);
    }

    @Override
    public void create() {
        ocean = new BaseActor();
        ocean.setTexture(new Texture(Gdx.files.internal("water.jpg")));
        ocean.setPosition(0, 0);
        mainStage.addActor(ocean);

        BaseActor overlay = ocean.clone();
        overlay.setPosition(-50, -50);
        overlay.setColor(1, 1, 1, 0.25f);
        uiStage.addActor(overlay);

        BaseActor rock = new BaseActor();
        rock.setTexture(new Texture(Gdx.files.internal("rock.png")));
        rock.setEllipseBoundary();

        rockList = new ArrayList<BaseActor>();
        int[] rockCoords = {200, 0, 200, 100, 250, 200, 360, 200, 470, 200};
        for (int i = 0; i < 5; ++i) {
            BaseActor r = rock.clone();
            r.setPosition(rockCoords[2 * i], rockCoords[2 * i + 1]);
            mainStage.addActor(r);
            rockList.add(r);
        }

        BaseActor starfish = new BaseActor();
        starfish.setTexture(new Texture(Gdx.files.internal("starfish.png")));
        starfish.setEllipseBoundary();
        startFishList = new ArrayList<BaseActor>();
        int[] starfistcoords = {400, 100, 100, 400, 650, 400};
        for (int i = 0; i < 3; ++i) {
            BaseActor s = starfish.clone();
            s.setPosition(starfistcoords[2 * i], starfistcoords[2 * i + 1]);
            mainStage.addActor(s);
            startFishList.add(s);
        }

        turtle = new PhysicsActor();
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 1; i <= 6; ++i) {
            String filename = "turtle-" + i + ".png";
            Texture tex = new Texture(Gdx.files.internal(filename));
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames[i - 1] = new TextureRegion(tex);
        }
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        Animation<TextureRegion> anim = new Animation<TextureRegion>(0.1f, framesArray, Animation.PlayMode.LOOP);
        turtle.storeAnimation("swim", anim);
        Texture frame1 = new Texture(Gdx.files.internal("turtle-1.png"));
        turtle.storeAnimation("rest", frame1);
        turtle.setOrigin(turtle.getWidth()/2, turtle.getHeight()/2);
        turtle.setPosition(20, 20);
        turtle.setRotation(90);
        turtle.setEllipseBoundary();
        turtle.setMaxSpeed(100);
        turtle.setDeceleration(200);
        mainStage.addActor(turtle);
    }

    @Override
    public void update(float dt) {

            turtle.setAccelerationXY(0, 0);
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                turtle.rotateBy(90 * dt);
            }
            if( input.isKeyPressed(Input.Keys.RIGHT)){
                turtle.rotateBy(-90* dt);
            }
            if(input.isKeyPressed(Input.Keys.UP)){
                turtle.accelerateForward(100);
            }

            // set animation.
            if(turtle.getSpeed() > 1 && turtle.getAnimationName().equals("rest")){
                turtle.setActiveAnimation("swim");
            }
            if(turtle.getSpeed() < 1 && turtle.getAnimationName().equals("swim")){
                turtle.setActiveAnimation("rest");
            }

            turtle.setX(MathUtils.clamp(turtle.getX(), 0, viewWidth - turtle.getWidth()));
            turtle.setY(MathUtils.clamp(turtle.getY(), 0, viewHeight - turtle.getHeight()));
            for(BaseActor r : rockList){
                turtle.overlaps(r, true);
            }

            ArrayList<BaseActor> removeList = new ArrayList<BaseActor>();
            for(BaseActor s : startFishList){
                if(turtle.overlaps(s, false)){
                    removeList.add(s);
                }
            }

            for(BaseActor b : removeList){
                b.remove();
                startFishList.remove(b);
            }
    }
}
