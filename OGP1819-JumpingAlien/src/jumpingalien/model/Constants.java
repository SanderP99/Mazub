package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Value;

@Value
public class Constants {

    /**
     * The vertical acceleration for all objects that fall
     */
    static final double maxVerticalAcceleration = -10.0;

    /**
     * The time between the sprite changes for a walking mazub
     */
    static final double frameRate = 0.075;

    /**
     * The maximum horizontal speed of mazub in meters per second while running
     */
    static final double mazubMaxHorizontalSpeedRunning = 3.0;

    /**
     * The maximum horizontal speed of mazub in meters per second while ducking
     */
    static final double mazubMaxHorizontalSpeedDucking = 1.0;

    /**
     * The minimum horizontal speed of mazub in meters per second while running
     */
    static final double mazubMinHorizontalSpeed = 1.0;

    /**
     * The maximum vertical speed of mazub in meters per second
     */
    static final double mazubMaxVerticalSpeed = 8.0;

    /**
     * The horizontal acceleration of mazub in meter per second squared
     */
    static final double mazubHorizontalAcceleration = 0.9;

    /**
     * The initial amount of hitpoints for mazub
     */
    static final int mazubHitPoints = 100;

    /**
     * The maximum amount of hitpoint for mazub
     */
    static final int mazubMaxHitPoints = 500;

    /**
     * The maximum horizontal speed of a shark in meters per second
     */
    static final double sharkMaxHorizontalSpeed = Double.POSITIVE_INFINITY;

    /**
     * The minimum horizontal speed of a shark in meters per second
     */
    static final double sharkMinHorizontalSpeed = 0;

    /**
     * The maximum vertical speed of a shark in meters per second
     */
    static final double sharkMaxVerticalSpeed = 2;

    /**
     * The initial amount of hitpoints of a shark
     */
    static final int sharkHitPoints = 100;

    /**
     * The maximum amount of hitpoint of a shark
     */
    static final int sharkMaxHitPoints = Integer.MAX_VALUE;

    /**
     * The horizontal acceleration of a shark in meters per second squared
     */
    static final double sharkHorizontalAcceleration = 1.5;

    /**
     * The maximum horizontal speed of a slime in meters per second
     */
    static final double slimeMaxHorizontalSpeed = 2.5;

    /**
     * The maximum vertical speed of a slime
     */
    static final double slimeMaxVerticalSpeed = 0;

    /**
     * The horizontal acceleration of a slime
     */
    static final double slimeHorizontalAcceleration = 0.7;

    /**
     * The initial hitpoints of a slime
     */
    static final int slimeHitPoints = 100;

    /**
     * The maximum hitpoints of a slime
     */
    static final int slimeMaxHitPoints = Integer.MAX_VALUE;

    /**
     * The horizontal acceleration of a plant
     */
    static final double plantHorizontalAcceleration = 0;

    /**
     * The vertical acceleration of a plant
     */
    static final double plantVerticalAcceleration = 0;

    /**
     * The vertical speed of skullcab
     */
    static final double skullcabVerticalSpeed = 0.5;

    /**
     * The hitpoints of scullcab
     */
    static final int skullcabHitPoints = 3;

    /*
     * The seconds scullcab lives
     */
    static final double skullcabSecondsToLive = 12;

    /**
     * The horizontal speed of sneezewort
     */
    static final double sneezewortHorizontalSpeed = 0.5;

    /**
     * The hitpoints of of sneezewort
     */
    static final int sneezewortHitPoints = 1;

    /**
     * The seconds sneezewort lives
     */
    static final double sneezewortSecondsToLive = 10;

    /**
     * The maximum number of schools in a world
     */
    public static final int worldMaxSchools = 10;

    /**
     * The maximum number of objects in the world, excluding the player
     */
    static final int worldMaxObjects = 100;

}
