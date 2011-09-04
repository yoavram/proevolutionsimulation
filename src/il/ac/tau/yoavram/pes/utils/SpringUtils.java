package il.ac.tau.yoavram.pes.utils;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtils {
	public static <T> T getBean(String[] args, Class<T> clazz) {
		if (args.length != 1) {
			String usage = "Usage: "
					+ clazz.getSimpleName()
					+ " <xml spring configuration>\n\txml spring configuration must be in classpath\n";
			System.err.println(usage);
			throw new IllegalArgumentException(usage);
		}

		AbstractXmlApplicationContext ctx = getContext(args[0]);
		T t = ctx.getBean(clazz);
		return t;
	}

	public static AbstractXmlApplicationContext getContext(
			String classpathLocation) {
		AbstractXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				classpathLocation);
		ctx.registerShutdownHook();
		return ctx;
	}
}
