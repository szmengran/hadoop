FROM java:8-jre
MAINTAINER Joe <android_li@sina.cn>

ENV JAR_FILE hbase.jar

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone 

ADD ./target/$JAR_FILE /app/
CMD java -Xmx200m -jar /app/$JAR_FILE

EXPOSE 30001