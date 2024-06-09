# https://ossrs.net/lts/zh-cn/docs/v5/doc/webrtc#sfu-one-to-one
FROM registry.cn-hangzhou.aliyuncs.com/ossrs/srs:5
# 作者
MAINTAINER lizhao
# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# 重要
# https://ossrs.net/lts/zh-cn/docs/v5/doc/webrtc#config-candidate
ENV CANDIDATE=106.52.188.209

# 注意替换为自己的服务器证书
COPY ./server-cert.pem conf/server.crt
COPY ./server-privkey.pem conf/server.key

ENTRYPOINT ["./objs/srs", "-c conf/https.docker.conf"]

EXPOSE 1935
EXPOSE 1985
EXPOSE 1990
EXPOSE 5060
EXPOSE 8080
EXPOSE 8088
EXPOSE 8000/udp
EXPOSE 10080/udp
