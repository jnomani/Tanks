
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public abstract class Bullet extends JComponent{

	private BufferedImage img;
	private BufferedImage explosion;
	private final String eString = "src/SpiralExplosion.png";
	
	protected BufferedImage getImage(){
		return img;
	}
	
	public abstract int getDamage();
	
	private int x, y, angle;
	protected int power;
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	

	public int getPrevX() {
		return prevX;
	}

	public int getPrevY() {
		return prevY;
	}

	private int xincrement, time;
	private int yOrig, xOrig;
	private double acceleration;
	
	private int prevX, prevY;
	
	public Bullet(String sprite){
		try {
			img = ImageIO.read(new File(sprite));
			img = ImageUtils.makeTransparent(img);
			explosion = ImageIO.read(new File(eString));
			explosion = ImageUtils.makeTransparent(explosion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setParams(int x, int y, int angle, int power){
		this.x = x - 10;
		this.y = y - 12;
		yOrig = this.y;
		xOrig = this.x;
		this.angle = angle;
		if((int) Math.abs(this.angle) < 90) xincrement = 1;
		else if( Math.abs(this.angle) > 90) xincrement = -1;
		else xincrement = 0;
		this.power = power;
		time = 0;
		
	}
	
	public Bullet(){
		img = null;
	}
	
	public double getFinalVelocity(){
		double xFinal = (power * 3 * Math.abs(Math.cos(Math.toRadians(angle))));
		double yFinal = Math.sqrt(Math.pow(power * Math.sin(Math.toRadians(Math.abs(angle))), 2) + (2 * acceleration * (y - yOrig)));
		return Math.sqrt(Math.pow(xFinal, 2)  + Math.pow(yFinal, 2));
	}
	
	protected void setAccel(double acc){
		this.acceleration = acc;
	}
	
	
	public void explode(Graphics g) {
		g.drawImage(explosion, getPrevX(), getPrevY(), null);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public abstract void fire(Graphics g);
	
	public abstract int getHitRadius();
	
	@Override
	public void paint(Graphics g){
		prevX = x;
		prevY = y;
		Graphics2D g2 = (Graphics2D)g;
		x = (int) (xOrig + xincrement * time * power * 3 * Math.abs(Math.cos(Math.toRadians(angle)))) ;
		y =  yOrig - (int) ((power * Math.sin(Math.toRadians(Math.abs(angle))) * time + (-acceleration) * Math.pow(time, 2)) * 2);
		g2.drawImage(img,x, y, null);
		time ++;
		try {
			Thread.sleep(40);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
