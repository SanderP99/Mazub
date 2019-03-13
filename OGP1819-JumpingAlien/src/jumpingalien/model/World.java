package jumpingalien.model;

public class World {
	
	public int getWorldSizeX() {
		return this.worldSizeX;
	}
	
	private void setWorldSizeX(int worldSizeX) {
		this.worldSizeX = worldSizeX;
	}
	
	private int worldSizeX;
	
	public int getWorldSizeY() {
		return this.worldSizeY;
	}
	
	private void setWorldSizeY(int worldSizeY) {
		this.worldSizeY = worldSizeY;
	}

	private int worldSizeY;
	
	public int getTileLength() {
		return this.tileLength;
	}
	
	private void setTileLength(int length) {
		this.tileLength = length;
	}
	
	private int tileLength;
	
	public int getTargetTileX() {
		return this.targetTileX;
	}
	
	private void setTargetTileX(int targetTileX) {
		assert isValidTargetTileX(targetTileX);
		this.targetTileX = targetTileX;
	}
	
	private boolean isValidTargetTileX(int targetTileX) {
		if (targetTileX > getWorldSizeX())
			return false;
		return true;
	}
	
	private int targetTileX;
	
	public int getTargetTileY() {
		return this.targetTileY;
	}
	
	private void setTargetTileY(int targetTileY) {
		assert isValidTargetTileY(targetTileY);
		this.targetTileY = targetTileY;
	}
	
	private boolean isValidTargetTileY(int targetTileY) {
		if (targetTileY > getWorldSizeY())
			return false;
		return true;
	}
	
	private int targetTileY;
}
