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

module light1 (clk, rst, outN, outS, outE, outW, sensor_light, general_sensors, debug_port);
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

reg [7:0] count;
reg [2:0]outN;
reg [2:0]outS;
reg [2:0]outE;
reg [2:0]outW;

assign debug_port = count;

always @(posedge clk or negedge rst)
begin
	if (rst == 1'b0)
	begin
		count <= 7'd0;
		outN <= Go;
		outS <= Stop;
		outE <= Stop;
		outW <= Stop;
	end
	else
	begin
		count <= count + 1'b1;

		case (count)
			8'd0 :
			begin
				outN <= Stop;
				outE <= Go;
			end
			8'd64:
			begin
				outE <= Stop;
				outS <= Go;
			end
			8'd128:
			begin
				outS <= Stop;
				outW <= Go;
			end
			8'd192:
			begin
				outW <= Stop;
				outN <= Go;
			end
		endcase
	end
end

endmodule

