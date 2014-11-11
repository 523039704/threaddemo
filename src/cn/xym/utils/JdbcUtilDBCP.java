package cn.xym.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;



public class JdbcUtilDBCP {
	
	private static DataSource datasource = null;
	static{
		try{
		//读取资源文件中的信息
		InputStream in = JdbcUtilDBCP.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
		Properties config = new Properties();
		config.load(in);
		
		BasicDataSourceFactory factory = new BasicDataSourceFactory();
		datasource = factory.createDataSource(config);
		}catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	public static Connection getConnection() throws SQLException{
		return datasource.getConnection();
	}
	
	public static void release(Connection conn, Statement st, ResultSet rs){
		if (rs != null){
			try{
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (st != null){
			try{
				st.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			st = null;
		}
		if (conn != null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
