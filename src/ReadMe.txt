存在问题：
由于数据库Connection资源是比较宝贵的，如果反复对每次来访问的Connection资源进行
分配,释放的话，这样势必会影响整个网站的访问性能，所以要做一点优化：引入数据库连接池

连接池实现原理：
1.用户给servlet发送请求，请求Dao要Connection
2.Dao从“连接池”中取出Connection资源，与DB的通讯
3.当用户离开之后，释放该Connection,那么该Connection被释放到连接池中，等待下一个用户来

Demo目标:
通过简单的增删改查来做到下面几个关于连接池的方式，让我们更了解几种优化的方式
1.自定义一个Pool,来实现类似于现在开源连接池为我们做的一些操作
2.使用Tomcat内置的连接池（apache dbcp）
3.使用DBCP数据库连接池
4.使用C3P0数据库连接池（推荐）

步骤：
1.搭建开发环境：
	jar包：
	mysql驱动包：
		mysql-connector-java-5.0.8-bin.jar
	DBCP数据库连接池要用的jar：
		commons-collections-3.1.jar
		commons-dbcp-1.2.2.jar
		commons-pool.jar
	C3P0数据库连接池要用的jar：
		mchange-commons-0.2.jar
		c3p0-0.9.2-pre1.jar
		
	配置文件：
		自定义Pool: db.properties
		DBCP: dbcpconfig.properties
		C3P0: c3p0-config.xml
		
2.数据库创建:
	create database test1;
	use test1;
	
	create table user(
		id varchar(40) primary key,
		username varchar(20),
		password varchar(20)
	);
	

3.建立组织包
	cn.xym.domain
	cn.xym.junit
	cn.xym.utils	
	
	