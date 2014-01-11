
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Lightweight extends Bullet {

	public static final String BLACK = "src/BSpiralBullet.png";
	public static final String RED = "src/RSpiralBullet.png";
	

	@Override
	public int getDamage() {
		return (int)getFinalVelocity() / 2;
	}

	
	
	public Lightweight(String sprite){
		super(sprite);
		setAccel(.1);
	}
	
	public Lightweight(){
		super();
	}
	

	public void setParam(int x, int y, int angle, int power) {

		super.setParams(x, y, angle, power * 3);

	}

	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		//g2.rotate(Math.toRadians(.1), getX() + getWidth() / 2, getY() + getHeight() / 2);
		super.paint(g2);
		//g2.rotate(-Math.toRadians(.1), getX() + getWidth() / 2 , getY() + getHeight() / 2);
		
	}

	


	



	@Override
	public void fire(Graphics g) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public int getHitRadius() {
		// TODO Auto-generated method stub
		return 35;
	}
}
