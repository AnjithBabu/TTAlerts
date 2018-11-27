package utils;


import com.abdevs.project.ttcalerts.MainActivity;

public class AsyncRouter {

    private  static int USE_RSS_FEED = 1;
    private static int USE_TWITTER_STATUS = 2;

    // start rss downloading
    public static void connectToDownloadRssFeederTask( Object activity){
       new FeedDataDownloader((MainActivity) activity, USE_RSS_FEED).execute();
    }

    // start twitter authentication
    public static void connectToDownloadTwitterFeed( Object activity){
        new FeedDataDownloader((MainActivity) activity, USE_TWITTER_STATUS).execute();
    }

    // start html page downloading
    public static void connectToDownloadClosure( Object activity, int choice){
        new HTMLDataDownloader((MainActivity) activity, choice).execute();
    }

    // start html page downloading
    public static void connectToDownloadHTMLFeeds( Object activity, int choice){
        new HTMLDataDownloader((MainActivity) activity, choice).execute();
    }

    //
    public static void readAndWriteToAppStorage( Object activity, int option){
        new AppStorageHandler((MainActivity) activity, option).execute();
    }
}
