package com.me.myverilogTown;

import java.util.*;

public class verilogTownMap 
{
	/* Assume grid size is 1 larger on all edges to accomodate invisible starting points */
	private int grid_x;
	private int grid_y; 
	private verilogTownGridNode grid[][];
	private int markPathCount; // used as a count for a mark path idetifier

	/* Constructor */
	verilogTownMap(int size_x, int size_y) 
	{
		this.grid_x = size_x+2;
		this.grid_y = size_y+2;
		this.markPathCount = 0;

		this.grid = new verilogTownGridNode [size_x+2][size_y+2];

		for (int i = 0; i < size_x+2; i++)
		{
			for (int j = 0; i < size_y+2; j++)
			{
				/* Initalize to Non roads */
				this.grid[i][j] = new verilogTownGridNode(i, j, GridType.NON_ROAD);
			}
		}
	}

	void initFindPathMarker()
	{
		markPathCount ++;
	}


	Stack<verilogTownGridNode> backTraversePath(verilogTownGridNode end, verilogTownGridNode start, verilogTownGridNode current)
	{
		Stack<verilogTownGridNode> path = new Stack<verilogTownGridNode>();
		path.push(end);
		verilogTownGridNode traverse = current;

		/* push the current element onto the stack */
		path.push(traverse);
		while (traverse != start)
		{
			traverse = traverse.getVisitedBy();
		}

		return path;
	}

	/* given a start and end finds path between two in the form of a stack */
	Stack<verilogTownGridNode> findPath(verilogTownGridNode start, verilogTownGridNode end)
	{
		Queue<verilogTownGridNode> queue = new LinkedList<verilogTownGridNode>();
		verilogTownGridNode current;

		/* initialize QUEUE with start for BFS and get a new marker */	
		queue.add(start);
		this.initFindPathMarker();

		/* while QUEUE not empty */
		while ((current = queue.poll()) != null)
		{
			/* special case if destination/end if end then start adding path back to stack */
			if (current.getNorth() == end)
			{
				return backTraversePath(end, start, current);
			}
			else if (current.getSouth() == end)
			{
				return backTraversePath(end, start, current);
			}
			else if (current.getEast() == end)
			{
				return backTraversePath(end, start, current);
			}
			else if (current.getWest() == end)
			{
				return backTraversePath(end, start, current);
			}

			/* add all children to QUEUE and mark as to be visited (because wave based then this will be the shortest path) */
			if (current.getNorth() == null && current.isAlreadyVisited(markPathCount))
			{
				queue.add(current.getNorth());
				current.getNorth().setVisited(markPathCount, current);

			}
			else if (current.getSouth() == null && current.isAlreadyVisited(markPathCount))
			{
				queue.add(current.getSouth());
				current.getSouth().setVisited(markPathCount, current);
			}
			else if (current.getEast() == null && current.isAlreadyVisited(markPathCount))
			{
				queue.add(current.getEast());
				current.getEast().setVisited(markPathCount, current);
			}
			else if (current.getWest() == null && current.isAlreadyVisited(markPathCount))
			{
				queue.add(current.getWest());
				current.getWest().setVisited(markPathCount, current);
			}
		}

		/* Error in that it never found the start */
		return null;




	}

	/* hard code initialization of firt_map.png */
	void verilogTownMapHardCode_first_map()
	{
		/* Tiles are 64x64 */
		/* Map is 20x20 */
		/* top row - 21 */
		this.grid[3][21].set_START_NEDGE2S(this.grid[3][20]);
		this.grid[4][21].set_END_N2NEDGE();
		this.grid[17][21].set_START_NEDGE2S(this.grid[17][20]);
		this.grid[18][21].set_END_N2NEDGE();
		/* row 20 */
		this.grid[1][20].set_CORNER_ROAD_W2S(this.grid[1][19]);
		this.grid[2][20].set_STRAIGHT_ROAD_W2W(this.grid[1][20]);
		this.grid[3][20].set_INTER_TURN_S2WS(this.grid[2][20], this.grid[3][19]);
		this.grid[4][20].set_INTER_TURN_W2NW(this.grid[4][21], this.grid[3][20]);
		this.grid[5][20].set_STRAIGHT_ROAD_W2W(this.grid[4][20]);
		this.grid[6][20].set_STRAIGHT_ROAD_W2W(this.grid[5][20]);
		this.grid[7][20].set_STRAIGHT_ROAD_W2W(this.grid[6][20]);
		this.grid[8][20].set_STRAIGHT_ROAD_W2W(this.grid[7][20]);
		this.grid[9][20].set_STRAIGHT_ROAD_W2W(this.grid[8][20]);
		this.grid[10][20].set_STRAIGHT_ROAD_W2W(this.grid[9][20]);
		this.grid[11][20].set_STRAIGHT_ROAD_W2W(this.grid[10][20]);
		this.grid[12][20].set_STRAIGHT_ROAD_W2W(this.grid[11][20]);
		this.grid[13][20].set_STRAIGHT_ROAD_W2W(this.grid[12][20]);
		this.grid[14][20].set_STRAIGHT_ROAD_W2W(this.grid[13][20]);
		this.grid[15][20].set_STRAIGHT_ROAD_W2W(this.grid[14][20]);
		this.grid[16][20].set_STRAIGHT_ROAD_W2W(this.grid[15][20]);
		this.grid[17][20].set_INTER_TURN_S2WS(this.grid[16][20], this.grid[17][19]);
		this.grid[18][20].set_INTER_TURN_W2NW(this.grid[18][21], this.grid[17][20]);
		/* row 19 */
		this.grid[1][19].set_STRAIGHT_ROAD_S2S(this.grid[1][18]);
		this.grid[2][19].set_CORNER_ROAD_N2E(this.grid[3][19]);
		this.grid[3][19].set_STRAIGHT_ROAD_E2E(this.grid[4][19]);
		this.grid[4][19].set_INTER_TURN_N2EN(this.grid[5][19], this.grid[4][20]);
		this.grid[5][19].set_STRAIGHT_ROAD_E2E(this.grid[6][19]);
		this.grid[6][19].set_STRAIGHT_ROAD_E2E(this.grid[7][19]);
		this.grid[7][19].set_STRAIGHT_ROAD_E2E(this.grid[8][19]);
		this.grid[8][19].set_INTER_TURN_E2SE(this.grid[8][18], this.grid[9][19]);
		this.grid[9][19].set_INTER_TURN_N2EN(this.grid[10][19], this.grid[9][20]);
		this.grid[10][19].set_STRAIGHT_ROAD_E2E(this.grid[11][19]);
		this.grid[11][19].set_STRAIGHT_ROAD_E2E(this.grid[12][19]);
		this.grid[12][19].set_STRAIGHT_ROAD_E2E(this.grid[13][19]);
		this.grid[13][19].set_STRAIGHT_ROAD_E2E(this.grid[14][19]);
		this.grid[14][19].set_STRAIGHT_ROAD_E2E(this.grid[15][19]);
		this.grid[15][19].set_STRAIGHT_ROAD_E2E(this.grid[16][19]);
		this.grid[16][19].set_STRAIGHT_ROAD_E2E(this.grid[17][19]);
		this.grid[17][19].set_INTER_TURN_E2SE(this.grid[17][18], this.grid[18][19]);
		this.grid[18][19].set_STRAIGHT_ROAD_N2N(this.grid[18][20]);
		/* row 18 */
		this.grid[1][18].set_STRAIGHT_ROAD_S2S(this.grid[1][17]);
		this.grid[2][18].set_STRAIGHT_ROAD_N2N(this.grid[2][19]);
		this.grid[8][18].set_STRAIGHT_ROAD_S2S(this.grid[8][17]);
		this.grid[9][18].set_STRAIGHT_ROAD_N2N(this.grid[9][19]);
		this.grid[17][18].set_STRAIGHT_ROAD_S2S(this.grid[17][17]);
		this.grid[18][18].set_STRAIGHT_ROAD_N2N(this.grid[18][19]);
		/* row 17 */
		this.grid[1][17].set_STRAIGHT_ROAD_S2S(this.grid[1][16]);
		this.grid[2][17].set_STRAIGHT_ROAD_N2N(this.grid[2][18]);
		this.grid[8][17].set_STRAIGHT_ROAD_S2S(this.grid[8][16]);
		this.grid[9][17].set_STRAIGHT_ROAD_N2N(this.grid[9][18]);
		this.grid[17][17].set_STRAIGHT_ROAD_S2S(this.grid[17][16]);
		this.grid[18][17].set_STRAIGHT_ROAD_N2N(this.grid[18][18]);
		/* row 16 */
		this.grid[1][16].set_STRAIGHT_ROAD_S2S(this.grid[1][15]);
		this.grid[2][16].set_STRAIGHT_ROAD_N2N(this.grid[2][17]);
		this.grid[8][16].set_STRAIGHT_ROAD_S2S(this.grid[8][15]);
		this.grid[9][16].set_STRAIGHT_ROAD_N2N(this.grid[9][17]);
		this.grid[17][16].set_STRAIGHT_ROAD_S2S(this.grid[17][15]);
		this.grid[18][16].set_STRAIGHT_ROAD_N2N(this.grid[18][17]);
		/* row 15 */
		this.grid[1][15].set_STRAIGHT_ROAD_S2S(this.grid[1][14]);
		this.grid[2][15].set_STRAIGHT_ROAD_N2N(this.grid[2][16]);
		this.grid[8][15].set_STRAIGHT_ROAD_S2S(this.grid[8][14]);
		this.grid[9][15].set_STRAIGHT_ROAD_N2N(this.grid[9][16]);
		this.grid[17][15].set_STRAIGHT_ROAD_S2S(this.grid[17][14]);
		this.grid[18][15].set_STRAIGHT_ROAD_N2N(this.grid[18][16]);
		/* row 14 */
		this.grid[1][14].set_STRAIGHT_ROAD_S2S(this.grid[1][13]);
		this.grid[2][14].set_INTER_TURN_W2NW(this.grid[2][15], this.grid[1][14]);
		this.grid[3][14].set_STRAIGHT_ROAD_W2W(this.grid[2][14]);
		this.grid[4][14].set_STRAIGHT_ROAD_W2W(this.grid[3][14]);
		this.grid[5][14].set_STRAIGHT_ROAD_W2W(this.grid[4][14]);
		this.grid[6][14].set_STRAIGHT_ROAD_W2W(this.grid[5][14]);
		this.grid[7][14].set_STRAIGHT_ROAD_W2W(this.grid[6][14]);
		this.grid[8][14].set_INTER_TURN_S2WS(this.grid[7][14], this.grid[8][13]);
		this.grid[9][14].set_INTER_TURN_W2NW(this.grid[9][15], this.grid[8][14]);
		this.grid[10][14].set_STRAIGHT_ROAD_W2W(this.grid[9][14]);
		this.grid[11][14].set_STRAIGHT_ROAD_W2W(this.grid[10][14]);
		this.grid[12][14].set_STRAIGHT_ROAD_W2W(this.grid[11][14]);
		this.grid[13][14].set_STRAIGHT_ROAD_W2W(this.grid[12][14]);
		this.grid[14][14].set_STRAIGHT_ROAD_W2W(this.grid[13][14]);
		this.grid[15][14].set_STRAIGHT_ROAD_W2W(this.grid[14][14]);
		this.grid[16][14].set_STRAIGHT_ROAD_W2W(this.grid[15][14]);
		this.grid[17][14].set_INTER_TURN_S2WS(this.grid[16][14], this.grid[17][13]);
		this.grid[18][14].set_INTER_TURN_W2NW(this.grid[18][15], this.grid[17][14]);
		this.grid[19][14].set_STRAIGHT_ROAD_W2W(this.grid[18][14]);
		this.grid[20][14].set_STRAIGHT_ROAD_W2W(this.grid[19][14]);
		this.grid[21][14].set_START_EEDGE2W(this.grid[20][14]);
		/* row 13 */
		this.grid[1][13].set_INTER_TURN_E2SE(this.grid[1][12], this.grid[2][13]);
		this.grid[2][13].set_INTER_TURN_N2EN(this.grid[3][13], this.grid[2][14]);
		this.grid[3][13].set_STRAIGHT_ROAD_E2E(this.grid[4][13]);
		this.grid[4][13].set_STRAIGHT_ROAD_E2E(this.grid[5][13]);
		this.grid[5][13].set_STRAIGHT_ROAD_E2E(this.grid[6][13]);
		this.grid[6][13].set_STRAIGHT_ROAD_E2E(this.grid[7][13]);
		this.grid[7][13].set_STRAIGHT_ROAD_E2E(this.grid[8][13]);
		this.grid[8][13].set_STRAIGHT_ROAD_E2E(this.grid[9][13]);
		this.grid[9][13].set_INTER_TURN_N2EN(this.grid[10][13], this.grid[9][14]);
		this.grid[10][13].set_STRAIGHT_ROAD_E2E(this.grid[11][13]);
		this.grid[11][13].set_STRAIGHT_ROAD_E2E(this.grid[12][13]);
		this.grid[12][13].set_STRAIGHT_ROAD_E2E(this.grid[13][13]);
		this.grid[13][13].set_STRAIGHT_ROAD_E2E(this.grid[14][13]);
		this.grid[14][13].set_STRAIGHT_ROAD_E2E(this.grid[15][13]);
		this.grid[15][13].set_STRAIGHT_ROAD_E2E(this.grid[16][13]);
		this.grid[16][13].set_STRAIGHT_ROAD_E2E(this.grid[17][13]);
		this.grid[17][13].set_INTER_TURN_E2SE(this.grid[17][12], this.grid[18][13]);
		this.grid[18][13].set_INTER_TURN_N2EN(this.grid[19][13], this.grid[18][14]);
		this.grid[19][13].set_STRAIGHT_ROAD_E2E(this.grid[20][13]);
		this.grid[20][13].set_STRAIGHT_ROAD_E2E(this.grid[21][13]);
		this.grid[21][13].set_END_E2EEDGE();
		/* row 12 */
		this.grid[1][12].set_STRAIGHT_ROAD_S2S(this.grid[1][11]);
		this.grid[2][12].set_STRAIGHT_ROAD_N2N(this.grid[2][13]);
		this.grid[17][12].set_STRAIGHT_ROAD_S2S(this.grid[17][11]);
		this.grid[18][12].set_STRAIGHT_ROAD_N2N(this.grid[18][13]);
		/* row 11 */
		this.grid[1][11].set_STRAIGHT_ROAD_S2S(this.grid[1][10]);
		this.grid[2][11].set_STRAIGHT_ROAD_N2N(this.grid[2][12]);
		this.grid[17][11].set_STRAIGHT_ROAD_S2S(this.grid[17][10]);
		this.grid[18][11].set_STRAIGHT_ROAD_N2N(this.grid[18][12]);
		/* row 10 */
		this.grid[1][10].set_STRAIGHT_ROAD_S2S(this.grid[1][9]);
		this.grid[2][10].set_STRAIGHT_ROAD_N2N(this.grid[2][11]);
		this.grid[17][10].set_STRAIGHT_ROAD_S2S(this.grid[17][9]);
		this.grid[18][10].set_STRAIGHT_ROAD_N2N(this.grid[18][11]);
		/* row 9 */
		this.grid[1][9].set_STRAIGHT_ROAD_S2S(this.grid[1][8]);
		this.grid[2][9].set_INTER_TURN_W2NW(this.grid[2][10], this.grid[1][9]);
		this.grid[3][9].set_STRAIGHT_ROAD_W2W(this.grid[2][9]);
		this.grid[4][9].set_STRAIGHT_ROAD_W2W(this.grid[3][9]);
		this.grid[5][9].set_STRAIGHT_ROAD_W2W(this.grid[4][9]);
		this.grid[6][9].set_STRAIGHT_ROAD_W2W(this.grid[5][9]);
		this.grid[7][9].set_STRAIGHT_ROAD_W2W(this.grid[6][9]);
		this.grid[8][9].set_STRAIGHT_ROAD_W2W(this.grid[7][9]);
		this.grid[9][9].set_STRAIGHT_ROAD_W2W(this.grid[8][9]);
		this.grid[10][9].set_STRAIGHT_ROAD_W2W(this.grid[9][9]);
		this.grid[11][9].set_STRAIGHT_ROAD_W2W(this.grid[10][9]);
		this.grid[12][9].set_INTER_TURN_S2WS(this.grid[11][9], this.grid[12][8]);
		this.grid[13][9].set_STRAIGHT_ROAD_W2W(this.grid[12][9]);
		this.grid[14][9].set_STRAIGHT_ROAD_W2W(this.grid[13][9]);
		this.grid[15][9].set_STRAIGHT_ROAD_W2W(this.grid[14][9]);
		this.grid[16][9].set_STRAIGHT_ROAD_W2W(this.grid[15][9]);
		this.grid[17][9].set_INTER_TURN_S2WS(this.grid[16][9], this.grid[17][8]);
		this.grid[18][9].set_INTER_TURN_W2NW(this.grid[18][10], this.grid[17][9]);
		this.grid[19][9].set_STRAIGHT_ROAD_W2W(this.grid[18][9]);
		this.grid[20][9].set_STRAIGHT_ROAD_W2W(this.grid[19][9]);
		this.grid[21][9].set_START_EEDGE2W(this.grid[20][9]);
		/* row 8 */
		this.grid[1][8].set_INTER_TURN_E2SE(this.grid[1][7], this.grid[2][8]);
		this.grid[2][8].set_INTER_TURN_N2EN(this.grid[3][8], this.grid[2][9]);
		this.grid[3][8].set_STRAIGHT_ROAD_E2E(this.grid[4][8]);
		this.grid[4][8].set_STRAIGHT_ROAD_E2E(this.grid[5][8]);
		this.grid[5][8].set_STRAIGHT_ROAD_E2E(this.grid[6][8]);
		this.grid[6][8].set_STRAIGHT_ROAD_E2E(this.grid[7][8]);
		this.grid[7][8].set_STRAIGHT_ROAD_E2E(this.grid[8][8]);
		this.grid[8][8].set_STRAIGHT_ROAD_E2E(this.grid[9][8]);
		this.grid[9][8].set_INTER_TURN_N2EN(this.grid[10][8], this.grid[9][9]);
		this.grid[10][8].set_STRAIGHT_ROAD_E2E(this.grid[11][8]);
		this.grid[11][8].set_STRAIGHT_ROAD_E2E(this.grid[12][8]);
		this.grid[12][8].set_INTER_TURN_E2SE(this.grid[12][7], this.grid[13][8]);
		this.grid[13][8].set_INTER_TURN_N2EN(this.grid[14][13], this.grid[13][9]);
		this.grid[14][8].set_STRAIGHT_ROAD_E2E(this.grid[15][8]);
		this.grid[15][8].set_STRAIGHT_ROAD_E2E(this.grid[16][8]);
		this.grid[16][8].set_STRAIGHT_ROAD_E2E(this.grid[17][8]);
		this.grid[17][8].set_STRAIGHT_ROAD_E2E(this.grid[18][8]);
		this.grid[18][8].set_INTER_TURN_N2EN(this.grid[19][8], this.grid[18][8]);
		this.grid[19][8].set_STRAIGHT_ROAD_E2E(this.grid[20][8]);
		this.grid[20][8].set_STRAIGHT_ROAD_E2E(this.grid[21][8]);
		this.grid[21][8].set_END_E2EEDGE();
		/* row 7 */
		this.grid[1][7].set_STRAIGHT_ROAD_S2S(this.grid[1][6]);
		this.grid[2][7].set_STRAIGHT_ROAD_N2N(this.grid[2][8]);
		this.grid[12][7].set_STRAIGHT_ROAD_S2S(this.grid[12][6]);
		this.grid[13][7].set_STRAIGHT_ROAD_N2N(this.grid[13][8]);
		/* row 6 */
		this.grid[1][6].set_STRAIGHT_ROAD_S2S(this.grid[1][5]);
		this.grid[2][6].set_STRAIGHT_ROAD_N2N(this.grid[2][7]);
		this.grid[12][6].set_STRAIGHT_ROAD_S2S(this.grid[12][5]);
		this.grid[13][6].set_INTER_TURN_W2NW(this.grid[13][7], this.grid[12][6]);
		this.grid[14][6].set_STRAIGHT_ROAD_E2E(this.grid[13][6]);
		this.grid[15][6].set_CORNER_ROAD_N2W(this.grid[14][6]);
		/* row 5 */
		this.grid[1][5].set_STRAIGHT_ROAD_S2S(this.grid[1][4]);
		this.grid[2][5].set_STRAIGHT_ROAD_N2N(this.grid[2][6]);
		this.grid[12][5].set_INTER_TURN_E2SE(this.grid[12][4], this.grid[13][5]);
		this.grid[13][5].set_INTER_TURN_N2EN(this.grid[14][5], this.grid[13][6]);
		this.grid[14][5].set_CORNER_ROAD_E2S(this.grid[14][4]);
		this.grid[15][5].set_STRAIGHT_ROAD_N2N(this.grid[15][6]);
		/* row 4 */
		this.grid[1][4].set_STRAIGHT_ROAD_S2S(this.grid[1][3]);
		this.grid[2][4].set_CORNER_ROAD_W2N(this.grid[2][5]);
		this.grid[3][4].set_STRAIGHT_ROAD_W2W(this.grid[2][4]);
		this.grid[4][4].set_STRAIGHT_ROAD_W2W(this.grid[3][4]);
		this.grid[5][4].set_STRAIGHT_ROAD_W2W(this.grid[4][4]);
		this.grid[6][4].set_INTER_TURN_S2WS(this.grid[5][4], this.grid[6][3]);
		this.grid[7][4].set_STRAIGHT_ROAD_W2W(this.grid[6][4]);
		this.grid[8][4].set_STRAIGHT_ROAD_W2W(this.grid[7][4]);
		this.grid[9][4].set_STRAIGHT_ROAD_W2W(this.grid[8][4]);
		this.grid[10][4].set_STRAIGHT_ROAD_W2W(this.grid[9][4]);
		this.grid[11][4].set_STRAIGHT_ROAD_W2W(this.grid[10][4]);
		this.grid[12][4].set_CORNER_ROAD_S2W(this.grid[11][4]);
		this.grid[13][4].set_STRAIGHT_ROAD_N2N(this.grid[13][5]);
		this.grid[14][4].set_STRAIGHT_ROAD_S2S(this.grid[13][3]);
		this.grid[15][4].set_STRAIGHT_ROAD_N2N(this.grid[15][5]);
		/* row 3 */
		this.grid[1][3].set_CORNER_ROAD_S2E(this.grid[2][3]);
		this.grid[2][3].set_STRAIGHT_ROAD_E2E(this.grid[3][3]);
		this.grid[3][3].set_STRAIGHT_ROAD_E2E(this.grid[4][3]);
		this.grid[4][3].set_STRAIGHT_ROAD_E2E(this.grid[5][3]);
		this.grid[5][3].set_STRAIGHT_ROAD_E2E(this.grid[6][3]);
		this.grid[6][3].set_INTER_TURN_E2SE(this.grid[6][2], this.grid[7][2]);
		this.grid[7][3].set_INTER_TURN_N2EN(this.grid[8][3], this.grid[7][4]);
		this.grid[8][3].set_STRAIGHT_ROAD_E2E(this.grid[9][3]);
		this.grid[9][3].set_STRAIGHT_ROAD_E2E(this.grid[10][3]);
		this.grid[10][3].set_STRAIGHT_ROAD_E2E(this.grid[11][3]);
		this.grid[11][3].set_STRAIGHT_ROAD_E2E(this.grid[12][3]);
		this.grid[12][3].set_STRAIGHT_ROAD_E2E(this.grid[13][3]);
		this.grid[13][3].set_CORNER_ROAD_E2N(this.grid[13][4]);
		this.grid[14][3].set_STRAIGHT_ROAD_S2S(this.grid[13][2]);
		this.grid[15][3].set_STRAIGHT_ROAD_N2N(this.grid[15][4]);
		/* row 2 */
		this.grid[6][2].set_STRAIGHT_ROAD_S2S(this.grid[6][1]);
		this.grid[7][2].set_STRAIGHT_ROAD_N2N(this.grid[7][3]);
		this.grid[14][2].set_STRAIGHT_ROAD_S2S(this.grid[14][1]);
		this.grid[15][2].set_STRAIGHT_ROAD_N2N(this.grid[15][3]);
		/* row 1 */
		this.grid[6][1].set_STRAIGHT_ROAD_S2S(this.grid[6][0]);
		this.grid[7][1].set_STRAIGHT_ROAD_N2N(this.grid[7][2]);
		this.grid[14][1].set_STRAIGHT_ROAD_S2S(this.grid[14][0]);
		this.grid[15][1].set_STRAIGHT_ROAD_N2N(this.grid[15][2]);
		/* row 0 */
		this.grid[6][0].set_END_E2EEDGE();
		this.grid[7][0].set_START_SEDGE2N(this.grid[7][1]);
		this.grid[14][0].set_END_E2EEDGE();
		this.grid[15][0].set_START_SEDGE2N(this.grid[15][1]);
	}
}
