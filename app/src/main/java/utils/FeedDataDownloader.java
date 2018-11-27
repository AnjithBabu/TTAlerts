package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.abdevs.project.ttcalerts.MainActivity;
import com.abdevs.project.ttcalerts.R;
import com.abdevs.project.ttcalerts.SubwayAlertsFragment;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import pojos.Feed;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class FeedDataDownloader extends AsyncTask<Void, Void, String> {

    private ArrayList<Feed> rssFeeds = null;
    private MainActivity mainAct;
    private int option = 0;
    private ProgressDialog progressDialog;
    private static final String screenName = "TTCnotices";
    private  Feed tempFeed = null;



    public FeedDataDownloader(MainActivity act, int option){
        this.mainAct = act;
        this.option = option;
        this.progressDialog = new ProgressDialog(act);
    }

    @Override
    protected String doInBackground(Void... urls) {

        try {

            if(AppConstants.isOnline(mainAct)){

                if (option == 1) {
                    loadXmlFromNetwork(AppConstants.TTC_ALERT_FEED_URL);
                } else if (option == 2) {
                    loadFromTwitterFeed();
                }

            }else {
                rssFeeds = null;
            }

        }catch (XmlPullParserException | IOException ppe){

            /* TODO  Handle the exception and show alert */
            ppe.printStackTrace();
        }
        return null;
    }

    protected void onPreExecute() {

        if(progressDialog != null) {
            progressDialog.setMessage("Getting alerts...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    FeedDataDownloader.this.cancel(true);
                }
            });
        }else{
            Log.d("TTCAlerts_Warning","Progress Dialog is null");
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        FragmentManager fgmntMngr = mainAct.getSupportFragmentManager();
        Bundle bdl = new Bundle();
        bdl.putSerializable("ttcSbwayAlertFeeds", rssFeeds);
        Fragment sbwayAlertFragment = new SubwayAlertsFragment();
        sbwayAlertFragment.setArguments(bdl);
        fgmntMngr.beginTransaction().replace(R.id.content_frame,
                    sbwayAlertFragment).commit();

        if(progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }


    // Load RSS feed from the TTC twitter page using TwitRSS.me
    // TTC alert page name : @TTCNotices

    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        RssFeedDOMParserHandler rssFeedXmlParser = new RssFeedDOMParserHandler();

        try {
            stream = downloadUrl(urlString);
            rssFeeds = rssFeedXmlParser.parse (stream);

        }catch (IOException pe){

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.connect();
        return conn.getInputStream();
    }

    private void loadFromTwitterFeed(){

        TwitterAuthenticator authObj = new TwitterAuthenticator();
        rssFeeds = new ArrayList<Feed>();
        Feed feedObj = null;

        Twitter twitter = authObj.getTwitterInstance();
        List<twitter4j.Status> statuses = null;

        try{
            statuses = twitter.getUserTimeline(screenName);
        }catch (TwitterException te){
            te.printStackTrace();
        }

        for(twitter4j.Status status: statuses){

            feedObj = new Feed();
            feedObj.setTitle(status.getText());
            feedObj.setDescription(status.getText());
            feedObj.setPubDate(status.getCreatedAt());

            rssFeeds.add(feedObj);
        }
    }
}
