package de.tu_darmstadt.gdi1.bomberman.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.framework.AbstractBombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import de.tu_darmstadt.gdi1.framework.view.UserInterface;


/**
 * Das User Interface. Nimmt zwar Benutzereingaben und so weiter entgegen, darf aber nix selbst -
 * reicht alles an den Controller weiter, der der einzige wirkliche soziale Kontakt des GUI ist.
 */
public class Gui extends UserInterface<GameElement> {

	protected AbstractBombermanController controller;

	Logger logger = Logger.getLogger(BombermanController.class.getName());

	public Gui (AbstractBombermanController ctr)
	{
		super();
		controller = ctr;

		setTitle("The amazing TU Darmstadt Bomberman!");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	protected void handleNonFrameworkEvents(IUserInterfaceEvent<GameElement> event) {
		if (!(event instanceof UIEvent))
		{
			// TODO: mourn around
			return;
		}

		UIEvent bmevent = (UIEvent) event;
		switch (bmevent.getType())
		{
			case QUIT_GAME:
				dispose();
				break;
		}
	}

	@Override
	protected void mouseClicked(int x, int y) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void keyboardKeyTyped(KeyEvent evt) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void keyboardKeyPressed(KeyEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void keyboardKeyReleased(KeyEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void menuItemClicked(String itemName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void closingRequested() {
		ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.USER_QUIT);
		controller.handleEventImmediately(evt);
	}

	@Override
	public ImageIcon getComponentForList(List<GameElement> aList, Point coordinates) {
		// TODO
		ArrayList<ImageIcon> iconList = new ArrayList<ImageIcon>();
		for (GameElement gameElement : aList) {
			iconList.add(gameElement.getImageIcon());
		}

		if (iconList.size() == 1) {
			return iconList.get(0);
		}
		else {
			return combineImages(iconList);
		}
	}
	
	private ImageIcon combineImages(ArrayList<ImageIcon> imageList)
	{
		BufferedImage bufferedImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
		Graphics temp = bufferedImage.createGraphics();
		for (ImageIcon imageIcon : imageList) {
			BufferedImage bi = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.drawImage(imageIcon.getImage(), 0, 0, null);
			g.dispose();
			temp.drawImage(bi, 0, 0, null);
		}
		temp.dispose();

		return new ImageIcon(bufferedImage);
	}

}
