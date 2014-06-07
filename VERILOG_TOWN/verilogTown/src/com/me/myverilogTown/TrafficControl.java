package com.me.myverilogTown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TrafficControl {
    private IntersectionType type = null;

    /* Grid which cars going north connects from */
    private GridNode goNorth = null;
    private GridNode goSouth = null;
    private GridNode goEast = null;
    private GridNode goWest = null;
    private GridNode internalUpperLeft = null;
    private GridNode internalUpperRight = null;
    private GridNode internalLowerLeft = null;
    private GridNode internalLowerRight = null;

    /* Traffic light for cars "facing north" */
    private TrafficSignalState facingNorth = TrafficSignalState.STOP;
    private TrafficSignalState facingSouth = TrafficSignalState.STOP;
    private TrafficSignalState facingEast = TrafficSignalState.STOP;
    private TrafficSignalState facingWest = TrafficSignalState.STOP;

    public static final int PROCEED_NORTH = 0;
    public static final int PROCEED_SOUTH = 1;
    public static final int PROCEED_EAST = 2;
    public static final int PROCEED_WEST = 3;
    public static final int INTERNAL = 4;
    
    public void render_signal(TrafficSignalState signal, GridNode grid,
            float rot, SpriteBatch batch, Texture stop, Texture go,
            Texture left, Texture right, Texture forward) {
        // TODO: define grid size = 64 px
        int x = (grid.getX() - 1) * 64;
        int y = (grid.getY() - 1) * 64;
        Texture texture = null;
        switch (signal) {
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
        batch.draw(texture, x, y, 32, 32, 64, 64, 0.7f, 0.7f, rot, 0, 0,
                64, 64, false, false);
    }

    public float rotate(int dir) {
        switch (dir) {
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

    public void render_traffic_signal(SpriteBatch batch, Texture stop,
            Texture go, Texture left, Texture right, Texture forward) {
        if (goNorth != null)
            render_signal(facingNorth, goNorth, rotate(PROCEED_NORTH),
                    batch, stop, go, left, right, forward);
        if (goSouth != null)
            render_signal(facingSouth, goSouth, rotate(PROCEED_SOUTH),
                    batch, stop, go, left, right, forward);
        if (goEast != null)
            render_signal(facingEast, goEast, rotate(PROCEED_EAST),
                    batch, stop, go, left, right, forward);
        if (goWest != null)
            render_signal(facingWest, goWest, rotate(PROCEED_WEST),
                    batch, stop, go, left, right, forward);
    }
    
    public void render_highlighted_stop(SpriteBatch batch, Texture stop_highlighted){
    	 if (goNorth != null){
    		 batch.draw(stop_highlighted, (goNorth.getX() - 1) * 64, (goNorth.getY() - 1) * 64, 
    				 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false); 
    	 }
         if (goSouth != null){
        	 batch.draw(stop_highlighted, (goSouth.getX() - 1) * 64, (goSouth.getY() - 1) * 64, 
    				 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false); 
         }
         if (goEast != null){
        	 batch.draw(stop_highlighted, (goEast.getX() - 1) * 64, (goEast.getY() - 1) * 64, 
    				 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false); 
         }
         if (goWest != null){
        	 batch.draw(stop_highlighted, (goWest.getX() - 1) * 64, (goWest.getY() - 1) * 64, 
    				 32, 32, 64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false); 
         }
    }
        
        
       /* button.addListener(new ClickListener(){
        	@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
        		Gdx.app.log("button is pressed", "button is pressed");
				return true;
			}
        });*/	
    		

    /**
     * Sets the nodes surrounding this traffic light.
     * 
     * @param N
     *            The node that north-bound cars are from.
     * @param S
     *            The node that south-bound cars are from.
     * @param E
     *            The node that east-bound cars are from.
     * @param W
     *            The node that west-bound cars are from.
     */
    public void setNSEW(GridNode N, GridNode S, GridNode E, GridNode W, 
    					GridNode IUL, GridNode IUR, GridNode ILL, GridNode ILR){

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

    /**
     * Sets the nodes surrounding this traffic light.
     * 
     * @param N
     *            The node that north-bound cars are from.
     * @param S
     *            The node that south-bound cars are from.
     * @param E
     *            The node that east-bound cars are from.
     */
    public void setNSE(GridNode N, GridNode S, GridNode E,
    				GridNode IUL, GridNode IUR, GridNode ILL, GridNode ILR) {
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

    /**
     * Sets the nodes surrounding this traffic light.
     * 
     * @param S
     *            The node that south-bound cars are from.
     * @param E
     *            The node that east-bound cars are from.
     * @param W
     *            The node that west-bound cars are from.
     */
    public void setSEW(GridNode S, GridNode E, GridNode W,
    				GridNode IUL, GridNode IUR, GridNode ILL, GridNode ILR) {
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

    /**
     * Sets the nodes surrounding this traffic light.
     * 
     * @param N
     *            The node that north-bound cars are from.
     * @param S
     *            The node that sound-bound cars are from.
     * @param W
     *            The node that west-bound cars are from.
     */
    public void setNSW(GridNode N, GridNode S, GridNode W,
    		GridNode IUL, GridNode IUR, GridNode ILL, GridNode ILR) {
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

    /**
     * Sets the nodes surrounding this traffic light.
     * 
     * @param N
     *            The node that north-bound cars are from.
     * @param E
     *            The node that east-bound cars are from.
     * @param W
     *            The node that west-bound cars are from.
     */
    public void setNEW(GridNode N, GridNode E, GridNode W,
    		GridNode IUL, GridNode IUR, GridNode ILL, GridNode ILR) {
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

    /**
     * Returns the state of the traffic light the car will see when entering
     * this intersection from the given direction.
     * 
     * @param direction
     *            The direction which the car is entering from.
     * @return The state of the traffic signal.
     */
    public TrafficSignalState getSignalWhen(int direction) {
        switch (direction) {
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

    /**
     * Sets the status of the traffic signals.
     * 
     * @param northSignal
     *            The status of the signal for cars going north.
     * @param southSignal
     *            The status of the signal for cars going south.
     * @param eastSignal
     *            The status of the signal for cars going east.
     * @param westSignal
     *            The status of the signal for cars going west.
     */
    public void setSignalsStatus(
            TrafficSignalState northSignal,
            TrafficSignalState southSignal,
            TrafficSignalState eastSignal,
            TrafficSignalState westSignal) {
        this.facingNorth = northSignal;
        this.facingSouth = southSignal;
        this.facingEast = eastSignal;
        this.facingWest = westSignal;
    }

    public IntersectionType getIntersectionType() {
        return type;
    }
    
    public int getX(){
    	if(goNorth != null){
    		return (goNorth.getX()-2)*64+41;
    	}
    	else if(goSouth != null){
    		return (goSouth.getX()-1)*64+41;
    	}
    	else
    		return 0;
    }
    
    public int getY(){
    	if(goNorth != null){
    		return (goNorth.getY()*64)+32;
    	}
    	else if(goSouth != null){
    		return (goSouth.getY()-3)*64+41;
    	}
    	else
    		return 0;
    }
    
    
 
		/*batch.draw(intersection, ((goNorth.getX()-2)*64)+41, (goNorth.getY()*64)+32, 0, 0, 
				64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);*/

		/*batch.draw(intersection, (goSouth.getX()-1)*64+41, (goSouth.getY()-3)*64+41, 0, 0, 
		64, 64, 0.7f, 0.7f, 0f, 0, 0, 64, 64, false, false);*/

}
