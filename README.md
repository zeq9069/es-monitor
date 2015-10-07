# es-monitor
<hr>
对elasticsearch与对用数据库中数据的监控，判断数据库中是否存在没有建立索引的数据，有的话打印出来id

##编译运行
	 
<p>mvn assembly:assembly</p>
<p>java -jar -Dconfig=&lt;config.properties路径&gt;   es-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar</p>
