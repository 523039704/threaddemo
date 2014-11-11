package runnable.message;
//定义一个取钱的线程类
public class depositThread implements Runnable {

	private Account account;  
    private double depositAmount;  
       
    public depositThread(Account account, double depositAmount) {  
        super();  
        this.account = account;  
        this.depositAmount = depositAmount;  
    }  
  
  
    public void run() {  
    for(int i=0;i<100;i++){  
         account.deposit(depositAmount);  
         try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
      }  
    }  
  

}
