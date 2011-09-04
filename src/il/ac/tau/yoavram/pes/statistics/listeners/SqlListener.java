package il.ac.tau.yoavram.pes.statistics.listeners;

import il.ac.tau.yoavram.pes.SimulationConfigurer;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Records data entries to an SQL database specified by the {@link DataSource}
 * property.</br>
 * 
 * If used as a proper listener, this will record all data entries, much as a
 * {@link CsvWriterListener} does.</br> If used as a final listener, this will
 * record the last data entry - this is the original purpose for this
 * class.</br>
 * <p>
 * <a
 * href="http://static.springsource.org/spring/docs/2.0.6/reference/jdbc.html"
 * >Spring help</a> </br> <a href=
 * "http://static.springsource.org/spring/docs/2.0.0/api/org/springframework/jdbc/core/JdbcTemplate.html"
 * >api</a>
 * 
 * @author yoavram
 * @version Charles
 * 
 */
public class SqlListener implements DataListener {

	private static final String SQL_TEMPLATE = "INSERT INTO %s (%s) VALUES ('%s',%s,%s)";

	private static final Logger logger = Logger.getLogger(SqlListener.class);

	private static final String JOB_NAME_KEY = SimulationConfigurer.JOB_NAME_KEY;
	private static final String TIME = SimulationConfigurer.TIME;

	private String columns;
	private String table;
	private String jobName;
	private String time;
	private JdbcTemplate jdbcTemplate;

	@Override
	public void init() {
		if (time == null) {
			time = TimeUtils.toSqlTime(new Date());
		}
		if (jobName == null) {
			throw new NullPointerException("No job name given");
		}
	}

	@Override
	public void listen(Number[] data) {
		String values = StringUtils.join(data, ", ");
		String sql = String.format(SQL_TEMPLATE, getTable(), columns,
				getJobName(), getTime(), values);
		int rows = jdbcTemplate.update(sql);
		if (rows < 1) {
			logger.warn(String.format("Database update (%s) affected %d rows",
					sql, rows));
		}
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void setDataFieldNames(List<String> dataFieldNames) {
		columns = String.format("`%s`, `%s`, `%s`", JOB_NAME_KEY, TIME,
				StringUtils.join(dataFieldNames, "`, `"));
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTime() {
		return time;
	}

	public void setSqlTime(String time) {
		this.time = time;
	}

	public void setTime(String timeString) {
		this.time = TimeUtils.toSqlTime(timeString);
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setLazyInit(true);
	}

}
