# 基础镜像，openjkd使用17版本
FROM openjdk:17
# 作者
MAINTAINER lizhao
# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
#VOLUME /logs/user
# 应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar
ADD target/user-0.0.1-SNAPSHOT-spring-boot.jar ./user.jar
# 启动容器时的进程
ENTRYPOINT ["java","-Xms128m", "-Xmx512m","-jar","/user.jar"]
#ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5105","/user.jar"]
#ENTRYPOINT ["java","-jar","/user.jar", "--spring.profiles.active=prod"]
# 暴露8084端口
EXPOSE 8083
# 远程调试端口
EXPOSE 5105
