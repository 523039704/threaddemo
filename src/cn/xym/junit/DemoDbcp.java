package cn.xym.junit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import cn.xym.utils.JdbcPool;
import cn.xym.utils.JdbcTomcat;
import cn.xym.utils.JdbcUtilC3P0;
import cn.xym.utils.JdbcUtilDBCP;



/**
 * Ϊ�˷�����ص����ڹ������ӳص�ѧϰ�ϣ��Ͳ��������ܹ��ķ�ʽ��д������
 * ֱ���ڴ�����дDao���Service��Ķ�����
 * 
 * Demo:
 * ��ͬ�ķ�ʽ, ʵ��ͨ�����ӳػ�ȡconnection, ����ɾ�Ĳ����
 * @author xym
 *
 */
public class DemoDbcp {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		DemoDbcp demo = new DemoDbcp();
		
		//1. �Զ���pool�ķ�ʽ
//		demo.createThread();
		
		//2. ʹ��Tomcat�������ӳصķ�ʽ (����Ҫ��web�в�����,��Ϊ��tomcat�Ĺ��������ҾͲ�������)
		//addUserOfTomcatPool();
		
		//3. ʹ�ÿ�ԴDBCP���ӳ�
		//addUserOfDBCPPool();
		
		//4. ʹ��C3P0���ӳأ��Ƽ����ַ������������ҿ��������������¿�C3P0��DBCP�����
		addUserOfC3P0Pool();
		
		//ɾ�Ĳ�Ͳ�д��

	}
	
	private void createThread() {
		for (int i=0; i<15; ++i){
			new TestThread().start(); 
		}
	}

	private static void addUserOfMyPool(){
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = JdbcPool.getInstance().getConnection();
			String sql = "insert into user(id,username,password) values(?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, UUID.randomUUID().toString());
			st.setString(2, "root");
			st.setString(3, "111");
			st.executeUpdate();
			Thread.sleep(2000);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			release(conn, st, rs);
		}
	}

	@SuppressWarnings("unused")
	private static void addUserOfTomcatPool(){

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = JdbcTomcat.getConnection();
			String sql = "insert into user(id,username,password) values(?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, UUID.randomUUID().toString());
			st.setString(2, "root");
			st.setString(3, "123");
			st.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			release(conn, st, rs);
		}
	}
	
	@SuppressWarnings("unused")
	private static void addUserOfDBCPPool(){

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = JdbcUtilDBCP.getConnection();
			String sql = "insert into user(id,username,password) values(?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, UUID.randomUUID().toString());
			st.setString(2, "root");
			st.setString(3, "123");
			st.executeUpdate();
			System.out.println("OK!��ӳɹ�!DBCP���ӳصķ�ʽ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			release(conn, st, rs);
		}
	}
	

	private static void addUserOfC3P0Pool(){

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = JdbcUtilC3P0.getConnection();
			String sql = "insert into user(id,username,password) values(?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, UUID.randomUUID().toString());
			st.setString(2, "root");
			st.setString(3, "123");
			st.executeUpdate();
			System.out.println("OK!��ӳɹ�!C3P0���ӳصķ�ʽ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			release(conn, st, rs);
		}
	}
	
	
	

	class TestThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			//�����Զ����pool��10��connection���ͷŷ���
			addUserOfMyPool();
			
		}
	}

	
	
	//����������release��

	private static void release(Connection conn, Statement st,
			ResultSet rs) {
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
