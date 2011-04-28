package il.ac.tau.yoavram.pes.statistics.listeners;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import javax.sql.DataSource;

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
	private static final Logger logger = Logger.getLogger(SqlListener.class);

	private static final String SIM_ID = "sim_id";
	private static final String TIME = "time";

	private String columns;
	private String qMarks;
	private String sql;
	private String table;
	private String simulationId;
	private Timestamp time;
	private JdbcTemplate jdbcTemplate;

	@Override
	public void init() {
		if (time == null) {
			time = new Timestamp(System.currentTimeMillis());
		}
		if (simulationId == null) {
			throw new NullPointerException("No simulation ID given");
		}
	}

	@Override
	public void listen(Number[] data) {
		if (sql == null) {
			sql = String.format("INSERT INTO %s (%s) VALUES (%s)", getTable(),
					columns, qMarks);
		}
		Object[] newdata = new Object[data.length + 2];
		System.arraycopy(data, 0, newdata, 2, data.length);
		newdata[0] = getSimulationId();
		newdata[1] = getTime();

		int rows = jdbcTemplate.update(sql, newdata);
		if (rows < 1) {
			logger.warn(String.format("Db update (%s) affected %d rows", sql,
					rows));
		}
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void setDataFieldNames(List<String> dataFieldNames) {
		StringBuilder cSb = new StringBuilder();
		StringBuilder qSb = new StringBuilder();
		cSb.append(SIM_ID).append(", ").append(TIME);
		qSb.append("?, ?");
		for (String fieldName : dataFieldNames) {
			cSb.append(", ").append(fieldName);
			qSb.append(", ?");
		}
		columns = cSb.toString();
		qMarks = qSb.toString();
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getSimulationId() {
		return simulationId;
	}

	public void setSimulationId(String simulationId) {
		this.simulationId = simulationId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public void setTimeString(String timeString) {
		try {
			this.time = new Timestamp(TimeUtils.getDateFormat()
					.parse(timeString).getTime());
		} catch (ParseException e) {
			logger.error("Failed parsing time string: " + timeString
					+ ", expection: " + e);
		}
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setLazyInit(true);
	}

}
