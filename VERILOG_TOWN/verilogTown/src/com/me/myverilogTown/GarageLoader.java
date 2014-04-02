package com.me.myverilogTown;

import com.badlogic.gdx.graphics.g2d.Sprite;

/* 
 * Class used to simplify loading all of a levelLogic's cars into arrays and into
 * a Sprite array for graphics
 */

public class GarageLoader {
	
	private Car[] garage;
	private Sprite[] graphicsGarage;
	
	public GarageLoader(LevelLogic logic){
		garage = logic.getCars();
		for(int i = 0 ; i < garage.length; i++)
			graphicsGarage[i] = garage[i].getCarSprite();
	}

	public Car[] getGarage() {
		return garage;
	}

	public void setGarage(Car[] garage) {
		this.garage = garage;
	}

	public Sprite[] getGraphicsGarage() {
		return graphicsGarage;
	}
	
	public void setGraphicsGarage(Sprite[] graphicsGarage) {
		this.graphicsGarage = graphicsGarage;
	}

}
