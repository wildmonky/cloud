# https://ossrs.net/lts/zh-cn/docs/v5/doc/webrtc#sfu-one-to-one
# nginx代理
FROM registry.cn-hangzhou.aliyuncs.com/ossrs/httpx:1
# 作者
MAINTAINER lizhao
# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

ARG CANDIDATE=106.52.188.209

# 注意替换为自己的服务器证书
COPY ../server-cert.pem ./etc/server.crt
COPY ../server-privkey.pem ./etc/server.key

ENTRYPOINT ["./bin/httpx-static", "-http 80", "-https 443", "-ssk ./etc/server.key", "-ssc ./etc/server.crt", "-proxy http://$CANDIDATE:1989/sig", "-proxy http://$CANDIDATE:1985/rtc", "-proxy http://$CANDIDATE:8080"]

EXPOSE 80
EXPOSE 443