package db.pool;

import org.apache.commons.pool.PoolableObjectFactory;

public class PoolObjectFactory implements PoolableObjectFactory {

	private String sql = null;
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	// 创建一个新对象or线程
	@Override
	public Object makeObject() throws Exception {
		// TODO Auto-generated method stub
		TestThread test = new TestThread(sql);
		System.out.println(sql);
		test.start();
		System.out.println("创建线程:" + test.getName());
		return test;
	}

	// 销毁对象or线程
	@Override
	public void destroyObject(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		if (arg0 instanceof TestThread) {
			TestThread test = new TestThread();
			System.out.println("销毁线程:" + test.getName());
			test.destroy();
		}

	}

	// "激活"对象or线程
	@Override
	public void activateObject(Object arg0) throws Exception {
	}

	// "钝化"对象or线程
	@Override
	public void passivateObject(Object arg0) throws Exception {
	}

	// 检测对象or线程是否"有效";
	@Override
	public boolean validateObject(Object arg0) {
		return true;
	}

}
