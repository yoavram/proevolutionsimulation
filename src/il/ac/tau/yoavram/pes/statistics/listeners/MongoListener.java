package il.ac.tau.yoavram.pes.statistics.listeners;

import il.ac.tau.yoavram.pes.io.MongoDbBean;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;

public class MongoListener implements DataListener {
	private static final Logger logger = Logger.getLogger(MongoListener.class);

	private MongoDbBean mongo;
	private String collection;
	private List<String> dataFieldNames;
	BasicDBObject doc;

	@Override
	public void init() {
		if (mongo == null)
			throw new NullPointerException(MongoDbBean.class.getName()
					+ " must be given");
		doc = new BasicDBObject();

	}

	@Override
	public void close() throws IOException {
		mongo.close();
	}

	@Override
	public void listen(Number[] data) {
		int len = data.length;
		if (data.length != dataFieldNames.size()) {
			logger.error("data length: " + data.length + ", field names: "
					+ dataFieldNames.size());
			len = Math.min(data.length, dataFieldNames.size());
		}

		doc.clear();
		for (int i = 0; i < len; i++) {
			doc.put(dataFieldNames.get(i), data[i]);
		}

		getMongo().getCollection().insert(doc);
	}

	@Override
	public void setDataFieldNames(List<String> dataFieldNames) {
		this.dataFieldNames = dataFieldNames;
	}

	public void setMongo(MongoDbBean mongo) {
		this.mongo = mongo;
	}

	public MongoDbBean getMongo() {
		return mongo;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollection() {
		return collection;
	}

}
