# es-monitor

对elasticsearch与对用数据库中数据的监控，判断数据库中是否存在没有建立索引的数据，有的话打印出来id

##运行环境
+ elasticearch服务运行
+ postgresql数据库

##编译运行
<p>mvn assembly:assembly</p>
<p>java -jar -Dconfig=&lt;config.properties路径&gt;   es-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar</p>

##配置文件说明

elasticsearch.server=127.0.0.1
elasticsearch.port=9300
elasticsearch.cluster.name=www.ncss.cn
elasticsearch.index=ncss-job
elasticsearch.type=job

pg.datasource=jdbc:postgresql://localhost:5432/ncss?useUnicode=true&characterEncoding=UTF-8
pg.username=postgres
pg.password=root
pg.table=job_ads
pg.table.key=job_id
pg.datasource.pools=10

heart.rate=1
