package com.me.myverilogTown;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreAndTimeTexture
{
	private Texture		numbers_chiller[];
	private Texture		colon_chiller;
	private int			time;
	private int			gapTime;
	private int			gapScore;
	private int			size;
	private SpriteBatch	batch;

	public ScoreAndTimeTexture(
			SpriteBatch batch,
			int gapTime,
			int gapScore,
			int size)
	{
		numbers_chiller = new Texture[10];
		for (int i = 0; i < 10; i++)
			numbers_chiller[i] = new Texture("data/chiller_num_" + i + ".png");
		colon_chiller = new Texture("data/chiller_colon.png");
		this.gapTime = gapTime;
		this.gapScore = gapScore;
		this.size = size;
		this.batch = batch;
	}

	public void drawTime(float x, float y, double time)
	{
		batch.draw(numbers_chiller[(int) (time / 60) / 10], x, y, size, size);
		batch.draw(numbers_chiller[(int) (time / 60) % 10], x + gapTime, y, size, size);
		batch.draw(colon_chiller, (float) (x + 1.77 * gapTime), y, size, size);
		batch.draw(numbers_chiller[(int) (time % 60) / 10], (float) (x + 2.54 * gapTime), y, size, size);
		batch.draw(numbers_chiller[(int) (time % 60) % 10], (float) (x + 3.54 * gapTime), y, size, size);
	}

	public void drawScore(float x, float y, int score, int digit)
	{
		if (digit == 2)
		{
			batch.draw(numbers_chiller[(int) (score % 100) / 10], x, y, size, size);
			batch.draw(numbers_chiller[(int) (score % 10)], x + gapScore, y, size, size);
		}
		else if (digit == 3)
		{
			batch.draw(numbers_chiller[(int) (score % 1000) / 100], x, y, size, size);
			batch.draw(numbers_chiller[(int) (score % 100) / 10], x + gapScore, y, size, size);
			batch.draw(numbers_chiller[(int) (score % 10)], x + 2 * gapScore, y, size, size);
		}
	}
	
	public void dispose(){
		for(int j = 0; j < 10; j++){
			numbers_chiller[j].dispose();
		}
		colon_chiller.dispose();
	}
}
