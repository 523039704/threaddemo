package com.lcq.network;  
  
/** 
 *  
 * 类名：MainClient 
 * 功能：建立主客户端类，接受服务器端的信息并显示，与服务器端进行交互 
 * 时间： 
 * 作者：lcq 
 * 版本： 
 *  
 */  
  
  
import java.net.Socket;  
  
public class MainClient {  
  
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
          
        Socket socket = new Socket("127.0.0.1", 4000);  
            new ClientInputThread(socket).start();  
            new ClientOutputThread(socket).start();  
    }  
}  