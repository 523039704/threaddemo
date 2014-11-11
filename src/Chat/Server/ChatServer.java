package Chat.Server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ChatServer {

	public static void main(String[] args) {
		ChatRoom cr = new ChatRoom("Chat Server V1.0");
		if(args.length == 0){
			cr.smf.ShowMessage("Empty args!\nPlease transfer the port by args!\n");
			cr.smf.ShowMessage("Now using default port:8888...");
			cr.Accept(8888);
		} else {
			cr.Accept(Integer.parseInt(args[0]));
		}
			
	}

}

class ChatRoom {
	ServerSocket ss;
	ServerMonitorFrame smf;
	List<Client> clientList = Collections.synchronizedList(new ArrayList<Client>(10));

	ChatRoom(String s) {
		smf = new ServerMonitorFrame(s);
	}

	void Accept(int port) {
		try {
			ss = new ServerSocket(port);
			smf.ShowMessage("Accepting clients...\n");
			while (smf.keepRunning) {
				Socket sClient = ss.accept();
				smf.ShowMessage("\n[Client " + sClient.getInetAddress().toString() + " connected!]\n");				
				Client c = new Client(sClient, this);
				new Thread(c).start();// 监听所有客户端
				clientList.add(c);				
				smf.ShowMessage("[现时客户端连接数：" + clientList.size() + "]\n");
			}

		} catch(BindException e){
System.out.println("端口" + port + "已被占用！ChatServer初始化失败！请使用另外的端口号重新运行！");
			smf.ShowMessage("端口" + port + "已被占用！ChatServer初始化失败！请使用另外的端口号重新运行！");
			//System.exit(0);
		} catch (IOException e) {			
System.out.println("接收客户端连接时出现I/O异常！");
			smf.ShowMessage("接收客户端连接时出现I/O异常！");
			//e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				System.out.println("关闭ServerSocket失败！");
				e.printStackTrace();
			}
		}
	}
}

class ServerMonitorFrame extends Frame {
	TextArea ta = new TextArea("", 15, 65, TextArea.SCROLLBARS_HORIZONTAL_ONLY);
	StringBuffer strServerLog = new StringBuffer();
	boolean keepRunning = true;

	ServerMonitorFrame(String s) {
		super(s);
		ta.setText("");
		add(ta, BorderLayout.CENTER);
		setLocation(300, 150);
		this.addWindowListener(new FrameMonitor());
		pack();
		setVisible(true);
	}

	public void ShowMessage(String s) {
		strServerLog.append(s);
		ta.setText(strServerLog.toString());
		ta.setCaretPosition(strServerLog.length());
	}

	class FrameMonitor extends WindowAdapter {// 关闭服务器，扫地工作
		public void windowClosing(WindowEvent e) {
			keepRunning = false;
			System.exit(0);
		}
	}

}

class Client implements Runnable {
	ChatRoom cr = null;
	Socket s = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	boolean keepRunning = false;
	private String strTem;
	String clientName = null;	

	Client(Socket s, ChatRoom cr) {
		this.s = s;
		this.cr = cr;
		this.clientName = s.getInetAddress().toString();
		keepRunning = true;

		try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() { // 读取客户端信息线程
		try {
			while (keepRunning) {
				strTem = dis.readUTF();
				if(strTem.length()>5 && strTem.substring(0, 5).equalsIgnoreCase("name:")){
					SendMsg(clientName + " 更名为：" + strTem.substring(5));//保留有一个换行符
					clientName = strTem.substring(5, strTem.length()-1);//不保留换行符
				} else {
					SendMsg(clientName + ":" + strTem);
				}
				
			}
		} catch(SocketException e) {
			System.out.println("\n[从客户端 " + clientName + "接收信息时出错！可能客户端已经关闭。]\n");
		} catch (EOFException e) {
			cr.smf.ShowMessage("\n[Client " + clientName + " Disconnected!]\n");
		} catch (IOException e) {
			System.out.println("\n[接收客户端 " + clientName + "消息时出现I/O异常！]\n");
			e.printStackTrace();
		} finally {
			keepRunning = false;
			cr.clientList.remove(this);
			cr.smf.ShowMessage("[现时客户端连接数：" + cr.clientList.size() + "]\n");
			try {
				if (dis != null) dis.close();				
				if(dos != null) dos.close();				
				if (s != null) s.close();
				
			} catch (IOException e) {
				System.out.println("Client " + clientName + "的线程：清理内存时出现I/O异常！");
				e.printStackTrace();
			}
		}
	}
	
	public void SendMsg(String s){//将MsgSender的功能包装到这个类
		for(int i=0; i<cr.clientList.size(); i++){
			Client cliCur = cr.clientList.get(i);
			try {
				cliCur.dos.writeUTF(s);
				cliCur.dos.flush();
			} catch (IOException e) {
				System.out.println("发送信息至客户端时出错！可能客户端已经关闭。");
				try {
					if(dos != null) dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}			
		}
	}
}