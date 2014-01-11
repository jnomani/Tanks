
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;


import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class Game extends JFrame{
	private int turnIndex = 0; 
	Landset terrain;
	private Timer moveTimer;
	private final int players = 2;
	int spaces = 0;
	
	public Game(){
		
		setSize(1002, 600);
		terrain = new Landset(1000, 600, players);
		setResizable(false);
		setTitle("Tanks");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(terrain);
		setVisible(true);
		
		InputMap imap = terrain.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap ap = terrain.getActionMap();
		
		
		KeyStroke rightKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0 , false);
		KeyStroke rightKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0 , true);
		KeyStroke leftKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0 , false);
		KeyStroke leftKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0 , true);
		KeyStroke upKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0 , false);
		KeyStroke upKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0 , true);
		KeyStroke downKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0 , false);
		KeyStroke downKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0 , true);
		KeyStroke spaceKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0 , false);
		KeyStroke spaceKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0 , true);
		KeyStroke EKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_E, 0 , true);
		
		imap.put(downKeyPressed, "down key pressed");
		imap.put(downKeyReleased, "down key released");
		imap.put(rightKeyPressed, "right key pressed");
		imap.put(rightKeyReleased, "right key released");
		imap.put(leftKeyPressed, "left key pressed");
		imap.put(leftKeyReleased, "left key released");
		imap.put(upKeyPressed, "up key pressed");
		imap.put(upKeyReleased, "up key released");
		imap.put(spaceKeyPressed, "space key pressed");
		imap.put(spaceKeyReleased, "space key released");
		imap.put(EKeyReleased, "E key released");
		
		ap.put("right key pressed", new RightAction(false));
		ap.put("right key released", new RightAction(true));
		ap.put("left key pressed", new LeftAction(false));
		ap.put("left key released", new LeftAction(true));
		ap.put("up key pressed", new UpAction(false));
		ap.put("up key released", new UpAction(true));
		ap.put("down key pressed", new DownAction(false));
		ap.put("down key released", new DownAction(true));
		ap.put("space key pressed", new SpaceAction(false));
		ap.put("space key released", new SpaceAction(true));
		ap.put("E key released", new EAction());
	}
	
	private class EAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			terrain.players.get(turnIndex).incrementBulletType();
			
		}
		
	}
	
	private class DownAction extends AbstractAction{
		private boolean onKeyRelease; 
		
		public DownAction(boolean b){
			onKeyRelease = b;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!onKeyRelease){
				if(moveTimer != null && moveTimer.isRunning()) return;
				moveTimer = new Timer(1, new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						terrain.players.get(turnIndex).setAngle(1);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//repaint();
					}
					
				});
				moveTimer.start();
			}else{
				if(moveTimer != null && moveTimer.isRunning()){
					moveTimer.stop();
					moveTimer = null;
				}
			}
			
		}
		
	}
	
	private class UpAction extends AbstractAction{
		private boolean onKeyRelease; 
		
		public UpAction(boolean b){
			onKeyRelease = b;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!onKeyRelease){
				if(moveTimer != null && moveTimer.isRunning()) return;
				moveTimer = new Timer(1, new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						terrain.players.get(turnIndex).setAngle(-1);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//repaint();
					}
					
				});
				moveTimer.start();
			}else{
				if(moveTimer != null && moveTimer.isRunning()){
					moveTimer.stop();
					moveTimer = null;
				}
			}
			
		}
		
	}
	
	private class LeftAction extends AbstractAction{
private boolean onKeyRelease;
		
		public LeftAction(boolean onKeyRelease){
			this.onKeyRelease = onKeyRelease;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!onKeyRelease){
				if(moveTimer != null && moveTimer.isRunning()) return;
				moveTimer = new Timer(10, new ActionListener(){
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(spaces < 100){
						terrain.animate(-1, turnIndex);
						//repaint();
						spaces++;
						}
					}
					
				});
				moveTimer.start();
			}else{
				if(moveTimer != null && moveTimer.isRunning()){
					moveTimer.stop();
					moveTimer = null;
				}
			}
			
		}
	}
	
	private class SpaceAction extends AbstractAction{

	private boolean onKeyRelease;
	
	public SpaceAction(boolean onKeyRelease){
		this.onKeyRelease = onKeyRelease;
	}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!terrain.getBulletState()) return;
			if(onKeyRelease){
			moveTimer.stop();
			moveTimer =  null;
			terrain.Fire(turnIndex);
			turnIndex++;
			if(turnIndex >= players) turnIndex = 0;
			spaces = 0;
			
			}else{
				
				if(moveTimer != null && moveTimer.isRunning()) return;
				terrain.setPowerVar(-terrain.getPowerVar() + 1);
				moveTimer = new Timer(300, new ActionListener(){

					private int behaviour = 0;
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						if(terrain.getPowerVar() <= 1) behaviour  = 1;
						if(terrain.getPowerVar() == 10) behaviour = -1;
						
						terrain.setPowerVar(behaviour);
						
						//repaint();
						
						
					}
					
				});
				
				moveTimer.start();
			}
		}
		
	}
	private class RightAction extends AbstractAction{

		private boolean onKeyRelease;
		
		public RightAction(boolean onKeyRelease){
			this.onKeyRelease = onKeyRelease;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Bullet.moose((int)(Math.random() * 10) + 1);
			if(!onKeyRelease){
				if(moveTimer != null && moveTimer.isRunning()) return;
				moveTimer = new Timer(10, new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(spaces < 100){
							terrain.animate(1, turnIndex);
							//repaint();
							spaces++;
							}
					}
					
				});
				moveTimer.start();
			}else{
				if(moveTimer != null && moveTimer.isRunning()){
					moveTimer.stop();
					moveTimer = null;
				}
			}
			
		}
		
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game g = new Game();
		(new Thread(new logicThread(g))).start();

	}

	
	
}

class logicThread implements Runnable{

	public volatile Game g;
	
	
	public logicThread(Game g){
		this.g = g;
		g.createBufferStrategy(1);
		
	}
	
	private boolean isGameAlive(){
		//System.out.println(g.terrain.players.size());
		return g.terrain.players.size() > 1;
	}
	
	@Override
	public void run() {
		
		
		while(isGameAlive()){
			
			
			try {
				g.repaint();
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e){
				System.out.println(e.getLocalizedMessage());
			}
		}
		

		JOptionPane.showMessageDialog(null, g.terrain.players.get(0).getName() + " Wins!");
        System.exit(0);

	}
	
}
