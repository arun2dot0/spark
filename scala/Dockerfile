FROM apache/spark:latest
USER spark
COPY --chmod=0755 target/scala-2.12/HelloWorldApp-assembly-0.1.jar /app/helloworld.jar
#RUN chown -R spark:spark /opt/spark/work-dir
# Switch to the new user
