package il.ac.tau.yoavram.pes.tests;

//
//nonrandom - Tim Tyler 1998.
//

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class RandomApplet extends java.applet.Applet implements Runnable {
	private int nextInt() {
		return RandomUtils.nextInt();
		//return rnd.nextInt();
		//return twister.nextInt();
	}
	//org.apache.commons.math.random.MersenneTwister twister=new org.apache.commons.math.random.MersenneTwister();
	int[][] out_grid;

	int a_atgrid = 0;

	boolean RepaintAll = true;

	Thread calculate;

	Image image_Image;
	Graphics main_image;
	int lastx = -1, lasty = -1;
	final static int xoff = 0, yoff = 0;
	int side = 100;
	int x, y;
	//
	final static int MAXNUM = 100000;

	// TT_Random TT_rnd = new TT_Random();

	 Random rnd = new Random();

	int hside = 128;
	int vside = 128;

	// /
	long start = 128;
	long stop = 128;

	long diff_a;
	long diff_b;
	long diff_c;
	int i;
	// /

	int gridsize = 3;
	int new_size;

	int generation = 0;

	int width;
	int height;

	Color WorldColor = new Color(64, 64, 64);
	Color GridColor = Color.black;
	Color foreground_Color = Color.white;

	// ...........................Methods........................

	public void init() {
		int c;
		boolean random = false;

		if (getParameter("h") != null)
			hside = (new Integer(getParameter("h"))).intValue();

		if (getParameter("v") != null)
			vside = (new Integer(getParameter("v"))).intValue();

		if (getParameter("g") != null)
			gridsize = (new Integer(getParameter("g"))).intValue();

		new_size = (gridsize > 1) ? gridsize : 2;

		// tell browser...
		width = hside * gridsize;
		height = vside * gridsize;

		resize(width + xoff, height + yoff);

		out_grid = new int[hside][vside];

		start();

		 rnd.setSeed(99);

		calculate = new Thread(this);

		calculate.start();
	}

	public void docalculate() {
		generation++;

		for (x = 0; x < hside; x++) {
			for (y = 0; y < vside; y++) {

				// *very* poor quality
				///////out_grid[x][y] = (int) rnd.nextLong();
				out_grid[x][y] = nextInt();
				// out_grid[x][y] = (int) TT_rnd.nextInt();

				// poor quality
				// out_grid[x][y] = rnd.nextInt();

				// much more reasonable output
				// out_grid[x][y] = (int) (rnd.nextDouble() * 128);
			}
		}

		// System.out.println("wwwibejdbiu");
	}

	public void redraw_grid(Graphics g) {
		int width = hside * gridsize;

		g.setColor(WorldColor);
		g.fillRect(0, 0, width, width);

		if (gridsize > 1) {
			g.setColor(GridColor);
			for (x = 0; x <= width; x += gridsize) {
				g.drawLine(x, 0, x, width);
			}

			g.setColor(GridColor);
			for (y = 0; y <= width; y += gridsize) {
				g.drawLine(0, y, width, y);
			}
		}
	}

	// .....................Run Method.......................

	public void run() {
		while (true) {

			docalculate();

			update_image(main_image);

			repaint();

			updateStatus();
		}
	}

	public void start() {
		image_Image = createImage(hside * gridsize + 1, vside * gridsize + 1);
		main_image = image_Image.getGraphics();

		RepaintAll = true;
	}

	// public void stop() {
	// if (calculate != null) {
	// calculate.stop();
	// calculate = null;
	// }
	// }

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		g.drawImage(image_Image, xoff, yoff, this);
	}

	public void update_image(Graphics g) {
		if (RepaintAll) {
			RepaintAll = false;
			redraw_grid(g);
		}

		for (x = 0; x < hside; x++) {
			for (y = 0; y < vside; y++) {
				if ((out_grid[x][y] & 1) != 0)
					g.setColor(foreground_Color);
				else
					g.setColor(WorldColor);

				g.fillRect(x * gridsize + 1, y * gridsize + 1, new_size - 1,
						new_size - 1);
			}
		}
	}

	public void updateStatus() {
		showStatus("Generation:" + generation);
	}

	public static void main(String[] args) {
		RandomApplet n = new RandomApplet();
		n.init();
	}
}