package db.pool;

import org.apache.commons.pool.impl.GenericObjectPool; 

public class MainTest {

 

	public static void main(String[] args) throws Exception {

		PoolObjectFactory factory = new PoolObjectFactory();
		GenericObjectPool pool = new GenericObjectPool(factory);
		pool.setMaxActive(50); // 能从池中借出的对象的最大数目
	 
		pool.setMaxIdle(20); // 池中可以空闲对象的最大数目
		pool.setMaxWait(100000); // 对象池空时调用borrowObject方法，最多等待多少毫秒
		pool.setTimeBetweenEvictionRunsMillis(600000);// 间隔每过多少毫秒进行一次后台对象清理的行动
		pool.setNumTestsPerEvictionRun(-1);// －1表示清理时检查所有线程
		pool.setMinEvictableIdleTimeMillis(3000);// 设定在进行后台对象清理时，休眠时间超过了3000毫秒的对象为过期
		// Thread_select select= new Thread_select();
		// int cishu=select.sqlsum();
		int cishu = 500000;
		int yushu = cishu % 1000;
		int id = 1000;
		cishu = cishu / 1000;
		if (yushu >= 0) {
			cishu = cishu + 1;
		}
		for (int a = 0; a < cishu; a++) {
		    	TestThread testThread = (TestThread) pool.borrowObject();  
		    	testThread.setPool(pool);  
		    	testThread.setIsRunning(true);  
			
			if (a == 0) {
				factory.setSql( "id>" + id);
			} else if (a == 50) {
				factory.setSql( "id<='" + (id * a) + "' and id>='"+ (id * a + yushu) + "'");
			} else {
				factory.setSql(  "id<='" + (id * a) + "' and id>'"	+ id * (a + 1) + "'");
			}
		}

	}

}
