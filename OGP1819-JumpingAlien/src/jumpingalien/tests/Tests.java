package jumpingalien.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jumpingalien.facade.Facade;
import jumpingalien.facade.IFacade;
import jumpingalien.internal.gui.sprites.JumpingAlienSprites;
import jumpingalien.model.Mazub;
import jumpingalien.model.School;
import jumpingalien.model.Slime;
import jumpingalien.model.Sneezewort;
import jumpingalien.model.World;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

public class Tests {

    // Precision constants for checks involving floating point numbers.
    public final static double HIGH_PRECISION = 0.1E-10;
    public final static double LOW_PRECISION = 0.01;

    // Some constants as defined in the assignment.
    public final static double MINIMUM_HORIZONTAL_VELOCITY = 1.0;
    public static final double V_VELOCITY_JUMPING = 8.0;
    public final static double HORIZONTAL_ACCELERATION = 0.9;
    public static final double V_ACCELERATION_JUMPING = -10.0;

    // Variable referencing the facade.
    IFacade facade = new Facade();

    // Variables referencing some predefined mazub's
    private static Mazub mazub_0_0, mazub_0_1000, mazub_100_0, mazub_100_1000, mazub_20_45;

    // Variables referencing some predefined sneezeworts.
    private static Sneezewort sneezewort_120_10;

    // Variables referencing some predefined schools.
    private static School someSchool;

    // Variables referencing a proper array of sprites for mazubs, for sneezeworts,
    // for skullcabs, for slimes and for sharks.
    private static Sprite[] mazubSprites;
    private static Sprite[] sneezewortSprites;
    private static Sprite[] skullcabSprites;
    private static Sprite[] slimeSprites;
    private static Sprite[] sharkSprites;

    // Variable referencing a predefined world.
    private static World world_100_200, world_250_400;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
	// Set up the array of sprites with several pictures to
	// move to the left and to the right.
	mazubSprites = new Sprite[] { new Sprite("Stationary Idle", 100, 50), new Sprite("Stationary Ducking", 75, 30),
		new Sprite("Ending Right Move", 90, 45), new Sprite("Ending Left Move", 90, 45),
		new Sprite("Moving Right and Jumping", 90, 45), new Sprite("Moving Left and Jumping", 90, 45),
		new Sprite("Moving Right and Ducking", 90, 30), new Sprite("Moving Left and Ducking", 90, 30),
		new Sprite("Moving Right 1", 90, 45), new Sprite("Moving Right 2", 90, 45),
		new Sprite("Moving Right 3", 90, 45), new Sprite("Moving Right 4", 90, 45),
		new Sprite("Moving Right 5", 90, 45), new Sprite("Moving Left 1", 90, 45),
		new Sprite("Moving Left 2", 90, 45), new Sprite("Moving Left 3", 90, 45),
		new Sprite("Moving Left 4", 90, 45), new Sprite("Moving Left 5", 90, 45), };
	sneezewortSprites = new Sprite[] { new Sprite("Sneezewort Moving Left", 40, 30),
		new Sprite("Sneezewort Moving Right", 40, 30) };
	skullcabSprites = new Sprite[] { new Sprite("Scullcab Moving Up", 50, 40),
		new Sprite("Scullcab Moving Down", 50, 40) };
	slimeSprites = new Sprite[] { new Sprite("Slime Moving Right", 60, 60),
		new Sprite("Slime Moving Left", 60, 60) };
	sharkSprites = new Sprite[] { new Sprite("Shark Resting", 70, 30), new Sprite("Shark Moving Left", 70, 30),
		new Sprite("Shark Moving Right", 70, 30) };
//		// Instruction to see to it that the default sprite for all game objects has the
//		// proper size for the test suite.
	JumpingAlienSprites.setDefaultMazubSprite(mazubSprites[0]);
	JumpingAlienSprites.setDefaultPlantSprite(sneezewortSprites[0]);
	JumpingAlienSprites.setDefaultSlimeSprite(slimeSprites[0]);
	JumpingAlienSprites.setDefaultSharkSprite(sharkSprites[0]);
    }

    @BeforeEach
    void setUp() throws Exception {
	facade.cleanAllSlimeIds();
	mazub_0_0 = facade.createMazub(0, 0, mazubSprites);
	mazub_0_1000 = facade.createMazub(0, 1000, mazubSprites);
	mazub_100_0 = facade.createMazub(100, 0, mazubSprites);
	mazub_100_1000 = facade.createMazub(100, 1000, mazubSprites);
	mazub_20_45 = facade.createMazub(20, 45, mazubSprites);
	sneezewort_120_10 = facade.createSneezewort(120, 10, sneezewortSprites);
	world_100_200 = facade.createWorld(10, 100, 200, new int[] { 10, 20 }, 20, 10);
	world_250_400 = facade.createWorld(5, 250, 400, new int[] { 33, 222 }, 25, 40);
	someSchool = facade.createSchool(world_100_200);
    }

    @Test
    void floatingSlime() {
	final Slime newSlime = facade.createSlime(10, 10, 10, someSchool, slimeSprites);
	final double oldY = facade.getActualPosition(newSlime)[1];
	facade.addGameObject(newSlime, world_100_200);
	facade.advanceWorldTime(world_100_200, 0.15);
	assertEquals(oldY, facade.getActualPosition(newSlime)[1]);
	for (int i = 0; i < 20; i++) {
	    facade.advanceWorldTime(world_100_200, 0.15);
	    assertEquals(oldY, facade.getActualPosition(newSlime)[1]);
	}
    }

    @Test
    void slimesOnTopOfEachother() {
	final Slime newSlime = facade.createSlime(10, 10, 10, someSchool, slimeSprites);
	final int slimeHeigth = newSlime.getCurrentSprite().getHeight();
	final Slime newSlime2 = facade.createSlime(20, 10, 10 + slimeHeigth, someSchool, slimeSprites);
	facade.addGameObject(newSlime, world_100_200);
	facade.addGameObject(newSlime2, world_100_200);
	assertEquals(2, facade.getAllGameObjects(world_100_200).size());
	facade.advanceWorldTime(world_100_200, 0.1);
	assertEquals(0, facade.getActualPosition(newSlime)[0] - facade.getActualPosition(newSlime2)[0], LOW_PRECISION);
    }

    @Test
    void sharkWrongSprites() {
	final Sprite[] sprites = { new Sprite("Shark Resting", 70, 30), new Sprite("Shark Moving", 70, 30) };
	assertThrows(ModelException.class, () -> facade.createShark(10, 10, sprites));
    }
}
