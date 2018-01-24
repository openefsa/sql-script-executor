package sql;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SQLExecutor implements AutoCloseable {

	private Connection con;
	
	public SQLExecutor(Connection con) {
		this.con = con;
	}

	/**
	 * Read an sql file and execute the queries composed by statements separated by ';'
	 * @param query
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void exec(File file) throws IOException, SQLException {
		Path path = Paths.get(file.getAbsolutePath());
		String queries = new String(Files.readAllBytes(path));
		exec(queries);
	}
	
	/**
	 * Execute queries composed by statements separated by ';'
	 * @param sql
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void exec(String sql) throws SQLException {
		
		// get the statements
		List<SQLStatement> statements = getStatements(sql);
		
		// execute them in the order
		for (SQLStatement stmt : statements) {
			stmt.exec();
		}
	}

	/**
	 * Parse the document and get sql statements
	 * @param query
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private List<SQLStatement> getStatements(String sql) {

		List<SQLStatement> statements = new ArrayList<>();

		StringTokenizer st = new StringTokenizer(sql, ";");

		while(st.hasMoreTokens()) {
			
			String query = st.nextToken();
			
			if (query.trim().isEmpty())
				continue;
			
			statements.add(new SQLStatement(con, query));
		}

		return statements;
	}

	@Override
	public void close() throws SQLException {
		if (con != null)
			con.close();
	}
}
