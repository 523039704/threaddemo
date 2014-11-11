package com.lcq.network;  
  
/** 
 *  
 * 类名：ClientOutputThread 
 * 功能：建立主客户端的输出线程类， 向服务器端传送信息 
 * 时间： 
 * 作者：lcq 
 * 版本： 
 *  
 */  
  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.net.Socket;  
  
public class ClientOutputThread extends Thread{  
  
    Socket socket = new Socket();  
    public ClientOutputThread(Socket socket){  
        this.socket = socket;  
    }  
  
      
    @Override  
    public void run() {  
          
          
        while(true){  
              
            try {  
                OutputStream os = socket.getOutputStream();  
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
                  
                String line = "客户输出："+br.readLine();  
                os.write(line.getBytes());  
                  
                  
                  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
          
              
        }  
          
          
    }  
}  
