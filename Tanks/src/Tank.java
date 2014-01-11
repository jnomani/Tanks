import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.*;
public class Tank extends JComponent{
	
	private boolean black;
	
	private boolean isAlive;
	
	public static final String[] RED ={"src/T1.png", "src/T1Turret.png"};
	public static final String[] BLACK = {"src/T0.png", "src/T0Turret.png"};
	
	private BufferedImage tankBody;
	private BufferedImage turret;

	private int x, y;
	private int xTurret, yTurret;
	double angle = 0;
	double slope;

    private final String name;

	private int xOrig, yOrig;
	
	private int health = 100;
	private Stack<Bullet> bullets = new Stack<Bullet>();
	
	private Stack[] ammunition;
	private Bullet[] bTypes;
	 
	private int[] xCoords;
	
	public int getX() {
		return x + (tankBody.getWidth() / 2);
	}
	
	public Bullet Fire(int power){
		Bullet b= bullets.pop();
		
		int tX = (int)(turret.getWidth() * (Math.cos(Math.toRadians(angle))) + x + 27);
		int tY = (int)(turret.getWidth() * (Math.sin(Math.toRadians(angle))) + y + 14);
		b.setParams(tX, tY,(int) angle, power);
		
		return b;
	}
    public String getName(){
        return name;
    }

    public boolean isAlive(){
    return isAlive;
    }

	
	private int bulletLog = 0;
	
	public void incrementBulletType(){
		bulletLog++;
		if(bulletLog >= ammunition.length) bulletLog = 0; 
	}
	
	public Tank(String[] sprite,int x,int y, Stack<Bullet>[] ammo, Bullet[] bType){
		String n = JOptionPane.showInputDialog("Enter a Name: ");
		if (n == "") {
			
			System.exit(0);
		
		}
		name = n;
        isAlive = true;
		this.ammunition = ammo;
		this.bTypes = bType;
		black = (sprite == BLACK);
		
	
		File f = null;
		try {
			f = new File(sprite[0]);
			tankBody = ImageIO.read(f);
			tankBody = ImageUtils.makeTransparent(tankBody);
			f = new File(sprite[1]);
			turret = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println(e.getMessage() + ": " + f.getAbsolutePath());
		}
		
		setXY(x, y);

		setCoordinateArray();
		
		//(new Thread(new HitListener(l))).start();
	}
	
	private void setCoordinateArray(){
		
		xCoords = new int[tankBody.getWidth()];
		
		for(int i = 0 ; i < xCoords.length; i++){
			xCoords[i] = x + i;
			//System.out.print(xCoords[i] + ", ");
		}
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int damage) {
		this.health -= damage;
		if(health > 0 ) isAlive = true;
		else isAlive = false;
	}
	
	

	public int getxOrig() {
		return xOrig;
	}

	public void setXY(int x,int y){
		if(this.y - y > 30) return;
		
		xOrig = x;
		yOrig = y;
		this.x = x - (tankBody.getWidth() / 2);
		this.y = y - (tankBody.getHeight()) ;
		setCoordinateArray();
		xTurret = this.x + (tankBody.getWidth() / 2);
		yTurret = this.y + (tankBody.getHeight() / 4);
		
	}
	
	public void setAngle(int increment){
		if(angle > 0 && increment > 0 ) return;
		if(Math.abs(angle) >= 180 && increment < 0) return;
		angle += increment;
	}

	@Override
	public void paint(Graphics g){
		if(!isAlive) return;
		bullets = ammunition[bulletLog];
		
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(tankBody, x, y, null);
		g2.rotate(Math.toRadians(angle), xTurret, yTurret + (turret.getHeight() / 2));
		g2.drawImage(turret, xTurret, yTurret, null);
		g2.rotate(-1 *Math.toRadians(angle), xTurret, yTurret + (turret.getHeight() / 2));
		g2.setColor(black ? Color.BLACK : Color.RED);
		g2.drawString("Health: " + health, x, y - 100);
		g2.drawString(bTypes[bulletLog].getClass().getName() + ": " + bullets.size(), x, y - 75);
		g2.drawString(name, x , y - 125);
		
	}
	public boolean isHit(int x, int y){
		
		for(int i = 0; i < xCoords.length; i++){
			if(xCoords[i] == x){
				return y > Tank.this.y;
			}
		} return false;
	}
	

}


