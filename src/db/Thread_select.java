package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 

import util.Testdemo; 

/**
 * 多线程 数据库检索的demo
 * @author liu
 *
 */
class Ji implements Runnable {
	
	@SuppressWarnings("unused")
	private String s = null;
	private String w = null;
	String sql = null;
	
	public Ji(){}
	public Ji(String w, String s){
		this.s= s;
		this.w= w;
		sql = "select * from `order` WHERE  " + s;
	}
	PreparedStatement pt = null;
	Connection connect = Connect.getConnection(); // 同样先要获取连接，即连接到数据库

	public void run() {
		try {
			pt = connect.prepareStatement(sql);
			ResultSet rs = pt.executeQuery();
			while (rs.next()) {
				System.out.println( w+"  线程"+rs.getString("id")+"  "+ rs.getString("orderid")); // 这里将问号赋值
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
 

public class Thread_select {
	 public static void main(String[] args) throws Exception{
		  Testdemo  t = Testdemo .getThreadPool(5); 	 
		Thread_select select= new Thread_select();
//		 int cishu=select.sqlsum();
		 int cishu=50000;
		 int yushu=cishu%1000;
		 int id=         1000;
		     cishu=cishu/1000;
		 if(yushu>=0)
		 {
			 cishu=cishu+1; 
		 }
		 
 
		 for(int a=0; a<cishu;a++)
		 {
			 if(a==0)
			 {
				   t.execute(new Ji(String.valueOf(1),"id>"+id))   ;
			 }else if(a==50)
			 {
				   t.execute( new Ji(String.valueOf(2),"id<='"+(id*a)+"' and id>='"+(id*a+yushu)+"'") ) ;
			 }else
			 {
				   t.execute(  new Ji(String.valueOf(3),"id<='"+(id*a)+"' and id>'"+id*(a+1)+"'"))  ;
			 }
		 }
		 
		      System.out.println("开始：  "+t);  
		      t.destroy();// 所有线程都执行完成才destory  
		      System.out.println("结束：  "+t);  
	 
	 }
	 
	public int sqlsum()
	{
		int sum=0;
		PreparedStatement pt = null;
		Connection connect = Connect.getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			pt = connect.prepareStatement("select COUNT(*) as   id  from `order`");
			ResultSet rs = pt.executeQuery();
			if (rs.next()) {
				sum=rs.getInt("id");
				System.out.println("总数----->"+sum); // 这里将问号赋值
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connect.close();
			} catch (SQLException e) {
			}
		}
	return sum;	
	} 
}
