package il.ac.tau.yoavram.simba;

import org.apache.log4j.Logger;

import il.ac.tau.yoavram.pes.Event;

public class EnvironmentalChange implements Event {
	private static final Logger logger = Logger
			.getLogger(EnvironmentalChange.class);

	Environment environment;
	double environmentalChangeFrequency;
	double environmentalChangeIntensity;

	public double getEnvironmentalChangeIntensity() {
		return environmentalChangeIntensity;
	}

	public void setEnvironmentalChangeIntensity(
			double environmentalChangeIntensity) {
		this.environmentalChangeIntensity = environmentalChangeIntensity;
	}

	public double getEnvironmentalChangeFrequency() {
		return environmentalChangeFrequency;
	}

	public void setEnvironmentalChangeFrequency(
			double environmentalChangeFrequency) {
		this.environmentalChangeFrequency = environmentalChangeFrequency;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void happen() {
		if (cern.jet.random.Uniform.staticNextDouble() < getEnvironmentalChangeFrequency()) {
			getEnvironment().change(getEnvironmentalChangeIntensity());
		}

	}
}
