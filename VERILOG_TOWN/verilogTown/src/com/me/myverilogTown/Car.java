package com.me.myverilogTown;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Car
{
    private verilogTownGridNode start_point;
    private verilogTownGridNode end_point;
    private verilogTownGridNode current_point;
    private int start_time;
    private boolean is_crashed;
    private boolean is_done_path;
    private verilogTownGridNode location;
    private int x;
    private int y;
    private verilogTownGridNode next_spot;
    private Stack<verilogTownGridNode> path;

    /* Necessary for displaying car */
    private Texture carTexture;
    private Sprite carSprite;

    /* Animations for crashed states */
    private Animation crashedRight;
    private Animation crashedLeft;
    private Animation crashedUp;
    private Animation crashedDown;

    /* Necessary variables for proper rendering of car sprites */
    protected Vector2 position;
    protected float width;
    protected float height;
    protected float xScale;
    protected float yScale;
    protected Rectangle bounds;
    protected Vector2 velocity;
    protected float speed;
    protected float rotation;

    public Car(verilogTownGridNode start,
            verilogTownGridNode end,
            int starting_time,
            verilogTownMap level,
            Vector2 position,
            float width,
            float height,
            float rotation,
            float speed)
    {
        // how do I assert(start >= 1)
        this.start_point = start;
        this.end_point = end;
        this.current_point = null;
        this.is_crashed = false;
        this.is_done_path = false;
        this.start_time = starting_time;
        this.position = position;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.rotation = rotation;
        velocity = new Vector2(0, 0);

        /* intialize path from start to finish */
        // not needed if done at set_current_point this.path =
        // level.findPath(this.start_point, this.end_point);
    }

    /* For graphics rendering, could be merged with the constructor above */
    public Car(Vector2 position, float width, float height, float rotation,
            float speed, Texture texture)
    {
        this.position = position;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.rotation = rotation;
        velocity = new Vector2(0, 0);
        this.xScale = 1;
        this.yScale = 1;
        this.carTexture = texture;
        carSprite = new Sprite(texture);
        carSprite.setPosition(position.x, position.y);
        carSprite.setSize(width, height);
    }

    /**
     * @return the position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Sprite getCarSprite() {
        return carSprite;
    }

    public void setCarSprite(Sprite carSprite) {
        this.carSprite = carSprite;
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * @return the bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * @param bounds
     *            the bounds to set
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getxScale() {
        return xScale;
    }

    public void setxScale(float xScale) {
        this.xScale = xScale;
    }

    public float getyScale() {
        return yScale;
    }

    public void setyScale(float yScale) {
        this.yScale = yScale;
    }

    public void update(Car car) {
        bounds.x = position.x;
        bounds.y = position.y;
    }

    public void set_current_point(verilogTownGridNode grid, verilogTownMap level) {
        this.current_point = grid;
        /* recalculate the path ... this might be computationaly expensive */
        this.path = level.findPath(grid, this.end_point);
        if (this.path == null)
        {
            /* Couldn't find a path */
            Gdx.app.log("Car", "Couldn't find a path");
        }
    }

    public verilogTownGridNode get_current_point() {
        return this.current_point;
    }

    public verilogTownGridNode get_start_point() {
        return this.start_point;
    }

    public verilogTownGridNode get_end_point() {
        return this.end_point;
    }

    public verilogTownGridNode get_next_point_on_path() {
        return this.path.pop();
    }

    public int get_start_time() {
        return this.start_time;
    }

    public void set_is_crashed() {
        this.is_crashed = true;
    }

    public boolean get_is_crashed() {
        this.current_point = null;
        return this.is_crashed;
    }

    public void set_is_done_path() {
        this.is_done_path = true;
        this.current_point = null;
    }

    public boolean get_is_done_path() {
        return this.is_done_path;
    }

}
