package com.me.myverilogTown;

import java.util.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class LevelLogic
{
    private VerilogTownMap clevel;
    private Car cars[];
    private int num_cars;
    private int time_step;

    public LevelLogic()
    {
        /* init current level map data structure */
        this.clevel = new VerilogTownMap(20, 20); // firts_map

        /*
         * init a simple time step where a unit is the maximum time it takes a
         * car's front to travel through a grid point
         */
        time_step = 0;

        /* this might be where the XML read map goes */
        /* hard coded */
        clevel.verilogTownMapHardCode_first_map();

        /* after reading the number of cars from level */
        num_cars = 10; // hard coded
        cars = new Car[num_cars];

        /* initialize cars */
        cars[0] = new Car(clevel.grid[7][0], clevel.grid[4][21], 1, clevel,
                null, 0, 0, 0, 0);
        cars[1] = new Car(clevel.grid[15][0], clevel.grid[4][21], 1, clevel,
                null, 0, 0, 0, 0);
        cars[2] = new Car(clevel.grid[21][9], clevel.grid[4][21], 1, clevel,
                null, 0, 0, 0, 0);
        cars[3] = new Car(clevel.grid[21][14], clevel.grid[14][0], 2, clevel,
                null, 0, 0, 0, 0);
        cars[4] = new Car(clevel.grid[3][21], clevel.grid[14][0], 2, clevel,
                null, 0, 0, 0, 0);
        cars[5] = new Car(clevel.grid[17][21], clevel.grid[14][0], 2, clevel,
                null, 0, 0, 0, 0);
        cars[6] = new Car(clevel.grid[7][0], clevel.grid[4][21], 3, clevel,
                null, 0, 0, 0, 0);
        cars[7] = new Car(clevel.grid[15][0], clevel.grid[14][0], 3, clevel,
                null, 0, 0, 0, 0);
        cars[8] = new Car(clevel.grid[21][9], clevel.grid[14][0], 4, clevel,
                null, 0, 0, 0, 0);
        cars[9] = new Car(clevel.grid[21][14], clevel.grid[18][21], 4, clevel,
                null, 0, 0, 0, 0);
    }

    public void update()
    {
        int x;
        int y;

        /* increment time */
        time_step++;

        for (int i = 0; i < num_cars; i++)
        {
            if (!cars[i].get_is_done_path())
            {
                Gdx.app.log("LevelLogic", "Car=" + i);

                /* check if a car needs to be started */
                if (cars[i].get_start_time() == time_step)
                {
                    /*
                     * IF - time to start - currently assume that there won't be
                     * a back log of cars - probably results in crash logic
                     */
                    cars[i].set_current_point(cars[i].get_start_point(), clevel);
                    /* now remove the start point */
                    cars[i].get_next_point_on_path();
                }
                /* move next spot */
                else if (cars[i].get_start_time() < time_step)
                {
                    /* ELSE IF - Moving car then move to next spot */
                    VerilogTownGridNode spot;

                    spot = cars[i].get_next_point_on_path();

                    /* check if car made it */
                    if (spot == cars[i].get_end_point())
                    {
                        cars[i].set_is_done_path();
                        Gdx.app.log("LevelLogic", "Car=" + i + "done");
                    }
                    else
                    {
                        cars[i].set_current_point(spot, clevel);
                        /* now remove the current point */
                        cars[i].get_next_point_on_path();

                        /* debug info */
                        y = cars[i].get_current_point().get_y();
                        x = cars[i].get_current_point().get_x();
                        Gdx.app.log("LevelLogic", "Car=" + i + " At x=" + x
                                + " y=" + y);
                    }

                }

            }

        }
    }
}
