package com.me.myverilogTown;

public enum GridType
{
    START_NEDGE2S, // means a start point on the north boarder so going south to
                   // STRAIGHT_ROAD_S
    START_SEDGE2N,
    START_EEDGE2W,
    START_WEDGE2E,

    END_S2SEDGE,
    END_N2NEDGE,
    END_W2WEDGE,
    END_E2EEDGE,

    STRAIGHT_ROAD_N2N,
    STRAIGHT_ROAD_S2S,
    STRAIGHT_ROAD_E2E,
    STRAIGHT_ROAD_W2W,

    /*
     * corner 1
     * ┏━━━
     * ┃a
     * ┃ b
     * ┃  ┏━
     * ┃  ┃
     */
    CORNER_ROAD_W2S, // a West then South
    CORNER_ROAD_N2E, // b
    /*
     * corner 2
     * ━━━━━┓
     *     b┃
     *    a ┃
     * ━━┓  ┃
     *   ┃  ┃
     * 
     */
    CORNER_ROAD_E2S, // a
    CORNER_ROAD_N2W, // b
    /*
     * corner 3
     * 
     *   ┃  ┃
     *   ┃  ┃
     * ━━┛  ┃
     *    a ┃
     *     b┃
     * ━━━━━┛
     * 
     */
    CORNER_ROAD_S2W, // a
    CORNER_ROAD_E2N, // b
    /*
     * corner 4
     * ┃  ┃
     * ┃  ┃
     * ┃  ┗━━
     * ┃  b
     * ┃a
     * ┗━━━━
     */
    CORNER_ROAD_S2E, // a
    CORNER_ROAD_W2N, // b

    /*
     * Full Intersection
     * 
     *   ┃  ┃
     * ━━┛  ┗━━
     *    a b
     *    c d
     * ━━┓  ┏━━
     *   ┃  ┃
     */
    INTER_TURN_N2EN, // d links to North and East
    INTER_TURN_W2NW, // b
    INTER_TURN_S2WS, // a
    INTER_TURN_E2SE, // c
    /*
     * Sample Partial Intersection - Note this is doable since all nodes only
     * point forward. If backwards have more cases
     * -ab-
     * -cd-
     * ||
     * b = STRAIGHT_ROAD_W
     * a = INTER_TURN_S2WS // special case since no in path, but doesn;t matter
     * c = INTER_TURN_E2SE
     * d = INTER_TURN_N2EN
     */

    NON_ROAD
}
