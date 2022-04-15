传统Webshell已经无法满足攻防战场的需求，人们把目光转向了无文件shell。前段时间因项目对这块有所接触，借此闲暇之际对之前了解的内容梳理一下，写一些简单的能够落地的Demo。
Tips: 在开发过程中为了处理依赖方便，我把tools.jar安装到了本地maven仓库，具体命令如下：`mvn install:install-file -Dfile=tools.jar -DgroupId=com.sun -DartifactId=tools -Dversion=1.8 -Dpackaging=jar`;

# Log

+ 2022-4-14 添加Agent内存马实现demo，开发测试环境jdk8 + tomcat7

  
