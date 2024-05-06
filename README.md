# 睿思云数据填报系统
睿思云数据填报系统系统，基于Springboot + VUE 构建，采用mysql数据库，直接运行com.ruisitech.bi.RsbiDatawriteApplication启动系统。<br/>
系统前端代码在 web-vue 目录中, 进入目录，执行 npm install 安装系统，执行npm run dev 启动前端。 <br/>

## 主要功能模块：
<img src="https://www.ruisitech.com/img/introduce.png" />
1.制作表单  <br>
2.构建表 <br>
3.数据填报 <br>
4.数据审核  <br>
5.数据建模  <br>
6.统计分析  <br>
7.手机端填报 <br>

## 产品数据库配置：
数据库配置在 application.yml 文件
```yaml
datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dbType: mysql #定义支撑库类型
    druid:
      master:  # 支持库
        url: jdbc:mysql://localhost:3306/rsbi_datawrite?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8
        username: root
        password: 12345678
        driver-class-name: com.mysql.cj.jdbc.Driver
        initialSize: 5
        minIdle: 10
        maxActive: 50
        maxWait: 60000
      tables:  #填报表存放库
        url: jdbc:mysql://localhost:3306/rsbi?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8
        username: root
        password: 12345678
        driver-class-name: com.mysql.cj.jdbc.Driver
        initialSize: 5
        minIdle: 10
        maxActive: 50
        maxWait: 60000
```

mysql的支撑库rsbi_datawrite备份文件在datas目录下，在mysql创建 rsbi_datawrite数据库后，把数据库文件还原到rsbi_datawrite数据库中。同时创建一个空的库，库名为：rsbi，此库主要用来存放用户创建的填报表

## 产品截图：<br/>
构建表单<br/>
![olap](https://www.ruisitech.com/img/dw1.jpg?v4)  <br/>
数据填报<br/>
![1](https://www.ruisitech.com/img/dw2.jpg?v5)  <br/>
数据建模<br/>
![2](https://www.ruisitech.com/img/dw3.jpg?v3)  <br/>
统计分析<br/>
![2](https://www.ruisitech.com/img/dw4.jpg?v3)  <br/>
