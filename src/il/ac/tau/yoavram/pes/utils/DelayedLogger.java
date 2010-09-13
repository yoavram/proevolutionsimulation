package il.ac.tau.yoavram.pes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DelayedLogger {
	private List<String> logMsgs;
	private Logger inner;

	public DelayedLogger(Logger logger) {
		inner = logger;
		logMsgs = new ArrayList<String>();
	}

	private boolean isReady() {
		return Logger.getRootLogger().getAllAppenders().hasMoreElements();
	}

	public void info(String msg) {
		if (isReady()) {
			if (!logMsgs.isEmpty())
				for (String s : logMsgs)
					inner.info(s);
			inner.info(msg);
		} else {
			logMsgs.add(msg);
			System.out.println(msg);
		}
	}
}
