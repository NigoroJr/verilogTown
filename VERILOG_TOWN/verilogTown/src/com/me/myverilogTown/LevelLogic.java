package com.me.myverilogTown;

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

		/* Queues to do processing steps */
		car_processing_q = new LinkedList<Integer>();
		car_crashing_q = new LinkedList<Integer>();
	}

	public boolean update(Car cars[], int num_cars, verilogTownMap clevel, Random randomno)
	{
		/* increment time */
		time_step ++; // a second of time at 25 FPS

		/* simulation of traffic lights.  This is where the Verilog simulation would go */
		if (time_step % 50 == 0)
		{
			for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
			{
				//clevel.crash_signal(i, randomno.nextInt(4)); // Trying to cause crashes
				clevel.cycle_signal_3(i, randomno.nextInt(32)); // Random all signals randoms
				//clevel.cycle_signal_2(i, randomno.nextInt(16)); // Random all signals one at a time
				//clevel.cycle_signal(i, randomno.nextInt(4)); // All GO
			}
		}
	
		/* load up a queue with what needs to be processed */
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

			/* animate the car so that the pixel location is updated */
			cars[car_index].animate_car();

			/* check if a car needs to be started in the simulation */
			if (cars[car_index].get_start_time() == time_step)
			{
				/* IF - time to start - currently assume that there won't be a back log of cars - probably results in crash logic */
				car_starts(cars[car_index], clevel);
				cars[car_index].set_animate_state(CarAnimateStates.MOVING);
			}
			/* move next spot */
			else if (cars[car_index].at_next_grid())
			{
				/* ELSE IF - A car has reached the center of a grid point then we need to do some game logic */

				if (cars[car_index].get_animate_state() != CarAnimateStates.STOPPED)
				{
					/* IF - Car is not stopped then update the details of the car (as in path) */
					update_spot(cars[car_index], cars[car_index].get_current_point(), clevel);

					if (cars[car_index].get_is_done_path())
					{
						/* IF done path then record this detail and skip the rest of the processing */
						count_cars_done++;
						continue;
					}
				}

				/* check the logic of the game */
				verilogTownGridNode current_spot = cars[car_index].get_current_point();
				TrafficSignal signal = current_spot.get_traffic_signal(); 

				/* Check what/if there is a traffic signal and a response needed */
				if (signal == TrafficSignal.NO_SIGNAL)
				{
					/* IF - you're just on a road then do what you want */
					cars[car_index].set_animate_state(CarAnimateStates.MOVING);
					car_has_free_movement(cars[car_index], current_spot, clevel);
					cars[car_index].check_animate_turn();
				}
				else if (signal == TrafficSignal.GO)
				{
					/* IF - you're at a GO signal then you're free to do what you want */
					cars[car_index].set_animate_state(CarAnimateStates.MOVING);
					car_has_free_movement(cars[car_index], current_spot, clevel);
					cars[car_index].check_animate_turn();
				}
				else if (	signal == TrafficSignal.GO_RIGHT ||  
						signal == TrafficSignal.GO_LEFT ||  
						signal == TrafficSignal.GO_FORWARD)   
				{
					/* ELSE IF - you're forced by the traffic signal to do something */
					cars[car_index].set_animate_state(CarAnimateStates.MOVING);
					car_has_forced_movement(cars[car_index], current_spot, signal, clevel); 
					cars[car_index].check_animate_turn();
				}
				else if (signal == TrafficSignal.STOP)
				{
					/* ELSE IF - the signal says STOP */
					cars[car_index].set_animate_state(CarAnimateStates.STOPPED);
				}
			}
		}

		/* check for crashes by iterating through the animation poiints and looking for rectangle overlaps */
		for (int i = 0; i < num_cars; i++)
		{
			/* could be sped up by only looking at running cars...saves checks */
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
		
		if (count_cars_done >= num_cars)
		{
			/* IF all cars done then record this */
			return true;
		}

		return false;
	}

	private void update_spot(Car the_car, verilogTownGridNode current_spot, verilogTownMap clevel)
	{
		verilogTownGridNode next_spot;

		/* get the next point on the path */
		next_spot = the_car.get_next_point_on_path();
		/* set the new point as the next_spot */
		the_car.set_current_point(current_spot, next_spot, null, clevel);
	}

	private void car_starts(Car the_car, verilogTownMap clevel)
	{
		int x;
		int y;

		/* set the starting point */
		the_car.set_current_point(null, the_car.get_start_point(), null, clevel);
		/* setup the path */
		the_car.set_path(the_car.get_start_point(), null, clevel);
		/* setup the animation details */
		the_car.set_animate_start();
		the_car.animation_direction(null, the_car.get_start_point());
		/* update that this car has started */
		the_car.set_is_start_path();
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
			/* IF - can't do the forced turn, don't do anything except STOP the car */
			/*Gdx.app.log("LevelLogic", "Thinks it's an illegal turn");*/
			the_car.set_animate_state(CarAnimateStates.STOPPED);
		}
		else
		{
			next_spot = the_car.get_next_point_on_path();

			/* Check for cars in front that you will stop for if going same direction */
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
		}
	}

	private void car_has_free_movement(Car the_car, verilogTownGridNode current_spot, verilogTownMap clevel)
	{
		int x;
		int y;
		verilogTownGridNode next_spot;
		
		next_spot = the_car.get_next_point_on_path();
		
		/* Check for cars in front that you will stop for if going same direction */
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

