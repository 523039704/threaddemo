package cn.xym.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcTomcat {
	private static DataSource ds;
	static {
		try{
			//初始化上下文环境
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/test1");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
}
