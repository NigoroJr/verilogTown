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

public enum IntersectionType
{
	/** The three characters proceeding THREE_WAY represents where car is headed
	 * when entering the intersection.
	 * 
	 * <pre>
	 * <code>
	 *    | v |   |
	 *    +---+---+---
	 *    |   |   | <
	 *    +---+---+---
	 *    |   |   |
	 *    +---+---+---
	 *    |   | ^ |
	 * </code>
	 * </pre>
	 * 
	 * In this case, the type of this intersection is THREE_WAY_NSW, since cars
	 * entering this intersection are headed north, south, and west as shown
	 * with the arrows. */
	FOUR_WAY, THREE_WAY_NSE, THREE_WAY_SEW, THREE_WAY_NSW, THREE_WAY_NEW
}
