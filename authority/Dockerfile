# 基础镜像，openjkd使用17版本
FROM openjdk:17
# 作者
MAINTAINER lizhao
# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# jar 包运行路径
#WORKDIR /app/authority
# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
#VOLUME /logs/authority
# 应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar
ADD target/authority-0.0.1-SNAPSHOT-spring-boot.jar ./authority.jar
# 启动容器时的进程
#ENTRYPOINT ["java","-jar","/authority.jar"]
ENTRYPOINT ["java","-Xms128m","-Xmx512m","-jar","/authority.jar"]
# 远程调试
#ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5105","-jar","/authority.jar"]
#ENTRYPOINT ["java","-jar","/authority.jar", "--spring.profiles.active=prod"]
# 暴露8082端口
EXPOSE 8082
# 远程调试端口
EXPOSE 5105