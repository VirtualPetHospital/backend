# 基础镜像
FROM openjdk:8-jdk-alpine

# 指定工作目录
WORKDIR /app
# 将jar包添加到工作目录
COPY target/file-service-1.0-SNAPSHOT.jar .
# 暴露接口
EXPOSE 8085
# 启动命令
ENTRYPOINT ["java","-jar","/app/file-service-1.0-SNAPSHOT.jar", "--spring.profiles.active=prod"]

