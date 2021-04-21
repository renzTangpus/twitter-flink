This project's purpose is to analyze which English words or topics are commonly used on Tweets about Covid by getting data on Twitter using Twitter API and Apache Flink then output to file.

The code works but has a lot to improve i.e Add Kafka Integration. Will modify when not busy.

TO RUN:

Create Twitter Developer Account

Modify Tokens on TwitterData.java

Modify output file path on TwitterData.java

Right click project -> Run as -> Maven Clean(To delete/clean previous jar file)

Right click project -> Run as -> Maven install -> copy the jar file path

Go to Terminal then Go to Directory of your installed Flink then run: ./flink run -> Job ID will be displayed.

Go to browser -> Type http://localhost:8081/#/overview (To check if the job is already running. Can use for debugging as well.

Go to /Kafka_Output/TweeterOut to see the output file containing analyzed Twitter data(word count)

Note: The output file is updating while the Flink Job is running. You might want to cancel the Flink Job to save Disk Utilization.

If you have concerns about this project, feel free to email me @

Gmail: renztangpus@gmail.com.

LinkedIn: www.linkedin.com/in/renz-marty-tangpus-247272167

Thanks.
