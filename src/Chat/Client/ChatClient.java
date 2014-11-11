package Chat.Client;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class ChatClient {

	public static void main(String[] args) {
		ChatFrame cf = new ChatFrame("Chat V1.0");
		cf.ShowMessage("\n[请在输入框内把一下内容进行复制并敲击回车即可（格式:127.0.0.1:8888）]\n");
	}
}

class ChatFrame extends Frame {
	TextField tf = new TextField();
	TextArea ta = new TextArea("", 15, 65, TextArea.SCROLLBARS_HORIZONTAL_ONLY );
	StringBuffer strContext = new StringBuffer();
	Socket sConnect = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	boolean bConnected = false;
	String strIp = null;
	int port = 0;
	
	Receiver receiver = new Receiver(this);

	ChatFrame(String s) {//窗口构造函数
		super(s);
		setBounds(300, 350, 300, 300);

		add(tf, BorderLayout.SOUTH);
		add(ta, BorderLayout.CENTER);
		this.addWindowListener(new FrameMonitor());
		tf.addActionListener(new TextFieldMonitor());
		pack();

		setVisible(true);
	}

	private class FrameMonitor extends WindowAdapter {//关闭程序，扫地工作
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			if(dos != null){
				try {
					dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(dis != null){
				try {
					dis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			receiver.close();
			
			if(sConnect != null){
				try {
					sConnect.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.exit(0);
		}
	}

	private class TextFieldMonitor implements ActionListener {//监视回车键，发送信息
		
		public void actionPerformed(ActionEvent e) {
			if(!bConnected){
				int rtnVal = 0;
				if((rtnVal = tf.getText().indexOf(':')) == -1) return;
				strIp = tf.getText().substring(0, rtnVal);
				port = Integer.parseInt(tf.getText().substring(rtnVal + 1, tf.getText().length()));
				bConnected = ConnectServer(strIp, port);
			} else {
				SendMessage(tf.getText());			
			}
			tf.setText("");			
		}
	}

	void ShowMessage(String s) {//显示信息
		if(s == null || s.length() == 0) return;
		strContext.append(s);
		ta.setText(strContext.toString());
		ta.setCaretPosition(strContext.length());
	}

	boolean ConnectServer(String strIp, int port) {//连接服务器，初始化两个I/O线程
		this.strIp = strIp;
		this.port = port;
		this.ShowMessage("\n[Connecting Server " + strIp + ", port: " + port + "]\n");
		try {
			sConnect = new Socket(strIp, port);
			dos = new DataOutputStream(sConnect.getOutputStream());
			dis = new DataInputStream(sConnect.getInputStream());
		} catch (UnknownHostException e) {
			this.ShowMessage("\n[Connecting Server failed! Please input correct IP and port address!]\n");
			//e.printStackTrace();
			return false;
			//System.exit(0);
		} catch (IOException e) {
			this.ShowMessage("\n[Connecting Server failed! Please input correct IP and port address!]\n");
			//e.printStackTrace();
			return false;
			//System.exit(0);
		}
		
		Thread tReceiver = new Thread(receiver);
		tReceiver.start();
		receiver.tMyself = tReceiver;
		
		ShowMessage("[Server " + strIp + " Connected!]\n\n");
		ShowMessage("[You can use \"Name:???\" to change your name]\n");
		return true;
	}
	
	private void SendMessage(String s){//发送信息
		if(s == null || s.length() == 0) return;
		try {
			dos.writeUTF(s.trim() + "\n");
			dos.flush();
		} catch (IOException e) {
				try {
					if(dos != null) dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			e.printStackTrace();
		}
	}
	
	
	private class Receiver implements Runnable{//接收信息线程类
		private ChatFrame cf;
		private Thread tMyself;
		private boolean keepRunning = true;
		
		Receiver(ChatFrame cf){
			this.cf = cf;
		}
		
		public void run() {		
			try {
				while(keepRunning){
					ShowMessage(dis.readUTF());
				}
			} catch(EOFException e) {
				ShowMessage("\n[服务器已退出！]\n");
				this.close();					
			} catch(SocketException e){
				cf.ShowMessage("\n[This system is closing!]\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				keepRunning = false;
			}
			
		}
		
		public void close(){
			if(tMyself != null){
				keepRunning = false;
				tMyself.interrupt();
			}
			else{
				keepRunning = false;
			}
		}
	}
}