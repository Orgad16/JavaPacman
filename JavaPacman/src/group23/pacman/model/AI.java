package group23.pacman.model;

import java.util.ArrayList;
import java.util.Random;

/** The class which deals with the AI movement of moving character objects. **/

public class AI {
	
	/* Constants for the map offset and size*/
	private static final int GRID_SIZE = 30;
	private static final int X_OFFSET = 158;
	private static final int Y_OFFSET = 9;
	
	/* Board object to help determine whether a move is valid */
	private Board board;
	
	/* There are currently 4 types of AI
	 * 1) Type == 0, is not an AI, it is a player controlled object
	 * 2) Type == 1, is an AI with completely random movements
	 * 3) Type == 2, is an AI which chases the main character 
	 * 4) Type == 3, is an AI which will chase the main character until it gets too close, at which point it will lose control and start moving randomly
	 * 5) Type == 4, is an AI which will chase the main character until it gets too close, at which point it will try to run away to its corner */
	private int type;
	
	/* Random number generator for picking directions */
	private Random rand;
	
	/* Count value to prevent excessive bouncing between 2 nodes */
	private int count;
	
	/* Nodes that exist in the map */
	private ArrayList<Node> nodes;
	
	/* Path which determines the next move */
	private ArrayList<Node> path;

	/* Conditionals for different behaviours of ghost. If a ghost is not chasing, it is scattering */
	private boolean chase;
	
	
	public AI(Board board, int type) {
		
		this.board = board;
		this.type = type;
		rand = new Random();
		count = 0;
		nodes = new ArrayList<Node>();
		path = new ArrayList<Node>();
		chase = false;
		
		
	}
	
	/* Finds the shortest path using Dijkstra's Algorithm */
	public ArrayList<Node> computeShortest(Node source, Node destination) {
		
		/* Initilising Step:
		 * queue = Queue of nodes
		 * dist = Distance of node from the start 
		 * prev = Previous node connected to the current node
		 * isRemoved = Booleans which determine if a node is removed from the queue */
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Integer> dist = new ArrayList<Integer>();
		ArrayList<Node> prev = new ArrayList<Node>();
		ArrayList<Boolean> isRemoved = new ArrayList<Boolean>();
		

		Node currentNode, newNode;
		int currentNPos, newNPos;
		int tempDist;
		int currentSmallest;
 		/* false infinity */
		int infinity = (1<<25);
		
		boolean initial = true;
		
		for (int i = 0 ; i < this.nodes.size(); i++) {
			queue.add(this.nodes.get(i));
			if(nodes.get(i) == source){
				dist.add(0);
				prev.add(source);
			}
			else {
				dist.add(infinity);
				prev.add(null);
			}
			isRemoved.add(false);
		}
		
		/* Sets the index of the current node */
		currentNode = source;
		currentNPos = this.nodes.indexOf(source);
		
		while (!queue.isEmpty()) {
			
			boolean firstSmallest = true;
			
			/* Does not do this loop on the first iteration */
			if (initial == false) {
				currentSmallest = (1<<25) + 1;
				for (int i = 0 ; i < nodes.size(); i++) {	
					if (isRemoved.get(i) == false) {
						if (firstSmallest==true) {
							currentNPos = i;
							currentSmallest = dist.get(i);
							firstSmallest = false;
						}
						/* If the distance of the node from the source node is smaller than the current smallest distance 
						 * Replace the current smallest distance with the new distance */
						else if (dist.get(i) < currentSmallest) {
							currentNPos = i;
							currentSmallest = dist.get(i);							
						}
					}	
				}
			}
			
			initial = false;
			
			/* Step to remove the node from the queue, because we are going to expand it now*/
			currentNode = nodes.get(currentNPos);
			isRemoved.set(currentNPos, true);
			currentNPos = queue.indexOf(currentNode);
			queue.remove(currentNPos);
			
			/* Loop through all connected edges of the current node */
			for (int i = 0 ; i < currentNode.getEdges().size(); i++) {
				currentNPos = nodes.indexOf(currentNode);
				newNode = currentNode.getEdges().get(i);
				tempDist = dist.get(currentNPos) + Math.abs(currentNode.getX() - newNode.getX()) + Math.abs(currentNode.getY() - newNode.getY());
				newNPos = nodes.indexOf(newNode);
				/* If the new distance is smaller than the old distance, replace it 
				 * And set the previous node of (newNode) to the current node */
				if (tempDist < dist.get(newNPos)) {
					dist.set(newNPos, tempDist);
					prev.set(newNPos, currentNode);
				}
			}	
		}

		
		/////////////////////////////////////////END OF ALGORITHM////////////////////////////////////////////
	
		ArrayList<Node> backwardsPath = new ArrayList<Node>();
		int currentPos;
		Node prevNode;
		Node currentNode2 = destination;
		currentPos = this.nodes.indexOf(destination);
		/* Starting from the destination, back track using the prev ArrayList and store each node into backwardsPath */
		while(!source.equals(this.nodes.get(currentPos))){
			backwardsPath.add(currentNode2);
			prevNode = currentNode2;
			currentNode2 = prev.get(currentPos);
			currentPos = this.nodes.indexOf(currentNode2);
			
			/* If the previous node is the same as the current node, there is no path */
			if (currentNode2 == prevNode){
				return null;
			}
		}
		 
		return backwardsPath;

	}

	
	
	/* Chooses a direction using the private move generator method,while checking if the direction is a valid move on the board */
	public char chooseMovement(boolean hasLeftSpawn, char currentDirection, int ghostX, int ghostY, int pacmanX, int pacmanY, char pacmanDirection) {
		
		char direction;
		/* Random */
		if (type == 1) {
			/* End scatter behaviour if it has reached the corner */
			if (ghostX == X_OFFSET + GRID_SIZE && ghostY == Y_OFFSET + GRID_SIZE) {
				chase = true;
			}
			if (chase) {
				direction = randomMove(hasLeftSpawn, currentDirection);
				while (!board.isValidDestination(hasLeftSpawn, direction, ghostX, ghostY)) {
					direction = randomMove(hasLeftSpawn, currentDirection);
				}
			}
			else {
				int pacmanXNew = pacmanX;
				int pacmanYNew = pacmanY;
				
				/* Top left corner */
				pacmanXNew = X_OFFSET + GRID_SIZE;
				pacmanYNew = Y_OFFSET + GRID_SIZE;
				setNodes(ghostX, ghostY, pacmanXNew, pacmanYNew);
				path = computeShortest(nodes.get(0), nodes.get(nodes.size() - 1));
				direction = posCompMove(ghostX, ghostY, path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
			}
			return direction;
		}
		/* Chaser */
		else if (type == 2) {
			/* End scatter behaviour if it has reached the corner */
			if (ghostX == X_OFFSET + 23*GRID_SIZE && ghostY == Y_OFFSET + GRID_SIZE) {
				chase = true;
			}
			int pacmanXNew = pacmanX;
			int pacmanYNew = pacmanY;
			
			if (!chase) {
				/* Top right corner */
				pacmanXNew = X_OFFSET + 23*GRID_SIZE;
				pacmanYNew = Y_OFFSET + GRID_SIZE;
			}
			setNodes(ghostX, ghostY, pacmanXNew, pacmanYNew);
			path = computeShortest(nodes.get(0), nodes.get(nodes.size() - 1));
			direction = posCompMove(ghostX, ghostY, path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
			return direction;
		}
		/* Crazy Ghost */ 
		else if (type == 3) {
			/* End scatter behaviour if it has reached the corner */
			if (ghostX == X_OFFSET + GRID_SIZE && ghostY == Y_OFFSET + 23*GRID_SIZE) {
				chase = true;
			}
			int pacmanXNew = pacmanX;
			int pacmanYNew = pacmanY;
			if (chase) {
				/* If the ghost is within an 5 GRID_SIZE radius of pacman, it will move randomly. */
				if (Math.sqrt(Math.pow(ghostX - pacmanX, 2) + Math.pow(ghostY - pacmanY, 2)) < 5*GRID_SIZE) {
					direction = randomMove(hasLeftSpawn, currentDirection);
					while (!board.isValidDestination(hasLeftSpawn, direction, ghostX, ghostY)) {
						direction = randomMove(hasLeftSpawn, currentDirection);
					}
				}
				else {
					setNodes(ghostX, ghostY, pacmanXNew, pacmanYNew);
					path = computeShortest(nodes.get(0), nodes.get(nodes.size() - 1));
					direction = posCompMove(ghostX, ghostY, path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
				}
			}
			else {
				/* Bottom left corner */
				pacmanXNew = X_OFFSET + GRID_SIZE;
				pacmanYNew = Y_OFFSET + 23*GRID_SIZE;
				setNodes(ghostX, ghostY, pacmanXNew, pacmanYNew);
				path = computeShortest(nodes.get(0), nodes.get(nodes.size() - 1));
				direction = posCompMove(ghostX, ghostY, path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
			}
			return direction;
		}
		/* Shy Ghost */
		else if (type == 4) {
			/* End scatter behaviour if it has reached the corner */
			if (ghostX == X_OFFSET + 23*GRID_SIZE && ghostY == Y_OFFSET + 23*GRID_SIZE) {
				chase = true;
			}
			
			int pacmanXNew = pacmanX;
			int pacmanYNew = pacmanY;
			
			/* If the ghost is within an 5 GRID_SIZE radius of pacman, it will move to its corner. */
			if (Math.sqrt(Math.pow(ghostX - pacmanX, 2) + Math.pow(ghostY - pacmanY, 2)) < 5*GRID_SIZE || !chase) {
				/* Bottom right corner */
				pacmanXNew = X_OFFSET + 23*GRID_SIZE;
				pacmanYNew = Y_OFFSET + 23*GRID_SIZE;
			}
			
			setNodes(ghostX, ghostY, pacmanXNew, pacmanYNew);
			path = computeShortest(nodes.get(0), nodes.get(nodes.size() - 1));
			direction = posCompMove(ghostX, ghostY, path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
			return direction;
		}
		return 'S';
	}
	
	
	/* Computes a move which depends on how close pacman is in a certain x/y direction */
	private char posCompMove(int ghostX, int ghostY, int pacmanX, int pacmanY) {
		if (Math.abs(ghostX - pacmanX) >= Math.abs(ghostY - pacmanY)){
			if (ghostX >= pacmanX) {
				return 'L';
			}
			else {
				return 'R';
			}
		}
		else {
			
			if(ghostY >= pacmanY) {
				return 'U';
			}
			else {
				return 'D';
			}
		}
	}
	
	/* Generates a random direction for the AI to move in */
	private char randomMove(boolean hasLeftSpawn, char currentDirection) {
		
		int nextDir = rand.nextInt(4);
		
		if (!hasLeftSpawn) {
			count = 1;
		}
		switch (nextDir) {
			case 0 :
				if (currentDirection == 'D') {
					if (count%2 == 0) {
						return currentDirection;
					}
					count++;
				}
				return 'U';
			case 1 :
				if (currentDirection == 'U') {
					if (count%2 == 0) {
						return currentDirection;
					}
					count++;
				}
				return 'D';
			case 2 :
				if (currentDirection == 'R') {
					if (count%2 == 0) {
						return currentDirection;
					}
					count++;
				}
				return 'L';
			case 3 :
				if (currentDirection == 'L') {
					if (count%2 == 0) {
						return currentDirection;
					}
					count++;
				}
				return 'R';
			default : 
				return currentDirection;
		}
	}
	
	
	/* Checks the board object if we are able to turn at a certain (x,y) position */
	public boolean canTurn(int x, int y) {
		
		return board.isNode(x, y);
	}

	/* PUBLIC SETTERS */
	public void setChase(boolean bool) {
		this.chase = bool;
	}
	
	/* Pacman position and ghost position changes frequently 
	 * So this is used to set up the nodes for each path find */
	public void setNodes(int ghostX, int ghostY, int pacmanX, int pacmanY) {

		/* Remove previous nodes */
		nodes.clear();
		
		/* Add a node for ghost's position */
		nodes.add(new Node(ghostX, ghostY));
		
		/* Find all the turning points in the map and create nodes for them */
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 25; j++) {
				if (board.isNode(i*GRID_SIZE + X_OFFSET, j*GRID_SIZE + Y_OFFSET)) {
					nodes.add(new Node(i*GRID_SIZE + X_OFFSET, j*GRID_SIZE + Y_OFFSET));
				}
			}
		}
		
		/* Add a node for pacman's position */
		nodes.add(new Node(pacmanX, pacmanY));
		
		/* Find all the connecting edges between nodes */
		for (int i = 0; i< nodes.size(); i++) {
			nodes.get(i).addEdges(nodes, board);
		}
		
	}
}

