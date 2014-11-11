package runnable.message;
//定义一个取钱的的线程
public class DrawThread implements Runnable {
    private Account account;  
    private double drawAmount;  
      
      
    public DrawThread(Account account, double drawAmount) {  
        super();  
        this.account = account;  
        this.drawAmount = drawAmount;  
    }  
  
    public void run() {  
        for(int i=0;i<100;i++){  
           account.draw(drawAmount);    
           try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        }  
        
    }  

}
