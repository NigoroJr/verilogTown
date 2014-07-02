package levelEditor;

/**
 * Class shared among grids used in order to keep track of the cursor entered
 * from. This approach is taken because mouseEntered() method in the
 * MouseListener interface has some delay before it's called, and it is hard to
 * determine the direction in which the cursor entered a grid. Also, it is more
 * convenient for keeping track of the dragging.
 * 
 * @author Naoki
 * 
 */
class StateTracker {
    private boolean isDragging;
    private int previouslyExitedFrom;

    public StateTracker() {
        isDragging = false;
        previouslyExitedFrom = MapGrid.NIL;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void clicked() {
        isDragging = true;
    }

    public void released() {
        isDragging = false;
    }

    public void setPreviouslyExitedFrom(int from) {
        previouslyExitedFrom = from;
    }

    /**
     * Get the direction it entered from depending on which direction the cursor
     * previously exited from. For example, if the cursor exited from the SOUTH
     * in the last grid, it means that the cursor entered this grid from the
     * NORTH.
     * 
     * @return The direction the cursor entered from.
     */
    public int getEnteredFrom() {
        int ret = MapGrid.NIL;
        switch (previouslyExitedFrom) {
            case MapGrid.NORTH:
                ret = MapGrid.SOUTH;
                break;
            case MapGrid.SOUTH:
                ret = MapGrid.NORTH;
                break;
            case MapGrid.EAST:
                ret = MapGrid.WEST;
                break;
            case MapGrid.WEST:
                ret = MapGrid.EAST;
                break;
        }
        return ret;
    }
}