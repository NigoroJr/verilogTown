package com.me.myverilogTown;

public class verilogTownMap 
{
	/* Assume grid size is 1 larger on all edges to accomodate invisible starting points */
	private int grid_x;
	private int grid_y; 
	private verilogTownGridNode grid[][];

	/* Constructor */
	verilogTownMap(int size_x, int size_y) 
	{
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

	/* hard code initialization of firts_map.png */
	void verilogTownMapHardCode()
	{
		/* Tiles are 64x64 */
		/* Map is 20x20 */
		/* top row */
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



	
		
	}
}
