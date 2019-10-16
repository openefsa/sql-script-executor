package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLStatement {

	private Connection con;
	private String query;

	public SQLStatement(Connection con, String query) {
		this.con = con;
		this.query = query;
	}

	public void exec() throws SQLException {

		try (PreparedStatement stmt = con.prepareStatement(query);) {
			stmt.execute();
			stmt.close();
		}

	}

	@Override
	public String toString() {
		return query;
	}
}
