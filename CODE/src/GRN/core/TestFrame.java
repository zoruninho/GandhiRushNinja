import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TestFrame extends JFrame implements GameDisplay,MouseListener {
	private final int WIDTH = 600, HEIGHT = 600;
	private Image img;
	GameEngine engine;
	int x = 0, y = 0, xup = 0, yup = 0;
	int xs = 0, ys = 0;
	
	public TestFrame(){
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		try {
			img = ImageIO.read(new File("ic_launcher.png"));
		} catch (IOException e) {System.out.println("image not found");}
		
		engine = new GameEngine(1, WIDTH, HEIGHT, this);
		
		this.setContentPane(new Panel());
		this.getContentPane().setBackground(Color.black);
		this.getContentPane().addMouseListener(this);
		engine.start();
	}

	private class Panel extends JPanel{
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	
			
			int dec = img.getWidth(this)/2;
			int pos[][] = engine.getDrawPos();
			for(int i=0; i<pos.length; i++){
				g.drawImage(img, pos[i][0]-dec, pos[i][1]-dec, this);
				g.setColor(Color.red);
				if(xup != 0)
					g.drawLine(x, y, xup, yup);
				
				g.drawImage(img, xs, ys, this);
			
			}
			
		}
	}
	
	public static void main(String[] args) {
		new TestFrame();
	}

	public void refreshDisplay() {
		this.getContentPane().repaint();
		this.revalidate();
	}

	public void mouseClicked(MouseEvent arg0) {
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
		int dec = img.getWidth(this)/2;
		xup = 0;
		x = arg0.getX();
		y = arg0.getY();
		engine.setDownPosition(x, y);
	}
	public void mouseReleased(MouseEvent arg0) {
		int dec = img.getWidth(this)/2;

		int pos[][] = engine.getDrawPos();
		xs = pos[0][0]-dec;
		ys = pos[0][1]-dec;
		
		xup = arg0.getX();
		yup = arg0.getY();
		engine.setUpPosition(xup, yup, dec);
		
	}

}
