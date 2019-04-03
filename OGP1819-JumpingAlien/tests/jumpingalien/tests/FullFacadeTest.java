package jumpingalien.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.*;
import jumpingalien.model.*;
import jumpingalien.facade.Facade;
import jumpingalien.facade.IFacade;
import jumpingalien.util.Sprite;
import jumpingalien.util.ModelException;

class FullFacadeTest {

	// Precision constants for checks involving floating point numbers.
	public final static double HIGH_PRECISION = 0.1E-10;
	public final static double LOW_PRECISION = 0.01;

	// Some constants as defined in the assignment.
	public final static double MINIMUM_HORIZONTAL_VELOCITY = 1.0;
	public static final double V_VELOCITY_JUMPING = 8.0;
	public final static double HORIZONTAL_ACCELERATION = 0.9;
	public static final double V_ACCELERATION_JUMPING = -10.0;
	public static final double WORLD_WIDTH = 10.24;
	public static final double WORLD_HEIGHT = 7.68;

	// Variable referencing the facade.
	IFacade facade = new Facade();

	// Variables referencing some predefined mazub's
	private static Mazub mazub_0_0, mazub_100_0, mazub_20_45;

	// Variables referencing some predefined plants
	private static Plant plant_120_10;

	// Variables referencing a proper array of sprites for mazubs and plants.
	private static Sprite[] mazubSprites;
	private static Sprite[] plantSprites;

	// Variable referencing a predefined world.
	private static World world_100_200, world_250_400;

	// Variables storing the actual score and the maximum score.
	private static int actualScore = 0;
	private static int maximumScore = 0;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Set up the array of sprites with several pictures to
		// move to the left and to the right.
		mazubSprites = new Sprite[] { new Sprite("Stationary Idle", 100, 50),
				new Sprite("Stationary Ducking", 75, 30),
				new Sprite("Ending Right Move", 90, 45),
				new Sprite("Ending Left Move", 90, 45),
				new Sprite("Moving Right and Jumping", 90, 45),
				new Sprite("Moving Left and Jumping", 90, 45),
				new Sprite("Moving Right and Ducking", 90, 30),
				new Sprite("Moving Left and Ducking", 90, 30),
				new Sprite("Moving Right 1", 90, 45),
				new Sprite("Moving Right 2", 90, 45),
				new Sprite("Moving Right 3", 90, 45),
				new Sprite("Moving Right 4", 90, 45),
				new Sprite("Moving Right 5", 90, 45), new Sprite("Moving Left 1", 90, 45),
				new Sprite("Moving Left 2", 90, 45), new Sprite("Moving Left 3", 90, 45),
				new Sprite("Moving Left 4", 90, 45),
				new Sprite("Moving Left 5", 90, 45), };
		plantSprites = new Sprite[] { new Sprite("Moving Left", 40, 30),
				new Sprite("Moving Right", 40, 30) };
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println();
		System.out.println("Score: " + actualScore + "/" + maximumScore);
	}

	@BeforeEach
	void setUp() throws Exception {
		mazub_0_0 = facade.createMazub(0, 0, mazubSprites);
		mazub_100_0 = facade.createMazub(100, 0, mazubSprites);
		mazub_20_45 = facade.createMazub(20, 45, mazubSprites);
		plant_120_10 = facade.createPlant(120, 10, plantSprites);
		world_100_200 = facade.createWorld(10, 100, 200, new int[] { 10, 20 }, 20, 10);
		world_250_400 = facade.createWorld(5, 250, 400, new int[] { 33, 222 }, 25, 40);
	}

	/*******************
	 * Tests for Mazub *
	 *******************/

	@Test
	void createMazub_LegalCase() throws Exception {
		maximumScore += 18;
		Mazub newMazub = facade.createMazub(10, 6, mazubSprites);
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newMazub));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newMazub),
				HIGH_PRECISION);
		assertTrue(facade.getOrientation(newMazub) == 0);
		assertFalse(facade.isMoving(newMazub));
		assertFalse(facade.isJumping(newMazub));
		assertFalse(facade.isDucking(newMazub));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(newMazub));
		assertEquals(0.0 , facade.getAcceleration(newMazub)[0], HIGH_PRECISION);
		assertTrue((Math.abs(facade.getAcceleration(newMazub)[1]) <= HIGH_PRECISION) ||
				( (Math.abs(facade.getAcceleration(newMazub)[1]) >= 10.0-HIGH_PRECISION) &&
				  (Math.abs(facade.getAcceleration(newMazub)[1]) <= 10.0+HIGH_PRECISION)));
		assertEquals(100, facade.getHitPoints(newMazub));
		if (facade.isTeamSolution()) {
			assertArrayEquals(mazubSprites, facade.getSprites(newMazub));
			assertSame(mazubSprites[0], facade.getCurrentSprite(newMazub));
		}
		actualScore += 18;
	}
	
	@Test
	void createMazub_IllegalSprites() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 8;
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, (Sprite[]) null));
			assertThrows(ModelException.class, () -> facade.createMazub(5, 10,
					new Sprite[] { new Sprite("a", 1, 2) }));
			Sprite[] oddSprites = new Sprite[13];
			for (int i = 0; i < oddSprites.length; i++)
				oddSprites[i] = new Sprite("z", i, i * 2);
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, oddSprites));
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, new Sprite[] { null, null, null, null,
							null, null, null, null, null, null }));
			actualScore += 8;
		}
	}

	@Test
	void createMazub_LeakTest() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 10;
			Sprite[] clonedSprites = Arrays.copyOf(mazubSprites, mazubSprites.length);
			Mazub theMazub = facade.createMazub(10, 20, clonedSprites);
			clonedSprites[1] = null;
			Sprite[] currentSprites = facade.getSprites(theMazub);
			assertArrayEquals(mazubSprites, currentSprites);
			actualScore += 10;
		}
	}

	@Test
	void getSprites_LeakTest() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 10;
			Sprite[] clonedSprites = Arrays.copyOf(mazubSprites, mazubSprites.length);
			Mazub theMazub = facade.createMazub(10, 20, clonedSprites);
			Sprite[] mazubSprites = facade.getSprites(theMazub);
			mazubSprites[3] = null;
			assertArrayEquals(clonedSprites, facade.getSprites(theMazub));
			actualScore += 10;
		}
	}

	@Test
	void terminate_ObjectInWorld() throws Exception {
		maximumScore += 10;
		facade.addGameObject(mazub_0_0, world_100_200);
		assertFalse(facade.isTerminatedGameObject(mazub_0_0));
		assertEquals(world_100_200, facade.getWorld(mazub_0_0));
		assertTrue(facade.getAllGameObjects(world_100_200).contains(mazub_0_0));
		facade.terminateGameObject(mazub_0_0);
		assertTrue(facade.isTerminatedGameObject(mazub_0_0));
		assertNull(facade.getWorld(mazub_0_0));
		assertFalse(facade.getAllGameObjects(world_100_200).contains(mazub_0_0));
		actualScore += 10;
	}

	@Test
	void changeActualPosition_LegalCases() throws Exception {
		maximumScore += 4;
		facade.changeActualPosition(mazub_0_0, new double[] { 2.4, 3.6 });
		facade.addGameObject(mazub_0_0, world_100_200);
		assertArrayEquals(new double[] { 2.4, 3.6 }, facade.getActualPosition(mazub_0_0),
				LOW_PRECISION);
		facade.changeActualPosition(mazub_100_0,
				new double[] { 33.6, Double.POSITIVE_INFINITY });
		assertArrayEquals(new double[] { 33.6, Double.POSITIVE_INFINITY },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		actualScore += 4;
	}

	@Test
	void changeActualPosition_IllegalCases() throws Exception {
		maximumScore += 4;
		// Null position
		assertThrows(ModelException.class,
				() -> facade.changeActualPosition(mazub_0_0, null));
		// Position with number of displacements different from 2.
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { 1.0, 2.0, 3.0 }));
		// Position with NaN as displacement
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { Double.NaN, 33.6 }));
		// Position with INFINITY as displacement
		actualScore += 4;
	}

	@Test
	void changeActualPosition_OutsideWorldBoundaries() throws Exception {
		maximumScore += 12;
		// Position to the left of the world
		Mazub theMazub = facade.createMazub(0, 0, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		facade.changeActualPosition(theMazub, new double[] {  -0.1, 33.6 });
 		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		// Position below the world
		theMazub = facade.createMazub(12, 7, mazubSprites);
		
		facade.addGameObject(theMazub, world_250_400);
		facade.changeActualPosition(theMazub, new double[] { 12.3, -0.1 });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		// Position to the right of the world
		theMazub = facade.createMazub(12, 7, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		double newX = (facade.getSizeInPixels(world_250_400)[0] + 1) * 0.01;
		facade.changeActualPosition(theMazub, new double[] { newX, 14.7 });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		// Position to above top of the world
		theMazub = facade.createMazub(12, 7, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		double newY = (facade.getSizeInPixels(world_250_400)[1] + 1) * 0.01;
		facade.changeActualPosition(theMazub, new double[] { 3.5, newY });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		actualScore += 12;
	}

	@Test
	void changeActualPosition_OnImpassableTerrain() throws Exception {
		maximumScore += 6;
		Mazub theMazub = facade.createMazub(0, 0, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		facade.setGeologicalFeature(world_250_400, 550, 300, SOLID_GROUND);
		assertThrows(ModelException.class,
				() -> facade.changeActualPosition(theMazub, new double[] { 5.2, 2.7 }));
		actualScore += 6;
	}

	@Test
	void changeActualPosition_DeadObject() throws Exception {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 10, 10, MAGMA);
		Mazub theMazub = facade.createMazub(0, 0, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		// After 0.2 seconds, mazub will be dead because of its contact with water.
		facade.advanceTime(theMazub, 0.15);
		facade.advanceTime(theMazub, 0.15);
		// Dead objects can not move nor jump.
		assertThrows(ModelException.class, () -> facade.startMoveRight(theMazub));
		assertThrows(ModelException.class, () -> facade.startJump(theMazub));
		facade.advanceTime(theMazub, 0.15);
		assertArrayEquals(new double[] {0.0,0.0}, facade.getActualPosition(theMazub));
		assertArrayEquals(new double[] {0.0,0.0}, facade.getVelocity(theMazub));
		assertArrayEquals(new double[] {0.0,0.0}, facade.getAcceleration(theMazub));
		// 0.6 seconds after its dead, mazub must be removed from its game world.
		for (int i=1; i<=3; i++)
			facade.advanceTime(theMazub, 0.15);
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertNull(facade.getWorld(theMazub));
		assertFalse(facade.getAllGameObjects(world_250_400).contains(theMazub));
		actualScore += 15;
	}

	@Test
	public void startMoveLeft_LegalCase() throws Exception {
		maximumScore += 8;
		facade.startMoveLeft(mazub_100_0);
		assertTrue(facade.isMoving(mazub_100_0));
		assertTrue(facade.getOrientation(mazub_100_0) < 0);
		assertArrayEquals(new double[] { -MINIMUM_HORIZONTAL_VELOCITY, 0.0 },
				facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { -HORIZONTAL_ACCELERATION, 0.0 },
				facade.getAcceleration(mazub_100_0), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazub_100_0));
		actualScore += 8;
	}

	@Test
	public void startMoveRight_LegalCase() throws Exception {
		maximumScore += 4;
		facade.startMoveRight(mazub_100_0);
		assertTrue(facade.isMoving(mazub_100_0));
		assertTrue(facade.getOrientation(mazub_100_0) > 0);
		assertArrayEquals(new double[] { MINIMUM_HORIZONTAL_VELOCITY, 0.0 },
				facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { HORIZONTAL_ACCELERATION, 0.0 },
				facade.getAcceleration(mazub_100_0), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[8], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void startMove_AlreadyMoving() throws Exception {
		maximumScore += 4;
		facade.startMoveLeft(mazub_100_0);
		assertThrows(ModelException.class, () -> facade.startMoveLeft(mazub_100_0));
		assertThrows(ModelException.class, () -> facade.startMoveRight(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void endMove_LegalCase() throws Exception {
		maximumScore += 6;
		facade.startMoveLeft(mazub_100_0);
		facade.endMove(mazub_100_0);
		assertFalse(facade.isMoving(mazub_100_0));
		assertTrue(facade.getOrientation(mazub_100_0) <= 0);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[3], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void endMove_NotMoving() throws Exception {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.endMove(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startJump_LegalCase() throws Exception {
		maximumScore += 6;
		facade.startJump(mazub_100_0);
		assertTrue(facade.isJumping(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, V_VELOCITY_JUMPING },
				facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, V_ACCELERATION_JUMPING },
				facade.getAcceleration(mazub_100_0), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void startJump_AlreadyJumping() throws Exception {
		maximumScore += 2;
		facade.startJump(mazub_100_0);
		assertThrows(ModelException.class, () -> facade.startJump(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endJump_PositiveVelocity() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub still has a velocity of 6.5 m/s after 0.15 seconds of jumping.
		facade.advanceTime(mazub_100_0, 0.15);
		facade.endJump(mazub_100_0);
		assertFalse(facade.isJumping(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void endJump_NegativeVelocity() throws Exception {
		maximumScore += 4;
		facade.startJump(mazub_100_0);
		// Mazub has a velocity of -1.0 m/s after 0.9 seconds of jumping.
		for (int i = 1; i <= 6; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.getVelocity(mazub_100_0)[1] < 0.0);
		double[] oldVelocity = facade.getVelocity(mazub_100_0);
		facade.endJump(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isJumping(mazub_100_0));
		assertArrayEquals(oldVelocity, facade.getVelocity(mazub_100_0), LOW_PRECISION);
		actualScore += 4;
	}

	@Test
	public void endJump_NotJumping() throws Exception {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.endJump(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startDuck_StationaryMazub() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[1], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void startDuck_AlreadyDucking() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startDuck(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[1], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startDuck_MazubMovingAndJumping() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		facade.startJump(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { -1.0, 8.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		assertArrayEquals(new double[] { 0.0, -10.0 },
				facade.getAcceleration(mazub_100_0), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_MazubDucking() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		facade.advanceTime(mazub_100_0, 0.15);
		facade.startDuck(mazub_100_0);
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { -1.0, 0.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		assertArrayEquals(new double[] { -0.9, 0.0 }, facade.getAcceleration(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_NotDucking() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_OverlappingImpassableTerrain() throws Exception {
		maximumScore += 8;
		facade.setGeologicalFeature(world_250_400, 220, 35, SOLID_GROUND);
		facade.addGameObject(mazub_100_0, world_250_400);
		// Mazub will move over a distance of 100.0 cm in 1 second.
		facade.startMoveRight(mazub_100_0);
		facade.startDuck(mazub_100_0);
		for (int i = 0; i < 10; i++)
			facade.advanceTime(mazub_100_0, 0.1);
		// Mazub will not be able to stand up, because of the solid ground at (220,35).
		facade.endDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		// Mazub will move over 50.0 cm in 0.5 seconds.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(mazub_100_0, 0.1);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[6], facade.getCurrentSprite(mazub_100_0));
		// Mazub will now be able to stand up.
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[8], facade.getCurrentSprite(mazub_100_0));
		actualScore += 8;
	}

	@Test
	public void endDuck_OverlappingOtherGameObject() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startDuck(mazub_100_0);
		Mazub otherMazub = facade.createMazub(120, 35, mazubSprites);
		facade.addGameObject(otherMazub, world_250_400);
		// Mazub will not be able to stand up, because of the newly added game object.
		facade.endDuck(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[1], facade.getCurrentSprite(mazub_100_0));
		assertTrue(facade.isDucking(mazub_100_0));
		// After removing the newly added game object, mazub can stand up.
		facade.removeGameObject(otherMazub, world_250_400);
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_StationaryMazub() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_0, world_100_200);
		facade.advanceTime(mazub_100_0, 0.15);
		assertArrayEquals(new double[] { 1.0, 0.0 },
				facade.getActualPosition(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void advanceTime_MazubMovingLeftWithinBoundaries() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		// Mazub will move 14.882 cm over 0.14 seconds; its velocity increases with 0.126
		// m/s
		facade.advanceTime(mazub_100_0, 0.14);
		assertEquals(0.85118, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(-1.126, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[14], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubMovingRightWithinBoundaries() throws Exception {
		maximumScore += 3;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveRight(mazub_100_0);
		// Mazub will move 18.3 cm over 0.17 seconds; its velocity increases with 0.153
		// m/s
		facade.advanceTime(mazub_100_0, 0.17);
		assertEquals(1.183, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.153, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[10], facade.getCurrentSprite(mazub_100_0));
		actualScore += 3;
	}

	@Test
	public void advanceTime_MazubReachingLeftBorder() throws Exception {
		maximumScore += 5;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.changeActualPosition(mazub_100_0, new double[] { 0.05, 0.0 });
		facade.startMoveLeft(mazub_100_0);
		// Mazub reaches left border after 0.1024 seconds.
		facade.advanceTime(mazub_100_0, 0.19);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 5;
	}

	@Test
	public void advanceTime_MazubReachingRightBorder() throws Exception {
		maximumScore += 3;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.changeActualPosition(mazub_100_0, new double[] {
				facade.getSizeInPixels(world_250_400)[0] / 100.0 - 0.05, 0.0 });
		facade.startMoveRight(mazub_100_0);
		// Mazub reaches right border after 0.1447 seconds.
		facade.advanceTime(mazub_100_0, 0.19);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 3;
	}

	@Test
	public void advanceTime_MazubCollidingWithMazubAtLeftSide() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_0_0, world_250_400);
		facade.startDuck(mazub_0_0);
		Mazub moving_mazub = facade.createMazub(95, 0, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveLeft(moving_mazub);
		// The moving mazub is at a distance of 20 cm from the other mazub and will
		// collide
		// with it after 0.184656 seconds.It will have a velocity of -1.1662 m/s
		facade.advanceTime(moving_mazub, 0.19);
		assertEquals(0.75, facade.getActualPosition(moving_mazub)[0], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubCollidingWithMazubAtRightSide() throws Exception {
		maximumScore += 6;
		Mazub mazubToCollideWith = facade.createMazub(200, 20, mazubSprites);
		facade.addGameObject(mazubToCollideWith, world_250_400);
		Mazub moving_mazub = facade.createMazub(95, 0, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveRight(moving_mazub);
		// The moving mazub is at a distance of 15 cm fro the other mazub and will collide
		// with it after 0.141 seconds.
		facade.advanceTime(moving_mazub, 0.15);
		assertEquals(1.1, facade.getActualPosition(moving_mazub)[0], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingImpassbleTerrainAtLeftSide() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 100, 10, SOLID_GROUND);
		Mazub moving_mazub = facade.createMazub(110, 0, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveLeft(moving_mazub);
		// The moving mazub will collide with the other mazub after 0.0489 seconds.
		facade.advanceTime(moving_mazub, 0.1);
		assertEquals(1.05, facade.getActualPosition(moving_mazub)[0], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingImpassbleTerrainAtRightSide() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 205, 15, SOLID_GROUND);
		Mazub moving_mazub = facade.createMazub(100, 0, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveRight(moving_mazub);
		// The moving mazub will collide with the other mazub after 0.141 seconds.
		facade.advanceTime(moving_mazub, 0.15);
		assertEquals(1.15, facade.getActualPosition(moving_mazub)[0], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingTopSpeed() throws Exception {
		maximumScore += 8;
		facade.addGameObject(mazub_100_0, world_250_400);
		// Mazub needs (3.0-1.0)/0.9 = 2.22 seconds to reach top speed.
		// Mazub will move over 452.778 cm in a period of 2.25 seconds (444.438 cm in
		// 2.2222
		// seconds at increasing speed, and 8.34 cm in a period of 2.25-0.2222 seconds at
		// top speed)
		facade.startMoveRight(mazub_100_0);
		for (int i = 1; i <= 15; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(3.0, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(5.52778, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		// After having reached its top speed, Mazub no longer accelerates.
		// It will move over 30.0 cm at top speed in a period of 0.1 seconds.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(5.52778 + 0.3, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(3.0, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[9], facade.getCurrentSprite(mazub_100_0));
		actualScore += 8;
	}

	@Test
	public void advanceTime_MazubJumpingUpwardsOnly() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_0_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub will jump over 108.75 cm in 0.15 seconds; it will have a velocity of
		// 6.5m/s
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(1.0875, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(6.5, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingOutOfWorld() throws Exception {
		maximumScore += 6;
		int worldHeight = facade.getSizeInPixels(world_250_400)[1] / 100;
		facade.changeActualPosition(mazub_100_0, new double[] { 1.0, worldHeight - 0.3 });
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub reaches top of the world after 0.0384 seconds.
		facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAndFallingDown() throws Exception {
		maximumScore += 6;
		// Mazub starts falling after 0.8 seconds; it will then have jumped over 320 cm;
		// its velocity is reduced to 0 m/s.
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(3.2, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		// After 0.18 seconds, mazub will have fallen over 16.2 cm; its velocity is
		// changed
		// to -1.8 m/s.
		facade.advanceTime(mazub_100_0, 0.18);
		assertEquals(3.2 - 0.162, facade.getActualPosition(mazub_100_0)[1],
				LOW_PRECISION);
		assertEquals(-1.8, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAndFallingOutOfWorld() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_100_0, world_250_400);
		// Mazub reaches ground after 1.6 seconds; its velocity is changed to -8.0 m/s
		facade.startJump(mazub_100_0);
		for (int i = 1; i <= 10; i++)
			facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(0.0, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(-8.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		// If time advances, mazub will fall out of its world.
		facade.advanceTime(mazub_100_0, 0.18);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAgainstImpassableTerrain() throws Exception {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 150, 200, SOLID_GROUND);
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub reaches solid ground after 0.2169 seconds, and then starts falling down.
		facade.advanceTime(mazub_100_0, 0.1);
		facade.advanceTime(mazub_100_0, 0.1169);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.5, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// Mazub falls down 5 cm over a period of 0.1 seconds
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.45, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// Mazub reaches bottom of world after falling 0.5385 seconds.
		for (int i = 0; i < 3; i++)
			facade.advanceTime(mazub_100_0, 0.18);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubLandingOnImpassableTerrain() throws Exception {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 100, 45, SOLID_GROUND);
		Mazub jumpingMazub = facade.createMazub(100, 150, mazubSprites);
		facade.addGameObject(jumpingMazub, world_250_400);
		facade.startJump(jumpingMazub);
		// Mazub reaches top after 0.8 seconds; the distance of the jump is 320 cm.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(jumpingMazub, 0.16);
		assertEquals(1.0, facade.getActualPosition(jumpingMazub)[0], LOW_PRECISION);
		assertEquals(4.70, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub is still falling after 0.9 seconds.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(jumpingMazub, 0.18);
		assertEquals(0.65, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub reaches lands on solid ground after another 0.0165 seconds.
		facade.advanceTime(jumpingMazub, 0.04);
		assertEquals(0.5, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubBumpingAgainstOtherGameObject() throws Exception {
		maximumScore += 15;
		Mazub mazubToBumpAgainst = facade.createMazub(150, 200, mazubSprites);
		facade.addGameObject(mazubToBumpAgainst, world_250_400);
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub reaches other mazub after 0.2169 seconds, and then starts falling down.
		facade.advanceTime(mazub_100_0, 0.1);
		facade.advanceTime(mazub_100_0, 0.1169);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.5, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// Mazub falls down 5 cm over a period of 0.1 seconds
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.45, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// Mazub reaches bottom of world after falling 0.5385 seconds.
		for (int i = 0; i < 3; i++)
			facade.advanceTime(mazub_100_0, 0.18);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubLandingOntTopOfGameObject() throws Exception {
		maximumScore += 15;
		Mazub jumpingMazub = facade.createMazub(100, 150, mazubSprites);
		facade.addGameObject(jumpingMazub, world_250_400);
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(jumpingMazub);
		// Mazub reaches top after 0.8 seconds; the distance of the jump is 320 cm.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(jumpingMazub, 0.16);
		assertEquals(1.0, facade.getActualPosition(jumpingMazub)[0], LOW_PRECISION);
		assertEquals(4.70, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub is still falling after 0.9 seconds.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(jumpingMazub, 0.18);
		assertEquals(0.65, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub reaches the top of the other mazub after another 0.0165 seconds.
		facade.advanceTime(jumpingMazub, 0.04);
		assertEquals(0.5, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubFalling() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.changeActualPosition(mazub_100_0, new double[] { 10.0, 2.5 });
		// After 0.1 seconds, Mazub will have fallen over 5 cm; its velocity has changed
		// to -1 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertArrayEquals(new double[] { 10.0, 2.5 - 0.05 },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		assertEquals(-1.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubMovingRightAndDucking() throws Exception {
		maximumScore += 12;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveRight(mazub_100_0);
		// Mazub will have moved over 14.822 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.126 m/s.
		facade.advanceTime(mazub_100_0, 0.14);
		assertEquals(1.14822, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.126, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		// Starting to duck
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertEquals(MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[6], facade.getCurrentSprite(mazub_100_0));
		// Mazub will have moved over 12 cm in a period of 0.12 seconds. Its
		// velocity stays constant at 1.0 m/s.
		facade.advanceTime(mazub_100_0, 0.12);
		assertEquals(1.14822 + 0.12, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[6], facade.getCurrentSprite(mazub_100_0));
		// Ending the duck
		facade.endDuck(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[8], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isDucking(mazub_100_0));
		assertEquals(HORIZONTAL_ACCELERATION, facade.getAcceleration(mazub_100_0)[0],
				LOW_PRECISION);
		// Mazub will have moved over 10.45 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.09 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.14822 + 0.12 + 0.1045, facade.getActualPosition(mazub_100_0)[0],
				0.1);
		assertEquals(1.09, facade.getVelocity(mazub_100_0)[0], 0.1E-10);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[9], facade.getCurrentSprite(mazub_100_0));
		actualScore += 12;
	}


	@Test
	public void advanceTime_MazubMovingLeftAndDucking() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		// Mazub will have moved over 14.822 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.126 m/s.
		facade.advanceTime(mazub_100_0, 0.14);
		assertEquals(1.0 - 0.14822, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(-1.126, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		// Starting to duck
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazub_100_0));
		// Mazub will have moved over 12 cm in a period of 0.12 seconds. Its
		// velocity stays constant at 1.0 m/s.
		facade.advanceTime(mazub_100_0, 0.12);
		assertEquals(1.0 - 0.14822 - 0.12, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazub_100_0));
		// Ending the duck
		facade.endDuck(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isDucking(mazub_100_0));
		assertEquals(-HORIZONTAL_ACCELERATION, facade.getAcceleration(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		// Mazub will have moved over 10.45 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.09 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.0 - 0.14822 - 0.12 - 0.1045,
				facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(-1.09, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[14], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void advanceTime_MazubMovingAndJumping() throws Exception {
		maximumScore += 10;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveRight(mazub_100_0);
		facade.startJump(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[4], facade.getCurrentSprite(mazub_100_0));
		// Mazub will have moved over 14.822 cm in 0.14 seconds, and will have a
		// horizontal
		// velocity of 1.126 m/s.
		// Mazub will also have jumped over 102.2 cm in 0.14 seconds, and will have a
		// vertical velocity of 6.6 m/s.
		facade.advanceTime(mazub_100_0, 0.14);
		assertArrayEquals(new double[] { 1.14822, 1.022 },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		assertArrayEquals(new double[] { 1.126, 6.6 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[4], facade.getCurrentSprite(mazub_100_0));
		actualScore += 10;
	}

	@Test
	public void advanceTime_MazubInWater() {
		maximumScore += 12;
		facade.setGeologicalFeature(world_250_400, 120, 10, WATER);
		facade.addGameObject(mazub_100_0, world_250_400);
		// No loss of hit points as long as the time in water is below 0.2 seconds.
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(100, facade.getHitPoints(mazub_100_0));
		// After 0.30 seconds in water, mazub looses 2 hit points.
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(98, facade.getHitPoints(mazub_100_0));
		// After 0.98 seconds in water, mazub has lost 8 hit points in total.
		for (int i = 1; i <= 4; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		facade.advanceTime(mazub_100_0, 0.08);
		assertEquals(92, facade.getHitPoints(mazub_100_0));
		actualScore += 12;
	}

	@Test
	public void advanceTime_MazubInAndOutWater() {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 120, 10, WATER);
		facade.addGameObject(mazub_100_0, world_250_400);
		// After 0.45 seconds in water, mazub looses 4 hit points.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(96, facade.getHitPoints(mazub_100_0));
		// After moving to the right for at least 0.2268 seconds, mazub is out of the
		// water.
		// The hit points after moving to the right for 0.9 seconds may therefore only
	 	// diminish with 2.
		facade.startMoveRight(mazub_100_0);
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_0, 0.18);
		assertEquals(94, facade.getHitPoints(mazub_100_0));
		assertEquals(2.2645, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		// After moving back to the left for 0.9 seconds, mazub will only have contact
		// with
		// water for < 0.2 seconds (its velocity increases)
		facade.endMove(mazub_100_0);
		facade.startMoveLeft(mazub_100_0);
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_0, 0.18);
		assertEquals(94, facade.getHitPoints(mazub_100_0));
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		// After moving for another 0.15 seconds, the hit points must be diminished with
		// 2.
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(92, facade.getHitPoints(mazub_100_0));
		actualScore += 15;
	}

	@Test
	public void advanceTime_DyingInWater() {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 120, 10, WATER);
		facade.addGameObject(mazub_100_0, world_250_400);
		// After 9.90 seconds in water, mazub has lost 98 hit points.
		for (int i = 1; i <= 99; i++)
			facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(2, facade.getHitPoints(mazub_100_0));
		// After another 0.15 seconds in water, mazub is dead.
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(0, facade.getHitPoints(mazub_100_0));
		// After another 0.60 seconds, mazub is terminated and removed from its world
		for (int i = 1; i <= 4; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubInMagma() {
		maximumScore += 16;
		facade.setGeologicalFeature(world_250_400, 120, 10, MAGMA);
		facade.addGameObject(mazub_100_0, world_250_400);
		// Mazub immediately looses 50 hit points when in contact with water.
		facade.advanceTime(mazub_100_0, 0.10);
		assertEquals(50, facade.getHitPoints(mazub_100_0));
		// After 0.15 seconds in magma, the loss of hit points is still equal to 50.
		facade.advanceTime(mazub_100_0, 0.05);
		assertEquals(50, facade.getHitPoints(mazub_100_0));
		// After 0.22 seconds in magma, mazug has lost all its hit points
		facade.advanceTime(mazub_100_0, 0.07);
		assertEquals(0, facade.getHitPoints(mazub_100_0));
		assertTrue(facade.isDeadGameObject(mazub_100_0));
		// After 0.45 seconds, mazub is still dead.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.isDeadGameObject(mazub_100_0));
		// After another 0.30 seconds, mazub is terminated and is no longer part of its
		// world.
		for (int i = 1; i <= 2; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 16;
	}

	@Test
	public void advanceTime_MazubInAndOutMagma() {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 120, 0, MAGMA);
		facade.addGameObject(mazub_100_0, world_250_400);
		// After jumping for 0.0063 seconds, mazub is out of the magma. The hit points
		// are diminished with 50.
		facade.startJump(mazub_100_0);
		facade.advanceTime(mazub_100_0, 0.01);
		assertEquals(50, facade.getHitPoints(mazub_100_0));
		// After another 0.19 seconds, mazub is still out of magma.
		facade.advanceTime(mazub_100_0, 0.19);
		assertEquals(1.40, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// After falling down for 0.5196 seconds, mazub is just above the magma
		facade.endJump(mazub_100_0);
		facade.advanceTime(mazub_100_0, 0.17);
		facade.advanceTime(mazub_100_0, 0.17);
		facade.advanceTime(mazub_100_0, 0.1796);
		assertEquals(50, facade.getHitPoints(mazub_100_0));
		assertEquals(0.05, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// After falling down a bit more, mazub is back in magma and its hitpoints
		// are reduced to 0.
		facade.advanceTime(mazub_100_0, 0.005);
		assertEquals(0, facade.getHitPoints(mazub_100_0));
		actualScore += 15;
	}

	@Test
	public void advanceTime_EatingSinglePlant() {
		maximumScore += 15;
		facade.addGameObject(mazub_0_0, world_250_400);
		facade.addGameObject(plant_120_10, world_250_400);
		// Mazub will reach the plant after 0.2677 seconds.
		facade.startMoveRight(mazub_0_0);
		facade.advanceTime(mazub_0_0, 0.19);
		assertEquals(100, facade.getHitPoints(mazub_0_0));
		assertEquals(0.206, facade.getActualPosition(mazub_0_0)[0], LOW_PRECISION);
		assertFalse(facade.isTerminatedGameObject(plant_120_10));
		facade.advanceTime(mazub_0_0, 0.1);
		assertEquals(0.328, facade.getActualPosition(mazub_0_0)[0], LOW_PRECISION);
		assertEquals(150, facade.getHitPoints(mazub_0_0));
		assertTrue(facade.isTerminatedGameObject(plant_120_10));
		assertNull(facade.getWorld(plant_120_10));
		actualScore += 15;
	}

	@Test
	public void advanceTime_EatingDeadPlant() {
		maximumScore += 15;
		facade.addGameObject(mazub_0_0, world_250_400);
		// Let the plant duck such that there is no overlap with the plant.
		facade.startDuck(mazub_0_0);
		facade.addGameObject(plant_120_10, world_250_400);
		// The plant dies after 10 seconds of game time, but is still in the world
		// for 0.6 seconds.
		for (int i=1; i<=100; i++)
			facade.advanceTime(plant_120_10, 0.1);
		facade.advanceTime(plant_120_10, 0.05);
		assertTrue(facade.isDeadGameObject(plant_120_10));
		assertEquals(world_250_400,facade.getWorld(plant_120_10));
		// Mazub will reach the plant after 0.2474 seconds.
		facade.endDuck(mazub_0_0);
		facade.startMoveRight(mazub_0_0);
		facade.advanceTime(mazub_0_0, 0.19);
		assertEquals(100, facade.getHitPoints(mazub_0_0));
		assertEquals(0.206, facade.getActualPosition(mazub_0_0)[0], LOW_PRECISION);
		assertFalse(facade.isTerminatedGameObject(plant_120_10));
		facade.advanceTime(mazub_0_0, 0.1);
		assertEquals(0.328, facade.getActualPosition(mazub_0_0)[0], LOW_PRECISION);
		assertEquals(80, facade.getHitPoints(mazub_0_0));
		assertTrue(facade.isTerminatedGameObject(plant_120_10));
		assertNull(facade.getWorld(plant_120_10));
		actualScore += 15;
	}

	@Test
	public void advanceTime_EatingSeveralPlants() {
		maximumScore += 10;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.addGameObject(plant_120_10, world_250_400);
		Plant plant2 = facade.createPlant(150, 30, plantSprites);
		facade.addGameObject(plant2, world_250_400);
		Plant plant3 = facade.createPlant(190, 40, plantSprites);
		facade.addGameObject(plant3, world_250_400);
		// Time must be advanced for mazub to eat all the plants.
		assertEquals(100, facade.getHitPoints(mazub_100_0));
		assertFalse(facade.isTerminatedGameObject(plant_120_10));
		facade.advanceTime(mazub_100_0, 0.01);
		assertEquals(250, facade.getHitPoints(mazub_100_0));
		assertTrue(facade.isTerminatedGameObject(plant_120_10));
		assertTrue(facade.isTerminatedGameObject(plant2));
		assertTrue(facade.isTerminatedGameObject(plant3));
		actualScore += 10;
	}

	@Test
	public void advanceTime_MaximumHitpointsReached() {
		maximumScore += 10;
		facade.addGameObject(mazub_100_0, world_250_400);
		for (int i = 1; i <= 10; i++)
			facade.addGameObject(facade.createPlant(100 + i * 8, i * 4, plantSprites),
					world_250_400);
		// Time must be advanced for mazub to eat all the plants.
		facade.advanceTime(mazub_100_0, 0.01);
		assertEquals(500, facade.getHitPoints(mazub_100_0));
		assertEquals(3, facade.getAllGameObjects(world_250_400).size());
		actualScore += 10;
	}

	@Test
	public void advanceTime_WindowUpdateFarFromBoundaries() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			World theWorld = facade.createWorld(10, 1000, 500, new int[] { 9900, 4900 },
					800, 600);
			Mazub theMazub = facade.createMazub(400, 300, mazubSprites);
			facade.addGameObject(theMazub, theWorld);
			facade.startGame(theWorld);
			facade.startMoveRight(theMazub);
			int[] windowPixelPosition = facade.getVisibleWindowPosition(theWorld);
			assertTrue(windowPixelPosition[0] + 200 <= 400);
			assertTrue(windowPixelPosition[0] + 800 + 200 >= 500);
			assertTrue(windowPixelPosition[1] + 200 <= 300);
			assertTrue(windowPixelPosition[1] + 600 + 200 >= 350);
			// After 0.4 seconds, mazub will have moved 47.2 cm to the right and will have
			// fallen down 80 cm.
			for (int i = 1; i <= 4; i++)
				facade.advanceTime(theMazub, 0.1);
			windowPixelPosition = facade.getVisibleWindowPosition(theWorld);
			assertTrue(windowPixelPosition[0] + 200 <= 447);
			assertTrue(windowPixelPosition[0] + 800 + 200 >= 547);
			assertTrue(windowPixelPosition[1] + 200 <= 220);
			assertTrue(windowPixelPosition[1] + 600 + 200 >= 270);
			actualScore += 15;
		}
	}

	@Test
	public void advanceTime_WindowUpdateNearBoundaries() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			World theWorld = facade.createWorld(10, 28, 17, new int[] { 100, 200 }, 150,
					120);
			Mazub theMazub = facade.createMazub(100, 70, mazubSprites);
			facade.addGameObject(theMazub, theWorld);
			facade.startGame(theWorld);
			facade.startMoveRight(theMazub);
			int windowLeft = facade.getVisibleWindowPosition(theWorld)[0];
			int windowRight = facade.getVisibleWindowPosition(theWorld)[0] + 150;
			int windowBottom = facade.getVisibleWindowPosition(theWorld)[1];
			int windowTop = facade.getVisibleWindowPosition(theWorld)[1] + 120;
			assertTrue(((windowLeft >= 100) && (windowLeft < 200)) ||
					((windowRight > 100) && (windowRight <= 200)));
			assertTrue(((windowBottom >= 70) && (windowBottom < 120)) ||
					((windowTop > 70) && (windowTop <= 120)));
			// After 0.37 seconds, mazub will have moved 43.16 cm to the right and will
			// have
			// fallen down 68.45 cm.
			facade.advanceTime(theMazub, 0.19);
			facade.advanceTime(theMazub, 0.18);
			windowLeft = facade.getVisibleWindowPosition(theWorld)[0];
			windowRight = facade.getVisibleWindowPosition(theWorld)[0] + 150;
			windowBottom = facade.getVisibleWindowPosition(theWorld)[1];
			windowTop = facade.getVisibleWindowPosition(theWorld)[1] + 120;
			assertTrue(((windowLeft >= 100) && (windowLeft < 200)) ||
					((windowRight > 100) && (windowRight <= 200)));
			assertTrue(((windowBottom >= 70) && (windowBottom < 120)) ||
					((windowTop > 70) && (windowTop <= 120)));
			actualScore += 15;
		}
	}

	@Test
	public void advanceTime_IllegalTime() throws Exception {
		maximumScore += 3;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		facade.startJump(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertThrows(ModelException.class,
				() -> facade.advanceTime(mazub_100_0, Double.NaN));
		actualScore += 3;
	}

	@Test
	public void getCurrentSprite_Scenario() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			World broadWorld = facade.createWorld(10, 1000, 200, new int[] { 10, 20 },
					200, 100);
			Mazub mazubToPlayWith = facade.createMazub(850, 0, mazubSprites);
			facade.addGameObject(mazubToPlayWith, broadWorld);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazubToPlayWith));
			facade.startMoveLeft(mazubToPlayWith);
			for (int i = 1; i < 12; i++) {
				// Mazub changes sprite every 0.075 seconds.
				facade.advanceTime(mazubToPlayWith, 0.08);
				assertEquals(mazubSprites[13 + i % 5],
						facade.getCurrentSprite(mazubToPlayWith));
			}
			facade.startDuck(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.15);
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazubToPlayWith));
			facade.endDuck(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.095);
			assertEquals(mazubSprites[14], facade.getCurrentSprite(mazubToPlayWith));
			facade.endMove(mazubToPlayWith);
			assertEquals(mazubSprites[3], facade.getCurrentSprite(mazubToPlayWith));
			for (int i = 1; i <= 6; i++) {
				facade.advanceTime(mazubToPlayWith, 0.15);
				assertEquals(mazubSprites[3], facade.getCurrentSprite(mazubToPlayWith));
			}
			facade.advanceTime(mazubToPlayWith, 0.12);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazubToPlayWith));
			facade.startMoveLeft(mazubToPlayWith);
			facade.startJump(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.15);
			assertEquals(mazubSprites[5], facade.getCurrentSprite(mazubToPlayWith));
			facade.endJump(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.07);
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazubToPlayWith));
			actualScore += 15;
		}
	}

	@Test
	public void getCurrentSprite_NewSpriteTooLarge() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			facade.setGeologicalFeature(world_250_400, 205, 10, SOLID_GROUND);
			facade.addGameObject(mazub_100_0, world_250_400);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
			facade.startMoveRight(mazub_100_0);
			// Mazub needs 0.141 seconds to reach the impassable terrain.
			// We invoke advanceTime twice to force a change of sprite half-way the move.
			// Because the move has ended, the sprite reflects the end of the move.
			facade.advanceTime(mazub_100_0, 0.08);
			facade.advanceTime(mazub_100_0, 0.08);
			assertEquals(mazubSprites[2], facade.getCurrentSprite(mazub_100_0));
			// After 1 second mazub should switch to stationary sprite, but that would
			// cause an overlap with stationary terrain.
			for (int i = 1; i <= 7; i++)
				facade.advanceTime(mazub_100_0, 0.15);
			assertEquals(mazubSprites[2], facade.getCurrentSprite(mazub_100_0));
			// After moving to the left for 0.05 seconds, mazub is still not able to
			// switch to the stationary sprite.
			facade.startMoveLeft(mazub_100_0);
			facade.advanceTime(mazub_100_0, 0.05);
			facade.endMove(mazub_100_0);
			for (int i = 1; i <= 7; i++)
				facade.advanceTime(mazub_100_0, 0.15);
			assertEquals(mazubSprites[3], facade.getCurrentSprite(mazub_100_0));
			facade.advanceTime(mazub_100_0, 0.01);
			// After moving to the left for another 0.05 seconds, mazub is able to
			// switch to the stationary sprite.
			facade.startMoveLeft(mazub_100_0);
			facade.advanceTime(mazub_100_0, 0.05);
			facade.endMove(mazub_100_0);
			for (int i = 1; i <= 7; i++)
				facade.advanceTime(mazub_100_0, 0.15);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
			actualScore += 15;
		}
	}

	/*******************
	 * Tests for Plant *
	 *******************/

	@Test
	void createPlant_LegalCase() throws Exception {
		maximumScore += 8;
		Plant newPlant = facade.createPlant(10, 6, plantSprites);
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newPlant));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newPlant),
				HIGH_PRECISION);
		assertTrue(facade.getOrientation(newPlant) < 0);
		assertArrayEquals(new double[] { -0.5, 0.0 }, facade.getVelocity(newPlant),
				LOW_PRECISION);
		assertEquals(1, facade.getHitPoints(newPlant));
		if (facade.isTeamSolution()) {
			assertArrayEquals(plantSprites, facade.getSprites(newPlant));
			assertEquals(plantSprites[0], facade.getCurrentSprite(plant_120_10));
		}
		actualScore += 8;
	}

	@Test
	void createPlant_IllegalSprites() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 4;
			// Array of sprites must be effective.
			assertThrows(ModelException.class,
					() -> facade.createPlant(5, 10, (Sprite[]) null));
			// Array of sprites must have exactly 2 elements.
			assertThrows(ModelException.class, () -> facade.createPlant(5, 10,
					new Sprite[] { new Sprite("a", 1, 2) }));
			assertThrows(ModelException.class,
					() -> facade.createPlant(5, 10, new Sprite[] { new Sprite("a", 1, 2),
							new Sprite("b", 2, 3), new Sprite("c", 3, 4) }));
			// Array of sprites must have effective elements.
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, new Sprite[] { null, null }));
			actualScore += 4;
		}
	}

	@Test
	void advanceTimePlant_NoOrientationSwitch() throws Exception {
		maximumScore += 8;
		facade.addGameObject(plant_120_10, world_250_400);
		facade.advanceTime(plant_120_10, 0.1);
		// The plant will have moved 5 cm to the left after 0.1 seconds
		assertArrayEquals(new double[] { 1.15, 0.1 },
				facade.getActualPosition(plant_120_10));
		if (facade.isTeamSolution())
			assertEquals(plantSprites[0], facade.getCurrentSprite(plant_120_10));
		actualScore += 8;
	}

	@Test
	void advanceTimePlant_OrientationSwitch() throws Exception {
		maximumScore += 8;
		facade.addGameObject(plant_120_10, world_250_400);
		for (int i = 1; i <= 4; i++)
			facade.advanceTime(plant_120_10, 0.15);
		// The plant will have moved 25 cm to the left after 0.5 seconds and 5 cm to
		// the right after and additional 0.1 seconds.
		assertArrayEquals(new double[] { 1.0, 0.1 },
				facade.getActualPosition(plant_120_10));
		if (facade.isTeamSolution())
			assertEquals(plantSprites[1], facade.getCurrentSprite(plant_120_10));
		actualScore += 8;
	}

	@Test
	void advanceTimePlant_OverlapHungryMazub() throws Exception {
		maximumScore += 12;
		facade.addGameObject(mazub_0_0, world_250_400);
		facade.addGameObject(plant_120_10, world_250_400);
		for (int i = 1; i <= 2; i++)
			facade.advanceTime(plant_120_10, 0.15);
		assertArrayEquals(new double[] { 1.05, 0.1 },
				facade.getActualPosition(plant_120_10));
		facade.advanceTime(plant_120_10, 0.15);
		assertTrue(facade.isTerminatedGameObject(plant_120_10));
		assertNull(facade.getWorld(plant_120_10));
		assertEquals(150, facade.getHitPoints(mazub_0_0));
		actualScore += 12;
	}

	@Test
	void advanceTimePlant_PlantDying() throws Exception {
		maximumScore += 12;
		facade.addGameObject(plant_120_10, world_250_400);
		// The plant is still alive after 9.9 seconds of game time.
		for (int i = 1; i <= 66; i++)
			facade.advanceTime(plant_120_10, 0.15);
		assertFalse(facade.isDeadGameObject(plant_120_10));
		// The plant dies after 10 seconds of game time.
		facade.advanceTime(plant_120_10, 0.15);
		assertTrue(facade.isDeadGameObject(plant_120_10));
		assertFalse(facade.isTerminatedGameObject(plant_120_10));
		assertEquals(world_250_400,facade.getWorld(plant_120_10));
		// The plant no longer moves onze it has died.
		double[] oldPosition = facade.getActualPosition(plant_120_10);
		facade.advanceTime(plant_120_10, 0.15);
		assertArrayEquals(oldPosition,facade.getActualPosition(plant_120_10));
		// The plant is destroyed 0.6 seconds after its dead.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(plant_120_10, 0.15);
		assertTrue(facade.isTerminatedGameObject(plant_120_10));
		assertNull(facade.getWorld(plant_120_10));
		actualScore += 12;
	}
	
	@Test
	void advanceTimePlant_PlantLeavingWorld() throws Exception {
		maximumScore += 8;
		Plant thePlant = facade.createPlant(15, 20, plantSprites);
		facade.addGameObject(thePlant, world_250_400);
		// The plant will leave its world after 0.3 seconds
		facade.advanceTime(thePlant, 0.15);
		facade.advanceTime(thePlant, 0.14);
		assertFalse(facade.isTerminatedGameObject(thePlant));
		assertEquals(world_250_400,facade.getWorld(thePlant));
		facade.advanceTime(thePlant, 0.03);
		assertTrue(facade.isTerminatedGameObject(thePlant));
		assertNull(facade.getWorld(thePlant));
		assertFalse(facade.getAllGameObjects(world_250_400).contains(thePlant));
		actualScore += 8;
	}
		


	/*******************
	 * Tests for World *
	 *******************/

	public final static int AIR = 0;
	public final static int SOLID_GROUND = 1;
	public final static int WATER = 2;
	public final static int MAGMA = 3;

	@Test
	void createWorld_AllDefaultGeologicalFeatures() throws Exception {
		maximumScore += 12;
		int[] targetTile = { -3, 999 };
		World newWorld = facade.createWorld(5, 10, 20, targetTile, 10, 20);
		assertEquals(5, facade.getTileLength(newWorld));
		assertArrayEquals(new int[] { 10 * 5, 20 * 5 }, facade.getSizeInPixels(newWorld));
		assertTrue(facade.getAllGameObjects(newWorld).isEmpty());
		assertNull(facade.getMazub(newWorld));
		assertArrayEquals(targetTile, facade.getTargetTileCoordinate(newWorld));
		assertArrayEquals(new int[] { 10, 20 },
				facade.getVisibleWindowDimension(newWorld));
		if (facade.isTeamSolution())
			assertArrayEquals(new int[] { 0, 0 },
					facade.getVisibleWindowPosition(newWorld));
		for (int tileX = 0; tileX < 10; tileX++)
			for (int tileY = 0; tileY < 20; tileY++)
				assertEquals(AIR,
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 12;
	}

	@Test
	void createWorld_AllExplicitGeologicalFeatures() throws Exception {
		maximumScore += 8;
		int[] geologicalFeatures = new int[] { AIR, SOLID_GROUND, WATER, WATER, MAGMA,
				AIR };
		World newWorld = facade.createWorld(5, 2, 3, new int[] { 0, 1 }, 4, 5,
				geologicalFeatures);
		for (int tileY = 0; tileY < 3; tileY++)
			for (int tileX = 0; tileX < 2; tileX++)
				assertEquals(geologicalFeatures[tileY * 2 + tileX],
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 8;
	}

	@Test
	void createWorld_SomeExplicitGeologicalFeatures() throws Exception {
		maximumScore += 4;
		int[] geologicalFeatures = new int[] { AIR, SOLID_GROUND, WATER, MAGMA };
		World newWorld = facade.createWorld(5, 2, 3, new int[] { 1, 2 }, 3, 3,
				geologicalFeatures);
		for (int tileY = 0; tileY < 3; tileY++)
			for (int tileX = 0; tileX < 2; tileX++)
				assertEquals(
						tileY * 2 + tileX < geologicalFeatures.length
								? geologicalFeatures[tileY * 2 + tileX]
								: AIR,
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 4;
	}

	@Test
	void createWorld_IllegalWindow() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 3;
			assertThrows(ModelException.class,
					() -> facade.createWorld(5, 2, 3, new int[] { 1, 1 }, 20, 30));
			actualScore += 3;
		}
	}

	@Test
	void createWorld_IllegalDimensions() throws Exception {
		maximumScore += 3;
		World newWorld = facade.createWorld(-10, 100, 200, new int[] { 10, 20 }, 10, 15);
		assertTrue(facade.getTileLength(newWorld) >= 0);
		newWorld = facade.createWorld(10, -100, 200, new int[] { 10, 20 }, 20, 30);
		assertTrue(facade.getSizeInPixels(newWorld)[0] >= 0);
		newWorld = facade.createWorld(10, 100, -200, new int[] { 10, 20 }, 10, 20);
		assertTrue(facade.getSizeInPixels(newWorld)[1] >= 0);
		actualScore += 3;
	}

	@Test
	void createWorld_IllegalGeologicalFeatures() throws Exception {
		maximumScore += 3;
		int[] geologicalFeatures = new int[] { AIR, -4, WATER, 12 };
		World newWorld = facade.createWorld(5, 2, 3, new int[] { 4, 7 }, 2, 2,
				geologicalFeatures);
		for (int tileY = 0; tileY < 3; tileY++)
			for (int tileX = 0; tileX < 2; tileX++)
				assertEquals(tileY * 2 + tileX == 2 ? WATER : AIR,
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 3;
	}

	@Test
	void getGeologicalFeatureAt_IllegalCases() throws Exception {
		maximumScore += 2;
		int tileSize = facade.getTileLength(world_100_200);
		// Any value can be returned outside the boundaries.
		facade.getGeologicalFeature(world_100_200, -tileSize - 1, 10);
		facade.getGeologicalFeature(world_100_200, 12, 200 * tileSize);
		actualScore += 2;
	}

	@Test
	void setGeologicalFeatureAt_LegalCase() throws Exception {
		maximumScore += 2;
		facade.setGeologicalFeature(world_100_200, 50, 100, MAGMA);
		assertEquals(MAGMA, facade.getGeologicalFeature(world_100_200, 50, 100));
		actualScore += 2;
	}

	@Test
	void setGeologicalFeatureAt_IllegalCases() throws Exception {
		maximumScore += 2;
		int tileSize = facade.getTileLength(world_100_200);
		facade.setGeologicalFeature(world_100_200, 50, 100, -20);
		assertTrue(facade.getGeologicalFeature(world_100_200, 50, 100) >= AIR);
		assertTrue(facade.getGeologicalFeature(world_100_200, 50, 100) <= MAGMA);
		facade.setGeologicalFeature(world_100_200, -tileSize - 1, 10, WATER);
		facade.setGeologicalFeature(world_100_200, 12, 200 * tileSize, SOLID_GROUND);
		actualScore += 2;
	}

	@Test
	void addGameObject_LegalCasePassableTerrain() throws Exception {
		maximumScore += 15;
		Mazub some_mazub = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(some_mazub, world_100_200);
		assertTrue(facade.hasAsGameObject(some_mazub, world_100_200));
		assertEquals(1, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(some_mazub));
		assertEquals(world_100_200, facade.getWorld(some_mazub));
		assertEquals(some_mazub, facade.getMazub(world_100_200));
		actualScore += 15;
	}

	@Test
	void addGameObject_SimpleIllegalCases() throws Exception {
		maximumScore += 3;
		// Non effective game object
		assertThrows(ModelException.class,
				() -> facade.addGameObject(null, world_100_200));
		// Terminated game object
		facade.terminateGameObject(mazub_0_0);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_0_0, world_100_200));
		// Terminated world.
		facade.terminateWorld(world_100_200);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, world_100_200));
		actualScore += 3;
	}

	@Test
	void addGameObject_ObjectInOtherWorld() throws Exception {
		maximumScore += 3;
		facade.addGameObject(mazub_20_45, world_250_400);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, world_100_200));
		actualScore += 3;
	}

	@Test
	void addGameObject_AtWorldBoundaries() throws Exception {
		maximumScore += 10;
		World small_world = facade.createWorld(2, 5, 8, new int[] { 3, 3 }, 4, 7);
		// Just inside boundaries
		facade.addGameObject(facade.createMazub(9, 6, mazubSprites), small_world);
		World other_small_world = facade.createWorld(2, 5, 8, new int[] { 10, 20 }, 4, 6);
		facade.addGameObject(facade.createMazub(3, 15, mazubSprites), other_small_world);
		// Just outside the boundaries
		World yet_another_small_world = facade.createWorld(2, 5, 8, new int[] { 12, 33 },
				3, 9);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, yet_another_small_world));
		World small_horizontal_world = facade.createWorld(10, 10, 50,
				new int[] { 10, 20 }, 4, 8);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_100_0, small_horizontal_world));
		World small_vertical_world = facade.createWorld(5, 100, 9, new int[] { 10, 20 },
				20, 4);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, small_vertical_world));
		actualScore += 10;
	}

	@Test
	void addGameObject_AdjacentToImpassableTerrain() throws Exception {
		maximumScore += 10;
		int width = mazubSprites[0].getWidth();
		int height = mazubSprites[0].getHeight();
		// Adjacent left and right
		facade.setGeologicalFeature(world_100_200, 220, 330, SOLID_GROUND);
		facade.addGameObject(facade.createMazub(220 - width, 335, mazubSprites),
				world_100_200);
		facade.addGameObject(facade.createMazub(230, 335, mazubSprites), world_100_200);
		// Adjacent bottom and top
		facade.setGeologicalFeature(world_100_200, 220, 530, SOLID_GROUND);
		facade.addGameObject(facade.createMazub(225, 530 - height, mazubSprites),
				world_100_200);
		facade.addGameObject(facade.createMazub(225, 540, mazubSprites), world_100_200);
		// Overlapping with top row of impassable terrain.
		facade.setGeologicalFeature(world_100_200, 220, 630, SOLID_GROUND);
		facade.addGameObject(facade.createMazub(225, 639, mazubSprites), world_100_200);
		actualScore += 10;
	}

	@Test
	void addGameObject_MazubOnImpassableTerrain() throws Exception {
		maximumScore += 8;
		facade.setGeologicalFeature(world_100_200, 220, 330, SOLID_GROUND);
		int width = mazubSprites[0].getWidth();
		int height = mazubSprites[0].getHeight();
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(220 - width + 2, 335, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(facade.createMazub(229, 335, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(225, 330 - height + 2, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(facade.createMazub(225, 338, mazubSprites),
						world_100_200));
		actualScore += 8;
	}

	@Test
	void addGameObject_PlantOnImpassableTerrain() throws Exception {
		maximumScore += 8;
		facade.setGeologicalFeature(world_100_200, 220, 330, SOLID_GROUND);
		Plant newPlant = facade.createPlant(210, 320, plantSprites);
		facade.addGameObject(newPlant, world_100_200);
		assertTrue(facade.hasAsGameObject(newPlant, world_100_200));
		assertEquals(world_100_200, facade.getWorld(newPlant));
		actualScore += 8;
	}

	@Test
	void addGameObject_AdjacentObjects() throws Exception {
		maximumScore += 10;
		Mazub mazubInWorld = facade.createMazub(140, 250, mazubSprites);
		facade.addGameObject(mazubInWorld, world_100_200);
		int width = mazubSprites[0].getWidth();
		int height = mazubSprites[0].getHeight();
		// Adjacent left and right
		facade.addGameObject(facade.createMazub(140 - width + 1, 270, mazubSprites),
				world_100_200);
		facade.addGameObject(facade.createMazub(140 + width - 1, 270, mazubSprites),
				world_100_200);
		mazubInWorld = facade.createMazub(40, 450, mazubSprites);
		facade.addGameObject(facade.createMazub(60, 450 - height + 1, mazubSprites),
				world_100_200);
		facade.addGameObject(facade.createMazub(60, 450 + height - 1, mazubSprites),
				world_100_200);
		actualScore += 10;

	}

	@Test
	void addGameObject_OverlappingObjects() throws Exception {
		maximumScore += 8;
		Mazub mazubInWorld = facade.createMazub(140, 250, mazubSprites);
		facade.addGameObject(mazubInWorld, world_100_200);
		int width = mazubSprites[0].getWidth();
		int height = mazubSprites[0].getHeight();
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(140 - width + 2, 270, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(140 + width - 2, 270, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(170, 250 - height + 2, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(170, 250 + height - 2, mazubSprites),
						world_100_200));
		actualScore += 8;
	}

	@Test
	void addGameObject_ActiveGameInWorld() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_0_0, world_100_200);
		facade.startGame(world_100_200);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, world_100_200));
		actualScore += 2;
	}

	@Test
	void addGameObject_SeveralMazubsAndPlants() throws Exception {
		maximumScore += 8;
		World bigWorld = facade.createWorld(5, 200, 110, new int[] { 10, 20 }, 50, 40);
		facade.addGameObject(mazub_0_0, bigWorld);
		for (int i = 0; i <= 3; i++) {
			facade.addGameObject(facade.createMazub((i + 1) * 100, i * 50, mazubSprites),
					bigWorld);
			facade.addGameObject(facade.createPlant(i * 50, (i + 2) * 50, plantSprites),
					bigWorld);
		}
		assertEquals(facade.getMazub(bigWorld), mazub_0_0);
		assertEquals(9, facade.getAllGameObjects(bigWorld).size());
		actualScore += 8;
	}

	@Test
	void addGameObject_TooManyObjects() throws Exception {
		maximumScore += 8;
		World bigWorld = facade.createWorld(5, 200, 110, new int[] { 10, 20 }, 50, 40);
		for (int i = 0; i <= 100; i++)
			facade.addGameObject(
					facade.createMazub((i % 10) * 100, (i / 10) * 50, mazubSprites),
					bigWorld);
		assertThrows(ModelException.class, () -> facade
				.addGameObject(facade.createMazub(100, 500, mazubSprites), bigWorld));
		actualScore += 8;
	}

	@Test
	void removeGameObject_LegalCase() throws Exception {
		maximumScore += 12;
		Mazub some_mazub = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(some_mazub, world_100_200);
		facade.removeGameObject(some_mazub, world_100_200);
		assertFalse(facade.hasAsGameObject(some_mazub, world_100_200));
		assertNull(facade.getWorld(some_mazub));
		assertNull(facade.getMazub(world_100_200));
		actualScore += 12;
	}

	@Test
	void removeGameObject_ObjectNotInWorld() throws Exception {
		maximumScore += 6;
		assertThrows(ModelException.class,
				() -> facade.removeGameObject(mazub_100_0, world_100_200));
		assertThrows(ModelException.class,
				() -> facade.removeGameObject(null, world_100_200));
		actualScore += 6;
	}

	@Test
	void getAllGameObjects_SeveralObjects() {
		maximumScore += 5;
		Mazub mazub1 = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(mazub1, world_100_200);
		Mazub mazub2 = facade.createMazub(600, 150, mazubSprites);
		facade.addGameObject(mazub2, world_100_200);
		Mazub mazub3 = facade.createMazub(500, 550, mazubSprites);
		facade.addGameObject(mazub3, world_100_200);
		assertEquals(3, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(mazub1));
		assertTrue(facade.getAllGameObjects(world_100_200).contains(mazub2));
		assertTrue(facade.getAllGameObjects(world_100_200).contains(mazub3));
		assertEquals(mazub1, facade.getMazub(world_100_200));
		facade.removeGameObject(mazub1, world_100_200);
		assertNull(facade.getMazub(world_100_200));
		actualScore += 5;
	}

	@Test
	void getAllGameObjects_LeakTest() {
		maximumScore += 15;
		Mazub mazub1 = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(mazub1, world_100_200);
		Mazub mazub2 = facade.createMazub(600, 150, mazubSprites);
		facade.addGameObject(mazub2, world_100_200);
		Set<Object> allGameObjects = facade.getAllGameObjects(world_100_200);
		allGameObjects.add(null);
		assertEquals(2, facade.getAllGameObjects(world_100_200).size());
		allGameObjects.remove(mazub1);
		assertTrue(facade.hasAsGameObject(mazub1, world_100_200));
		actualScore += 15;
	}

	@Test
	void getTargetTile_OnPassableTerrain() {
		maximumScore += 2;
		int[] targetTileCoords = new int[] { 10, 20 };
		World theWorld = facade.createWorld(10, 100, 200, targetTileCoords, 20, 10);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void getTargetTile_OnImpassableTerrain() {
		maximumScore += 2;
		int[] targetTileCoords = new int[] { 1, 2 };
		World theWorld = facade.createWorld(10, 10, 20, targetTileCoords, 20, 10,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void getTargetTile_OutsideWorld() {
		maximumScore += 2;
		int[] targetTileCoords = new int[] { -3, 2499 };
		World theWorld = facade.createWorld(10, 100, 200, targetTileCoords, 20, 10);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void setTargetTile_GameNotActive() {
		maximumScore += 2;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 1, 2 }, 20, 10);
		int[] targetTileCoords = new int[] { 4000, -100 };
		facade.setTargetTileCoordinate(theWorld, targetTileCoords);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void setTargetTile_GameActive() {
		maximumScore += 2;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 1, 2 }, 20, 10);
		facade.addGameObject(mazub_0_0, theWorld);
		facade.startGame(theWorld);
		int[] targetTileCoords = new int[] { 0, 0 };
		facade.setTargetTileCoordinate(theWorld, targetTileCoords);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void startGame_IllegalCase() {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.startGame(world_100_200));
		actualScore += 2;
	}

	@Test
	void isGameOver_WinningScenario() {
		maximumScore += 6;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 11, 1 }, 20, 10);
		facade.addGameObject(mazub_0_0, theWorld);
		assertFalse(facade.isGameOver(theWorld));
		assertFalse(facade.didPlayerWin(theWorld));
		facade.startGame(theWorld);
		assertFalse(facade.isGameOver(theWorld));
		assertFalse(facade.didPlayerWin(theWorld));
		// Mazub will reach the target tile after 0.1847 seconds.
		facade.startMoveRight(mazub_0_0);
		facade.advanceTime(mazub_0_0, 0.19);
		assertTrue(facade.isGameOver(theWorld));
		assertTrue(facade.didPlayerWin(theWorld));
		actualScore += 6;
	}

	@Test
	void isGameOver_LoosingScenario() {
		maximumScore += 6;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 30, 1 }, 20, 10);
		facade.addGameObject(mazub_0_0, theWorld);
		facade.startGame(theWorld);
		// Mazub will fall out of the world after 1.6 seconds.
		facade.startJump(mazub_0_0);
		for (int i = 1; i <= 10; i++)
			facade.advanceTime(mazub_0_0, 0.19);
		facade.advanceTime(mazub_0_0, 0.12);
		assertTrue(facade.isGameOver(theWorld));
		assertFalse(facade.didPlayerWin(theWorld));
		actualScore += 6;
	}

	@Test
	void advanceWorldTime_SimpleScenario() {
		maximumScore += 60;
		Mazub jumpingMazub = facade.createMazub(200, 75, mazubSprites);
		Plant plant1 = facade.createPlant(55, 380, plantSprites);
		Plant plant2 = facade.createPlant(210, 10, plantSprites);
		facade.setGeologicalFeature(world_250_400, 200, 70, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 15, 315, MAGMA);
		facade.setGeologicalFeature(world_250_400, 95, 20, WATER);
		facade.addGameObject(mazub_0_0, world_250_400);
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.addGameObject(jumpingMazub, world_250_400);
		facade.addGameObject(plant1, world_250_400);
		facade.addGameObject(plant2, world_250_400);
		assertEquals(5,facade.getAllGameObjects(world_250_400).size());
		facade.startGame(world_250_400);
		facade.startMoveRight(mazub_0_0);
		facade.startMoveLeft(jumpingMazub);
		facade.startJump(jumpingMazub);
		// After 0.1 seconds, the player mazub will bump against the stationary mazub.
		facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(100, facade.getHitPoints(mazub_0_0));
		assertEquals(mazubSprites[9], facade.getCurrentSprite(mazub_0_0));
		assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		assertArrayEquals(new double[] { 1.8955, 1.5 },
				facade.getActualPosition(jumpingMazub), LOW_PRECISION);
		assertEquals(mazubSprites[5], facade.getCurrentSprite(jumpingMazub));
		assertEquals(0.5, facade.getActualPosition(plant1)[0], LOW_PRECISION);
		assertEquals(plantSprites[0], facade.getCurrentSprite(plant1));
		assertEquals(2.05, facade.getActualPosition(plant2)[0], LOW_PRECISION);
		assertEquals(plantSprites[0], facade.getCurrentSprite(plant2));
		// After another 0.15 seconds, the stationary mazub will have eaten the second
		// plant, and the player mazub will have lost 2 hit points because of its
		// contact with water.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(4,facade.getAllGameObjects(world_250_400).size());
		assertEquals(0.1, facade.getActualPosition(mazub_0_0)[0], LOW_PRECISION);
		assertEquals(98, facade.getHitPoints(mazub_0_0));
		assertEquals(mazubSprites[2], facade.getCurrentSprite(mazub_0_0));
		assertEquals(150, facade.getHitPoints(mazub_100_0));
		assertTrue(facade.isTerminatedGameObject(plant2));
		assertArrayEquals(new double[] { 2.0 - 0.2781, 0.75 + 1.6875 },
				facade.getActualPosition(jumpingMazub), LOW_PRECISION);
		assertEquals(0.55 - 0.125, facade.getActualPosition(plant1)[0], LOW_PRECISION);
		// After a total playing time of 0.5 seconds, plant1 switches direction.
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(96, facade.getHitPoints(mazub_0_0));
		assertEquals(mazubSprites[2], facade.getCurrentSprite(mazub_0_0));
		assertArrayEquals(new double[] { 2.0 - 0.6861, 0.75 + 2.8875 },
				facade.getActualPosition(jumpingMazub), LOW_PRECISION);
		assertEquals(0.55 - 0.225, facade.getActualPosition(plant1)[0], LOW_PRECISION);
		// After about 1 second of total playing time, the jumping mazub will have eaten
		// the other plant.
		for (int i = 1; i <= 3; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(3,facade.getAllGameObjects(world_250_400).size());
		assertArrayEquals(new double[] { 2.0 - 1.45, 0.75 + 3.0 },
				facade.getActualPosition(jumpingMazub), LOW_PRECISION);
		assertEquals(150, facade.getHitPoints(jumpingMazub));
		assertTrue(facade.isTerminatedGameObject(plant1));
		// After another 0.2 seconds, jumping mazub falls into magma
		for (int i = 1; i <= 2; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		assertArrayEquals(new double[] { 2.0 - 1.848, 0.75 + 2.4 },
				facade.getActualPosition(jumpingMazub), LOW_PRECISION);
		assertEquals(100, facade.getHitPoints(jumpingMazub));
		// After 1.272 seconds of total playing time, the jumping mazub falls out of its
		// world.
		facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(2,facade.getAllGameObjects(world_250_400).size());
		assertTrue(facade.isTerminatedGameObject(jumpingMazub));
		assertNull(facade.getWorld(jumpingMazub));
		assertFalse(facade.getAllGameObjects(world_250_400).contains(jumpingMazub));
		assertEquals(100 - 6 * 2, facade.getHitPoints(mazub_0_0));
		// After 10 seconds in contact with water, the player will have no hit points
		// left.
		for (int i = 1; i <= 87; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		facade.advanceWorldTime(world_250_400, 0.1);
		assertTrue(facade.isDeadGameObject(mazub_0_0));
		// After another 0.6 seconds of playing time, the player must have been removed
		// from the game world, and the game has been lost.
		for (int i = 1; i <= 4; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_0_0));
		assertNull(facade.getWorld(mazub_0_0));
		assertTrue(facade.isGameOver(world_250_400));
		assertFalse(facade.didPlayerWin(world_250_400));
		actualScore += 60;
	}

}
