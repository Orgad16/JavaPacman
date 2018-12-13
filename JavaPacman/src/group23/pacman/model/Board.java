package group23.pacman.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import group23.pacman.system.SysData;
import javafx.util.Pair;

/**The class that deals with objects pertaining to the map. Has methods which determine
 * whether a moving character can traverse in a certain direction or along a certain path.
 */

public class Board {
	
	/* Constants - do not change */
	public static final int TILE_SIZE = 10;
	public static final int X_OFFSET = 158;
	public static final int Y_OFFSET = 9;
	public static final int OFFSET = 1;
	
	/* Adds objects to list for Game object to reference */
	private ArrayList<GameObject> objects;
	
	/* Grids for determining validity of path taken */
	private boolean[][] status;
	private boolean[][] node;
	private boolean[][] ghostOnlyPath;
	private List<Pair<Integer,Integer>> onlyTurns = new ArrayList<>();
	public boolean[][] getGhostOnlyPath() {
		return ghostOnlyPath;
	}

	/* Spawn point coordinates */
	private int[] ghostCoords;
	private int[] tempGhostsCoords;
	private int[] pacmanCoords;
	
	/* Keep track of clear condition */
	private int pellets;

	
	public Board() {
		
		/* Create the list and arrays of objects/states to be placed on the map */
		pacmanCoords = new int[2];
		ghostCoords = new int[2];
		tempGhostsCoords = new int[2];
		status = new boolean[75][75];
		node = new boolean[75][75];
		ghostOnlyPath = new boolean[75][75];
		objects = new ArrayList<GameObject>();
		pellets = 0 ;
	}
	
	
	public void createBoard(char map) {
		
		String line,mapTxt;
		
		/* Parse the map.txt file, loads the map into the game */
		try {
			switch (map) {
				case 'r' :
					mapTxt = "maps/ruins.txt";
					break;
				case 'f' :
					mapTxt = "maps/forest.txt";
					break;
				case 'd' :
					mapTxt = "maps/deserttemple.txt";
					break;
				case 's' :
					mapTxt = "maps/sea.txt";
					break;
				default :
					mapTxt = "maps/ruins.txt";
					break;
				
			}
			
			FileReader fileReader = new FileReader(mapTxt);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int row = 0;
			int position;
			/* Creates objects on the map based on their value in the text file
			 * 0 creates a wall
			 * P creates a pellet
			 * W creates a silver pellet
			 * 1 is an empty position
			 * R is a position that the character can be in but cannot turn 
			 * O is a position that the ghost can be in, but only if it has not left the spawn point.
			 * T is a turn node
			 * S is the spawn point of the main character
			 * G is the spawn point of a ghost
			 * Q is the question pellet
			 * Z is the position that the temp ghosts start from
			 */

			// getting the questions for the question pellets
			ArrayList<Question> questions = getQuestionsFromJson();
			int indexForQuestion = 0;

			while ((line = bufferedReader.readLine()) != null ) {
				position = 0;
				for (int i =0;i< line.length();i++) {
					if (line.charAt(i)==('0')) {
						Rectangle rect = new Rectangle();
						rect.setX(position*TILE_SIZE + X_OFFSET);
						rect.setY(row*TILE_SIZE + Y_OFFSET);
						rect.setWidth(TILE_SIZE);
						rect.setHeight(TILE_SIZE);
						Wall wall = new Wall(rect,map);
						status[position][row] = false;
						node[position][row] = false;
						ghostOnlyPath[position][row] = false;
						objects.add(wall);
						position++;
					}
					else if (line.charAt(i) == 'P') {
						Pellet pellet = new Pellet(position*TILE_SIZE + X_OFFSET,row*TILE_SIZE + Y_OFFSET);
						objects.add(pellet);
						status[position][row] = false;
						node[position][row] = false;
						ghostOnlyPath[position][row] = false;
						position++;
						pellets++;
					}
//					else if (line.charAt(i) == 'Q') {
//						QuestionPellet qPellet = new QuestionPellet(position*TILE_SIZE + X_OFFSET,row*TILE_SIZE + Y_OFFSET, questions.get(indexForQuestion));
//						objects.add(qPellet);
//						status[position][row] = false;
//						node[position][row] = false;
//						ghostOnlyPath[position][row] = false;
//						position++;
//						indexForQuestion++;
//					}
					else if (line.charAt(i) == 'W') {
						SilverPellet sPellet = new SilverPellet(position*TILE_SIZE + X_OFFSET,row*TILE_SIZE + Y_OFFSET);
						objects.add(sPellet);
						status[position][row] = false;
						node[position][row] = false;
						ghostOnlyPath[position][row] = false;
						position++;
					}
					else if (line.charAt(i) == '1' ) {
						status[position][row] = false;
						node[position][row] = false;
						ghostOnlyPath[position][row] = false;
						position++;
					}
					else if (line.charAt(i) == 'R' ) {
						status[position][row] = true;
						node[position][row] = false;
						ghostOnlyPath[position][row] = true;
						position++;
					}
					else if (line.charAt(i) == 'T' ) {
						status[position][row] = true;
						node[position][row] = true;
						ghostOnlyPath[position][row] = true;
						onlyTurns.add(new Pair<>(position,row));
						position++;
					}
					else if (line.charAt(i) == 'O'){
						status[position][row] = false;
						node[position][row] = false;
						ghostOnlyPath[position][row] = true;
						position++;
					}
					else if (line.charAt(i) == 'S'){
						pacmanCoords[0] = (position-2)*TILE_SIZE + X_OFFSET;
						pacmanCoords[1] = (row-2)*TILE_SIZE + Y_OFFSET;
						position++;
					}
					else if (line.charAt(i) == 'G'){
						ghostCoords[0] = (position-2)*TILE_SIZE + X_OFFSET;
						ghostCoords[1] = (row-2)*TILE_SIZE + Y_OFFSET;
						position++;
					}
//					else if (line.charAt(i) == 'Z'){
//						tempGhostsCoords[0] = (position-2)*TILE_SIZE + X_OFFSET;
//						tempGhostsCoords[1] = (row-2)*TILE_SIZE + Y_OFFSET;
//						position++;
//					}
				}
				row++;
			}
			bufferedReader.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Unable to open file ");
		}

		catch (IOException ex) {
			System.out.println("Error reading file ");
		}
	}
	
	/* Passes objects back to the game class - to check for collisions */
	public ArrayList<GameObject> getObjects() {
		
		return this.objects;
	}
	
	public int getTotalPellets() {
		
		return this.pellets;
	}
	
	
	/* Checks if character is in the exact x,y position to do a 90 degree turn */
	public boolean validTurningPoint(int x, int y) {
		
		if (((x - X_OFFSET)%TILE_SIZE == 0) && ((y - Y_OFFSET)%TILE_SIZE == 0)){
			return this.status[(x - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE ];
		}
		else {
			return false;
		}
	}
	
	
	/* Checks if the specified position is an intersection node. */
	public boolean isNode(int x, int y) {
		
		if (((x - X_OFFSET)%TILE_SIZE == 0) && ((y - Y_OFFSET)%TILE_SIZE == 0)){
			return this.node[(x - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE ];
		}
		else {
			return false;
		}
	}
	
	
	/* Checks if a location is valid when the character is moving in a certain direction */
	public boolean isValidDestination(boolean hasLeftSpawn, char direction, int x, int y) {
		
		if (hasLeftSpawn) {
	    	switch (direction) {
				case 'U':
					return this.status[(x - X_OFFSET)/TILE_SIZE][(y - OFFSET - Y_OFFSET)/TILE_SIZE];
				case 'D':
					return this.status[(x - X_OFFSET)/TILE_SIZE][(y + TILE_SIZE - Y_OFFSET)/TILE_SIZE];
				case 'L':
					return this.status[(x - OFFSET - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE];
				case 'R':
					return this.status[(x + TILE_SIZE - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE];
	    	}
		}
		else {
			switch (direction) {
			case 'U':
				return this.ghostOnlyPath[(x - X_OFFSET)/TILE_SIZE][(y - OFFSET - Y_OFFSET)/TILE_SIZE];
			case 'D':
				return this.ghostOnlyPath[(x - X_OFFSET)/TILE_SIZE][(y + TILE_SIZE - Y_OFFSET)/TILE_SIZE];
			case 'L':
				return this.ghostOnlyPath[(x - OFFSET - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE];
			case 'R':
				return this.ghostOnlyPath[(x + TILE_SIZE - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE];
			}
		}
    	return false;
    }
	
	
	public boolean isValidPos(int x, int y) {
		boolean validPos = this.status[(x - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE ] || this.ghostOnlyPath[(x - X_OFFSET)/TILE_SIZE][(y - Y_OFFSET)/TILE_SIZE ];
		return validPos;
	}
	
	
	/* Pass coordinates of characters to spawn on map */
	public int[] getPacman() {
		
		return pacmanCoords;
	}
	
	
	public int[] getGhost() {
		
		return ghostCoords;
	}

	public List<Pair<Integer, Integer>> getOnlyTurns() {
		return onlyTurns;
	}

	public int[] getTempGhosts() {
		return tempGhostsCoords;
	}


	/**
	 * This function will get the question from the json file using the SysData class and convert it to question entity
	 * @return array list of Questions
	 * @throws IOException
	 */
	private ArrayList<Question> getQuestionsFromJson() throws IOException {
		ArrayList<Question> qList = new ArrayList<>();

		// getting the questions as json array
		SysData sysData = new SysData();
		JsonArray jsonList = sysData.getQuestions();

		// loop over the json array and create a Question entity
		for (JsonElement element : jsonList) {

			// getting the question field
			String qField = element.getAsJsonObject().get("question").getAsString();

			// getting the answers for question
			ArrayList<String> aList = new ArrayList<>();
			for (JsonElement answer : element.getAsJsonObject().get("answers").getAsJsonArray()) {
				aList.add(answer.getAsString());
			}

			// getting the correct answer for that question
			int correctAns = element.getAsJsonObject().get("correct_ans").getAsInt();

			// getting the level of the question
			int qLevel = element.getAsJsonObject().get("level").getAsInt();

			// getting the team that wrote the question
			String qTeam = element.getAsJsonObject().get("team").getAsString();

			Question q = new Question(qField, aList, correctAns, qLevel, qTeam);

			qList.add(q);
		}

		return qList;


	}
	
}

