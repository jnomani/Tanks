import java.awt.*;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.*;

public class Landset extends JPanel {

	
	
	/* GUI Variables */
	private int[] xs, ys;
	public final int 
	HEIGHT, 
	WIDTH, 
	hINIT, //Base height of terrain (All curves in terrain are relative to this value)
	hVAR = 25, // Maximum variation in land peaks (Recommend keeping it < 300)
	sFACTOR = 450; //Amount of times to iterate over curve while smoothening (hVAR < sFACTOR < hVAR * 3)

	/* Game Variables */
	/*Change to ArryList ==> */// Tank[] players;

    ArrayList<Tank> players = new ArrayList<Tank>();

	int powerVar = 1;
	Bullet bullet = null;


	public void setBullet(Bullet b){
		this.bullet = b;
	}

	public Landset(int width, int height, int players) {
		
		
		WIDTH = width;
		HEIGHT = height;
		hINIT = (int) (HEIGHT * .5);
		if(WIDTH / players < 100) throw new RuntimeException("Rendering fail: Too many tanks");
		//this.players = new Tank[players];
		
		Bullet[] bType = {new Normal(), new Lightweight()};
		
		
		
		initPointArrays();
		for(int i = 0; i < players; i++){
			int x = (int)(Math.random() * WIDTH) + 1;
			if(i % 2 == 0) this.players.add(new Tank(Tank.BLACK, x, ys[x], initAmmunition(true), bType));
			else this.players.add(new Tank(Tank.RED, x, ys[x], initAmmunition(false), bType));

		}
		
		
	}
	
	void animate(int index, int turnIndex){
		System.out.println(players.get(turnIndex).getX());
		players.get(turnIndex).setXY(players.get(turnIndex).getX() + index, ys[players.get(turnIndex).getX() + index]);
		repaint();
			
		
	}
	
	public void setPowerVar(int behaviour){
		powerVar+= behaviour;
	}
	
	public int getPowerVar(){
		return powerVar;
	}

	public void paint(Graphics g) {
		
		for(int i = 0; i < players.size(); i++){
			players.get(i).setXY(players.get(i).getxOrig(), ys[players.get(i).getxOrig()]);
		}
		
		g.setColor(Color.GREEN);
		g.fillPolygon(xs, ys, WIDTH + 2);
		for(Tank t : players){
			t.paint(g);
		}
		g.setColor(Color.BLACK);
		g.drawString("Power: " + powerVar, WIDTH / 2, 30);
		
		
		
		if(bullet != null){
			bullet.paint(g);
		}
		handleCollision();
		handleTankStates();
	}

    private void handleTankStates(){
        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).isAlive()){
                players.remove(i);
                i--;
            }
        }
    }

	
	private void handleCollision(){
		
		if(bullet == null) return;
		if(bullet.getX() > WIDTH || bullet.getX() < 0){
			bullet = null;
			return;
		}
		boolean hit = false;
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).isHit(bullet.getX(), bullet.getY())){
				System.out.println(bullet.getDamage());
				players.get(i).setHealth(bullet.getDamage());
				hit = true;
			}
		}if(hit){ 
			bullet.explode(getGraphics());
			bullet = null;
			return;
		}
		if(bullet.getY() > ys[bullet.getX()]){
			bullet.explode(getGraphics());
			modTerrain(bullet.getX(), bullet.getY(), bullet.getHitRadius());
			bullet = null;
		}

        

		
	}

	private void initPointArrays() {
		xs = new int[WIDTH + 2];
		ys = new int[WIDTH + 2];
		
		double[] yVal = new double[WIDTH];
		yVal[0] = hINIT;
		
		generateRandomGrid(yVal);
		Transform(yVal);

		for (int i = 0; i < yVal.length; i++) {
			xs[i] = i;
			ys[i] = (int) yVal[i];
		}

		xs[WIDTH - 2] = WIDTH;
		ys[WIDTH - 2] = HEIGHT;

		xs[WIDTH - 1] = 0;
		ys[WIDTH - 1] = HEIGHT;
	}
	
	

	private void generateRandomGrid(double[] ys) {
		// ys[0] should already be set to hINIT.
		

		int variation = 0;
		for (int i = 1; i < ys.length; i++) {
			if (ys[i - 1] >= HEIGHT - 100)
				variation = 2;
			else if (ys[i - 1] <= 300 || (int) (Math.random() * 2.1) == 0)
				variation = -1;
			else
				variation = 1;

			ys[i] = ys[i - 1] - (variation * (int) (Math.random() * hVAR));
		}
	}

	private void Transform(double[] ys) {
		for (int varIndex = 0; varIndex <= sFACTOR; varIndex++) {
			for (int val1 = 0, val2 = 1; val1 < ys.length - 1; val1++, val2++) {
				double average = (ys[val1] + ys[val2]) / 2;
				ys[val1] = average;
				ys[val2] = average;
			}
		}
	}
	
	public void Fire(int turnIndex){
		bullet = players.get(turnIndex).Fire(powerVar);
		
	}
	
	private void modTerrain(int x, int y, int rad){
		for(int i = x; i > x - rad; i--){
			int val = (int) (((Math.sqrt(Math.pow(rad, 2) - Math.pow(i - x, 2))) + y));
			if(i < 0 || i > WIDTH) break;
			if(val != 0 && val > ys[i]){
				if(val > HEIGHT - 100) val = HEIGHT - 100;
				ys[i] = val;
			}
			
		}
		for(int i = x; i < x + rad; i++){
			int val = (int) (((Math.sqrt(Math.pow(rad, 2) - Math.pow(i - x, 2))) + y));
			
			if(val != 0 && val > ys[i]){ 
				if(val > HEIGHT - 100) val = HEIGHT - 100;
				ys[i] = val;
			}
		}
		
	}
	
	boolean getBulletState(){
		return bullet == null;
	}
	
	private Stack<Bullet>[] initAmmunition(boolean black){
		Stack[] ammo = new Stack[2];
			ammo[0] = new Stack<Bullet>();
			for(int j = 0; j < 100; j++){
				ammo[0].push(new Normal(black ? Normal.BLACK : Normal.RED));
			}
			ammo[1] = new Stack<Bullet>();
			for(int j = 0; j < 100; j++){
				ammo[1].push(new Lightweight(black ? Lightweight.BLACK : Lightweight.RED));
			}
		return ammo;
	}
}
