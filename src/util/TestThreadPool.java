package util;
//测试线程池  
public class TestThreadPool {  
  public static void main(String[] args) {  
      // 创建3个线程的线程池  
	  Testdemo  t = Testdemo .getThreadPool(1);  
      t.execute(  new Task()  );  
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      t.execute(new Runnable[] { new Task() });    
      System.out.println("开始：  "+t);  
      t.destroy();// 所有线程都执行完成才destory  
      System.out.println("结束：  "+t);  
  }  

  // 任务类  
  static class Task implements Runnable {  
      private static volatile int i = 1;  

      @Override  
      public void run() {// 执行任务  
          System.out.println("任务 " + (i++) + " 完成");  
      }  
  }  
}  