/*
The MIT License (MIT)

Copyright (c) 2014 Peter Jamieson, Naoki Mizuno, and Boyu Zhang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package com.me.myverilogTown;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Car
{
	private GridNode			start_point;
	private GridNode			end_point;
	private GridNode			original_end_point;
	private GridNode			current_point;
	private GridNode			way_point;
	private int					start_time;
	private boolean				is_crashed;
	private boolean				is_done_path;
	private boolean				is_start_path;
	private Stack<GridNode>		path;

	/* Necessary for displaying car */
	private Texture				carTexture;
	private Sprite				carSprite;
	private Sprite				crashSprite;

	/* Necessary variables for proper rendering of car sprites */
	protected float				position_x;
	protected float				position_y;
	protected float				width;
	protected float				height;
	protected float				xScale;
	protected float				yScale;
	protected float				speed;
	protected float				rotation;
	protected CarAnimateStates	animate_state;
	private boolean				at_signal;
	private Direction			direction;
	private Direction			animation_direction;

	/* turn variables */
	private boolean				animate_turn;
	private boolean				animate_uturn;
	private float				center_x;
	private float				center_y;
	private int					fps_start;
	private float				start_angle;
	private boolean				direction_clockwise;	// true = CW false = CCW
	private float				animating_turn_x;
	private float				animating_turn_y;
	private float				turn_rotation;
	
	private boolean				is_forced_turned;

	/** Sets the start and end grid and the start time of this car. TODO:
	 * starting_time is frames? TODO: setSpeed method
	 * 
	 * @param start
	 *            The grid where this car starts from.
	 * @param end
	 *            The grid where this car heads to.
	 * @param starting_time
	 *            Time when this car starts. */
	public Car(GridNode start, GridNode end, int starting_time)
	{
		// TODO: better constructor
		this(start, end, starting_time, 0, 0, 64, 64, 0, 4);
	}

	public Car(
			GridNode start,
			GridNode end,
			int starting_time,
			int position_x,
			int position_y,
			float width,
			float height,
			float rotation,
			float speed)
	{
		// how do I assert(start >= 1)
		this.start_point = start;
		this.end_point = end;
		this.original_end_point = end;
		this.current_point = null;
		this.way_point = null;
		this.animation_direction = null;
		this.direction = null;
		this.is_crashed = false;
		this.is_done_path = false;
		this.is_start_path = false;
		this.start_time = starting_time;
		this.position_x = position_x;
		this.position_y = position_y;
		this.width = width;
		this.height = height;
		this.speed = speed; // at 25fps, 8pixels/frame = ~ 20mph
		this.rotation = rotation;
		this.xScale = 1;
		this.yScale = 1;
		this.animate_state = CarAnimateStates.STOPPED;
		this.at_signal = false;
		this.animate_turn = false;
		this.animate_uturn = false;

		this.carTexture = new Texture("data/car_sheet.png");
		carTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		/* breakup all the pixel pictures of cars to pick 1 for your sprite.
		 * Crash is last one */
		TextureRegion[] carFrames;
		TextureRegion[][] tmp = TextureRegion.split(carTexture, carTexture.getWidth() / 2, carTexture.getHeight() / 2);
		carFrames = new TextureRegion[2 * 2];
		int index = 0;
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				carFrames[index++] = tmp[i][j];
			}
		}

		carSprite = new Sprite(carFrames[new Random().nextInt(3)]);

		// carSprite = new Sprite(new Texture("data/testCar.png"));

		carSprite.setPosition(this.position_x, this.position_y);
		carSprite.setSize(width, height);

		crashSprite = new Sprite(carFrames[3]); // crash sprite is last
		crashSprite.setSize(width, height);
	}

	public void softReset()
	{
		this.end_point = this.original_end_point; /* need to recopy the end point */
		this.current_point = null;
		this.way_point = null;
		this.animation_direction = null;
		this.direction = null;
		this.is_crashed = false;
		this.is_done_path = false;
		this.is_start_path = false;
		this.animate_state = CarAnimateStates.STOPPED;
		this.at_signal = false;
		this.animate_turn = false;
		this.animate_uturn = false;
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

	public void animation_direction(GridNode from, GridNode to)
	{
		GridType grid_type_to;
		GridType grid_type_from;
		grid_type_to = to.getType();

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
			grid_type_from = from.getType();
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
			else if (to.getX() > from.getX())
			{
				this.animation_direction = Direction.E;
			}
			else if (to.getX() < from.getX())
			{
				this.animation_direction = Direction.W;
			}
			else if (to.getY() > from.getY())
			{
				this.animation_direction = Direction.N;
			}
			else if (to.getY() < from.getY())
			{
				this.animation_direction = Direction.S;
			}
		}
	}

	public void set_car_direction(GridNode from, GridNode to)
	{
		GridType grid_type;
		grid_type = to.getType();

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
		else if (to.getX() > from.getX())
		{
			this.direction = Direction.E;
		}
		else if (to.getX() < from.getX())
		{
			this.direction = Direction.W;
		}
		else if (to.getY() > from.getY())
		{
			this.direction = Direction.N;
		}
		else if (to.getY() < from.getY())
		{
			this.direction = Direction.S;
		}
	}

	public void set_current_point_end(GridNode from, GridNode to)
	{
		from.removeCar();
		to.removeCar();
		this.current_point = to;
	}

	public boolean at_signal_check()
	{
		return at_signal;
	}

	public void set_current_point(
			GridNode from,
			GridNode to,
			GridNode via_point,
			VerilogTownMap level)
	{
		/* update which direction the car will now be going */
		set_car_direction(from, to);

		/* set the at signal check */
		at_signal = true;

		this.current_point = to;

		/* store the car in the grid details */
		if (from != null)
		{
			from.setCar(null);
		}
		to.setCar(this);

		/* check if car made it to the end */
		if (to == this.get_end_point())
		{
			Gdx.app.log("Car", "success done");
			this.set_current_point_end(from, to);
			this.set_is_done_path();
		}
		else if (to.getType() == GridType.END_S2SEDGE || to.getType() == GridType.END_N2NEDGE || to.getType() == GridType.END_E2EEDGE || to.getType() == GridType.END_W2WEDGE)
		{
			/* Failed to get to proper destination path */
			Gdx.app.log("Car", "failed done");
			this.set_current_point_end(from, to);
			this.set_is_done_path();
		}
	}

	public void crashed()
	{
		GridNode crash_point = this.get_current_point();

		Gdx.app.log("Car", "crash done" + " x=" + this.position_x + " y=" + this.position_y);
		crash_point.removeCar();;
		this.set_is_crashed();
		this.set_is_done_path();

		carSprite = crashSprite;
	}

	public void set_path(GridNode to, GridNode via_point, VerilogTownMap level)
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
			/* this is way point forced turn path calculations. Can be this
			 * update or in previous update */
			if (this.way_point == to)
			{
				/* IF you get the the way point */
				this.way_point = null;

				this.path = level.findPath(null, to, this.end_point, this);
			}
			else if (via_point != null)
			{
				/* If there's a new via point that you have to go to */
				this.way_point = via_point;

				/* create the path in to the way point and then to the end point */
				this.path = level.findPath(null, this.way_point, this.end_point, this);
				this.path = level.findPath(this.path, to, this.way_point, this);
			}
			else
			{
				/* If you're on your way to a way point */

				/* create the path in to the way point and then to the end point */
				this.path = level.findPath(null, this.way_point, this.end_point, this);
				this.path = level.findPath(this.path, to, this.way_point, this);
			}
		}
	}

	public boolean at_next_grid()
	{
		/* Checks if you're at the next spot */
		return (this.position_x % 64 == 0 && this.position_y % 64 == 0);
	}

	public void set_animate_start()
	{
		/* 64 is the size of the tiles, so start point */
		this.position_x = (this.get_current_point().getX() - 1) * 64;
		this.position_y = (this.get_current_point().getY() - 1) * 64;
	}

	public void check_animate_turn()
	{
		/* checks if 3 or 4 points is a turn or uturn which needs to go in an
		 * animation sequence */
		GridNode p1 = this.current_point;
		GridNode p2;
		GridNode p3;
		GridNode p4;

		if (this.animate_turn == true)
			return;

		p2 = this.get_next_point_on_path();
		if (p2 != null && !path.empty())
		{
			p3 = this.get_next_point_on_path();
		}
		else
		{
			put_next_point_back_on_path(p2);
			return;
		}

		if (p3 == null)
		{
			put_next_point_back_on_path(p2);
			return;
		}
		else if (!path.empty())
		{
			p4 = this.get_next_point_on_path();
		}
		else
		{
			p4 = null;
		}

		if (p4 != null && p1.getX() != p3.getX() && p1.getY() != p3.getY() && (p4.getX() == p1.getX() || p4.getY() == p1.getY()))
		{
			/* if this is a uturn */
			init_animate_uturn(p1, p2, p3, p4);
		}
		else if (p1.getX() != p3.getX() && p1.getY() != p3.getY())
		{
			/* if one of the directions is not the same, must be a turn assuming
			 * from and to are 3 points of a corner */
			init_animate_turn(p1, p2, p3);
		}

		/* replace the points on the path */
		if (p4 != null)
			put_next_point_back_on_path(p4);
		put_next_point_back_on_path(p3);
		put_next_point_back_on_path(p2);
	}

	public void init_animate_turn(GridNode p1, GridNode p2, GridNode p3)
	{
		/* sets all the variables to calulate the arc of the circle to travel on */
		this.animate_turn = true;
		fps_start = 0;
		float p1_x = (p1.getX() - 1) * 64;
		float p1_y = (p1.getY() - 1) * 64;
		float p3_x = (p3.getX() - 1) * 64;
		float p3_y = (p3.getY() - 1) * 64;
		animating_turn_x = p1_x;
		animating_turn_y = p1_y;

		/*p2-->p3 | | p1 */
		if (p1.getX() < p3.getX() && p1.getY() < p3.getY() && p2.getX() == p1.getX())
		{
			direction_clockwise = true;
			center_x = p3_x;
			center_y = p1_y;
			turn_rotation = 90; // relative to direction animation
			start_angle = 180; // relative to the center to p1
		}
		/*p1-- | | v */
		else if (p1.getX() < p3.getX() && p1.getY() > p3.getY() && p2.getX() == p3.getX())
		{
			direction_clockwise = true;
			center_x = p1_x;
			center_y = p3_y;
			turn_rotation = 360;
			start_angle = 90;
		}
		/*
		 * p1 | | <-- */
		else if (p1.getX() > p3.getX() && p1.getY() > p3.getY() && p2.getX() == p1.getX())
		{
			direction_clockwise = true;
			center_x = p3_x;
			center_y = p1_y;
			turn_rotation = 270;
			start_angle = 360;
		}
		/*A | | --p1 */
		else if (p1.getX() > p3.getX() && p1.getY() < p3.getY() && p2.getX() == p3.getX())
		{
			direction_clockwise = true;
			center_x = p1_x;
			center_y = p3_y;
			turn_rotation = 180;
			start_angle = 270;
		}
		/*--p1 | | v p3 */
		else if (p1.getX() > p3.getX() && p1.getY() > p3.getY() && p2.getX() == p3.getX())
		{
			direction_clockwise = false;
			center_x = p1_x;
			center_y = p3_y;
			turn_rotation = 180;
			start_angle = 90;
		}
		/*p3-- | | A */
		else if (p1.getX() > p3.getX() && p1.getY() < p3.getY() && p2.getX() == p1.getX())
		{
			direction_clockwise = false;
			center_x = p3_x;
			center_y = p1_y;
			turn_rotation = 90;
			start_angle = 0;
		}
		/*
		 * p3 | | >-- */
		else if (p1.getX() < p3.getX() && p1.getY() < p3.getY() && p2.getX() == p3.getX())
		{
			direction_clockwise = false;
			center_x = p1_x;
			center_y = p3_y;
			turn_rotation = 0;
			start_angle = 270;
		}
		/*v | | --p3 */
		else if (p1.getX() < p3.getX() && p1.getY() > p3.getY() && p2.getX() == p1.getX())
		{
			direction_clockwise = false;
			center_x = p3_x;
			center_y = p1_y;
			turn_rotation = 270;
			start_angle = 180;
		}
	}

	public void init_animate_uturn(
			GridNode p1,
			GridNode p2,
			GridNode p3,
			GridNode p4)
	{
		/* sets all the variables to calulate the arc of the circle to travel on */
		this.animate_turn = true;
		this.animate_uturn = true;
		fps_start = 0;
		float p1_x = (p1.getX() - 1) * 64;
		float p1_y = (p1.getY() - 1) * 64;
		animating_turn_x = p1_x;
		animating_turn_y = p1_y;

		/*p3--p2 | | | | p4 p1 */
		if (p1.getY() == p4.getY() && p1.getY() < p2.getY())
		{
			direction_clockwise = false;
			center_x = p1_x - 32;
			center_y = p1_y;
			turn_rotation = 90; // relative to direction animation
			start_angle = 0; // relative to the center to p1
		}
		/*p4--p3 | | p1--p2 */
		else if (p1.getX() == p4.getX() && p1.getX() < p2.getX())
		{
			direction_clockwise = false;
			center_x = p1_x;
			center_y = p1_y + 32;
			turn_rotation = 0; // relative to direction animation
			start_angle = 270; // relative to the center to p1
		}
		/*p1 p4 | | | | p2--p3 */
		else if (p1.getY() == p4.getY() && p1.getY() > p2.getY())
		{
			direction_clockwise = false;
			center_x = p1_x + 32;
			center_y = p1_y;
			turn_rotation = 270; // relative to direction animation
			start_angle = 180; // relative to the center to p1
		}
		/*p2--p1 | | p3--p4 */
		else if (p1.getX() == p4.getX() && p1.getX() > p2.getX())
		{
			direction_clockwise = false;
			center_x = p1_x;
			center_y = p1_y - 32;
			turn_rotation = 180; // relative to direction animation
			start_angle = 90; // relative to the center to p1
		}
	}

	public void animate_car()
	{
		/* local animation . Always moves position_x and position_y since these
		 * are whe the car logically is. Also can be turning so update those
		 * variables */
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

			if (this.animate_turn == true)
			{
				if (animate_uturn == false)
				{
					/* 90 Degree TURN */
					if (fps_start == 31)
					{
						/* animation done */
						animate_turn = false;
						return;
					}

					fps_start++;

					/* Radius of turn is 64 */
					animating_turn_x = (float) (this.center_x + 64 * Math.cos(Math.toRadians(this.start_angle)));
					animating_turn_y = (float) (this.center_y + 64 * Math.sin(Math.toRadians(this.start_angle)));

					if (this.direction_clockwise == true)
					{
						turn_rotation -= 2.8125; // 90 degrees / 32 frames (16
													// Frames to move through a
													// tile)
						start_angle -= 2.8125; // relative to the center to p1

					}
					else
					{
						turn_rotation += 2.8125; // 90 degrees / 32 frames
						start_angle += 2.8125; // relative to the center to p1
					}
				}
				else
				{
					/* U-TURN */
					if (fps_start == 47)
					{
						/* animation done */
						animate_turn = false;
						animate_uturn = false;
						return;
					}

					fps_start++;

					/* Radius of turn is 32 */
					animating_turn_x = (float) (this.center_x + 32 * Math.cos(Math.toRadians(this.start_angle)));
					animating_turn_y = (float) (this.center_y + 32 * Math.sin(Math.toRadians(this.start_angle)));

					if (this.direction_clockwise == true)
					{
						turn_rotation -= 3.75; // 90 degrees / 48 frames (16
												// Frames to move through a
												// tile)
						start_angle -= 3.75; // relative to the center to p1

					}
					else
					{
						turn_rotation += 3.75; // 180 degrees / 48 frames
						start_angle += 3.75; // relative to the center to p1
					}
				}

			}
		}
	}

	public boolean get_animate_turn()
	{
		return this.animate_turn;
	}

	public float get_turn_x()
	{
		return this.animating_turn_x;
	}

	public float get_turn_y()
	{
		return this.animating_turn_y;
	}

	public float get_turn_rotation()
	{
		return this.turn_rotation;
	}

	public void set_animate_state(CarAnimateStates state)
	{
		this.animate_state = state;
	}

	public CarAnimateStates get_animate_state()
	{
		return this.animate_state;
	}

	public GridNode get_current_point()
	{
		return this.current_point;
	}

	public GridNode get_start_point()
	{
		return this.start_point;
	}

	public GridNode get_end_point()
	{
		return this.end_point;
	}

	public void set_end_point_and_fail_on_getting_car_accross(GridNode point)
	{
		this.end_point = point;
	}

	public GridNode get_next_point_on_path()
	{
		return this.path.pop();
	}

	public void put_next_point_back_on_path(GridNode point)
	{
		this.path.push(point);
	}
	
	public Stack<GridNode> getPath(){
		return path;
	}

	public int get_start_time()
	{
		return this.start_time;
	}

	public boolean check_for_crash(Car car_two)
	{
		/* checks if car ones box has any points from car_two in it's rectangle */
		float c1_x = this.position_x + 10; // 10 is a fudge factor */
		float c1_y = this.position_y + 10;
		float c2_x = car_two.position_x + 10;
		float c2_y = car_two.position_y + 10;
		float c1_x_offset = this.position_x + 54; // 54 is a fudge factor =
													// 64-10 */
		float c1_y_offset = this.position_y + 54;
		float c2_x_offset = car_two.position_x + 54;
		float c2_y_offset = car_two.position_y + 54;

		float p1_x = c2_x;
		float p1_y = c2_y;
		float p2_x = c2_x_offset;
		float p2_y = c2_y;
		float p3_x = c2_x;
		float p3_y = c2_y_offset;
		float p4_x = c2_x_offset;
		float p4_y = c2_y_offset;

		if (c1_x <= p1_x && c1_x_offset >= p1_x && c1_y <= p1_y && c1_y_offset >= p1_y)
			return true;
		else if (c1_x <= p2_x && c1_x_offset >= p2_x && c1_y <= p2_y && c1_y_offset >= p2_y)
			return true;
		else if (c1_x <= p3_x && c1_x_offset >= p3_x && c1_y <= p3_y && c1_y_offset >= p3_y)
			return true;
		else if (c1_x <= p4_x && c1_x_offset >= p4_x && c1_y <= p4_y && c1_y_offset >= p4_y)
			return true;
		else
			return false;

	}

	public void set_is_crashed()
	{
		this.is_crashed = true;
	}

	public boolean get_is_crashed()
	{
		return this.is_crashed;
	}

	public void set_is_start_path()
	{
		this.is_start_path = true;
	}

	public boolean get_is_start_path()
	{
		return this.is_start_path;
	}

	public boolean get_is_running()
	{
		if (this.is_start_path == true && this.is_done_path == false)
			return true;
		else
			return false;
	}

	public void set_is_done_path()
	{
		this.is_done_path = true;
		this.current_point = null;
	}

	public boolean get_is_done_path()
	{
		return this.is_done_path;
	}

	public Direction get_direction()
	{
		return this.direction;
	}
	
	public void set_forced_turned(boolean is_forced_turned){
		this.is_forced_turned = is_forced_turned;
	}
	
	public boolean get_forced_turned(){
		return this.is_forced_turned;
	}
		
}
