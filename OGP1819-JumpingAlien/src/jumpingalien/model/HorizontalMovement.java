package jumpingalien.model;

public interface HorizontalMovement {

    public void setXPositionActual(double X_pos);

    public boolean isValidPixelPosition(int X_pos);

    public boolean isValidActualXPosition(double X_pos);

    public int getXPositionPixel();

    public double getXPositionActual();

    public int getMaxXPosition();

    public void setOrientation(int orientation);

    public boolean isValidOrientation(int orientation);

    public int getOrientation();

    public double getHorizontalSpeedMeters();

    public int getHorizontalSpeedPixels();

    void setHorizontalSpeedMeters(double speed);

    public double getMinSpeedMeters();

    public int getMinSpeedPixels();

    public double getMaxSpeedRunningMeters();

    public int getMaxSpeedRunningPixels();

    public double getMaxSpeedDuckingMeters();

    public int getMaxSpeedDuckingPixels();

    boolean isValidHorizontalSpeed();

    public double getHorizontalAcceleration();

    void setHorizontalAcceleration(double horizontalAcceleration);

}
