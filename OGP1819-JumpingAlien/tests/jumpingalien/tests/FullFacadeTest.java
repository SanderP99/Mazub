package jumpingalien.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.*;
import jumpingalien.model.Mazub;
import jumpingalien.facade.Facade;
import jumpingalien.facade.IFacade;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

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
	static IFacade facade = new Facade();

	// Variables referencing some prdefined mazub's
	private static Mazub mazub_0_0, mazub_100_0;

	// Variable referencing a proper arrays of sprites
	private static Sprite[] sprites;

	// Variables storing the actual score and the maximum score.
	private static int actualScore = 0;
	private static int maximumScore = 0;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Set up the array of sprites with several pictures to
		// move to the left and to the right.
		sprites = new Sprite[] { new Sprite("Stationary Idle", 100, 50),
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
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println();
		System.out.println("Score: " + actualScore + "/" + maximumScore);
	}

	@BeforeEach
	void setUp() throws Exception {
		// Set up the mazub's.
		mazub_0_0 = facade.createMazub(0, 0, sprites);
		mazub_100_0 = facade.createMazub(100, 0, sprites);
	}

	@Test
	void constructor_LegalCase() throws Exception {
		maximumScore += 18;
		Mazub newMazub = facade.createMazub(10, 6, sprites);
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newMazub));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newMazub),
				HIGH_PRECISION);
		assertTrue(facade.getOrientation(newMazub) == 0);
		assertFalse(facade.isMoving(newMazub));
		assertFalse(facade.isJumping(newMazub));
		assertFalse(facade.isDucking(newMazub));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(newMazub),
				HIGH_PRECISION);
		assertEquals(0.0 , facade.getAcceleration(newMazub)[1], HIGH_PRECISION);
		assertTrue((Math.abs(facade.getAcceleration(newMazub)[0]) <= HIGH_PRECISION) ||
				( (Math.abs(facade.getAcceleration(newMazub)[0]) >= 10.0-HIGH_PRECISION) &&
				  (Math.abs(facade.getAcceleration(newMazub)[0]) <= 10.0+HIGH_PRECISION)));
		if (facade.isTeamSolution()) {
			assertArrayEquals(sprites, facade.getSprites(newMazub));
			assertSame(sprites[0], facade.getCurrentSprite(newMazub));
		}
		actualScore += 18;
	}

	@Test
	void constructor_IllegalSprites() throws Exception {
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
	void constructor_LeakTest() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 10;
			Sprite[] clonedSprites = Arrays.copyOf(sprites, sprites.length);
			Mazub theMazub = facade.createMazub(10, 20, clonedSprites);
			clonedSprites[1] = null;
			Sprite[] mazubSprites = facade.getSprites(theMazub);
			assertArrayEquals(sprites, mazubSprites);
			actualScore += 10;
		}
	}

	@Test
	void getSprites_LeakTest() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 10;
			Sprite[] clonedSprites = Arrays.copyOf(sprites, sprites.length);
			Mazub theMazub = facade.createMazub(10, 20, clonedSprites);
			Sprite[] mazubSprites = facade.getSprites(theMazub);
			mazubSprites[3] = null;
			assertArrayEquals(clonedSprites, facade.getSprites(theMazub));
			actualScore += 10;
		}
	}

	@Test
	void changeActualPosition_LegalCase() throws Exception {
		maximumScore += 2;
		facade.changeActualPosition(mazub_0_0, new double[] { 2.04, 3.36 });
		assertArrayEquals(new double[] { 2.04, 3.36 },
				facade.getActualPosition(mazub_0_0), HIGH_PRECISION);
		actualScore += 2;
	}

	@Test
	void changeActualPosition_IllegalCases() throws Exception {
		maximumScore += 8;
		assertThrows(ModelException.class,
				() -> facade.changeActualPosition(mazub_0_0, null));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { 1.0, 2.0, 3.0 }));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { Double.NaN, 33.6 }));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { 33.6, Double.POSITIVE_INFINITY }));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { -1.0, 33.6 }));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { WORLD_WIDTH + 0.1, 33.6 }));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { 10.0, -0.5 }));
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { 10.8, WORLD_HEIGHT + 0.1 }));
		actualScore += 8;
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
			assertEquals(sprites[13], facade.getCurrentSprite(mazub_100_0));
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
			assertEquals(sprites[8], facade.getCurrentSprite(mazub_100_0));
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
			assertEquals(sprites[3], facade.getCurrentSprite(mazub_100_0));
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
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void startJUmp_AlreadyJumping() throws Exception {
		maximumScore += 2;
		facade.startJump(mazub_100_0);
		assertThrows(ModelException.class, () -> facade.startJump(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endJump_PositiveVelocity() throws Exception {
		maximumScore += 4;
		facade.startJump(mazub_100_0);
		facade.endJump(mazub_100_0);
		assertFalse(facade.isJumping(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void endJump_NegativeVelocity() throws Exception {
		maximumScore += 4;
		facade.startJump(mazub_100_0);
		// Advance time for more than 0.8 seconds such that velocity gets negative.
		for (int i = 1; i <= 6; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.getVelocity(mazub_100_0)[1] < 0.0);
		double[] oldVelocity = facade.getVelocity(mazub_100_0);
		facade.endJump(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isJumping(mazub_100_0));
		// Mazub's jump is finished, but it continues its fall with the
		// velocity at the time the jump was ended.
		assertArrayEquals(oldVelocity, facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		actualScore += 4;
	}

	@Test
	public void endJUmp_NotJumping() throws Exception {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.endJump(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startDuck_StationaryMazub() throws Exception {
		maximumScore += 4;
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		// Mazub is still stationary (only ducking), so its velocity and its acceleration
		// are 0.0
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[1], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void startDuck_AlreadyDucking() throws Exception {
		maximumScore += 2;
		facade.startDuck(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(sprites[1], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startDuck_MazubMovingAndJumping() throws Exception {
		maximumScore += 2;
		facade.startMoveLeft(mazub_100_0);
		facade.startJump(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { -1.0, 8.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[7], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_MazubDucking() throws Exception {
		maximumScore += 2;
		facade.startMoveLeft(mazub_100_0);
		facade.advanceTime(mazub_100_0, 0.15);
		facade.startDuck(mazub_100_0);
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { -1.0, 0.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[13], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_NotDucking() throws Exception {
		maximumScore += 2;
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void advanceTime_StationaryMazub() throws Exception {
		maximumScore += 4;
		facade.advanceTime(mazub_100_0, 0.15);
		assertArrayEquals(new double[] { 1.0, 0.0 },
				facade.getActualPosition(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_100_0),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void advanceTime_MazubMovingLeftWithinBoundaries() throws Exception {
		maximumScore += 6;
		facade.startMoveLeft(mazub_100_0);
		// Mazub will move 14.882 cm over 0.14 seconds; its velocity increases with 0.126
		// m/s
		facade.advanceTime(mazub_100_0, 0.14);
		assertEquals(0.85118, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(-1.126, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[14], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubMovingRightWithinBoundaries() throws Exception {
		maximumScore += 3;
		facade.startMoveRight(mazub_100_0);
		// Mazub will move 18.3 cm over 0.17 seconds; its velocity increases with 0.153
		// m/s
		facade.advanceTime(mazub_100_0, 0.17);
		assertEquals(1.183, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.153, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[10], facade.getCurrentSprite(mazub_100_0));
		actualScore += 3;
	}

	@Test
	public void advanceTime_MazubReachingLeftBorder() throws Exception {
		maximumScore += 5;
		facade.changeActualPosition(mazub_100_0, new double[] { 0.05, 0.0 });
		facade.startMoveLeft(mazub_100_0);
		// Note:
		// If Mazub reaches a border and keeps on moving, its position will no
		// longer change, but its velocity keeps on increasing with time.
		// Mazub reaches left border after 0.1024 seconds.
		// Its velocity increases with 0.171 m/s over the entire period of 0.19 seconds.
		facade.advanceTime(mazub_100_0, 0.19);
		assertEquals(0.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(-1.0 - 0.171, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		// Mazub will not move beyond the left border; its velocity further increases
		// with 0.108 m/s over 0.12 seconds.
		facade.advanceTime(mazub_100_0, 0.12);
		assertEquals(0.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(-1.0 - 0.171 - 0.108, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[17], facade.getCurrentSprite(mazub_100_0));
		actualScore += 5;
	}

	@Test
	public void advanceTime_MazubReachingRightBorder() throws Exception {
		maximumScore += 3;
		facade.changeActualPosition(mazub_100_0,
				new double[] { WORLD_WIDTH - 0.07, 0.0 });
		facade.startMoveRight(mazub_100_0);
		// Mazub reaches right border after 0.1447 seconds; its velocity increases with
		// 0.171 m/s over 0.19 seconds.
		facade.advanceTime(mazub_100_0, 0.19);
		assertEquals(WORLD_WIDTH, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(1.0 + 0.171, facade.getVelocity(mazub_100_0)[0], LOW_PRECISION);
		// Mazub will not move beyond the right border; its velocity further increases
		// with 0.072 m/s.
		facade.advanceTime(mazub_100_0, 0.08);
		assertEquals(WORLD_WIDTH, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(1.0 + 0.171 + 0.072, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[11], facade.getCurrentSprite(mazub_100_0));
		actualScore += 3;
	}

	@Test
	public void advanceTime_MazubReachingTopSpeed() throws Exception {
		maximumScore += 8;
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
			assertEquals(sprites[9], facade.getCurrentSprite(mazub_100_0));
		actualScore += 8;
	}

	@Test
	public void advanceTime_MazubJumpingUpwardsOnly() throws Exception {
		maximumScore += 6;
		facade.startJump(mazub_100_0);
		// Mazub will jump over 108.75 cm in 0.15 seconds; it will have a velocity of
		// 6.5m/s
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(1.0875, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(6.5, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingTop() throws Exception {
		maximumScore += 6;
		facade.changeActualPosition(mazub_100_0,
				new double[] { 1.0, WORLD_HEIGHT - 0.8 });
		facade.startJump(mazub_100_0);
		// Note:
		// If Mazub reaches a border and keeps on jumping, its position will no
		// longer change, but its velocity keeps changing with time.
		// Mazub needs 0.107 seconds to reach the top of the world; after a jump of
		// 0.15 seconds its velocity will have dropped to 6.5 m/s
		facade.advanceTime(mazub_100_0, 0.15);
		assertEquals(WORLD_HEIGHT, facade.getActualPosition(mazub_100_0)[1],
				LOW_PRECISION);
		assertEquals(6.5, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		// If time advances over 0.1 seconds, Mazub will stay at the top, and its velocity
		// further changes to 5.5 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(WORLD_HEIGHT, facade.getActualPosition(mazub_100_0)[1],
				LOW_PRECISION);
		assertEquals(5.5, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAndFallingDown() throws Exception {
		maximumScore += 6;
		// Mazub starts falling after 0.8 seconds; it will then have jumped over 320 cm;
		// its velocity is reduced to 0 m/s.
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
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingGround() throws Exception {
		maximumScore += 6;
		// Mazub reaches ground after 1.6 seconds; its velocity is changed to -8.0 m/s
		facade.startJump(mazub_100_0);
		for (int i = 1; i <= 10; i++)
			facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(0.0, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(-8.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		// If time advances over 0.1 seconds, Mazub will stay at the bottom, and its
		// velocity
		// further changes to -9.0 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(0.0, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(-9.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubFalling() throws Exception {
		maximumScore += 6;
		facade.changeActualPosition(mazub_100_0, new double[] { 1.0, 0.25 });
		// After 0.1 seconds, Mazub will have fallen over 5 cm; its velocity has changed
		// to -1 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertArrayEquals(new double[] { 1.0, 0.25 - 0.05 },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		assertEquals(-1.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubMovingRightAndDucking() throws Exception {
		maximumScore += 12;
		facade.startMoveRight(mazub_100_0);
		// After 0.14 seconds, mazub will have moved over 14.882 cm; its velocity is
		// raised
		// to 1.126 m/s. After the duck, the velocity is reset to 1 m/s
		facade.advanceTime(mazub_100_0, 0.14);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertEquals(1.1488, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[6], facade.getCurrentSprite(mazub_100_0));
		// After another 0.1 seconds, mazub has moved further over 10 cm. Its velocity is
		// unchanged.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.1488 + 0.1, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[6], facade.getCurrentSprite(mazub_100_0));
		// Ending the duck changes the sprite.
		facade.endDuck(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(sprites[8], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isDucking(mazub_100_0));
		// After another 0.16 seconds, mazub has moved further over 17.152 cm. Its
		// velocity is raised to 1.144 m/s.
		facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(1.1488 + 0.1 + 0.17152, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(1.144, facade.getVelocity(mazub_100_0)[0], HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[10], facade.getCurrentSprite(mazub_100_0));
		actualScore += 12;
	}


	@Test
	public void advanceTime_MazubMovingLeftAndDucking() throws Exception {
		maximumScore += 4;
		facade.startMoveLeft(mazub_100_0);
		// After 0.14 seconds, mazub will have moved over 14.882 cm; its velocity is
		// raised
		// to 1.126 m/s. After the duck, the velocity is reset to 1 m/s
		facade.advanceTime(mazub_100_0, 0.14);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertEquals(1.0 - 0.1488, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[7], facade.getCurrentSprite(mazub_100_0));
		// After another 0.1 seconds, mazub has moved further over 10 cm. Its velocity is
		// unchanged.
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.0 - 0.1488 - 0.1, facade.getActualPosition(mazub_100_0)[0],
				LOW_PRECISION);
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_0)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[7], facade.getCurrentSprite(mazub_100_0));
		// Ending the duck changes the sprite.
		facade.endDuck(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(sprites[13], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isDucking(mazub_100_0));
		// After another 0.16 seconds, mazub has moved further over 17.152 cm. Its
		// velocity is raised to 1.144 m/s.
		facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(1.0 - 0.1488 - 0.1 - 0.17152,
				facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(-1.144, facade.getVelocity(mazub_100_0)[0], HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[15], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void advanceTime_MazubMovingAndJumping() throws Exception {
		maximumScore += 10;
		facade.startMoveRight(mazub_100_0);
		facade.startJump(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(sprites[4], facade.getCurrentSprite(mazub_100_0));
		// Mazub will move over 14.822 cm in horizontal direction and over 102.2 cm in
		// vertical direction.
		// It's speed will have changed to 1.126 m/s in horizontal direction and to 6.6
		// m/s in vertical direction.
		facade.advanceTime(mazub_100_0, 0.14);
		assertArrayEquals(new double[] { 1.14822, 1.022 },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		assertArrayEquals(new double[] { 1.126, 6.6 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sprites[4], facade.getCurrentSprite(mazub_100_0));
		actualScore += 10;
	}

	@Test
	public void advanceTime_IllegalTime() throws Exception {
		maximumScore += 3;
		facade.startMoveLeft(mazub_100_0);
		facade.startJump(mazub_100_0);
		facade.startDuck(mazub_100_0);
		facade.advanceTime(mazub_100_0, Double.NaN);
		double[] newPosition = facade.getActualPosition(mazub_100_0);
		assertTrue(newPosition[0] >= 0.0 && newPosition[0] <= 1023.0);
		assertTrue(newPosition[1] >= 0.0 && newPosition[0] <= 767.0);
		actualScore += 3;
	}

	@Test
	public void getCurrentSprite_Scenario() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
			facade.startMoveLeft(mazub_100_0);
			for (int i = 1; i < 12; i++) {
				facade.advanceTime(mazub_100_0, 0.08);
				assertEquals(sprites[13 + i % 5], facade.getCurrentSprite(mazub_100_0));
			}
			facade.startJump(mazub_100_0);
			facade.advanceTime(mazub_100_0, 0.15);
			assertEquals(sprites[5], facade.getCurrentSprite(mazub_100_0));
			facade.endJump(mazub_100_0);
			facade.advanceTime(mazub_100_0, 0.07);
			assertEquals(sprites[13], facade.getCurrentSprite(mazub_100_0));
			facade.startDuck(mazub_100_0);
			facade.advanceTime(mazub_100_0, 0.15);
			assertEquals(sprites[7], facade.getCurrentSprite(mazub_100_0));
			facade.endDuck(mazub_100_0);
			facade.advanceTime(mazub_100_0, 0.095);
			assertEquals(sprites[14], facade.getCurrentSprite(mazub_100_0));
			facade.endMove(mazub_100_0);
			for (int i = 1; i <= 6; i++)
				facade.advanceTime(mazub_100_0, 0.15);
			assertEquals(sprites[3], facade.getCurrentSprite(mazub_100_0));
			facade.advanceTime(mazub_100_0, 0.17);
			assertEquals(sprites[0], facade.getCurrentSprite(mazub_100_0));
			actualScore += 15;
		}
	}

}
