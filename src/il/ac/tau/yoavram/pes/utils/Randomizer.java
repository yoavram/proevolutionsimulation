package il.ac.tau.yoavram.pes.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cern.jet.random.Uniform;

public class Randomizer extends Thread {
	private static int COUNTER = 0;
	private static final int CAPACITY = 100000;

	private Uniform generator;
	private BlockingQueue<Double> doubles;

	public Randomizer() {
		super(Randomizer.class.getSimpleName() + Integer.toString(COUNTER));
		doubles = new LinkedBlockingQueue<Double>(CAPACITY);
		generator = new Uniform(0, 1, Uniform.makeDefaultGenerator());
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				doubles.put(generator.nextDouble());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public double nextDouble() {
		try {
			return doubles.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Double.NaN;
	}

	public int nextInt(int from, int to) {
		return (int) (from + ((1 + to - from) * nextDouble()));
	}

}
