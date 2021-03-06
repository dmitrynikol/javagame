package de.tu_darmstadt.gdi1.bomberman.game.elements;

import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Stone extends GameElement
{
	@Override
	public GameElement clone ()
	{
		return new Stone();
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		return new ImageIcon(skinPath+"stone.png");
	}

	@Override
	public String getDescription ()
	{
		return "Stone";
	}

	@Override
	public char getParsingSymbol ()
	{
		return '*';
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public boolean isDestroyable () {
		return true;
	}

	@Override
	public void destroy ()
	{
		super.destroy();
		List<GameElement> present = gameBoard.getElements(x, y);
		Random generator = new Random();
		if (generator.nextInt(4) == 0)
		{
			PowerUp powerUp = new PowerUp();
			powerUp.setGameBoard(gameBoard);
			powerUp.setGameData(gameData);
			powerUp.setCoordinates(x, y);
			present.add(powerUp);
			gameBoard.setElements(x, y, present);
		}
	}
}
