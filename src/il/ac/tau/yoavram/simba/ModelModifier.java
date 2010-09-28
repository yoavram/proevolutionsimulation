package il.ac.tau.yoavram.simba;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import il.ac.tau.yoavram.pes.SimulationConfigurer;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//NEVER USED
public class ModelModifier {
	public static void main(String[] args) throws IOException {
		System.out.println("Starting " + ModelModifier.class.getSimpleName());
		SimulationConfigurer configurer = new SimulationConfigurer(args);
		if (configurer.getSpringXmlConfig() == null) {
			System.err.println("Spring XML config file not defined");
			System.err.println();
			System.exit(1);
		}
		if (configurer.getProperties() == null) {
			System.err.println("Properties not defined");
			System.err.println();
			System.exit(1);
		}
		Properties properties = configurer.getProperties();
		Double mu = properties.containsKey("mutationRate") ? Double.NaN
				: Double.valueOf(properties.getProperty("mutationRate"));
		Double benMu = properties.containsKey("beneficialMutationProbability") ? Double.NaN
				: Double.valueOf(properties
						.getProperty("beneficialMutationProbability"));
		Double s = properties.containsKey("selectionCoefficient") ? Double.NaN
				: Double.valueOf(properties.getProperty("selectionCoefficient"));

		AbstractXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				configurer.getSpringXmlConfig().toString());
		SimbaModel model = ctx.getBean(SimbaModel.class);
		for (List<Bacteria> population : model.getPopulations()) {
			for (Bacteria b : population) {
				if (!Double.isNaN(mu)) {
					b.setMutationRate(mu);
				}
				if (!Double.isNaN(benMu)) {
					b.setBeneficialMutationProbability(benMu);
				}
				if (!Double.isNaN(s)) {
					b.setSelectionCoefficient(s);
				}
			}
		}
		model.serialize();
	}
}
