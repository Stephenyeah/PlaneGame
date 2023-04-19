package cn.sxt.game;


import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;




public class MyGameFrame extends Frame{
	
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bg = GameUtil.getImage("images/bg.jpg");
	
	//make plane and shells
	plane plane = new plane(planeImg,300,400);
	Shell shell = new Shell();
	Shell[] shells = new Shell[50];
	
	Explode bao;
	Date startTime = new Date();
	Date endTime;
	int period; // game duration

	@Override
	public void paint(Graphics g) { //g as a pen
		Color c = g.getColor();

		g.drawImage(bg, 0, 0, null);
		
		plane.drawSelf(g); // paint plane
		
		shell.draw(g);
		
		//draw all the shell
		for(int i=0; i<shells.length;i++) {
			shells[i].draw(g);
			
			
			//plane and shell the impact checking
			boolean peng = shells[i].getRect().intersects(plane.getRect());
		    
			if(peng) {
				plane.live =false;
				//System.out.println("Crash !!!");
				if(bao ==null) {
			        bao = new Explode(plane.x,plane.y);
			        
			        endTime = new Date();
			        
			        period = (int)((endTime.getTime() - startTime.getTime())/1000);			        
				}
				
				bao.draw(g);
				
			}
			if(!plane.live) {
				
				g.setColor(Color.red);
				Font f =new Font("black",Font.BOLD,40);
			      g.setFont(f);
				g.drawString("Time:"+period+" Seconds",150, 300);
			}
			
			
		}	
		g.setColor(c);
		
	}


	// help us draw the window repeated again and again
	class PaintThread extends Thread {
		@Override
		public void run() {
			while(true) {
				//System.out.println("Window repaint one");
				repaint(); //repaint.
				try {
				Thread.sleep(40);//ls = 1000ms
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
//define keyboard listing value	
class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
		 //System.out.println("pressdown"+e.getKeyCode());
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			//System.out.println("loosen"+e.getKeyCode());
			plane.minusDirection(e);
		}
		
	}
	
	
	//initialization windows
public void launchFrame() {
		this.setTitle("My second game!");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		this.setLocation(300,300);
		
		this.addWindowListener(new WindowAdapter() {
		
					@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start(); //start the thread of the window repaint
		addKeyListener(new KeyMonitor()); // add key Listener
		
		//initial 50 shells
		for(int i=0;i<shells.length;i++) {
			shells[i]= new Shell();
		}
		
}

//double buffering
private Image offScreenImage = null;

public void update(Graphics g) {
	if(offScreenImage == null)
	offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT); 
	
	Graphics gOff = offScreenImage.getGraphics();
	paint(gOff);
	g.drawImage(offScreenImage, 0, 0, null);
}

public static void main(String[] args) {
	MyGameFrame f = new MyGameFrame();
	f.launchFrame();
 }
}