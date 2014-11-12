package db.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.pool.impl.GenericObjectPool;

import util.Testdemo;
import db.Connect; 

class TestThread   extends Thread  {
	@SuppressWarnings("unused")
	

	String sql = null;
	private boolean isRunning;  
	private GenericObjectPool pool;  
	  
	    public boolean getIsRunning() {  
	        return isRunning;  
	    }  
	  
	    public synchronized void setIsRunning(boolean flag) {  
	        isRunning = flag;  
	        if (flag) {  
	            this.notify();  
	        }  
	    }  
	  
	    public void setPool(GenericObjectPool pool) {  
	        this.pool = pool;  
	    }  
	  
	    public TestThread () {  
	        isRunning = false;  
	    }  
	  
	    public TestThread ( String s) {  
	     
		 
			sql = "select * from `order` WHERE  " + s;
	        isRunning = false;  
	    }  
	  
	    public void destroy() {  
	        System.out.println("destroy中");  
	        this.interrupt();  
	    }  
	 
	 
	PreparedStatement pt = null;
	Connection connect = Connect.getConnection(); // 同样先要获取连接，即连接到数据库

	public void run() {
		try {
			pt = connect.prepareStatement(sql);
			ResultSet rs = pt.executeQuery();
			while (rs.next()) {
				System.out.println( rs.getString("id")+"  "+ rs.getString("orderid")); // 这里将问号赋值
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 try {
			 connect.close();
		 } catch (SQLException e) {
		 }
	}
}





 