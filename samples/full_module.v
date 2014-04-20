module sample (clk, rst, in_single, in_bus, out_single_r, out_bus_r, out_single_w, out_bus_w);
input clk;
input rst;
input in_single;
input [15:0]in_bus;

output out_single_r;
output [15:0]out_bus_r;
output out_single_w;
output [15:0]out_bus_w;

reg out_single_r;
reg [15:0]out_bus_r;
wire out_single_w;
wire [15:0]out_bus_w;

parameter SAMPLE = 2'b01,
	  SAMPLE2 = 2'd2,
	  SAMPLE3 = 2'h3;

reg [1:0] temp;

assign temp[0] = in_single;
assign temp[1] = (in_single == 1'b1) ? 1'b1 : 1'b0;

always @(*) // Combinational
	out_bus_r[0] = 1'b1;

always @(*) // Combinational
	if (in_single == 1'b1)
		out_bus_r[1] = 1'b1;
	else
		out_bus_r[1] = 1'b0;
	
always @(*) // Combinational
begin
	if (in_single == 1'b1)
		out_bus_r[2] = 1'b1;
	else
		out_bus_r[2] = 1'b0;
end

always @(*) // Combinational
begin
	if (in_single == 1'b1)
		out_bus_r[3] = 1'b1;
	else
		out_bus_r[3] = 1'b0;
	out_bus_r[4] = 1'b1;
end

always @(posedge clk or negedge rst)// Combinational
begin
	if (rst == 1'b0) // <= >= != || && ! 
	begin
		out_bus_r[15:0] <= 16'd0;
	end
	else
	begin
		case(in_bus)
			16'd0:
			begin
				out_bus_r[7:5] <= {in_bus[1:0], in_bus[0]} + out_bus_r[7:5];
			end
			16'd1:
				out_bus_r[8:5] <= {1'b1, in_bus[1:0], in_bus[0]} - out_bus_r[7:5]; // +, -, *, /, %, &&, &, |, ^, ~
			default: 		
				out_bus_r[9] <= 2'b00; 

			{out_bus_r[10], out_bus_r[12]} <= 2'b11;	
				
				
				
	end
end
	

endmodule

