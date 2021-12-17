package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.google.gdata.util.common.base.PercentEscaper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TwitterDao implements CrdDao<Tweet, String>{
  final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  // URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  // URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  // Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet entity) {
    // Construct URI
    URI uri;

    PercentEscaper percentEscaper = new PercentEscaper("", false);
    ArrayList<Double> globalAddress = (ArrayList<Double>) entity.getCoordinates().getCoordinates();

    String uriGlobalAddress = "status" + EQUAL + percentEscaper.escape(entity.getText())
        + AMPERSAND
        + "longitude" + EQUAL + globalAddress.get(0)
        + AMPERSAND
        + "latitude" + EQUAL + globalAddress.get(1);

    uri = URI.create(API_BASE_URI
        + POST_PATH
        + QUERY_SYM
        + uriGlobalAddress);

    // Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);

    try {
      // Validate response and deserialize response to Tweet object
      return parseResponseBody(response, HTTP_OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Invalid tweet input", e);
    }
  }

  public Tweet parseResponseBody(HttpResponse response,
      Integer expectedStatusCode) {
    Tweet tweet = null;

    // Check response status
    Integer status = response.getStatusLine().getStatusCode();
    if(status != expectedStatusCode) {
      try {
        logger.info(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        logger.info("Response has no entity");
        logger.error(e.getMessage());
      }
      throw new RuntimeException("Empty response body");
    }

    if(response.getEntity() == null) {
      throw new RuntimeException("Empty response body.");
    }

    // convert response entity to str
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Failed to convert entity to String.", e);
    }

    // deserialize from Json string to tweet object
    try {
      tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Unable to convert JSON to Object.", e);
    }
    return tweet;
  }

  @Override
  public Tweet findById(String s) {
    URI uri;

    try {
      String findID = API_BASE_URI
          + SHOW_PATH
          + QUERY_SYM
          + "id"
          + EQUAL
          + s;

      uri = new URI(findID);
    } catch (URISyntaxException ex) {
      logger.error(ex.getMessage());
      throw new IllegalArgumentException("Invalid id input", ex);
    }
    HttpResponse response = httpHelper.httpGet(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  @Override
  public Tweet deleteById(String s) {
    URI uri;

    try {
      String deleteID = API_BASE_URI
          + DELETE_PATH
          + "/"
          + s
          + ".json";

      uri = new URI(deleteID);
    } catch (URISyntaxException ex) {
      logger.error(ex.getMessage());
      throw new IllegalArgumentException("Invalid id input", ex);
    }
    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }
}
