package com.me.myverilogtown;

import java.util.*;
import java.util.Random.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class LevelLogic
{
	private int time_step;
	private int count_cars_done;
	Queue<Integer> car_processing_q;
	Queue<Integer> car_crashing_q;
	
	public LevelLogic()
	{

		/* init a simple time step where a unit is the maximum time it takes a car's front to travel through a grid point */
		time_step = 0;
		count_cars_done = 0;

		car_processing_q = new LinkedList<Integer>();
		car_crashing_q = new LinkedList<Integer>();
	}

	public boolean update(Car cars[], int num_cars, verilogTownMap clevel, Random randomno)
	{
		/* increment time */
		time_step ++; // a second of time at 25 FPS

		if (time_step % 50 == 0)
		{
			for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
			{
				//clevel.crash_signal(i, randomno.nextInt(4)); // Trying to cause crashes
				clevel.cycle_signal_3(i, randomno.nextInt(32)); // Random all signals randoms
				//clevel.cycle_signal_2(i, randomno.nextInt(16)); // Random all signals one at a time
				//clevel.cycle_signal(i, randomno.nextInt(4)); // All GO
			}
			//clevel.display_traffic_lights();
		}
	
		/* load up a queue with indices so we can process in priority.  Basically, if a car has another car in front we delay the processing */
		for (int i = 0; i < num_cars; i++)
		{
			if (cars[i].get_start_time() == time_step || cars[i].get_is_running())
			{
				car_processing_q.add(i);
				car_crashing_q.add(i);
			}
		}

		while (!car_processing_q.isEmpty())
		{
			int car_index = car_processing_q.remove();

			cars[car_index].animate_car();

			/* check if a car needs to be started */
			if (cars[car_index].get_start_time() == time_step)
			{
				/* IF - time to start - currently assume that there won't be a back log of cars - probably results in crash logic */
				car_starts(cars[car_index], clevel);
				cars[car_index].set_animate_state(CarAnimateStates.MOVING);
			}
			/* move next spot */
			else if (cars[car_index].at_next_grid())
			{
				/* ELSE IF - Moving car then move to next spot */

				/*Gdx.app.log("LevelLogic", "Car="+ car_index);*/

				if (cars[car_index].get_animate_state() != CarAnimateStates.STOPPED)
				{
					update_spot(cars[car_index], cars[car_index].get_current_point(), clevel);

					if (cars[car_index].get_is_done_path())
					{
						count_cars_done++;
						continue;
					}
				}

				/* check the future */
				verilogTownGridNode current_spot = cars[car_index].get_current_point();
				TrafficSignal signal = current_spot.get_traffic_signal(); 

				if (signal == TrafficSignal.NO_SIGNAL)
				{
					/* IF - you're at a stop light and you can GO then just do what you want OR you're not at a traffic light */
					cars[car_index].set_animate_state(CarAnimateStates.MOVING);
					car_has_free_movement(cars[car_index], current_spot, clevel);
					cars[car_index].check_animate_turn();
				}
				else if (signal == TrafficSignal.GO)
				{
					/* IF - you're at a stop light and you can GO then just do what you want OR you're not at a traffic light */
					cars[car_index].set_animate_state(CarAnimateStates.MOVING);
					car_has_free_movement(cars[car_index], current_spot, clevel);
					cars[car_index].check_animate_turn();
				}
				else if (	signal == TrafficSignal.GO_RIGHT ||  
						signal == TrafficSignal.GO_LEFT ||  
						signal == TrafficSignal.GO_FORWARD)   
				{
					cars[car_index].set_animate_state(CarAnimateStates.MOVING);
					car_has_forced_movement(cars[car_index], current_spot, signal, clevel); 
					cars[car_index].check_animate_turn();
				}
				else if (signal == TrafficSignal.STOP)
				{
					cars[car_index].set_animate_state(CarAnimateStates.STOPPED);

					/*
					int y = cars[car_index].get_current_point().get_y();
					int x = cars[car_index].get_current_point().get_x();
					float ax = cars[car_index].getPosition_x();
					float ay = cars[car_index].getPosition_y();
					Gdx.app.log("LevelLogic", "Car waiting at stop light:"+ car_index +" At x="+ x +" y="+ y + " ax=" + ax + " ay=" + ay);
					*/
				}
			}
		}

		/* check for crashes */
		for (int i = 0; i < num_cars; i++)
		{
			/* could be sped up */
			if (cars[i].get_is_running())
			{
				for (int j = i+1; j < num_cars; j++)
				{
					if (cars[j].get_is_running())
					{
						if (cars[i].check_for_crash(cars[j]))
						{
							cars[i].crashed();
							i++;
							cars[j].crashed();
							count_cars_done+=2;
							break; // break from j loop
						}
					}
				}
			}
		}
		
		/*Gdx.app.log("LevelLogic", "Cars done:"+ count_cars_done +" to num_cars="+ num_cars);*/
		if (count_cars_done >= num_cars)
		{
			return true;
		}

		return false;
	}

	private void update_spot(Car the_car, verilogTownGridNode current_spot, verilogTownMap clevel)
	{
		verilogTownGridNode next_spot;

		next_spot = the_car.get_next_point_on_path();
		the_car.set_current_point(current_spot, next_spot, null, clevel);
		if (the_car.get_is_done_path())
			return;
		the_car.set_path(next_spot, null, clevel);
	}

	private void car_starts(Car the_car, verilogTownMap clevel)

	{
		int x;
		int y;

		the_car.set_current_point(null, the_car.get_start_point(), null, clevel);
		the_car.set_path(the_car.get_start_point(), null, clevel);
		the_car.set_animate_start();
		the_car.animation_direction(null, the_car.get_start_point());
		the_car.set_is_start_path();

		/*y = the_car.get_current_point().get_y();
		x = the_car.get_current_point().get_x();
		float ay = the_car.getPosition_y();
		float ax = the_car.getPosition_x();
		Gdx.app.log("LevelLogic-starts", "At x="+ x +" y="+ y + " ax=" + ax + " ay=" + ay);*/
	}

	private void car_has_forced_movement(Car the_car, verilogTownGridNode current_spot, TrafficSignal signal, verilogTownMap clevel)
	{
		int x;
		int y;
		verilogTownGridNode next_spot;
		verilogTownGridNode turn_via_point;
		verilogTownGridNode second_spot;
		verilogTownGridNode third_spot;

		/* get the turn unless it's illegal */
		turn_via_point = clevel.get_turn(current_spot, signal, the_car.get_direction());
		if (turn_via_point == null)
		{
			/*Gdx.app.log("LevelLogic", "Thinks it's an illegal turn");*/
			the_car.set_animate_state(CarAnimateStates.STOPPED);
		}
		else
		{
			next_spot = the_car.get_next_point_on_path();

			if (car_in_front_check(the_car, current_spot, next_spot, clevel))
			{
				the_car.put_next_point_back_on_path(next_spot);
				the_car.set_animate_state(CarAnimateStates.STOPPED);
				return;
			}


			/* update the animation direction */
			the_car.animation_direction(current_spot, next_spot);

			/* rebuild the path since forced turn */
			the_car.set_path(current_spot, turn_via_point, clevel);

			/* debug info */
			/*
			y = the_car.get_current_point().get_y();
			x = the_car.get_current_point().get_x();
			float ax = the_car.getPosition_x();
			float ay = the_car.getPosition_y();
			Gdx.app.log("LevelLogic-forced", "At x="+ x +" y="+ y + " ax=" + ax + " ay=" + ay);
			*/
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
			the_car.put_next_point_back_on_path(next_spot);
			the_car.set_animate_state(CarAnimateStates.STOPPED);
			return;
		}

		/* update the animation direction */
		the_car.animation_direction(current_spot, next_spot);

		/* If only animate put next spot back on stack */
		the_car.set_path(current_spot, null, clevel);
	
		/* debug info */
		/*
		y = the_car.get_current_point().get_y();
		x = the_car.get_current_point().get_x();
		float ax = the_car.getPosition_x();
		float ay = the_car.getPosition_y();
		Gdx.app.log("LevelLogic-move", "At x="+ x +" y="+ y + " ax=" + ax + " ay=" + ay);
		*/
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
				/* IF - it's going the same direction then don't crash and wait */
				return true;
			}
		}

		return false;
	}
}

