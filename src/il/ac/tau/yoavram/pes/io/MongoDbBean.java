package il.ac.tau.yoavram.pes.io;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 
 * @author yoavram
 * @see http://www.mongodb.org/display/DOCS/Java+Language+Center
 */
public class MongoDbBean implements Closeable {
	private Mongo mongo;
	private DB db;
	private String host = "loalhost";
	private String password;
	private String username;
	private int port = DBAddress.defaultPort();
	private String dbName;
	private String collectioName;
	private DBCollection collection;

	public void init() throws UnknownHostException, MongoException {
		if (mongo == null) {
			mongo = new Mongo(host, port);
		}
		if (db == null) {
			if (!mongo.getDatabaseNames().contains(getDbName())) {
				throw new IllegalArgumentException("DB with name "
						+ getDbName() + " not found in MongoDB");
			}
			db = mongo.getDB(dbName);
		}
		if (username != null && password != null) {
			if (!db.authenticate(username, password.toCharArray())) {
				throw new IllegalArgumentException(
						"Username and passowrd are not applicable for MongoDB "
								+ getDb().getName());
			}
		}
		if (collection == null) {
			if (!getDb().getCollectionNames().contains(getCollectioName())) {
				throw new IllegalArgumentException("Collection "
						+ getCollectioName() + " not found in MongoDB "
						+ getDb().getName());
			}
			collection = getDb().getCollection(getCollectioName());
		}
	}

	@Override
	public void close() throws IOException {
		if (mongo != null)
			mongo.close();
	}

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		if (dbName == null && db != null)
			dbName = db.getName();
		return dbName;
	}

	public void setDbName(String dbname) {
		this.dbName = dbname;
	}

	public String getCollectioName() {
		if (collectioName == null && collection != null)
			collectioName = collection.getName();
		return collectioName;
	}

	public void setCollectioName(String collectioName) {
		this.collectioName = collectioName;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

}
