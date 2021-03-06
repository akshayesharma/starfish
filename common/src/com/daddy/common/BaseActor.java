package com.daddy.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseActor extends Actor {
    public TextureRegion region;
    public Polygon boundingPolygon;

    public BaseActor() {
        super();
        region = new TextureRegion();
        boundingPolygon = null;
    }

    public void setTexture(Texture t) {
        setWidth(t.getWidth());
        setHeight(t.getHeight());
        region.setRegion(t);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (isVisible()) {
            batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

    public void setRectangleBoundary(){
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0,0, w,0, w,h, 0,h};
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }

    /**
     * this is based on how a circle's co-ordinates are calculated.
     * if circle is having the origin at x, y then at any point P with angle theta the point is
     * x + r*cos(theta), y + r*sin(theta)
     * now for eclipse we have changed r to w/2 and h/2 respectively and if h and w are same then it will draw a circle.
     */
    public void setEllipseBoundary(){
        int n = 8; // number of vertices.
        float w = getWidth();
        float h = getHeight();
        float[] vertices = new float[2*n];
        for(int i=0; i< n; ++i){
            float t = i*6.28f/n;
            // x cordinate
            vertices[2*i] = w/2* MathUtils.cos(t) + w/2;
            // y cordinate
            vertices[2*i + 1] = h/2 * MathUtils.sin(t) + h/2;
        }

        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }

    public Polygon getBoundingPolygon(){
        boundingPolygon.setPosition(getX(), getY());
        boundingPolygon.setRotation(getRotation());
        return boundingPolygon;
    }

    public boolean overlaps(BaseActor other, boolean resolve){
        Polygon poly1 = this.getBoundingPolygon();
        Polygon poly2 = other.getBoundingPolygon();
        if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())){
            return false;
        }else{
            Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
            boolean polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
            if(polyOverlap && resolve){
                this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
            }
            float significant = 0.5f;
            return (polyOverlap && (mtv.depth > significant));
        }
    }

    public void copy(BaseActor original){
        this.region = new TextureRegion(original.region);
        if(original.boundingPolygon != null){
            this.boundingPolygon = new Polygon(original.boundingPolygon.getVertices());
            this.boundingPolygon.setOrigin(original.boundingPolygon.getOriginX(), original.boundingPolygon.getOriginY());
        }
        this.setPosition(original.getX(), original.getY());
        this.setOriginX(original.getOriginX());
        this.setOriginY(original.getOriginY());
        this.setWidth(original.getWidth());
        this.setHeight(original.getHeight());
        this.setColor(original.getColor());
        this.setVisible(original.isVisible());
    }

    public BaseActor clone(){
        BaseActor newbie = new BaseActor();
        newbie.copy(this);
        return newbie;
    }
}
