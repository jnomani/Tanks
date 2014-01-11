
import java.awt.Graphics;

public class Normal extends Bullet{

	public static final String BLACK = "src/BBullet.png";
	public static final String RED = "src/RBullet.png";

	public Normal(String sprite){
		super(sprite);
		setAccel(.2);
	}
	
	public void setParam(int x, int y, int angle, int power){
		
		super.setParams(x, y, angle, power * 3);
		
	}

	public Normal(){
		super();
	}
	
	public void paint(Graphics g){
		
		super.paint(g);
	}

	@Override
	public int getDamage() {
		
		return (int)getFinalVelocity();
	}


	@Override
	public void fire(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHitRadius() {
		
		return 50;
	}
	
}
