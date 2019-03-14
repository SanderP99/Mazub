package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

public class World {
	
	World(int worldSizeX, int worldSizeY, int tileLength, int targetTileX, int targetTileY, int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures){
		setTileLength(tileLength);
		setWorldSizeX(worldSizeX);
		setWorldSizeY(worldSizeY);
		setTargetTileX(targetTileX);
		setTargetTileY(targetTileY);
	}
	
	@Basic
	public int getWorldSizeX() {
		return this.worldSizeX;
	}
	
	private void setWorldSizeX(int worldSizeX) {
		if (worldSizeX % this.getTileLength() == 0)
			this.worldSizeX = worldSizeX;
		else
			this.worldSizeX = worldSizeX + (this.getTileLength() - (worldSizeX % this.getTileLength()));
	}
	
	private int worldSizeX;
	
	@Basic
	public int getWorldSizeY() {
		return this.worldSizeY;
	}
	
	private void setWorldSizeY(int worldSizeY) {
		if (worldSizeY % this.getTileLength() == 0)
			this.worldSizeY = worldSizeY;
		else
			this.worldSizeY = worldSizeY + (this.getTileLength() - (worldSizeY % this.getTileLength()));
	}

	private int worldSizeY;
	
	@Basic
	public int getTileLength() {
		return this.tileLength;
	}
	
	private void setTileLength(int length) {
		assert length > 0;
		this.tileLength = length;
	}
	
	private int tileLength;
	
	@Basic
	public int getTargetTileX() {
		return this.targetTileX;
	}
	
	private void setTargetTileX(int targetTileX) {
		assert isValidTargetTileX(targetTileX);
		assert !isTerminated();
		this.targetTileX = targetTileX;
	}
	
	private boolean isValidTargetTileX(int targetTileX) {
		if (targetTileX > (getWorldSizeX()/getTileLength() - 1) || targetTileX < 0)
			return false;
		return true;
	}
	
	private int targetTileX;
	
	@Basic
	public int getTargetTileY() {
		return this.targetTileY;
	}
	
	private void setTargetTileY(int targetTileY) {
		assert isValidTargetTileY(targetTileY);
		assert !isTerminated();
		this.targetTileY = targetTileY;
	}
	
	private boolean isValidTargetTileY(int targetTileY) {
		if (targetTileY > (getWorldSizeY()/getTileLength() - 1) || targetTileY < 0)
			return false;
		return true;
	}
	
	private int targetTileY;
	
	/**
	 * Check whether this world is terminated.
	 */
	@Basic
	@Raw
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	public void terminate() {
		if (!isTerminated()) {
			this.isTerminated = true;
		}
	}
	
	private boolean isTerminated;
	
}
