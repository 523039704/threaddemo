package com.lcq.network;  
  
  
/** 
 *  
 * 类名：ServerInputThread 
 * 功能：建立主服务器的输入线程类，接收客户端的信息后打印出来 
 * 时间： 
 * 作者：lcq 
 * 版本： 
 *  
 */  
  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.Socket;  
  
public class ServerInputThread extends Thread{  
  
    private Socket socket;  
    public ServerInputThread(Socket socket){  
        this.socket = socket;  
    }  
    @Override  
    public void run() {  
          
          
        while(true){  
            try {  
                  
                //建立输入流  
                InputStream is = socket.getInputStream();  
                byte[] by = new byte[1024];  
                //将输入流里的字节读到字节数组里，并返回读的字节数  
                int length = is.read(by);  
                //将字节数组里的length个字节转换为字符串  
                String str = new String(by,0,length);  
                //打印出字符串  
                 System.out.println(str);  
                  
                  
                  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
              
        }  
    }  
  
      
      
      
      
      
      
      
      
      
      
      
}  