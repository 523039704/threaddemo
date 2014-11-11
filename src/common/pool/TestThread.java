package common.pool;


import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import org.apache.commons.pool.*;
import org.apache.commons.pool.impl.*;

/**
 * <p>Title: 测试的线程类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author George Hill
 * @version 1.0
 */

public class TestThread implements Runnable {

  // 线程名称
  private String name;

  // 对象池
  private KeyedObjectPool pool;

  // 连接的网络地址
  private InetSocketAddress address;

  public TestThread(String name, KeyedObjectPool pool,
                    InetSocketAddress address) {
    this.name = name;
    this.pool = pool;
    this.address = address;
  }

  public void run() {
    System.out.println(name + ": Client Start");
    SocketChannel channel = null;

    try {
      channel = (SocketChannel) pool.borrowObject(address);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    // 从对象池中借出对象成功
    if (channel != null) {
      System.out.println(name + ": Borrow Channel successfully!");

      try {
        channel.configureBlocking(false);

        // 创建Selector
        Selector selector = Selector.open();
        // 向Selector注册我们需要的READ事件
        SelectionKey skey = channel.register(selector, SelectionKey.OP_READ);

        boolean stop = false;
        int n = 0;
        int read = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println("Client Start");

        // 轮询
        while (!stop) {
          // 获取Selector返回的时间值
          n = selector.select();

          // 当传回的值大于0事，读事件发生了
          if (n > 0) {
            Set set = selector.selectedKeys();
            Iterator it = set.iterator();

            while (it.hasNext()) {
              skey = (SelectionKey) it.next();
              it.remove();

              if (skey.isReadable()) {
                SocketChannel sc = (SocketChannel) skey.channel();

                while ( (read = sc.read(buffer)) != -1) {
                  if (read == 0) {
                    break;
                  }

                  buffer.flip();
                  byte[] array = new byte[read];
                  buffer.get(array);
                  String s = new String(array);
                  System.out.print(s);
                  buffer.clear();

                  if (s.indexOf("new") != -1) {
                    stop = true;
                    System.out.println();
                  }
                }
              }
            }
          }
        }
      }
      catch (IOException ioe) {
        ioe.printStackTrace();
      }

      try {
        pool.returnObject(address, channel);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    System.out.println(name + ": Client Stop");
  }

  /**
   * 测试方法
   * @param args String[] 控制台参数
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    SocketPoolableObjectFactory factory = new SocketPoolableObjectFactory();
    StackKeyedObjectPoolFactory poolFactory = new StackKeyedObjectPoolFactory(factory);
    KeyedObjectPool pool = poolFactory.createPool();

    // 创建连接清华BBS的线程
    Thread t1 = new Thread(new TestThread("清华", pool, new InetSocketAddress("bbs.tsinghua.edu.cn", 23)));
    t1.start();
    // 创建连接华南理工BBS的线程
    Thread t2 = new Thread(new TestThread("华南理工", pool, new InetSocketAddress("bbs.gznet.edu.cn", 23)));
    t2.start();
  }

}
