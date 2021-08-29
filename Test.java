import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import BaseClasses3D.*;
public class Test extends JPanel {
	Camera camera = new Camera(0, 0, -250);
	double deltaTime;
	double time;
	RectPrism rq = new RectPrism(0, 0, 0, 250, 250, 250, new Vector3(255, 255, 255));
	Text t = new Text("cube", "Arial", 50, Text.ALIGN_CENTER, 0, 0, 0, 0, 5);
	Object3D main = new Object3D();
	Test()
	{
		t.setColor(new Vector3(0, 0, 0));
		main.addToChildren(rq);
		main.addToChildren(t);
	}
	@Override
	public Dimension getPreferredSize() {
		//Sets the size of the panel
		return new Dimension(800,600);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.drawBackground(g);
		// rq.draw(g, camera, time, 1);
		// t.draw(g, camera, time, 1);
		main.draw(g, camera, time, 1);
	}
	public void animate()
	{
		long oldTime = System.nanoTime();
		boolean firstLoop = true;
		while (true)
		{
			if (1/(deltaTime)>60 && !firstLoop)
			{
				try {
					Thread.sleep((long)((1/deltaTime-240)*0.001));
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
			deltaTime = (int)(System.nanoTime() - oldTime) *.000000001;
			oldTime = System.nanoTime();
			firstLoop = false;
			this.time += deltaTime;
			rq.rotate(new Vector3(deltaTime*.5, deltaTime*.923476, deltaTime*.763489));
			repaint();
			//System.out.println(1/deltaTime);
		}
	}

	public void drawBackground(Graphics g)
	{
		g.setColor(new Color(135, 206, 235));
		g.fillRect(0, 0, 800, 600);
	}
}
