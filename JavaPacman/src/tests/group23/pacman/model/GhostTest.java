package group23.pacman.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class GhostTest {

    private final int SPRITE_HEIGHT = 30;
    private final int SPRITE_WIDTH = 30;
    private final int OFFSET = 10;

    private Random random;
    private Ghost testGhost;
    private Board bordTester;
    private Pacman pacman1;

    @Before
    public void setUp() throws Exception{
        random = new Random();

        int  x1= random.nextInt();
        int  y1= random.nextInt();
        int ghostNum = random.nextInt(6)+1;
        int  type = random.nextInt(3)+1;

        bordTester = new Board();
        testGhost = new Ghost(x1,y1,bordTester,type,ghostNum);
        pacman1 = new Pacman(x1,y1,bordTester);
    }

    @Test
    public void collidedWith() {
        assertTrue("The ghost is", testGhost.collidedWith(pacman1));
    }

    @Test
    public void setDirection(){
        char vectorTest = 'S';
        testGhost.setDirection(vectorTest);
        assertEquals('S',testGhost.getDirection());
    }
    
    @After
    public void tearDown() throws Exception {
        pacman1 = null;
        bordTester = null;
        testGhost = null;
    }
}