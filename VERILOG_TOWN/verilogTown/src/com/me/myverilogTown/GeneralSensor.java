package com.me.myverilogTown;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GeneralSensor
{
	private Texture		sensorTexture;
	private int			gridX;
	private int			gridY;
	private SpriteBatch	batch;
	private GridNode	gridNode;
	private int			number;

	public GeneralSensor(SpriteBatch batch, int number, int x, int y)
	{
		this.batch = batch;
		this.number = number;
		this.sensorTexture = new Texture("data/sensor" + number + ".png");
		this.gridX = x;
		this.gridY = y;
	}

	public void setXY(int x, int y)
	{
		this.gridX = x;
		this.gridY = y;
	}

	public int getX()
	{
		return this.gridX;
	}

	public int getY()
	{
		return this.gridY;
	}

	public void setGridNode(GridNode gridNode)
	{
		this.gridNode = gridNode;
	}

	public GridNode getGridNode()
	{
		return this.gridNode;
	}

	public void draw()
	{
		batch.draw(sensorTexture, 64 * (gridX - 1), 64 * (gridY - 1));
	}

	public void drawUnused()
	{
		batch.draw(sensorTexture, 64 * gridX, 64 * gridY);
	}

	public int getNumber()
	{
		return number;
	}

	public Texture getTexture()
	{
		return sensorTexture;
	}

	public String readSensorInfo()
	{
		String temp = "";
		if (this.gridNode == null || this.gridNode.getCar() == null)
			temp = "0000";
		else if (this.gridNode != null && this.gridNode.getCar() != null)
		{
			temp = "00" + Integer.toBinaryString(this.gridNode.getCar().get_end_point().getEndingCounter());
			temp = temp.substring(temp.length() - 3);
			temp = "1" + temp;
		}
		return temp;
	}
}
