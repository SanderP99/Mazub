package jumpingalien.model;

public interface VerticalMovement {

    public void setYPositionActual(double Y_pos);

    public int getYPositionPixel();

    public double getYPositionActual();

    public int getMaxYPosition();

    public void setOrientation(int orientation);

    public int getOrientation();

    public double getVerticalSpeedMeters();

    void setVerticalSpeedMeters(double speed);

    public double getMinSpeedMeters();

    public int getMinSpeedPixels();

    boolean isValidVerticalSpeed();

    public double getVerticalAcceleration();

    void setVerticalAcceleration(double verticalAcceleration);

    void fall();

    boolean isStandingOnImpassableTerrain();

    void setMaxVerticalSpeed(double speed);

    double getMaxVerticalSpeedMeters();

}
