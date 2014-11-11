package cn.xym.utils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 
 * @author Administrator
 *
 */
public class JdbcPool implements DataSource{
	
	//ģ��һ�������ӳ�������, ���connection����һ��Ҫ��Ū���̰߳�ȫ�ģ�����ͬ��add,remove��������ģ�
	private static List<Connection> list = Collections.synchronizedList(new LinkedList<Connection>());
	
	private static JdbcPool jdbcPool = new JdbcPool();
	
	private static Properties config = new Properties();
	
	/**
	 * ��̬���������ֻ��Ҫִ��һ�εģ�
	 * 1.��ȡ�����ļ�
	   2.��ʼ�����ӳ������connection����
	 */
	static{
		try{
			/**
			 * ��ȡ�����ļ���config��
			 * ����mysql�����
			 */
			InputStream in = JdbcPool.class.getClassLoader().getResourceAsStream("db.properties");
			config.load(in);
			Class.forName(config.getProperty("com.mysql.jdbc.Driver"));
			
			/**
			 * 
			 * ��ʼ�����ӳ������������ʼ���߱�10����connection����
			 */
			
			for (int i=0; i<10; i++){
				Connection conn = DriverManager.getConnection(
						config.getProperty("jdbc:mysql://localhost:3306/fgpbilling"),
						config.getProperty("root"),
						config.getProperty("111"));
				list.add(conn);
			}
			
		}catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	private JdbcPool() {
		// TODO Auto-generated constructor stub
	}
	
	public static JdbcPool getInstance(){
		return jdbcPool;
	}
	
	/**
	 * ���䣬�ͷ�connection���߼����?
	 * 		connection�Ļ�ȡ��ÿ�ζ���ȡlist��ͷһ���ڵ����
	 * 		connection���ͷţ���list�м���һ��connection����
	 * 
	 * ��˵�conn.close()�����õ�ʱ��������Ҫ��jdbcPool��list�м���connection����,
	 * �������Ҫ����ǿ��Connection���close()����
	 * 
	 * 
	 */
	@Override
	public Connection getConnection() throws SQLException {
		
		if (list.size() <= 0 ){
			throw new RuntimeException("��ݿ����ӳ��Ѿ����꣬��ȴ�");
		}
		//��ݿ����ӳ��л���connection����Ļ�
		Connection conn = list.get(0);
		list.remove(0);
		StrongConnection strongConnection = new StrongConnection(conn);
		System.out.println("�и�connection��������,��list���Ƴ�!��ǰ���ӳ���connection������" + list.size());
		return strongConnection;
	}
	
	/*
	 * ��ǿһ����Ĳ������£�
	 * 1.����һ���࣬ʵ���뱻��ǿ��ͬ�Ľӿ�
	   2.�����ж���һ����������ס����ǿ����(����ſ��Ե����������ķ���)
	   3.����һ�����캯����ձ���ǿ����
	   4.��������ǿ�ķ���
	   5.���ڲ�����ǿ�ķ�����ֱ�ӵ���Ŀ����󣨱���ǿ���󣩵ķ���
	   
	   ���£�
	   Ҫ����ǿ�Ķ���Connection
	   ��ôҪʵ��Connection�����нӿڣ�����һ������Connection�����ס���Connection�����
	   ��һ�����캯��������������󣬸���close()��������������ǿ�ķ�����,����Ĳ�����ǿ�ķ���
	   �� this.conn.������  ȥ���þ�OK��
	   
	 */
	class StrongConnection implements Connection{
		private Connection conn;
		
		public StrongConnection(Connection conn) {
			this.conn = conn;
		}

		@Override
		public void close() throws SQLException {
			// TODO Auto-generated method stub
			
			JdbcPool.list.add(this.conn);
			System.out.println("�и�connection�ͷ���,�ص����ӳ�����list��!��ǰ���ӳ���connection������" + list.size());
		}

		/**
		 * ����ķ���ʵ�� ȫ������
		 * this.conn.������
		 * �ķ�ʽ���ã�����̫���˹��������Ҿ�дǰ�漸��
		 * ����֮����д��̬���?��ʵ����ǿ���ʱ��Ͳ���Ҫ��ô�鷳�ˣ���
		 */
		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.unwrap(iface);
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.isWrapperFor(iface);
		}

		@Override
		public Statement createStatement() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createStatement();
		}

		@Override
		public PreparedStatement prepareStatement(String sql)
				throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String nativeSQL(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void commit() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void rollback() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isClosed() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setCatalog(String catalog) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCatalog() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setTransactionIsolation(int level) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearWarnings() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Statement createStatement(int resultSetType,
				int resultSetConcurrency) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql,
				int resultSetType, int resultSetConcurrency)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setHoldability(int holdability) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getHoldability() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Statement createStatement(int resultSetType,
				int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql,
				int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql,
				int autoGeneratedKeys) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql,
				int[] columnIndexes) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql,
				String[] columnNames) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Clob createClob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Blob createBlob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public NClob createNClob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SQLXML createSQLXML() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isValid(int timeout) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setClientInfo(String name, String value)
				throws SQLClientInfoException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setClientInfo(Properties properties)
				throws SQLClientInfoException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getClientInfo(String name) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Array createArrayOf(String typeName, Object[] elements)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Struct createStruct(String typeName, Object[] attributes)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setSchema(String schema) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getSchema() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void abort(Executor executor) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds)
				throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	
	//����ķ��������ǲ�����
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
