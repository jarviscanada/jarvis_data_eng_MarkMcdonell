package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller{
  final Logger logger = LoggerFactory.getLogger(TwitterController.class);

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";
  private Service service;

  @Autowired
  public TwitterController(Service service){ this.service = service; }

  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3){
      logger.info("error in postTweet arguments received");
      throw new IllegalArgumentException("invalid number of args");
    }

    String textBody = args[1];
    String coordinates = args[2];
    String[] coordinatesArr = coordinates.split(COORD_SEP);

    if (coordinatesArr.length < 2) {
      logger.info("error in postTweet coordinates array");
      throw new IllegalArgumentException("invalid number of coordinates for tweet");
    }

    Double longitude;
    Double latitude;

    try{
      longitude = Double.parseDouble(coordinatesArr[0]);
      latitude = Double.parseDouble(coordinatesArr[1]);
    } catch (NumberFormatException e){
      logger.info("error in postTweet parse coordinates");
      logger.error(e.getMessage());
      throw new IllegalArgumentException("invalid location input / format", e);
    }
    Tweet tweet = TweetUtil.createTweet(textBody, longitude, latitude);
    return service.postTweet(tweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    if (args.length < 2) {
      logger.info("error in showTweet arguments received");
      throw new IllegalArgumentException("Invalid amount of arguments!");
    }
    String[] fields = args.length > 2 ? args[2].split(COMMA) : null;
    return service.showTweet(args[1], fields);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length < 2) {
      logger.info("error in deleteTweet arguments received");
      throw new IllegalArgumentException("invalid number of arguments!");
    }
    String[] ids = args[1].split(COMMA);
    return service.deleteTweets(ids);
  }
}
