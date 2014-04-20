package com.me.myverilogTown;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrafficControl {
    private IntersectionType type = null;

    /* Grid which cars going north connects from */
    private GridNode goNorth = null;
    private GridNode goSouth = null;
    private GridNode goEast = null;
    private GridNode goWest = null;

    /* Traffic light for cars "facing north" */
    private TrafficSignalState facingNorth = TrafficSignalState.STOP;
    private TrafficSignalState facingSouth = TrafficSignalState.STOP;
    private TrafficSignalState facingEast = TrafficSignalState.STOP;
    private TrafficSignalState facingWest = TrafficSignalState.STOP;

    public static final int PROCEED_NORTH = 0;
    public static final int PROCEED_SOUTH = 1;
    public static final int PROCEED_EAST = 2;
    public static final int PROCEED_WEST = 3;

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
                return 90f;
            case PROCEED_WEST:
                return 270f;
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
    public void setNSEW(GridNode N, GridNode S, GridNode E, GridNode W) {

        type = IntersectionType.FOUR_WAY;

        this.goNorth = N;
        this.goSouth = S;
        this.goEast = E;
        this.goWest = W;

        goNorth.setTrafficControl(this, PROCEED_NORTH);
        goSouth.setTrafficControl(this, PROCEED_SOUTH);
        goEast.setTrafficControl(this, PROCEED_EAST);
        goWest.setTrafficControl(this, PROCEED_WEST);
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
    public void setNSE(GridNode N, GridNode S, GridNode E) {
        type = IntersectionType.THREE_WAY_NSE;

        this.goNorth = N;
        this.goSouth = S;
        this.goEast = E;

        goNorth.setTrafficControl(this, PROCEED_NORTH);
        goSouth.setTrafficControl(this, PROCEED_SOUTH);
        goEast.setTrafficControl(this, PROCEED_EAST);
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
    public void setSEW(GridNode S, GridNode E, GridNode W) {
        type = IntersectionType.THREE_WAY_SEW;

        this.goSouth = S;
        this.goEast = E;
        this.goWest = W;

        goSouth.setTrafficControl(this, PROCEED_SOUTH);
        goEast.setTrafficControl(this, PROCEED_EAST);
        goWest.setTrafficControl(this, PROCEED_WEST);
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
    public void setNSW(GridNode N, GridNode S, GridNode W) {
        type = IntersectionType.THREE_WAY_NSW;

        this.goNorth = N;
        this.goSouth = S;
        this.goWest = W;

        goNorth.setTrafficControl(this, PROCEED_NORTH);
        goSouth.setTrafficControl(this, PROCEED_SOUTH);
        goWest.setTrafficControl(this, PROCEED_WEST);
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
    public void setNEW(GridNode N, GridNode E, GridNode W) {
        type = IntersectionType.THREE_WAY_NEW;

        this.goNorth = N;
        this.goWest = W;
        this.goEast = E;

        goNorth.setTrafficControl(this, PROCEED_NORTH);
        goEast.setTrafficControl(this, PROCEED_EAST);
        goWest.setTrafficControl(this, PROCEED_WEST);
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
}
