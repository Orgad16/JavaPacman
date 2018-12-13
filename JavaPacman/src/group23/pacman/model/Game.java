package group23.pacman.model;

import group23.pacman.model.Pacman.STATE;
import group23.pacman.system.SysData;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Random;
import java.util.stream.Collectors;

import static group23.pacman.model.Board.TILE_SIZE;
import static group23.pacman.model.Board.X_OFFSET;
import static group23.pacman.model.Board.Y_OFFSET;

/**The class that handles all the game logics - collisions, level handling, and creation of the map.
 */


public class Game {
	
	/* Each game has a main character */
	private Pacman pacman;
	
	/* Currently only one ghost, intend to change this to array list later */
	private Ghost ghost;
	private Ghost ghost2;
	private Ghost ghost3;
	//private Ghost ghost4;

	ArrayList<TemporaryGhost> temporaryGhosts = new ArrayList<>(3);

	//TODO: add temporary ghost
	
	/*Ghost A.I scatter behaviour */
	private int scatterScore;
	private boolean scatter;
	private boolean countDown;
		
	/* ArrayList to access other game objects */
	private ArrayList<GameObject> objects;
	
	/* Board to determine valid coordinates and movements */
	private Board board;
	
	/* Keeps track of user's game progress */
	private int score;
	
	/* Each game has a unique map */
	private int map;
	
	/* Keep track of number of players/game mode */
	private int numPlayers;
	
	/* Game has array list of moving objects */
	private ArrayList<MovingCharacter> characters;
	
	/* Gas zone which spawns every 20 seconds */
	private GasZone gasZone;
	
	/* Time */
	private long scatterTime = 0;
	
	/* Used to manipulate time for showing to screen */
	private Timer timer;
	
	/* Clear condition */
	private int pellets;
	private int pelletsEaten;

	/* Poison pellet which spawns every 20 seconds */
	private PoisonPellet poisonPellet;

	private QuestionPellet questionPellet;

	/*Adds coordinates after eating pellet*/
	private ArrayList<GameObject> emptySpaces = new ArrayList<>();

	private List<Question> questionList = SysData.instance.getQuestionsFromJson();

	public Game(int map,int numPlayers,int player2Ghost,int player3Ghost) {
		
		this.map = map;
		this.numPlayers = numPlayers;
		
		/* Initial score is 0 */
		score = 0;
		
		/* Create new board (with user selected map) to define valid coordinates */
		board = new Board();
		board.createBoard(map);
		
		/* Get reference to objects created on the board */
		objects = board.getObjects();
		
		/* Create gas zone */
		gasZone = new GasZone();
		
		/* Clear condition (number of pellets to eat) */
		pellets = board.getTotalPellets();
		pelletsEaten = 0;

		/* Set up character objects to add to ArrayList of MovingCharacter interface */
		characters = new ArrayList<MovingCharacter>();
		
		/* Only one pacman object will be created */
		pacman = new Pacman(board.getPacman()[0],board.getPacman()[1], board);

		poisonPellet = new PoisonPellet(0,0);
        questionPellet = new QuestionPellet(0,0,questionList);

		objects.add(poisonPellet);
		objects.add(questionPellet);

		setUpGhosts(player2Ghost,player3Ghost);
		
		/* Initialise the variables used to control the AI scatter behaviour */
		scatter = false;
		countDown = false;
		scatterScore = 0;

		/* Add all these moving characters to the array list */
		characters.add(pacman);
		characters.add(ghost);
		characters.add(ghost2);
		characters.add(ghost3);
	}

	/**
	 * this function will take the question pellet that the pacman ate and init the temp ghosts
	 * @param question the question the pacman ate
	 */
	private void setUpTempGhosts(QuestionPellet question) {

		//getting the question that the pacman ate
		Question q = question.getQuestion();
        List<Pair<Integer,Integer>> locations = movementLocations();
		Random rnd = new Random();
		for (int i=4; i<7; i++) {
		    Pair<Integer,Integer> loc = locations.get(rnd.nextInt(locations.size()));
            System.out.println(loc);
			// type field should by between 2 and 4 -> set to i-2
			// answer field should be between 0 and 2 -> set to i-4
			TemporaryGhost temp_ghost = new TemporaryGhost(loc.getKey() * TILE_SIZE + X_OFFSET ,loc.getValue() * TILE_SIZE + Y_OFFSET, board, i-2, i, q, i-4);
			characters.add(temp_ghost);
			temporaryGhosts.add(temp_ghost);
		}

	}

	/**
	 * this function will update the temp ghosts on board
	 */
	private void updateTempGhostsOnBoard() {
		for (TemporaryGhost temporaryGhost : temporaryGhosts) {
			temporaryGhost.update((int)pacman.getX(), (int)pacman.getY(), pacman.getDirection());
		}
	}


	// TODO: remove this function! not needed -> to setup the other users for the ghosts
	private void setUpGhosts(int player2Ghost,int player3Ghost) {
		
		Vector<Integer> vector = new Vector<Integer>(4);
		vector.add(1);
		vector.add(2);
		vector.add(3);
		vector.add(4);
		
		/* Set up ghosts according to game mode */
		if (numPlayers == 1) {
			ghost = new Ghost(board.getGhost()[0],board.getGhost()[1], board, 3,1);
			ghost2 = new Ghost(board.getGhost()[0],board.getGhost()[1], board, 2,2);
			ghost3 = new Ghost(board.getGhost()[0],board.getGhost()[1], board, 1,3);
		}
		
		else if (numPlayers == 2) {
			
			/* Get and set player ghost choices then remove them from remaining choices */
			vector.removeElement(player2Ghost);
			ghost = new Ghost(board.getGhost()[0],board.getGhost()[1], board, 0,player2Ghost);
			
			/* The AI's will now have the remaining ghost sprites not chosen by the players */
			ghost2 = new Ghost(board.getGhost()[0],board.getGhost()[1], board, 2,vector.elementAt(0));
			ghost3 = new Ghost(board.getGhost()[0],board.getGhost()[1], board, 1,vector.elementAt(1));
			//ghost4= new Ghost(board.getGhost()[0],board.getGhost()[1], board, 4,vector.elementAt(2));
		}


	}

	
	/* When updating the game state, we need to check for collisions before updating moving characters
	 * due to the nature of how we implemented the MovingCharacter interface */
	public void update( ) {
		
		checkCollisions();
		pacman.update();
		changeAIBehaviour();
		ghost.update((int)pacman.getX(), (int)pacman.getY(), pacman.getDirection());
		ghost2.update((int)pacman.getX(), (int)pacman.getY(), pacman.getDirection());
		ghost3.update((int)pacman.getX(), (int)pacman.getY(), pacman.getDirection());
		updateTempGhostsOnBoard();
		gasZone.update();
        poisonPellet.update(emptySpaces);
        questionPellet.update(emptySpaces);
		//TODO: remove the gasZone
	}

	public List<Pair<Integer,Integer>>  movementLocations(){
        List<Pair<Integer,Integer>> locations = board.getOnlyTurns();
        int x = (int) (pacman.getX() - X_OFFSET) / TILE_SIZE;
        int y = (int) (pacman.getY() - Y_OFFSET) / TILE_SIZE;

        // filter locations close to the pacman
        return locations.stream().filter(o -> ( x + 3 < o.getKey() && y + 3 < o.getValue())).collect(Collectors.toList());
    }


	/* Checks character movement collisions and player pellet collisions */
	private void checkCollisions() {
		
		for (MovingCharacter character : characters) {
			
			/* Checks for collision with a ghost. Lose a life when you collide. */
			if (character.getType() == GameObject.TYPE.GHOST) {
				/* If using whip, make them disappear (temporary) */
				if (pacman.getState() == STATE.POWER_UP) {
					if (pacman.getWhip().getHitBox().intersects(character.getHitBox()) && ((Ghost)character).getState() == Ghost.STATE.ALIVE){
						((Ghost) character).setState(Ghost.STATE.DEAD);
						score += 15;
					}
				}
				if (pacman.collidedWith((GameObject) character) && ((Ghost)character).getState() == Ghost.STATE.ALIVE) {
					pacman.playDeathAnim();
					gasZone.stopDrawing();
					return;
				}
			}
			
			/* Checks for collision with the gasZone */
			if (character.collidedWith(gasZone) && gasZone.getDrawGas()) {
				if (character.getType() == GameObject.TYPE.GHOST) {
				((Ghost) character).setState(Ghost.STATE.DEAD);
				}
				else {
					pacman.playDeathAnim();
					gasZone.stopDrawing();
				}
			}
			/* Restricts the character from moving into the spawn point after it has left the spawn point */
			if (character.getX() == 518 && character.getY() == 309) {
				character.setHasLeftSpawn();
			}
			
			/* If the currently queued direction is not equal to the current direction we are moving in, and it is possible 
			   for us to turn in our current (x,y) position, test if turn is valid (not into a wall), then set the queued direction
			   if valid. */
			if (character.checkforQueuedAction() && board.validTurningPoint((int) character.getX(), (int) character.getY())) {
				if (board.isValidDestination(character.getHasLeftSpawn(), character.getQDirection(), (int) character.getX(), (int) character.getY())){
					character.setDirection(character.getQDirection());
					character.updateDestination();
					return;
				}
			}
			if (board.isValidDestination(character.getHasLeftSpawn(), character.getDirection(), (int) character.getX(), (int) character.getY())) {
				character.updateDestination();
			}

			if (character.getType() == GameObject.TYPE.TEMP_GHOST) {
				if (pacman.collidedWith((GameObject) character) && ((TemporaryGhost)character).getState() == TemporaryGhost.STATE.ALIVE) {
					if (((TemporaryGhost) character).isRightGhost()) {
						//TODO: adjust the score based on the level of the question
						score += 250;
					}
					else {
						pacman.playDeathAnim();
					}
					characters.removeAll(temporaryGhosts);
					temporaryGhosts.removeAll(temporaryGhosts);
					return;
				}


					// TODO: do actions according to the collision
			}
			
		}
		
		/* Loops through the game objects to check if the player has collided with a pellet. Pellet is removed on collision */
		for (GameObject object : objects) {

			// TODO: add if for the other candies

			if (pacman.collidedWith(object)) {

				// collide with silver pellet
				if (object.getType() == GameObject.TYPE.SILVER_PELLET) {
					pacman.getWhip().addCharges();
				}

				// collide with pellet
				if (object.getType() == GameObject.TYPE.PELLET){
					pelletsEaten++;
					score += 10;

				}

				// collide with question pellet
				if (object.getType() == GameObject.TYPE.QUESTION_PELLET) {
					setUpTempGhosts((QuestionPellet) object);
				}

				// collide with poison pellet
				if (object.getType() == GameObject.TYPE.POISON_PELLET) {
					pacman.playDeathAnim();
					poisonPellet.stopDrawing();
				}
				emptySpaces.add(object);
				objects.remove(object);
				break;
			}
		}
	}
	

	/* Checks if pacman has died and resets all moving objects*/
	public void checkState() {
		
		if (pacman.getState() == Pacman.STATE.DEAD && pacman.getLives() > 0) {
			for (MovingCharacter character: characters) {
				if (character.getType() == GameObject.TYPE.PACMAN) {
					character.reset(board.getPacman()[0],board.getPacman()[1]);
				}
				else if (character.getType() == GameObject.TYPE.GHOST) {
					character.reset(board.getGhost()[0],board.getGhost()[1]);
				}
			}
		}
	}
	
	
	/* To change the movement behaviour to scatter for 5 seconds */
	public void changeAIBehaviour() {
		
		if (score>=scatterScore) {
			scatterScore = score + 600;
			scatter = true;
		}
		if (scatter && !countDown) {
			timer = new Timer(5);
			ghost.getAI().setChase(false);
			ghost2.getAI().setChase(false);
			ghost3.getAI().setChase(false);
			//ghost4.getAI().setChase(false);
			scatterTime = System.currentTimeMillis();
			countDown = true;
			scatter = false;
		}
		
		/* Count down 1 second */
		if (countDown) {
			if (System.currentTimeMillis() - scatterTime >= 1000) {
				timer.countDown(1);
				scatterTime = System.currentTimeMillis();
			}
		
			/* If 5 seconds is up, end scatter behaviour */
			if (timer.timedOut()) {
				ghost.getAI().setChase(true);
				ghost2.getAI().setChase(true);
				ghost3.getAI().setChase(true);
				//ghost4.getAI().setChase(true);
				countDown = false;
			}
		}

	}
	
	
	public boolean scoreBeaten() {
		
		ScoreHandler scoreHandler = new ScoreHandler();
		
		int scores[] = scoreHandler.getHighScores();
		
		return (score > scores[0] || score > scores[1] || score > scores[2]);

	}
	
	public void updateHighScores(String name) {
		
		ScoreHandler scoreHandler = new ScoreHandler();
		scoreHandler.writeScore(score, name, map);
	}
	
	
	
	/** ALL PUBLIC GETTERS BELOW **/

	/* Public getter to reference map type */
	public PoisonPellet getPoisonPellet() {

		return this.poisonPellet;
	}
	
	/* Public getter to reference Pacman object */
	public Pacman getPacman() {
		
		return this.pacman;
	}
	
	/* Public getter to reference ghost object(s) */
	public Ghost getGhost() {
		
		return this.ghost;
	}
	
	public Ghost getGhost2() {
		
		return this.ghost2;
	}
	
	public Ghost getGhost3() {
		
		return this.ghost3;
	}

	public ArrayList<TemporaryGhost> getTempGhost() {
		return temporaryGhosts;
	}

	//	public Ghost getGhost4() {
//
//		return this.ghost4;
//	}
	
	/* Public getter to reference other game objects (i.e walls, pellets ) */
	public ArrayList<GameObject> getOtherGameObjects() {
		
		return this.objects;
	}
	
	public boolean levelCleared() {
		
		return (this.pellets == pelletsEaten);
	}
	
	/* Public getter to reference map type */
	public int getMap() {
		
		return this.map;
	}
	
	/* Public getter to reference map type */
	public GasZone getGasZone() {
		
		return this.gasZone;
	}	
	
	/* Public getter to reference game mode */
	public int getPlayers() {
		
		return this.numPlayers;
	}
	
	public String getCharges() {
		
		String charges = Integer.toString(pacman.getWhip().getCharges());
		
        return charges;
		
	}
	
	/* Returns the user's score in string format */
	public String getScore() {
		
		String tempScore = Integer.toString(this.score);
		tempScore = new StringBuilder(tempScore).reverse().toString();
        while (tempScore.length() < 4){
           	tempScore = tempScore + "x";
        }
        return tempScore;
	}
	
	public int getIntScore() {
		
		return score;
	}


}
