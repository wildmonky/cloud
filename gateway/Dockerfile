# 注意将配置文件中的localhost改为宿主机ip，否则会指向容器
# 基础镜像，openjkd使用17版本
FROM openjdk:17
# 作者
MAINTAINER lizhao
# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# 添加 host 内部使用host.docker.internal连接docker宿主机
#RUN echo 'host.docker.internal host-gateway' >> /etc/hosts
# jar 包运行路径
#WORKDIR /app/gateway
# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹, docker自定义挂载位置
#VOLUME /logs/gateway
# 应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar
COPY target/gateway-0.0.1-SNAPSHOT-spring-boot.jar ./gateway.jar
# 启动容器时的进程
ENTRYPOINT ["java","-jar","/gateway.jar"]
# 远程调试
#ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5105","/gateway.jar"]
#ENTRYPOINT ["java","-jar","/gateway.jar", "--spring.profiles.active=prod"]
# 暴露容器的8083端口， 但未绑定至宿主机端口， 需在配置文件（Configurations）中配置
EXPOSE 8081
# 远程调试端口
EXPOSE 5105
