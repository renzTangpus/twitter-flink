package p1;


import java.util.Properties;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;

import org.apache.flink.util.Collector;

public class TwitterData 
{

    public static void main(String[] args) throws Exception
    {
	final StreamExecutionEnvironment env =  StreamExecutionEnvironment.getExecutionEnvironment();
	
	Properties twitterCredentials = new Properties();
	twitterCredentials.setProperty(TwitterSource.CONSUMER_KEY, "<consumer key>"); // Modify this.
	twitterCredentials.setProperty(TwitterSource.CONSUMER_SECRET, "consumer secret"); // Modify this.
	twitterCredentials.setProperty(TwitterSource.TOKEN, "<token>"); // Modify this.
	twitterCredentials.setProperty(TwitterSource.TOKEN_SECRET, "token secret"); // Modify this.

	DataStream<String> twitterData = env.addSource(new TwitterSource(twitterCredentials));

	twitterData.flatMap(new TweetParser())
	.keyBy(0)
	.sum(1)
	.writeAsText("/home/renz/Kafka_Output/TweeterOut"); //Modify this.
	
	env.execute("Twitter Trend About Covid");
	
    }

    public static class TweetParser	implements FlatMapFunction<String, Tuple2<String, Integer>>
    {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception
	{
		ObjectMapper jsonParser = new ObjectMapper();
	    JsonNode node = jsonParser.readValue(value, JsonNode.class);

//	    boolean createdAt = node.has("created_at");
	    
	    // Users that talk about Covid
	    boolean hasText = 
			    node.has("text") && 
			    (node.get("text").asText().contains("Covid") ||
			    node.get("text").asText().contains("covid"));
	    
/*	    boolean hasLoc =
	    		node.has("user") &&
	    		node.get("user").has("location") &&
	    		node.get("user").has("lang") &&
	    		node.get("user").has("name") &&
    					(node.get("user").get("location").asText().contains("France") ||
	    				node.get("user").get("location").asText().contains("England") ||
	    				node.get("user").get("location").asText().contains("España") ||
	    				node.get("user").get("location").asText().contains("Manila") ||
	    				node.get("user").get("location").asText().contains("Philippines") ||
	    				node.get("user").get("location").asText().contains("United States") ||
	    				node.get("user").get("location").asText().contains("Russia") ||
	    				node.get("user").get("location").asText().contains("India") ||
	    				node.get("user").get("location").asText().contains("Italy") ||
	    				node.get("user").get("location").asText().contains("Turkey") ||
	    				node.get("user").get("location").asText().contains("Spain"));
	    */
	    boolean isEnglish =
	    		node.has("lang") &&
	    		node.get("lang").asText().equals("en");
	    
	    String[] excl = {"shit", "fuck", "fucking"," a "," an "," The "," rt ","RT"," for "," at "," that "," is "," this "," with "," we "," We ",
	    		" didnt "," know "," gave "," that "," That "," been "," there "," those "," from "," by "," to "," has "," have "," so "," her "," his ",
	    		" Hey "," its "," Im "," of "," the "," all "," A "," An "," for "," it "," It "," My "," my "," they "," and "," I "," you "," me ",
	    		" some "," in "," on "," was "," were "," did "," do "," done "," dont "," put "," are "," take "," but "," then "," get ","I"," Most ",
	    		" because "," being "," ask "," about "," ever "," says "," will "," doing "," our "," us "," isnt "," way "," as "," As "," or "," got ",
	    		" who "," when "," you "," You "," Your "," your "," yours "," Oh ", " going "};
	    
	    if(hasText) {
	    	if(isEnglish){ //if(hasLoc && isEnglish) {
	    		
	    		String tweet = node.get("text").asText().replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
	    		for(int i = 0; i < excl.length; ++i) {
	    			tweet = tweet.replaceAll(excl[i], "");
	    		}
	    		
	    		
	    		String[] tweetWords = tweet.split(" ");
	    			for (String word : tweetWords) {
					out.collect(new Tuple2<String, Integer>(word,1));
	    			
				}
	    	}
	    	
	    }
	}    }} 