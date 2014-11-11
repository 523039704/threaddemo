package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
	/**
	 * 数据库连接
	 * 
	 * @return 返回所建立的数据库连接
	 */

	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fgpbilling", "root","111");
		} catch (Exception e) {
			e.getMessage();
		}
		return con;
	}
}