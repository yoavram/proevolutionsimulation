package il.ac.tau.yoavram.pes.io;

import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;

public class PropertiesSqlPersister implements PropertiesPersister {
	private final static Logger logger = Logger
			.getLogger(PropertiesSqlPersister.class);

	private JdbcTemplate jdbcTemplate;
	private String table;

	@Override
	public boolean persist(Properties properties) {
		List<Object> keys = Lists.newArrayList();
		List<Object> values = Lists.newArrayList();
		for (Entry<Object, Object> e : properties.entrySet()) {
			keys.add(e.getKey());
			values.add(e.getValue());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(getTable()).append(" (");
		sb.append(StringUtils.join(keys, ", "));
		sb.append(") VALUES (");
		sb.append(StringUtils.join(values, ", "));
		sb.append(")");
		String sql = sb.toString();
		int rows = jdbcTemplate.update(sql);
		if (rows < 1) {
			logger.warn(String.format(
					"Properties failed to persist to database with query %s",
					sql));
		} else {
			logger.info(String.format(
					"Properties persisted to database in table %s", getTable()));
		}
		return rows > 0;
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
}
