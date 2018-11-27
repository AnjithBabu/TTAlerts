package utils;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuthenticator {

    //Your Twitter App's Consumer Key
    private static final String consumerKey = "acYtZynqviZft91SunuzFEfNp";

    //Your Twitter App's Consumer Secret
    private static final String consumerSecret = "y89a1dDATsKXVspBxdOQ5m9uR0Q0UZzIGuIPg0GLCQzubiGCGm";

    //Your Twitter Access Token
    private static final String accessToken = "1006936629209063429-sdxhzbKGUUmSu9z1ORbYj97EGosFBv";

    //Your Twitter Access Token Secret
    private static final String accessTokenSecret = "VGZpAWjutlbW6TTVje8zNfWjaWGfMVWbcAdqgIuhG76Gd";

    public static Twitter getTwitterInstance(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        return twitter;
    }
}
