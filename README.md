# 超市订单管理系统 - SSM

前端地址：https://github.com/MishonChen/demo-vue.git

本项目技术栈: SSM（Spring、SpringMVC、Mybatis)

Java版本：JDK 8+（推荐17）

数据库：Mysql 

### 项目使用
1.  克隆项目
    git clone https://github.com/MishonChen/soms-ssm.git
2. 配置Tomcat(推荐9+)
     在idea的运行按钮中配置本地的Tomcat 
3. 修改数据库配置文件
    修改src/main/resources/db.properties文件
    jdbc.driver=com.mysql.cj.jdbc.Driver  jdbc.url=jdbc:mysql://localhost:3306/数据库名?useUnicode=true&characterEncoding=utf8  jdbc.username=root  #修改为你的用户名 jdbc.password=123456 #修改为你的密码 
