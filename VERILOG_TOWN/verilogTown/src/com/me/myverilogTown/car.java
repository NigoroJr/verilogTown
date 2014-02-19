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
	private int start_time;
	private boolean is_crashed;
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


	public Car(verilogTownGridNode start, verilogTownGridNode end, int starting_time, verilogTownMap level,
				Vector2 position, float width, float height, float rotation,
				float speed)
	{
		this.start_point = start;
		this.end_point = end;
		this.is_crashed = false;
		this.start_time = starting_time;
		this.position = position;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.rotation = rotation;
		velocity = new Vector2(0, 0);


		/* intialize path from start to finish */
		this.path = level.findPath(this.start_point, this.end_point);
		/* debugging to see if path is good */
		level.showPath(this.path);
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
	
	/* Auto generated getters and setters below */
		
	 
	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * @param position the position to set
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
	 * @param width the width to set
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
	 * @param height the height to set
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
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public Vector2 getVelocity(){
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity){
		this.velocity = velocity;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public void setRotation(float rotation){
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

	public void update(Car car){
		bounds.x = position.x;
		bounds.y = position.y;
	}
}

