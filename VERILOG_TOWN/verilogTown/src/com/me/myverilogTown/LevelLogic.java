package com.me.myverilogtown;

import java.util.*;
import java.util.Random.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class LevelLogic
{
	private int time_step;
	Queue<Integer> car_processing_q;
	
	
	public LevelLogic()
	{

		/* init a simple time step where a unit is the maximum time it takes a car's front to travel through a grid point */
		time_step = 0;

		car_processing_q = new LinkedList<Integer>();
	}

	public void update(Car cars[], int num_cars, verilogTownMap clevel)
	{

		Random randomno = new Random();
		/* increment time */
		time_step ++;

		for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
		{
			clevel.cycle_signal_3(i, randomno.nextInt(32)); // Random all signals randoms
			//clevel.cycle_signal_2(i, randomno.nextInt(16)); // Random all signals one at a time
			//clevel.cycle_signal(i, randomno.nextInt(4)); // All GO
		}
		clevel.display_traffic_lights();

		/* load up a queue with indices so we can process in priority.  Basically, if a car has another car in front we delay the processing */
		for (int i = 0; i < num_cars; i++)
		{
			car_processing_q.add(i);
			cars[i].set_processed(false);
		}

		while (!car_processing_q.isEmpty())
		{
			int car_index = car_processing_q.remove();

			if (!cars[car_index].get_is_done_path())
			{
				Gdx.app.log("LevelLogic", "Car="+ car_index);

				/* check if a car needs to be started */
				if (cars[car_index].get_start_time() == time_step)
				{
					/* IF - time to start - currently assume that there won't be a back log of cars - probably results in crash logic */
					car_starts(cars[car_index], clevel);
				}
				/* move next spot */
				else if (cars[car_index].get_start_time() < time_step)
				{
					/* ELSE IF - Moving car then move to next spot */
					verilogTownGridNode current_spot = cars[car_index].get_current_point();
					TrafficSignal signal = current_spot.get_traffic_signal(); 

					if (	(signal == TrafficSignal.GO) || signal == TrafficSignal.NO_SIGNAL)
					{
						/* IF - you're at a stop light and you can GO then just do what you want OR you're not at a traffic light */
						car_has_free_movement(cars[car_index], current_spot, clevel);
					}
					else if ( 	signal == TrafficSignal.GO_RIGHT ||  
							signal == TrafficSignal.GO_LEFT ||  
							signal == TrafficSignal.GO_FORWARD)   
					{
						car_has_forced_movement(cars[car_index], current_spot, signal, clevel); 
					}
					else
					{
						cars[car_index].set_processed(true);

						int y = cars[car_index].get_current_point().get_y();
						int x = cars[car_index].get_current_point().get_x();
						Gdx.app.log("LevelLogic", "Car waiting at stop light:"+ car_index +" At x="+ x +" y="+ y);
					}
				}
				else
				{
					/* ELSE - the car is not yet going so it is processed */
					cars[car_index].set_processed(true);
				}

				/* if not processed add to Q for later */
				if (cars[car_index].get_processed() == false)
				{
					Gdx.app.log("LevelLogic", "Added back="+car_index);
					car_processing_q.add(car_index);
				}
			}
		}
	}

	private void car_starts(Car the_car, verilogTownMap clevel)
	{
		int x;
		int y;

		the_car.set_current_point(null, the_car.get_start_point(), null, clevel);
		the_car.set_is_start_path();
		the_car.set_processed(true);

		y = the_car.get_current_point().get_y();
		x = the_car.get_current_point().get_x();
		Gdx.app.log("LevelLogic-starts", "At x="+ x +" y="+ y);
	}

	private void car_has_forced_movement(Car the_car, verilogTownGridNode current_spot, TrafficSignal signal, verilogTownMap clevel)
	{
		int x;
		int y;
		verilogTownGridNode next_spot;
		verilogTownGridNode turn_via_point;

		/* get the turn unless it's illegal */
		turn_via_point = clevel.get_turn(current_spot, signal, the_car.get_direction());
		if (turn_via_point == null)
		{
			Gdx.app.log("LevelLogic", "Thinks it's an illegal turn");
			the_car.set_processed(true);
		}
		else
		{
			next_spot = the_car.get_next_point_on_path();

			if (car_in_front_check(the_car, current_spot, next_spot, clevel))
			{
				return;
			}

			/* rebuild the path since forced turn */
			the_car.set_current_point(current_spot, next_spot, turn_via_point, clevel);
			the_car.set_processed(true);

			/* debug info */
			y = the_car.get_current_point().get_y();
			x = the_car.get_current_point().get_x();
			Gdx.app.log("LevelLogic-forced", "At x="+ x +" y="+ y);
		}
	}

	private void car_has_free_movement(Car the_car, verilogTownGridNode current_spot, verilogTownMap clevel)
	{
		int x;
		int y;
		verilogTownGridNode next_spot;
		
		next_spot = the_car.get_next_point_on_path();
		
		if (car_in_front_check(the_car, current_spot, next_spot, clevel))
		{
			return;
		}

		/* check if car made it */
		if (next_spot == the_car.get_end_point())
		{
			Gdx.app.log("LevelLogic", "done");
			the_car.set_current_point_end(current_spot, next_spot);
			the_car.set_is_done_path();
			the_car.set_processed(true);
		}
		else
		{
			the_car.set_current_point(current_spot, next_spot, null, clevel);
			the_car.set_processed(true);
	
			/* debug info */
			y = the_car.get_current_point().get_y();
			x = the_car.get_current_point().get_x();
			Gdx.app.log("LevelLogic", "At x="+ x +" y="+ y);
		}
	}

	private boolean car_in_front_check(Car the_car, verilogTownGridNode current_spot, verilogTownGridNode next_spot, verilogTownMap clevel)
	{
		Car car_in_front;
		GridType car_in_front_grid_type;
		
		/* check if there's a car going in the same direction ahead */
		car_in_front = next_spot.get_car();

		if (car_in_front != null)
		{
			car_in_front_grid_type = car_in_front.get_current_point().get_grid_type();

			if (car_in_front.get_processed() == true)
			{
				/* IF - car already processed then */
				if (car_in_front.get_direction() == the_car.get_direction() || 
						car_in_front_grid_type == GridType.CORNER_ROAD_W2S || 
						car_in_front_grid_type == GridType.CORNER_ROAD_E2S ||
						car_in_front_grid_type == GridType.CORNER_ROAD_E2N || 
						car_in_front_grid_type == GridType.CORNER_ROAD_W2N ||
						car_in_front_grid_type == GridType.CORNER_ROAD_N2E || 
						car_in_front_grid_type == GridType.CORNER_ROAD_S2E ||
						car_in_front_grid_type == GridType.CORNER_ROAD_N2W || 
						car_in_front_grid_type == GridType.CORNER_ROAD_S2W)
				{
					/* IF - it's going the same direction then don't crash */
					the_car.put_next_point_back_on_path(next_spot);
					the_car.set_processed(true);
					return true;
				}
				else
				{
					/* ELSE - crash!!! */
					Gdx.app.log("LevelLogic", "CRASH!!!");
					the_car.set_current_point(current_spot, next_spot, null, clevel);
					the_car.set_processed(true);

					return true;
				}
			}
			else
			{
				/* ELSE put back for later processing */
				return true;
			}
		}

		return false;
	}
}
