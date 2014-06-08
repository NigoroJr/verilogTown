package com.me.myverilogTown;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class VerilogTownMap 
{
	/* Assume grid size is 1 larger on all edges to accomodate invisible starting points */
	private int grid_x;
	private int grid_y; 
	public GridNode grid[][];
	private int markPathCount; // used as a count for a mark path idetifier
	public TrafficControl traffic_signals[];

	/* Constructor */
	public VerilogTownMap(int size_x, int size_y) 
	{
		this.grid_x = size_x+2;
		this.grid_y = size_y+2;
		this.markPathCount = 0;

		this.grid = new GridNode [size_x+2][size_y+2];

		for (int i = 0; i < size_x+2; i++)
		{
			for (int j = 0; j < size_y+2; j++)
			{
				/* Initalize to Non roads */
				this.grid[i][j] = new GridNode(i, j, GridType.NON_ROAD);
			}
		}
	}

	/**
	 * Reads in the map using the parser in order to populate the grid array.
	 * 
	 * @param parser
	 */
	public void readMap(LevelXMLParser parser) 
	{
		grid = parser.getGridArray();
		traffic_signals = parser.getTrafficControls();
	}

	void initFindPathMarker()
	{
		markPathCount ++;
	}

	void showPath(Stack<GridNode> path)
	{
		GridNode traverse;

		/* debugging to see if path is good */
		while(!path.empty())
		{
			int x;
			int y;

			traverse = path.pop();
			y = traverse.getY();
			x = traverse.getX();

			Gdx.app.log("verilogTownMap-showPath", "Stack x="+ x +" y="+ y);
		}
	}

	Stack<GridNode> backTraversePath(Stack<GridNode> existing_path, GridNode end, GridNode start, GridNode current)
	{
		Stack<GridNode> path; 

		if (existing_path == null)
		{
			path = new Stack<GridNode>();
		}
		else
		{
			path = existing_path;
		}

		path.push(end);
		GridNode traverse = current;

		if (current != start)
		{
			/* push the current element onto the stack */
			path.push(traverse);
			/* get the next spot so that we don't push the start onto the stack */
			traverse = traverse.getVisitedBy();
			while (traverse != start)
			{
				path.push(traverse);
				traverse = traverse.getVisitedBy();
			}
		}

		return path;
	}

	void path_clean(Stack<GridNode> path)
	{
		if (path == null)
		{
			return;
		}

		while (!path.isEmpty())
		{
			path.pop();
		}
	}

	/* given a start and end finds path between two in the form of a stack */
	Stack<GridNode> findPath(Stack<GridNode> existing_path, GridNode start, GridNode end, Car the_car)
	{
		Queue<GridNode> queue = new LinkedList<GridNode>();
		GridNode current;
		GridNode possible_end = null;

		/* initialize QUEUE with start for BFS and get a new marker */	
		queue.add(start);
		this.initFindPathMarker();

		/* while QUEUE not empty */
		while ((current = queue.poll()) != null)
		{
			if (possible_end == null && 
					(current.getType() == GridType.END_S2SEDGE || 
					 current.getType() == GridType.END_N2NEDGE || 
					 current.getType() == GridType.END_E2EEDGE || 
					 current.getType() == GridType.END_W2WEDGE)
				)
			{
				/* IF - you find a possible end path from here then record just in case we can't find */
				possible_end = current;
			}

			/* special case if destination/end if end then start adding path back to stack */
			if (current.getNorth() == end)
			{
				while(!queue.isEmpty())
					queue.remove();
				return backTraversePath(existing_path, end, start, current);
			}
			else if (current.getSouth() == end)
			{
				while(!queue.isEmpty())
					queue.remove();
				return backTraversePath(existing_path, end, start, current);
			}
			else if (current.getEast() == end)
			{
				while(!queue.isEmpty())
					queue.remove();
				return backTraversePath(existing_path, end, start, current);
			}
			else if (current.getWest() == end)
			{
				while(!queue.isEmpty())
					queue.remove();
				return backTraversePath(existing_path, end, start, current);
			}

			/* add all children (that's why no else if) to QUEUE and mark as to be visited (because wave based then this will be the shortest path) */
			if (current.getNorth() != null && !current.getNorth().isAlreadyVisited(markPathCount))
			{
				queue.add(current.getNorth());
				current.getNorth().setVisited(markPathCount, current);
			}
			if (current.getSouth() != null && !current.getSouth().isAlreadyVisited(markPathCount))
			{
				queue.add(current.getSouth());
				current.getSouth().setVisited(markPathCount, current);
			}
			if (current.getEast() != null && !current.getEast().isAlreadyVisited(markPathCount))
			{
				queue.add(current.getEast());
				current.getEast().setVisited(markPathCount, current);
			}
			if (current.getWest() != null && !current.getWest().isAlreadyVisited(markPathCount))
			{
				queue.add(current.getWest());
				current.getWest().setVisited(markPathCount, current);
			}
		}

		/* Error in that it can't find a path */
		/* update cars end point */
		the_car.set_end_point_and_fail_on_getting_car_accross(possible_end);
		Gdx.app.log("verilogTownMap-findPath", "Car couldn't find path so forcing to new end");

		while(!queue.isEmpty())
			queue.remove();
		/* refind the path based on the new forced end */ 
		return findPath(existing_path, start, possible_end, the_car);
	}

	/* returns the grid point through the intersection for a given turn */
	GridNode get_turn(GridNode current, TrafficSignalState signal, Direction direction)
	{
		if (signal == TrafficSignalState.GO_FORWARD)
		{
			if (direction == Direction.N)
			{
				return current.getNorth().getNorth().getNorth();
			}
			else if (direction == Direction.S)
			{
				return current.getSouth().getSouth().getSouth();
			}
			else if (direction == Direction.E)
			{
				return current.getEast().getEast().getEast();
			}
			else if (direction == Direction.W)
			{
				return current.getWest().getWest().getWest();
			}
		}
		else if (signal == TrafficSignalState.GO_LEFT)
		{
			if (direction == Direction.N)
			{
				return current.getNorth().getNorth().getWest().getWest();
			}
			else if (direction == Direction.S)
			{
				return current.getSouth().getSouth().getEast().getEast();
			}
			else if (direction == Direction.E)
			{
				return current.getEast().getEast().getNorth().getNorth();
			}
			else if (direction == Direction.W)
			{
				return current.getWest().getWest().getSouth().getSouth();
			}
		}
		else if (signal == TrafficSignalState.GO_RIGHT)
		{
			if (direction == Direction.N)
			{
				return current.getNorth().getEast();
			}
			else if (direction == Direction.S)
			{
				return current.getSouth().getWest();
			}
			else if (direction == Direction.E)
			{
				return current.getEast().getSouth();
			}
			else if (direction == Direction.W)
			{
				return current.getWest().getNorth();
			}
		}

		return null;
	}

	public int get_num_traffic_signals()
	{
		return traffic_signals.length;
	}
	
	public void display_traffic_lights()
	{
		for (int i = 0; i < traffic_signals.length; i++)
		{
			Gdx.app.log("verilogTownMap-Traffic Light", " N="+ traffic_signals[i].getSignalWhen(TrafficControl.PROCEED_NORTH) +" S="+ traffic_signals[i].getSignalWhen(1) +" E="+ traffic_signals[i].getSignalWhen(2) +" W="+ traffic_signals[i].getSignalWhen(3));
		}
	}

	void render_traffic_signal_lights(SpriteBatch batch, Texture stop, Texture go, Texture left, Texture right, Texture forward)
	{
		for (int i = 0; i < traffic_signals.length; i++)
		{
			this.traffic_signals[i].render_traffic_signal(batch, stop, go, left, right, forward);
		}
	}

	public String read_traffic_signal (int light_index)
	{
		return traffic_signals[light_index].read_traffic_signal();
	}

	public void set_traffic_signal(int light_index, int N, int S, int E, int W)
	{
		traffic_signals[light_index].setSignalsStatus(TrafficSignalState.fromInt(N), TrafficSignalState.fromInt(S),TrafficSignalState.fromInt(E),TrafficSignalState.fromInt(W));
	}

	void cycle_signal(int light_index, int which)
	{
		if (which == 0)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 1)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 2)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP);
		else if (which == 3)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO);
	}

	void crash_signal(int light_index, int which)
	{
		if (which == 0)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.STOP);
		else if (which == 1)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.GO);
		else if (which == 2)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.GO);
		else if (which == 3)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.GO);
	}
	void cycle_signal_2(int light_index, int which)
	{
		if (which == 0)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 1)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 2)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP);
		else if (which == 3)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO);
		else if (which == 4)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 5)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 6)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP);
		else if (which == 7)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT);
		else if (which == 8)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 9)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 10)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP);
		else if (which == 11)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT);
		else if (which == 12)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 13)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 14)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP);
		else if (which == 15)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT);
	}
	void cycle_signal_3(int light_index, int which)
	{
		if (which == 0)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 1)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 2)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP);
		else if (which == 3)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO);
		else if (which == 4)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 5)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 6)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP);
		else if (which == 7)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT);
		else if (which == 8)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 9)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 10)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP);
		else if (which == 11)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT);
		else if (which == 12)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 13)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 14)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP);
		else if (which == 15)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT);
		else if (which == 16)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.GO_FORWARD, TrafficSignalState.STOP);
		else if (which == 17)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.GO_RIGHT);
		else if (which == 18)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 19)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 20)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 21)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.GO_LEFT, TrafficSignalState.GO_LEFT, TrafficSignalState.GO_LEFT);
		else if (which == 22)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.GO_RIGHT);
		else if (which == 23)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.STOP);
		else if (which == 24)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.GO, TrafficSignalState.GO);
		else if (which == 25)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.STOP);
		else if (which == 26)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT);
		else if (which == 27)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.GO_LEFT, TrafficSignalState.GO, TrafficSignalState.STOP, TrafficSignalState.STOP);
		else if (which == 28)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.GO_RIGHT, TrafficSignalState.STOP);
		else if (which == 29)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO_LEFT, TrafficSignalState.GO);
		else if (which == 30)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.STOP, TrafficSignalState.GO, TrafficSignalState.GO_LEFT);
		else if (which == 31)
			traffic_signals[light_index].setSignalsStatus(TrafficSignalState.STOP, TrafficSignalState.GO_RIGHT, TrafficSignalState.GO, TrafficSignalState.GO_LEFT);
	}
}
