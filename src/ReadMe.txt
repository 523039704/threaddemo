�������⣺
�������ݿ�Connection��Դ�ǱȽϱ���ģ����������ÿ�������ʵ�Connection��Դ����
����,�ͷŵĻ��������Ʊػ�Ӱ��������վ�ķ������ܣ�����Ҫ��һ���Ż����������ݿ����ӳ�

���ӳ�ʵ��ԭ��
1.�û���servlet������������DaoҪConnection
2.Dao�ӡ����ӳء���ȡ��Connection��Դ����DB��ͨѶ
3.���û��뿪֮���ͷŸ�Connection,��ô��Connection���ͷŵ����ӳ��У��ȴ���һ���û���

DemoĿ��:
ͨ���򵥵���ɾ�Ĳ����������漸���������ӳصķ�ʽ�������Ǹ��˽⼸���Ż��ķ�ʽ
1.�Զ���һ��Pool,��ʵ�����������ڿ�Դ���ӳ�Ϊ��������һЩ����
2.ʹ��Tomcat���õ����ӳأ�apache dbcp��
3.ʹ��DBCP���ݿ����ӳ�
4.ʹ��C3P0���ݿ����ӳأ��Ƽ���

���裺
1.�����������
	jar����
	mysql��������
		mysql-connector-java-5.0.8-bin.jar
	DBCP���ݿ����ӳ�Ҫ�õ�jar��
		commons-collections-3.1.jar
		commons-dbcp-1.2.2.jar
		commons-pool.jar
	C3P0���ݿ����ӳ�Ҫ�õ�jar��
		mchange-commons-0.2.jar
		c3p0-0.9.2-pre1.jar
		
	�����ļ���
		�Զ���Pool: db.properties
		DBCP: dbcpconfig.properties
		C3P0: c3p0-config.xml
		
2.���ݿⴴ��:
	create database test1;
	use test1;
	
	create table user(
		id varchar(40) primary key,
		username varchar(20),
		password varchar(20)
	);
	

3.������֯��
	cn.xym.domain
	cn.xym.junit
	cn.xym.utils	
	
	