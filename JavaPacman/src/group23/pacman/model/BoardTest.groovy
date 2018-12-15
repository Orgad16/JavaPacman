package group23.pacman.model

import org.junit.Before
import org.testng.annotations.Test

import java.util.Random;

class BoardTest extends GroovyTestCase {


    public static final int TILE_SIZE = 10;
    public static final int Y_OFFSET = 9;
    public static final int OFFSET = 1;

    private Random rand = new Random();



    @Test
    void testIsNode() {
        Board tester= new Board();

        /*random number to test the junit*/
        int x= rand.nextInt();
        int y= rand.nextInt();

        assertFalse("X: "+x+" Y: "+y+" is:",tester.isNode(x,y));
    }




}
