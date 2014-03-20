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
	private verilogTownGridNode way_point;
	private int start_time;
	private boolean success_getting_to_end_point;
	private boolean is_crashed;
	private boolean is_done_path;
	private boolean is_start_path;
	private boolean is_processed;
	private verilogTownGridNode location;
	private int x;
	private int y;
	private verilogTownGridNode next_spot;
	private Stack<verilogTownGridNode> path;
	private Direction direction;
	
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
				float speed,
				Texture texture)
	{
		// how do I assert(start >= 1)
		this.start_point = start;
		this.end_point = end;
		this.current_point = null;
		this.way_point = null;
		this.direction = null;
		this.success_getting_to_end_point = true;
		this.is_crashed = false;
		this.is_done_path = false;
		this.is_start_path = false;
		this.is_processed = false;
		this.start_time = starting_time;

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

	public float set_and_get_rotation_based_on_direction(){
		if (this.direction == Direction.N)
			this.rotation = 90;
		else if (this.direction == Direction.S)
			this.rotation = 270;
		else if (this.direction == Direction.W)
			this.rotation = 180;
		else if (this.direction == Direction.E)
			this.rotation = 0;
		return this.rotation;
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

	public void set_car_direction(verilogTownGridNode from, verilogTownGridNode to)
	{
		GridType grid_type;
		grid_type = to.get_grid_type();

		if (from == null) // starting move
		{
			if (grid_type == GridType.START_NEDGE2S)
			{
				this.direction = Direction.S;
			}
			else if (grid_type == GridType.START_SEDGE2N)
			{
				this.direction = Direction.N;
			}
			else if (grid_type == GridType.START_EEDGE2W)
			{
				this.direction = Direction.W;
			}
			else if (grid_type == GridType.START_WEDGE2E)
			{
				this.direction = Direction.E;
			}
		}
		/* corner conditions */
		else if (grid_type == GridType.CORNER_ROAD_W2S || grid_type == GridType.CORNER_ROAD_E2S)
		{
			this.direction = Direction.S;
		}
		else if (grid_type == GridType.CORNER_ROAD_N2E || grid_type == GridType.CORNER_ROAD_S2E)
		{
			this.direction = Direction.E;
		}
		else if (grid_type == GridType.CORNER_ROAD_N2W || grid_type == GridType.CORNER_ROAD_S2W)
		{
			this.direction = Direction.W;
		}
		else if (grid_type == GridType.CORNER_ROAD_E2N || grid_type == GridType.CORNER_ROAD_W2N)
		{
			this.direction = Direction.N;
		}
		else if (to.get_x() > from.get_x())
		{
			this.direction = Direction.E;
		}
		else if (to.get_x() < from.get_x())
		{
			this.direction = Direction.W;
		}
		else if (to.get_y() > from.get_y())
		{
			this.direction = Direction.N;
		}
		else if (to.get_y() < from.get_y())
		{
			this.direction = Direction.S;
		}
	}

	public void set_current_point_end(verilogTownGridNode from, verilogTownGridNode to) 
	{
		/* update which direction the car will now be going */
		set_car_direction(from, to);
		/* store the car in the grid details */
		from.set_car(null);

		this.current_point = to;
	}

	public void set_current_point(verilogTownGridNode from, verilogTownGridNode to, verilogTownGridNode via_point, verilogTownMap level) 
	{
		/* update which direction the car will now be going */
		set_car_direction(from, to);

		this.current_point = to;
		/* store the car in the grid details */
		if (from != null)
		{
			from.set_car(null);
		}
		to.set_car(this);

		/* update the path - some paths have way_points because of forced turns */
		if (via_point == null && this.way_point == null)
		{
			/* recalculate the path ... this might be computationaly expensive */
			level.path_clean(this.path);
			this.path = level.findPath(null, to, this.end_point, this);
		}
		else 
		{
			Gdx.app.log("Car", "in here!!!!");

			if (this.way_point == to)
			{
				this.way_point = null;

				this.path = level.findPath(null, to, this.end_point, this);
			}
			else if (via_point != null)
			{
				this.way_point = via_point; 

				this.path = level.findPath(null, this.way_point, this.end_point, this);
				this.path = level.findPath(this.path, to, this.way_point, this);
			}
			else
			{
				this.path = level.findPath(null, this.way_point, this.end_point, this);
				this.path = level.findPath(this.path, to, this.way_point, this);
			}
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
	public void set_end_point_and_fail_on_getting_car_accross(verilogTownGridNode point) {
		this.success_getting_to_end_point = false;
		this.end_point = point;
	}

	public verilogTownGridNode get_next_point_on_path() {
		return this.path.pop();
	}
	public void put_next_point_back_on_path(verilogTownGridNode point) {
		this.path.push(point);
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

	public void set_is_start_path() {
		this.is_start_path = true;
	}
	public boolean get_is_start_path() {
		return this.is_start_path;
	}

	public void set_processed(boolean value) {
		this.is_processed = value;
	}
	public boolean get_processed() {
		return this.is_processed;
	}
	public void set_is_done_path() {
		this.is_done_path = true;
		this.current_point = null;
	}
	public boolean get_is_done_path() {
		return this.is_done_path;
	}
	public Direction get_direction() {
		return this.direction;
	}
}
