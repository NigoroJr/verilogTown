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

package com.me.myverilogTown;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrafficControl
{
	private IntersectionType	type				= null;

	/* Grid which cars going north connects from */
	private GridNode			goNorth				= null;
	private GridNode			goSouth				= null;
	private GridNode			goEast				= null;
	private GridNode			goWest				= null;
	private GridNode			internalUpperLeft	= null;
	private GridNode			internalUpperRight	= null;
	private GridNode			internalLowerLeft	= null;
	private GridNode			internalLowerRight	= null;

	/* Traffic light for cars "facing north" */
	private TrafficSignalState	facingNorth			= TrafficSignalState.STOP;
	private TrafficSignalState	facingSouth			= TrafficSignalState.STOP;
	private TrafficSignalState	facingEast			= TrafficSignalState.STOP;
	private TrafficSignalState	facingWest			= TrafficSignalState.STOP;

	public static final int		PROCEED_NORTH		= 0;
	public static final int		PROCEED_SOUTH		= 1;
	public static final int		PROCEED_EAST		= 2;
	public static final int		PROCEED_WEST		= 3;
	public static final int		INTERNAL			= 4;

	public void render_signal(
			TrafficSignalState signal,
			GridNode grid,
			float rot,
			SpriteBatch batch,
			Texture stop,
			Texture go,
			Texture left,
			Texture right,
			Texture forward)
	{
		// TODO: define grid size = 64 px
		int x = (grid.getX() - 1) * 64;
		int y = (grid.getY() - 1) * 64;
		Texture texture = null;
		switch (signal)
		{
			case GO:
				texture = go;
			break;
			case GO_FORWARD:
				texture = forward;
			break;
			case GO_LEFT:
				texture = left;
			break;
			case GO_RIGHT:
				texture = right;
			break;
			case STOP:
				texture = stop;
			break;
			default:
				texture = stop;
		}
		batch.draw(texture, x, y, 32, 32, 64, 64, 0.7f, 0.7f, rot, 0, 0, 64, 64, false, false);
	}

	public float rotate(int dir)
	{
		switch (dir)
		{
			case PROCEED_NORTH:
				return 0f;
			case PROCEED_SOUTH:
				return 180f;
			case PROCEED_EAST:
				return 270;
			case PROCEED_WEST:
				return 90;
			default:
				return -45f;
		}
	}

	public void render_traffic_signal(
			SpriteBatch batch,
			Texture stop,
			Texture go,
			Texture left,
			Texture right,
			Texture forward)
	{
		if (goNorth != null)
			render_signal(facingNorth, goNorth, rotate(PROCEED_NORTH), batch, stop, go, left, right, forward);
		if (goSouth != null)
			render_signal(facingSouth, goSouth, rotate(PROCEED_SOUTH), batch, stop, go, left, right, forward);
		if (goEast != null)
			render_signal(facingEast, goEast, rotate(PROCEED_EAST), batch, stop, go, left, right, forward);
		if (goWest != null)
			render_signal(facingWest, goWest, rotate(PROCEED_WEST), batch, stop, go, left, right, forward);
	}

	public void render_highlighted_stop(
			SpriteBatch batch,
			Texture stop_highlighted)
	{
		if (goNorth != null)
		{
			batch.draw(stop_highlighted, (goNorth.getX() - 1) * 64, (goNorth.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
		if (goSouth != null)
		{
			batch.draw(stop_highlighted, (goSouth.getX() - 1) * 64, (goSouth.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
		if (goEast != null)
		{
			batch.draw(stop_highlighted, (goEast.getX() - 1) * 64, (goEast.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
		if (goWest != null)
		{
			batch.draw(stop_highlighted, (goWest.getX() - 1) * 64, (goWest.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
	}

	public void render_complied_failed_stop(
			SpriteBatch batch,
			Texture stop_complied_failed)
	{
		if (goNorth != null)
		{
			batch.draw(stop_complied_failed, (goNorth.getX() - 1) * 64, (goNorth.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
		if (goSouth != null)
		{
			batch.draw(stop_complied_failed, (goSouth.getX() - 1) * 64, (goSouth.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
		if (goEast != null)
		{
			batch.draw(stop_complied_failed, (goEast.getX() - 1) * 64, (goEast.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
		if (goWest != null)
		{
			batch.draw(stop_complied_failed, (goWest.getX() - 1) * 64, (goWest.getY() - 1) * 64, 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);
		}
	}

	/** Sets the nodes surrounding this traffic light.
	 * 
	 * <pre>
	 * <code>
	 *    | 4 |   |
	 * ---+---+---+---
	 *    | 0 | 1 | 7
	 * ---+---+---+---
	 *  5 | 2 | 3 |
	 * ---+---+---+---
	 *    |   | 6 |
	 * </code>
	 * </pre>
	 * 
	 * @param N
	 *            (6) The node that north-bound cars are from.
	 * @param S
	 *            (4) The node that south-bound cars are from.
	 * @param E
	 *            (5) The node that east-bound cars are from.
	 * @param W
	 *            (7) The node that west-bound cars are from.
	 * @param IUL
	 *            (0) The upper-left node in the intersection.
	 * @param IUR
	 *            (1) The upper-right node in the intersection.
	 * @param ILL
	 *            (2) The lower-left node in the intersection.
	 * @param ILR
	 *            (3) The lower-left node in the intersection. */
	public void setNSEW(
			GridNode N,
			GridNode S,
			GridNode E,
			GridNode W,
			GridNode IUL,
			GridNode IUR,
			GridNode ILL,
			GridNode ILR)
	{

		type = IntersectionType.FOUR_WAY;

		this.goNorth = N;
		this.goSouth = S;
		this.goEast = E;
		this.goWest = W;
		this.internalUpperLeft = IUL;
		this.internalUpperRight = IUR;
		this.internalLowerLeft = ILL;
		this.internalLowerRight = ILR;

		goNorth.setTrafficControl(this, PROCEED_NORTH);
		goSouth.setTrafficControl(this, PROCEED_SOUTH);
		goEast.setTrafficControl(this, PROCEED_EAST);
		goWest.setTrafficControl(this, PROCEED_WEST);
		internalUpperLeft.setTrafficControl(this, INTERNAL);
		internalUpperRight.setTrafficControl(this, INTERNAL);
		internalLowerLeft.setTrafficControl(this, INTERNAL);
		internalLowerRight.setTrafficControl(this, INTERNAL);
	}

	/** Sets the nodes surrounding this traffic light.
	 * 
	 * <pre>
	 * <code>
	 *    | 4 |   |
	 * ---+---+---+---
	 *    | 0 | 1 | 7
	 * ---+---+---+---
	 *  5 | 2 | 3 |
	 * ---+---+---+---
	 *    |   | 6 |
	 * </code>
	 * </pre>
	 * 
	 * @param N
	 *            (6) The node that north-bound cars are from.
	 * @param S
	 *            (4) The node that south-bound cars are from.
	 * @param E
	 *            (5) The node that east-bound cars are from.
	 * @param IUL
	 *            (0) The upper-left node in the intersection.
	 * @param IUR
	 *            (1) The upper-right node in the intersection.
	 * @param ILL
	 *            (2) The lower-left node in the intersection.
	 * @param ILR
	 *            (3) The lower-left node in the intersection. */
	public void setNSE(
			GridNode N,
			GridNode S,
			GridNode E,
			GridNode IUL,
			GridNode IUR,
			GridNode ILL,
			GridNode ILR)
	{
		type = IntersectionType.THREE_WAY_NSE;

		this.goNorth = N;
		this.goSouth = S;
		this.goEast = E;
		this.internalUpperLeft = IUL;
		this.internalUpperRight = IUR;
		this.internalLowerLeft = ILL;
		this.internalLowerRight = ILR;

		goNorth.setTrafficControl(this, PROCEED_NORTH);
		goSouth.setTrafficControl(this, PROCEED_SOUTH);
		goEast.setTrafficControl(this, PROCEED_EAST);
		internalUpperLeft.setTrafficControl(this, INTERNAL);
		internalUpperRight.setTrafficControl(this, INTERNAL);
		internalLowerLeft.setTrafficControl(this, INTERNAL);
		internalLowerRight.setTrafficControl(this, INTERNAL);
	}

	/** Sets the nodes surrounding this traffic light.
	 * 
	 * <pre>
	 * <code>
	 *    | 4 |   |
	 * ---+---+---+---
	 *    | 0 | 1 | 7
	 * ---+---+---+---
	 *  5 | 2 | 3 |
	 * ---+---+---+---
	 *    |   | 6 |
	 * </code>
	 * </pre>
	 * 
	 * @param S
	 *            (4) The node that south-bound cars are from.
	 * @param E
	 *            (5) The node that east-bound cars are from.
	 * @param W
	 *            (7) The node that west-bound cars are from.
	 * @param IUL
	 *            (0) The upper-left node in the intersection.
	 * @param IUR
	 *            (1) The upper-right node in the intersection.
	 * @param ILL
	 *            (2) The lower-left node in the intersection.
	 * @param ILR
	 *            (3) The lower-left node in the intersection. */
	public void setSEW(
			GridNode S,
			GridNode E,
			GridNode W,
			GridNode IUL,
			GridNode IUR,
			GridNode ILL,
			GridNode ILR)
	{
		type = IntersectionType.THREE_WAY_SEW;

		this.goSouth = S;
		this.goEast = E;
		this.goWest = W;
		this.internalUpperLeft = IUL;
		this.internalUpperRight = IUR;
		this.internalLowerLeft = ILL;
		this.internalLowerRight = ILR;

		goSouth.setTrafficControl(this, PROCEED_SOUTH);
		goEast.setTrafficControl(this, PROCEED_EAST);
		goWest.setTrafficControl(this, PROCEED_WEST);
		internalUpperLeft.setTrafficControl(this, INTERNAL);
		internalUpperRight.setTrafficControl(this, INTERNAL);
		internalLowerLeft.setTrafficControl(this, INTERNAL);
		internalLowerRight.setTrafficControl(this, INTERNAL);
	}

	/** Sets the nodes surrounding this traffic light.
	 * 
	 * <pre>
	 * <code>
	 *    | 4 |   |
	 * ---+---+---+---
	 *    | 0 | 1 | 7
	 * ---+---+---+---
	 *  5 | 2 | 3 |
	 * ---+---+---+---
	 *    |   | 6 |
	 * </code>
	 * </pre>
	 * 
	 * @param N
	 *            (6) The node that north-bound cars are from.
	 * @param S
	 *            (4) The node that south-bound cars are from.
	 * @param W
	 *            (7) The node that west-bound cars are from.
	 * @param IUL
	 *            (0) The upper-left node in the intersection.
	 * @param IUR
	 *            (1) The upper-right node in the intersection.
	 * @param ILL
	 *            (2) The lower-left node in the intersection.
	 * @param ILR
	 *            (3) The lower-left node in the intersection. */
	public void setNSW(
			GridNode N,
			GridNode S,
			GridNode W,
			GridNode IUL,
			GridNode IUR,
			GridNode ILL,
			GridNode ILR)
	{
		type = IntersectionType.THREE_WAY_NSW;

		this.goNorth = N;
		this.goSouth = S;
		this.goWest = W;
		this.internalUpperLeft = IUL;
		this.internalUpperRight = IUR;
		this.internalLowerLeft = ILL;
		this.internalLowerRight = ILR;

		goNorth.setTrafficControl(this, PROCEED_NORTH);
		goSouth.setTrafficControl(this, PROCEED_SOUTH);
		goWest.setTrafficControl(this, PROCEED_WEST);
		internalUpperLeft.setTrafficControl(this, INTERNAL);
		internalUpperRight.setTrafficControl(this, INTERNAL);
		internalLowerLeft.setTrafficControl(this, INTERNAL);
		internalLowerRight.setTrafficControl(this, INTERNAL);
	}

	/** Sets the nodes surrounding this traffic light.
	 * 
	 * <pre>
	 * <code>
	 *    | 4 |   |
	 * ---+---+---+---
	 *    | 0 | 1 | 7
	 * ---+---+---+---
	 *  5 | 2 | 3 |
	 * ---+---+---+---
	 *    |   | 6 |
	 * </code>
	 * </pre>
	 * 
	 * @param N
	 *            (6) The node that north-bound cars are from.
	 * @param E
	 *            (5) The node that east-bound cars are from.
	 * @param W
	 *            (7) The node that west-bound cars are from.
	 * @param IUL
	 *            (0) The upper-left node in the intersection.
	 * @param IUR
	 *            (1) The upper-right node in the intersection.
	 * @param ILL
	 *            (2) The lower-left node in the intersection.
	 * @param ILR
	 *            (3) The lower-left node in the intersection. */
	public void setNEW(
			GridNode N,
			GridNode E,
			GridNode W,
			GridNode IUL,
			GridNode IUR,
			GridNode ILL,
			GridNode ILR)
	{
		type = IntersectionType.THREE_WAY_NEW;

		this.goNorth = N;
		this.goWest = W;
		this.goEast = E;
		this.internalUpperLeft = IUL;
		this.internalUpperRight = IUR;
		this.internalLowerLeft = ILL;
		this.internalLowerRight = ILR;

		goNorth.setTrafficControl(this, PROCEED_NORTH);
		goEast.setTrafficControl(this, PROCEED_EAST);
		goWest.setTrafficControl(this, PROCEED_WEST);
		internalUpperLeft.setTrafficControl(this, INTERNAL);
		internalUpperRight.setTrafficControl(this, INTERNAL);
		internalLowerLeft.setTrafficControl(this, INTERNAL);
		internalLowerRight.setTrafficControl(this, INTERNAL);
	}

	/** Returns the state of the traffic light the car will see when entering
	 * this intersection from the given direction.
	 * 
	 * @param direction
	 *            The direction which the car is entering from.
	 * @return The state of the traffic signal. */
	public TrafficSignalState getSignalWhen(int direction)
	{
		switch (direction)
		{
			case PROCEED_NORTH:
				return facingNorth;
			case PROCEED_SOUTH:
				return facingSouth;
			case PROCEED_EAST:
				return facingEast;
			case PROCEED_WEST:
				return facingWest;
			default:
				return TrafficSignalState.NO_SIGNAL;
		}
	}

	/** Sets the status of the traffic signals.
	 * 
	 * @param northSignal
	 *            The status of the signal for cars going north.
	 * @param southSignal
	 *            The status of the signal for cars going south.
	 * @param eastSignal
	 *            The status of the signal for cars going east.
	 * @param westSignal
	 *            The status of the signal for cars going west. */
	public void setSignalsStatus(
			TrafficSignalState northSignal,
			TrafficSignalState southSignal,
			TrafficSignalState eastSignal,
			TrafficSignalState westSignal)
	{
		this.facingNorth = northSignal;
		this.facingSouth = southSignal;
		this.facingEast = eastSignal;
		this.facingWest = westSignal;
	}

	public IntersectionType getIntersectionType()
	{
		return type;
	}

	public int getX()
	{
		if (goNorth != null)
		{
			return (goNorth.getX() - 2) * 64 + 41;
		}
		else if (goSouth != null)
		{
			return (goSouth.getX() - 1) * 64 + 41;
		}
		else
		{
			return 0;
		}
	}

	public int getY()
	{
		if (goNorth != null)
		{
			return (goNorth.getY() * 64) + 32;
		}
		else if (goSouth != null)
		{
			return (goSouth.getY() - 3) * 64 + 41;
		}
		else
		{
			return 0;
		}
	}

	/* Index order where 0 is the last element in string 7 is MSB and 0 is LSB 4
	 * 017 523 6 */
	public String read_traffic_signal()
	{
		String return_string = "";

		if (goWest != null && goWest.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (goNorth != null && goNorth.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (goEast != null && goEast.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (goSouth != null && goSouth.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (internalLowerRight != null && internalLowerRight.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (internalLowerLeft != null && internalLowerLeft.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (internalUpperRight != null && internalUpperRight.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}
		if (internalUpperLeft != null && internalUpperLeft.getCar() != null)
		{
			return_string = return_string + "1";
		}
		else
		{
			return_string = return_string + "0";
		}

		return return_string;
	}

	/* batch.draw(intersection, ((goNorth.getX()-2)*64)+41,
	 * (goNorth.getY()*64)+32, 0, 0, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64,
	 * false, false); */

	/* batch.draw(intersection, (goSouth.getX()-1)*64+41,
	 * (goSouth.getY()-3)*64+41, 0, 0, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64,
	 * false, false); */

}
