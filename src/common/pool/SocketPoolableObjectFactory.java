package common.pool;

 


import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.apache.commons.pool.KeyedPoolableObjectFactory;

/**
 * <p>Title: </p>
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

public class SocketPoolableObjectFactory implements KeyedPoolableObjectFactory {

    /**
     * 创建新的对象
     * @param key Object 创建对象需要用到的参数
     * @return Object SocketChannel实例
     * @throws Exception
     */
    public Object makeObject(Object key) throws Exception {
    SocketAddress address = (SocketAddress) key;

    // 创建SocketChannel
    SocketChannel channel = SocketChannel.open(address);

    return channel;
  }

  /**
   * 销毁对象
   * @param key Object 创建对象时的参数
   * @param obj Object 需要销毁的对象
   * @throws Exception
   */
  public void destroyObject(Object key, Object obj) throws Exception {
    SocketChannel channel = (SocketChannel) obj;

    if (channel != null)
      channel.close();
    channel = null;
  }

  /**
   * 检验对象是否有效
   * @param key Object 创建对象时的参数
   * @param obj Object 需要进行检验的对象
   * @return boolean 有效返回true，无效返回false
   */
  public boolean validateObject(Object key, Object obj) {
    SocketChannel channel = (SocketChannel) obj;

    if (channel != null && channel.isOpen() && channel.isConnected())
      return true;

    return false;
  }

  /**
   * 将对象激活，这里不需要做任何工作
   * @param key Object
   * @param obj Object
   * @throws Exception
   */
  public void activateObject(Object key, Object obj) throws Exception {
  }

  /**
   * 将对象挂起，这里不需要做任何工作
   * @param key Object
   * @param obj Object
   * @throws Exception
   */
  public void passivateObject(Object key, Object obj) throws Exception {
  }

}

