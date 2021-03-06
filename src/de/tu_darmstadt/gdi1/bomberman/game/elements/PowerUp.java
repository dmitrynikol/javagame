package de.tu_darmstadt.gdi1.bomberman.game.elements;

import java.util.Random;
import javax.swing.ImageIcon;

public class PowerUp extends GameElement
{
	// PowerID decides of which PowerUp-Type this is
	// 1 = Increase detonation radius
	// 2 = Increase speed
	// 3 = Increase bomb count
	// 4 = SuperBomb
	protected int powerID;
	
	public int getpowerupID(){
		return powerID;
	}

	public PowerUp ()
	{	
		Random generator = new Random();
		int rnd = generator.nextInt( 99 ) + 1;
		if (rnd <= 35){
			powerID = 1;
		}else if (rnd <= 55 ){
			powerID =2;
		}else if(rnd <=90){
			powerID =3;
		}else 
			powerID =4;
	}
	
	public PowerUp (int rand){
		powerID = rand;
	}
	

	public boolean isDestroyable () {
		return true;
	}

	public boolean isSolid() {
		return false;
	}

	public GameElement clone() {
	
		return new PowerUp(powerID);
	}

	public ImageIcon getImageIcon() {
	
		switch(powerID){
			case 1:
				return new ImageIcon(skinPath+"increase_radius.png");
			case 2:
				return new ImageIcon(skinPath+"increase_speed_2.png");
			case 3:
				return new ImageIcon(skinPath+"increase_bombcount.png");
			default:
				return new ImageIcon(skinPath+"superbomb.png");
		}
		
	}

	public String getDescription() {

		switch(powerID){
			case 1:
				return "Increase Radius - PowerUp";
			case 2: 
				return "Increase Speed - PowerUp"; 
			case 3: 
				return "Increase bomb count - PowerUp";
			case 4: 
				return "Superbomb - PowerUp";
			default:
				return "Unknown - PowerUp";
		}
	}

	public char getParsingSymbol() {
		
		return ' ';
	}

	@Override
	public void destroy() {
		PowerUp.super.destroy();
		System.out.println(getDescription() + " died.");
	}
}
