package ca.jrvs.apps.twitter.dao.helper;

import java.io.IOException;
import java.net.URISyntaxException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.net.URI;
import org.apache.http.HttpResponse;

@Component
public class TwitterHttpHelper implements HttpHelper {

  final Logger logger = LoggerFactory.getLogger(TwitterHttpHelper.class);

  /**
   * Dependencies are specified as private member variables
   */
  private OAuthConsumer consumer;
  private HttpClient httpClient;

  /**
   * Construct Setup dependencies using secrets
   *
   * @param consumerKey
   * @param consumerSecret
   * @param accessToken
   * @param tokenSecret
   */
  public TwitterHttpHelper(String consumerKey,
      String consumerSecret,
      String accessToken,
      String tokenSecret) {
    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken, tokenSecret);
    httpClient = new DefaultHttpClient();
  }

  @Override
  public HttpResponse httpPost(URI uri) {
    try {
      HttpPost request = new HttpPost(uri);
      consumer.sign(request);
      return httpClient.execute(request);
    } catch (IOException |
        OAuthExpectationFailedException |
        OAuthMessageSignerException |
        OAuthCommunicationException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Failed to execute", e);
    }
  }

  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      HttpGet request = new HttpGet(uri);
      consumer.sign(request);
      return httpClient.execute(request);
    } catch (IOException |
        OAuthExpectationFailedException |
        OAuthMessageSignerException |
        OAuthCommunicationException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Failed to execute", e);
    }
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    //String consumerKey = System.getenv("consumerKey");
    //String consumerSecret = System.getenv("consumerSecret");
    //String accessToken = System.getenv("accessToken");
    //String tokenSecret = System.getenv("tokenSecret");
    String consumerKey="33DyMExRsQHPSEocIlbm34VrJ";
    String consumerSecret="55LzOlBu27aUeGtlsOwbGuTHCfm4VNKRhMwR8MJc4eKCmXWYVx";
    String accessToken="1445405322873614338-BwJL53GIt6XUnTae2uyl6DeyEHricm";
    String tokenSecret="h5UgbPXMYMysXPm0sF7iyJmIAkWrQ9g3oh4h7AasYjMoR";
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    HttpResponse response = httpHelper.httpPost(new URI (
        "https://api.twitter.com//1.1/statuses/update.json?status=first_tweet2"));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}

