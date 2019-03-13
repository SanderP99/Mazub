package jumpingalien.internal.game;

import java.util.Collection;
import java.util.Optional;

import jumpingalien.internal.gui.sprites.ImageSprite;
import jumpingalien.model.Plant;

public interface ObjectInfoProvider {

	public Collection<Plant> getPlants();

	public Optional<int[]> getLocation(Plant plant);

	public Optional<ImageSprite> getCurrentSprite(Plant plant);

}
