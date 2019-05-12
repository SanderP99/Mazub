package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Value;

@Value
public class Constants {

    static final double maxVerticalAcceleration = -10.0;

    static final double frameRate = 0.075;

    static final double mazubMaxHorizontalSpeedRunning = 3.0;

    static final double mazubMaxHorizontalSpeedDucking = 1.0;

    static final double mazubMinHorizontalSpeed = 1.0;

    static final double mazubMaxVerticalSpeed = 8.0;

    static final double mazubHorizontalAcceleration = 0.9;

    static final int mazubHitPoints = 100;

    static final int mazubMaxHitPoints = 500;

    static final double sharkMaxHorizontalSpeed = 5;

    static final double sharkMinHorizontalSpeed = 0;

    static final double sharkMaxVerticalSpeed = 2;

    static final int sharkHitPoints = 100;

    static final int sharkMaxHitPoints = Integer.MAX_VALUE;

    static final double sharkHorizontalAcceleration = 1.5;

    static final double slimeMaxHorizontalSpeed = 2.5;

    static final double slimeMaxVerticalSpeed = 0;

    static final double slimeHorizontalAcceleration = 0.7;

    static final int slimeHitPoints = 100;

    static final int slimeMaxHitPoints = Integer.MAX_VALUE;

    static final double plantHorizontalAcceleration = 0;

    static final double plantVerticalAcceleration = 0;

    static final double skullcabVerticalSpeed = 0.5;

    static final int skullcabHitPoints = 3;

    static final double skullcabSecondsToLive = 12;

    static final double sneezewortHorizontalSpeed = 0.5;

    static final int sneezewortHitPoints = 1;

    static final double sneezewortSecondsToLive = 10;

    public static final int worldMaxSchools = 10;

    static final int worldMaxObjects = 100;

}
