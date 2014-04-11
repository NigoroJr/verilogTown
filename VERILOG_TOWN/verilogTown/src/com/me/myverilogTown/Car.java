package com.me.myverilogtown;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private Sprite crashSprite;
	
	/* Necessary variables for proper rendering of car sprites */
	protected float position_x;
	protected float position_y;
	protected float width;
	protected float height;
	protected float xScale;
	protected float yScale;
	protected float speed;
	protected float rotation;
	protected CarAnimateStates animate_state;
	private boolean at_signal;
	private Direction animation_direction;

	public Car(verilogTownGridNode start, 
				verilogTownGridNode end, 
				int starting_time, 
				verilogTownMap level,
				int position_x, 
				int position_y, 
				float width, 
				float height, 
				float rotation,
				float speed,
				Texture texture,
				Random rand)
	{
		// how do I assert(start >= 1)
		this.start_point = start;
		this.end_point = end;
		this.current_point = null;
		this.way_point = null;
		this.direction = null;
		this.animation_direction = null;
		this.success_getting_to_end_point = true;
		this.is_crashed = false;
		this.is_done_path = false;
		this.is_start_path = false;
		this.is_processed = false;
		this.start_time = starting_time;

		this.position_x = position_x;
		this.position_y = position_y;
		this.width = width;
		this.height = height;
		this.speed = speed; // at 25fps, 8pixels/frame = ~ 20mph
		this.rotation = rotation;
		this.xScale = 1;
		this.yScale = 1;
		this.carTexture = texture;
		this.animate_state = CarAnimateStates.STOPPED;
		this.at_signal = false;

		TextureRegion[] carFrames; 
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/2, texture.getHeight()/2);
		carFrames = new TextureRegion[2 * 2];
		int index = 0;
		for (int i = 0; i < 2; i++)
	       	{
			for (int j = 0; j < 2; j++) 
			{
				carFrames[index++] = tmp[i][j];
			}
		}

		carSprite = new Sprite(carFrames[rand.nextInt(3)]);
		carSprite.setPosition(this.position_x, this.position_y);
		carSprite.setSize(width, height);

		crashSprite = new Sprite(carFrames[3]); // crash sprite is last
		crashSprite.setSize(width, height);
	}
	
	public float getPosition_x() 
	{
		return position_x;
	}
	public float getPosition_y() 
	{
		return position_y;
	}
	
	public Sprite getCarSprite() 
	{
		return carSprite;
	}

	public void setCarSprite(Sprite carSprite) 
	{
		this.carSprite = carSprite;
	}

	/**
	 * @return the width
	 */
	public float getWidth() 
	{
		return width;
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) 
	{
		this.width = width;
	}
	
	/**
	 * @return the height
	 */
	public float getHeight() 
	{
		return height;
	}
	
	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) 
	{
		this.height = height;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}

	public float set_and_get_rotation_based_on_direction()
	{
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
	
	public float getxScale() 
	{
		return xScale;
	}

	public void setxScale(float xScale) 
	{
		this.xScale = xScale;
	}

	public float getyScale() 
	{
		return yScale;
	}

	public void setyScale(float yScale) 
	{
		this.yScale = yScale;
	}


	public void animation_direction(verilogTownGridNode from, verilogTownGridNode to)
	{
		GridType grid_type_to;
		GridType grid_type_from;
		grid_type_to = to.get_grid_type();

		if (from == null) // starting move
		{
			if (grid_type_to == GridType.START_NEDGE2S)
			{
				this.animation_direction = Direction.S;
			}
			else if (grid_type_to == GridType.START_SEDGE2N)
			{
				this.animation_direction = Direction.N;
			}
			else if (grid_type_to == GridType.START_EEDGE2W)
			{
				this.animation_direction = Direction.W;
			}
			else if (grid_type_to == GridType.START_WEDGE2E)
			{
				this.animation_direction = Direction.E;
			}
		}
		else
		{
			grid_type_from = from.get_grid_type();
			/* corner conditions */
			if (grid_type_from == GridType.CORNER_ROAD_W2S || grid_type_from == GridType.CORNER_ROAD_E2S)
			{
				this.animation_direction = Direction.S;
			}
			else if (grid_type_from == GridType.CORNER_ROAD_N2E || grid_type_from == GridType.CORNER_ROAD_S2E)
			{
				this.animation_direction = Direction.E;
			}
			else if (grid_type_from == GridType.CORNER_ROAD_N2W || grid_type_from == GridType.CORNER_ROAD_S2W)
			{
				this.animation_direction = Direction.W;
			}
			else if (grid_type_from == GridType.CORNER_ROAD_E2N || grid_type_from == GridType.CORNER_ROAD_W2N)
			{
				this.animation_direction = Direction.N;
			}
			else if (to.get_x() > from.get_x())
			{
				this.animation_direction = Direction.E;
			}
			else if (to.get_x() < from.get_x())
			{
				this.animation_direction = Direction.W;
			}
			else if (to.get_y() > from.get_y())
			{
				this.animation_direction = Direction.N;
			}
			else if (to.get_y() < from.get_y())
			{
				this.animation_direction = Direction.S;
			}
		}
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
		from.set_car(null);
		to.set_car(null);
		this.current_point = to;
	}

	public boolean at_signal_check()
	{
		return at_signal;
	}

	public void set_current_point(verilogTownGridNode from, verilogTownGridNode to, verilogTownGridNode via_point, verilogTownMap level) 
	{
		/* update which direction the car will now be going */
		set_car_direction(from, to);

		/* set the at signal check */
		at_signal = true;

		this.current_point = to;
		/* store the car in the grid details */
		if (from != null)
		{
			from.set_car(null);
		}
		to.set_car(this);

		/* check if car made it */
		if (to == this.get_end_point())
		{
			Gdx.app.log("Car", "success done");
			this.set_current_point_end(from, to);
			this.set_is_done_path();
		}
		else if (
				to.get_grid_type() == GridType.END_S2SEDGE ||
				to.get_grid_type() == GridType.END_N2NEDGE ||
				to.get_grid_type() == GridType.END_E2EEDGE ||
				to.get_grid_type() == GridType.END_W2WEDGE)
		{
			/* Failed path */
			Gdx.app.log("Car", "failed done");
			this.set_current_point_end(from, to);
			this.set_is_done_path();
		}
	}

	public void crashed()
	{
		verilogTownGridNode crash_point = this.get_current_point();

		Gdx.app.log("Car", "crash done");
		crash_point.set_car(null);
		this.set_is_crashed();
		this.set_is_done_path();

		carSprite = crashSprite;
	}
		
	public void set_path(verilogTownGridNode to, verilogTownGridNode via_point, verilogTownMap level) 
	{
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

	public boolean at_next_grid()
	{
		return (this.position_x%64==0 && this.position_y%64==0);
	}

	public void set_animate_start()
	{
		this.position_x = (this.get_current_point().get_x()-1)*64;
		this.position_y = (this.get_current_point().get_y()-1)*64;
	}

	public void animate_car()
	{		
		if (this.animate_state == CarAnimateStates.MOVING)
		{
			if (this.animation_direction == Direction.N)
				this.position_y += this.speed;	
			else if (this.animation_direction == Direction.S)
				this.position_y -= this.speed;	
			else if (this.animation_direction == Direction.E)
				this.position_x += this.speed;	
			else if (this.animation_direction == Direction.W)
				this.position_x -= this.speed;	

			this.animate_state = CarAnimateStates.MOVED;
			Gdx.app.log("Car ", "x="+this.current_point.get_x()+ " y="+this.current_point.get_y()+" ax="+ this.position_x+" ay="+this.position_y);
		}
	}
	public void set_animate_state(CarAnimateStates state)
	{
		this.animate_state = state;
	}
	public CarAnimateStates get_animate_state()
	{
		return this.animate_state;
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
		return this.is_crashed;
	}

	public void set_is_start_path() {
		this.is_start_path = true;
	}
	public boolean get_is_start_path() {
		return this.is_start_path;
	}

	public void set_processed(boolean value) 
	{
		this.is_processed = value;
	}
	public void reset_states()
	{
		this.set_processed(false);	

		if (this.animate_state == CarAnimateStates.MOVED)
		{
			this.animate_state = CarAnimateStates.MOVING;
		}
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
