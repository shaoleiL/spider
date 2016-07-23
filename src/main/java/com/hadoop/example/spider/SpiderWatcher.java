package com.hadoop.example.spider;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
/**
 * 监视器需要是一个守护进程
 * @author Administrator
 *
 */
public class SpiderWatcher  implements Watcher{
	CuratorFramework client;
	List<String> childrenList;
	public SpiderWatcher() {
		String connectString = "192.168.1.171:2181,192.168.1.172:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		int sessionTimeoutMs = 5000;//会话超时时间，默认40S。这个值必须在4S--40S之间
		int connectionTimeoutMs = 1000;
		//获取客户端连接
		client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
		
		try {
			//监视spider下面所有子节点的变化情况，注册监视器，注意，这个监视器单次有效，需要重复注册
			childrenList = client.getChildren().usingWatcher(this).forPath("/spider");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 当监控器发现监控的节点下的子节点发生变化的时候，这个方法就会被调用
	 */
	@Override
	public void process(WatchedEvent event) {
		try {
			List<String> newChildrenList = client.getChildren().usingWatcher(this).forPath("/spider");
			for (String node : childrenList) {
				if(!newChildrenList.contains(node)){
					System.out.println("节点消失："+node);
					//给管理员发邮件或者发短信
					/**
					 * 发邮件：javamail
					 * 
					 * 发短信：云片网
					 */
				}
			}
			for (String node : newChildrenList) {
				if(!childrenList.contains(node)){
					System.out.println("新增节点："+node);
				}
			}
			//不能忘记这一行代码
			this.childrenList = newChildrenList;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		SpiderWatcher spiderWatcher = new SpiderWatcher();
		spiderWatcher.run();
	}


	private void run() {
		while(true){
			;
		}
	}
	
	
	
	

}
