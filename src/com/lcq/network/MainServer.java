package com.lcq.network;

/** 
 *  
 * 类名：MainServer 
 * 功能：建立主服务器类，接受客户端的信息并显示，与客户端进行交互 
 * 时间： 
 * 作者：lcq 
 * 版本： 
 *  
 */  
  
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class MainServer {  
  
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
          
        ServerSocket serverSocket = new ServerSocket(4000);  
        //调用socket的accept()方法侦听并接受到此套接字的连接，此方法在连接传入之前一直阻塞。   
        while(true){  
              
            Socket socket = serverSocket.accept();  
              
            new ServerInputThread(socket).start();  
            new SeverOutputThread(socket).start();  
              
              
              
        }  
          
  
    }  
  
}  
