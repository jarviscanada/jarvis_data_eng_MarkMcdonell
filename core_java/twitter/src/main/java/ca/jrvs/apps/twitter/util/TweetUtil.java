package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

public class TweetUtil {
  public static Tweet createTweet(String text,
      double longitude,
      double latitude) {

    final Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    Coordinates geoCoordinates = new Coordinates();
    Tweet tweet = new Tweet();

    try {
      geoCoordinates.setCoordinates(Arrays.asList(longitude, latitude));
      geoCoordinates.setType("Point");
      tweet.setText(text);
      tweet.setCoordinates(geoCoordinates);
    } catch (Exception e) {
      logger.info("failed to create tweet from TweetUtil");
      logger.error(e.getMessage());
    }
    return tweet;
  }
}
