module light1 (clk, rst, outN, outS, outE, outW, sensor_light, general_sensors);
input clk, rst;

output [29:0]debug_port;

output [2:0]outN; /* For cars going north - idx 8 (see below) */
output [2:0]outS; /* For cars going south - idx 4 (see below) */
output [2:0]outE; /* For cars going east - idx 5 (see below) */
output [2:0]outW; /* For cars going west - idx 7 (see below) */

/* Assuming this is the light on the map, here is the index of the sensor in the light.  = 1 when a car is there	 

	 4
	 017
	523
	  6

	  */
input [7:0] sensor_light; 
input [29:0] general_sensors; /* a level has a max of 30 user placed sensors */

/* Stop = 3'b000, Forward_only = 3'b001, Left_only = 3'b010, Right_only = 3'b011, Go = 3'b100 */
parameter Stop = 3'b000,
	  Forward_only = 3'b001,
	  Left_only = 3'b010,
	  Right_only = 3'b011,
	  Go = 3'b100;

reg [2:0]outN;
reg [2:0]outS;
reg [2:0]outE;
reg [2:0]outW;

assign debug_port = sensor_light;

always @(*)
begin
	/* Check if anyone is in the center of the light (as in currently moving through the intersection */
	if ((sensor_light[0] == 1'b0) && (sensor_light[1] == 1'b0) && (sensor_light[2] == 1'b0) && (sensor_light[3] == 1'b0))
	begin
		if (sensor_light[6] == 1'b1)
		begin
			/* If sensor going N has a car then let them through */
			outN = Go;
			outS = Stop;
			outE = Stop;
			outW = Stop;
		end
		else if (sensor_light[4] == 1'b1)
		begin
			outN = Stop;
			outS = Go;
			outE = Stop;
			outW = Stop;
		end
		else if (sensor_light[5] == 1'b1)
		begin
			outN = Stop;
			outS = Stop;
			outE = Go;
			outW = Stop;
		end
		else if (sensor_light[7] == 1'b1)
		begin
			outN = Stop;
			outS = Stop;
			outE = Stop;
			outW = Go;
		end
		else
		begin
			outN = Stop;
			outS = Stop;
			outE = Stop;
			outW = Stop;
		end
	end
	else
	begin
		outN = Stop;
		outS = Stop;
		outE = Stop;
		outW = Stop;
	end
end


endmodule
