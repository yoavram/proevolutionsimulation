package il.ac.tau.yoavram.pes.io;

import il.ac.tau.yoavram.pes.SimulationConfigurer;
import il.ac.tau.yoavram.pes.utils.SpringUtils;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Sets;

public class PropertiesSqlPersister implements PropertiesPersister {
	public static final String PROPERTIES = "properties";

	@SuppressWarnings("unused")
	private final static Logger logger = Logger
			.getLogger(PropertiesSqlPersister.class);

	private JdbcTemplate jdbcTemplate;
	private String table;

	@Override
	public boolean persist(Properties properties) {
		String sql = String.format("INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)",
				getTable(), SimulationConfigurer.JOB_NAME_KEY,
				SimulationConfigurer.TIME, PROPERTIES);
		int rows = jdbcTemplate.update(sql, properties
				.getProperty(SimulationConfigurer.JOB_NAME_KEY), TimeUtils
				.parseToSqlDate(properties
						.getProperty(SimulationConfigurer.TIME)), properties
				.toString());
		return rows > 0;

		/*
		 * List<Object> keys = Lists.newArrayList(); List<Object> values =
		 * Lists.newArrayList(); for (Entry<Object, Object> e :
		 * properties.entrySet()) { keys.add(e.getKey());
		 * values.add(e.getValue()); } StringBuilder sb = new StringBuilder();
		 * sb.append("INSERT INTO ").append(getTable()).append(" (");
		 * sb.append(StringUtils.join(keys, ", ")); sb.append(") VALUES (");
		 * sb.append(StringUtils.join(values, ", ")); sb.append(")"); String sql
		 * = sb.toString(); int rows = jdbcTemplate.update(sql); if (rows < 1) {
		 * logger.warn(String.format(
		 * "Properties failed to persist to database with query %s", sql)); }
		 * else { logger.info(String.format(
		 * "Properties persisted to database in table %s", getTable())); }
		 * return rows > 0;
		 */
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setLazyInit(true);
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		String xml = "properties_persister.xml";
		if (args.length != 1) {
			System.out
					.println("Usage: java "
							+ PropertiesSqlPersister.class
							+ " <properties filename | folder with properties file>\n"
							+ "Spring XML file with name "
							+ xml
							+ " must be in classpath, and it must define a propert data source for the persister.");
		}
		File file = new File(args[0]);
		Collection<File> files = Sets.newHashSet();
		if (file.isDirectory()) {
			files.addAll(Arrays.asList(file
					.listFiles((FilenameFilter) (FileFilterUtils
							.suffixFileFilter(".properties")))));
		} else if (file.isFile()) {
			files.add(file);
		}

		PropertiesSqlPersister pers = SpringUtils.getContext(xml).getBean(
				PropertiesSqlPersister.class);

		int success = 0;
		for (File f : files) {
			Properties prop = new Properties();
			prop.load(new FileInputStream(f));
			if (pers.persist(prop))
				success++;
		}
		System.out.println(String.format(
				"Successfuly persisted %d properties files out of %d", success,
				files.size()));
	}
}
