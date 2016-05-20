## run the docker container

docker run --rm -i -t -p 8088:8088 -p 8042:8042 -p 4040:4040 -v /Users/abhishek.srivastava/MyProjects/SparkStreaming1/target/scala-2.11:/app -h sandbox sequenceiq/spark:1.6.0 bash

## run the job

spark-submit --class com.abhi.TrendingHashTags /app/SparkStreaming1-assembly-1.0.jar > ~/output.txt 2> ~/debug.txt
