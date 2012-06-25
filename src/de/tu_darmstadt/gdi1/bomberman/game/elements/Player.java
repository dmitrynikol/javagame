package de.tu_darmstadt.gdi1.bomberman.game.elements;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Player extends GameElement
{
	/**
	 * In welche Richtung bewegt sich der Spieler aktuell?
	 */
	public enum direction {
		NULL,
		UP,
		DOWN,
		LEFT,
		RIGHT
	};

	private direction dir = direction.NULL;
	private int playerID;
	private long nextMoveAllowedTick = 0;
	private long moveDelay = 5;
	private int bombRadius = 0;
	private int myMaxBombs=  1;

	private ArrayList<Bomb> myBombs = new ArrayList<Bomb>();

	public Player (int playerID)
	{
		this.playerID = playerID;
	}

	
	@Override
	public void destroy() {
		Player.super.destroy();
		System.out.println("Player "+playerID+" died.");
		gameData.removePlayer(playerID);
		
	}

	public boolean isDestroyable () {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	// GETTER SETTER PARTAY ////////////////////////////////////////////////////////////////////////

	public int getPlayerID ()
	{
		return playerID;
	}

	public direction getDirection () {
		return dir;
	}

	public void setDirection (direction d) {
		dir = d;
	}
	
	public long getMoveDelay(){
	return moveDelay;	
	}
	
	public void setMoveDelay(long moveDelay){
		this.moveDelay = moveDelay;
		
	}
	
	public int getBombradius ()
	{
		return bombRadius;
	}

	public void setBombRadius (int bombRadius)
	{
		this.bombRadius = bombRadius;
	}

	public int getmymaxbombs(){
		return myMaxBombs;
	}
		
	public void setmyMaxBombs(int myMaxBombs){
		this.myMaxBombs = myMaxBombs;
	}
	

	// LOGIC ///////////////////////////////////////////////////////////////////////////////////////

	public boolean move (long ticknumber) {
		// Prüfen: darf ich mich überhaupt wieder bewegen?
		if (dir == direction.NULL || ticknumber < nextMoveAllowedTick)
			return false;

		// Die Richtung in Koordinaten übersetzen
		int newX = this.x, newY = this.y;
		switch (dir) {
			case UP: newY -= 1; break;
			case DOWN: newY += 1; break;
			case LEFT: newX -= 1; break;
			case RIGHT: newX += 1; break;
		}

		// Check that we do not walk off the board
		if (!gameBoard.checkCoordinates(newX, newY))
			return false;

		// Get who is already where we want to go
		List<GameElement> present = gameBoard.getElements(this.x, this.y);
		List<GameElement> target = gameBoard.getElements(newX, newY);

		// Collision detection baby!
		for (GameElement t : target) {
			if (t.isSolid())
				return false;
		}
		
		
		// Perform the move
		System.out.println("Player "+getPlayerID()+" moves to ("+newX+","+newY+")");
		present.remove(this);
		gameBoard.setElements(x, y, present);
		target.add(this);
		gameBoard.setElements(newX, newY, target);
		this.x = newX;
		this.y = newY;

		nextMoveAllowedTick = ticknumber + moveDelay;
	
		//found a PowerUp?
		for (int i = 0; i < target.size()-1; i++) {
			if (target.get(i) instanceof PowerUp){
				PowerUp up = ((PowerUp) target.get(i));

				System.out.println(getDescription() + " got a power up");
				switch(up.getpowerupID()){
					case 1: 
						// Radius der Bomben wird erhöht
						increaseBombRadius(); break;
					case 2:
						//Speed wird erhöht
						increaseSpeed(); break;
					case 3:
						//Anzahl der legbaren Bomben wird erhöht
						increaseBombCount(); break;
					case 4: 
						// Superbombe Bombenradius = max
						setSuperBomb();
						
						
				}
				//PowerUp wurde verbraucht
				target.remove(i);
				gameBoard.setElements(x, y, target);
			}

		}
		
			
		
		return true;
	}

	public Bomb dropBomb () {
		// Get the elements we are standing on top of
		List<GameElement> present = gameBoard.getElements(this.x, this.y);

		// Only one bomb per field!
		for (GameElement t : present) {
			if (t instanceof Bomb || myBombs.size() == myMaxBombs)
				return null;
		}
		
		// Bomb creation
		Bomb bomb = new Bomb(this, 60);
		bomb.setCoordinates(this.x, this.y);
		bomb.setGameBoard(gameBoard);
		bomb.setGameData(gameData);
		myBombs.add(bomb);

		// Perform the dropping
		present.add(present.indexOf(this), bomb);
		gameBoard.setElements(x, y, present);

		return bomb;
	}
	
	public List<GameElement> mightyBlow() {
		ArrayList<GameElement> list = new ArrayList<GameElement>();
		ArrayList<Stone> toExplode = new ArrayList<Stone>();
		for (int i = 0; i < gameBoard.getWidth(); i++) {
			for (int j = 0; j < gameBoard.getHeight(); j++) {
				List<GameElement> gE = gameBoard.getElements(i,j);
				for (GameElement elem : gE) {
					if (elem instanceof Stone) {
						toExplode.add((Stone) elem);
						list.add(elem);
					}
				}
			}
		}

		for (Stone e : toExplode) {
			e.destroy();
		}

		return list;
	}

	public void removeBombOwnage (Bomb b) {
		myBombs.remove(b);
	}

	// STAMMDATEN: PARSING, ICON, CLONING ... //////////////////////////////////////////////////////

	@Override
	public GameElement clone ()
	{
		return new Player(this.playerID);
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		if (playerID == 1)
			return new ImageIcon("resource/images/player_white.png");
		else if (playerID == 2)
			return new ImageIcon("resource/images/player_red.png");
		else if (playerID == 3)
			return new ImageIcon("resource/images/player_black.png");
		else
			return new ImageIcon("resource/images/player_blue.png");
	}

	@Override
	public String getDescription ()
	{
		if (playerID == 1)
			return "Player White";
		else if (playerID == 2)
			return "Player Red";
		else if (playerID == 3)
			return "Player Black";
		else
			return "Player Blue";
	}

	@Override
	public char getParsingSymbol ()
	{
		if (playerID == 1)
			return '1';
		else if (playerID == 2)
			return '2';
		else if (playerID == 3)
			return '3';
		else
			return '4';
	}
	
	// POWERUP ////////////////////////////////////////////////////////////////////////////
	
	public void increaseBombRadius(){
		if (getBombradius() < 9) {
			setBombRadius(getBombradius()+1);
			System.out.println("Bombradius: "+getBombradius());
		}
		else
			System.out.println("Bombradius: max. reached! (9)");
	}
	
	public void increaseBombCount(){
		if (getmymaxbombs() < 9) {
			setmyMaxBombs(getmymaxbombs()+1);
			System.out.println("BombCount: "+ getmymaxbombs());
		}
		else
			System.out.println("BombCount: max. reached! (9)");
	}

public void increaseSpeed(){
	if (getMoveDelay() > 3){
		setMoveDelay(getMoveDelay() - 1);
		System.out.println("MoveDelay: "+ getMoveDelay());
}
else
	System.out.println("MaxSpeed was reached!");
	}

public void setSuperBomb(){
	System.out.println("SUPERBOMBE");
	setBombRadius(9); 	
}
}