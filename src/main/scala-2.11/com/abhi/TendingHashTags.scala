package com.abhi

import java.util.Properties

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.Duration

import scala.io.Source

object TrendingHashTags {
  def main(args: Array[String]) : Unit = {
    val url = getClass.getResource("/twitterapi.properties")
    val source = Source.fromURL(url)
    val props = new Properties()
    props.load(source.bufferedReader())
    System.setProperty("twitter4j.oauth.consumerKey", props.get("consumer_key").toString)
    System.setProperty("twitter4j.oauth.consumerSecret", props.get("consumer_secret").toString)
    System.setProperty("twitter4j.oauth.accessToken", props.get("access_token").toString)
    System.setProperty("twitter4j.oauth.accessTokenSecret", props.get("access_token_secret").toString)

    val conf = new SparkConf().setAppName("Abhishek Spark Streaming")
    val ssc = new StreamingContext(conf, Seconds(2))
    ssc.checkpoint("checkpoint")
    val tweets = TwitterUtils.createStream(ssc, None)
    val tweetText = tweets.map(t => t.getText)
    val counts = tweetText.flatMap(_.split("\\s+")).map((_, 1)).reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(10), Seconds(10))
    counts.foreachRDD{rdd =>
      val c = rdd.count()
      println("Number of words " + c)
    }
//    val hashTags = words.filter(w => w.startsWith("#")).map(h => (h, 1))
//    val tagsWithCounts = hashTags.updateStateByKey{
//      (counts : Seq[Int], prevCount : Option[Int]) => prevCount.map{c => c + counts.sum}.orElse{Some(counts.sum)}
//    }
//    val topHashTags = tagsWithCounts.filter{
//      case (t, c) => c > 10
//    }
//    val sortedTopHashTags = topHashTags.transform{
//      rdd => rdd.sortBy({
//        case (w, c) => c
//      }, false)
//    }
//    sortedTopHashTags.print(10)
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(true)
  }
}