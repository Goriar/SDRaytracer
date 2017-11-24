package de.unitrier.raySDgoriar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* Implementation of a very simple Raytracer
   Stephan Diehl, Universität Trier, 2010-2016
*/

public class SDRaytracer extends JFrame {
	private static final long serialVersionUID = 1L;
	boolean profiling = false;
	int width = 1000;
	int height = 1000;

	@SuppressWarnings("rawtypes")
	Future[] futureList = new Future[width];
	int nrOfProcessors = Runtime.getRuntime().availableProcessors();
	ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

	Light[][] image = new Light[width][height];
	Scene scene;
	public static void main(String argv[]) {
		long start = System.currentTimeMillis();
		SDRaytracer sdr = new SDRaytracer();
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("time: " + time + " ms");
		System.out.println("nrprocs=" + sdr.nrOfProcessors);
	}

	SDRaytracer() {
		scene.createScene(this);

		if (!profiling)
			scene.renderImage(this);
		else
			scene.profileRenderImage(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		final SDRaytracer self = this;
		@SuppressWarnings("serial")
		JPanel area = new JPanel() {
			public void paint(Graphics g) {
				System.out.println("fovx=" + Scene.FOVX + ", fovy=" + Scene.FOVY + ", xangle=" + scene.x_angle_factor + ", yangle="
						+ scene.y_angle_factor);
				if (image == null)
					return;
				for (int i = 0; i < width; i++)
					for (int j = 0; j < height; j++) {
						g.setColor(image[i][j].getColor());
						// zeichne einzelnen Pixel
						g.drawLine(i, height - j, i, height - j);
					}
			}
		};

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				boolean redraw = false;
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					scene.x_angle_factor--;
					// mainLight.position.y-=10;
					// fovx=fovx+0.1f;
					// fovy=fovx;
					// maxRec--; if (maxRec<0) maxRec=0;
					redraw = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					scene.x_angle_factor++;
					// mainLight.position.y+=10;
					// fovx=fovx-0.1f;
					// fovy=fovx;
					// maxRec++;if (maxRec>10) return;
					redraw = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					scene.y_angle_factor--;
					// mainLight.position.x-=10;
					// startX-=10;
					// fovx=fovx+0.1f;
					// fovy=fovx;
					redraw = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					scene.y_angle_factor++;
					// mainLight.position.x+=10;
					// startX+=10;
					// fovx=fovx-0.1f;
					// fovy=fovx;
					redraw = true;
				}
				if (redraw) {
					scene.createScene(self);
					scene.renderImage(self);
					repaint();
				}
			}
		});

		area.setPreferredSize(new Dimension(width, height));
		contentPane.add(area);
		this.pack();
		this.setVisible(true);
	}
}
