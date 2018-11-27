package utils;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuthenticator {

    //Your Twitter App's Consumer Key
    private static final String consumerKey = "acYtZynqviZsdfsdfsdft9nuzFEfNp";

    //Your Twitter App's Consumer Secret
    private static final String consumerSecret = "y89a1dDATsfsdfsdfs5m9uR0Q0UZzIGuIPg0GLCQzubiGCGm";

    //Your Twitter Access Token
    private static final String accessToken = "10069366292asdas";

    //Your Twitter Access Token Secret
    private static final String accessTokenSecret = "VGZpAsfsfdsfssWjutlbW6TTVjcAdqgIuhG76Gd";

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
