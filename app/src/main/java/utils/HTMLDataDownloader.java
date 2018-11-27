package utils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.abdevs.project.ttcalerts.MainActivity;
import com.abdevs.project.ttcalerts.R;
import com.abdevs.project.ttcalerts.SubwayAlertsFragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import pojos.Feed;

public class HTMLDataDownloader extends AsyncTask<Void, Void, String> {

    private MainActivity mainAct;
    private ProgressDialog progressDialog;
    private HTMLParserHandler htmlPrsr;
    private ArrayList<String> closureList;
    private ArrayList<Feed> twitterAlrtList;
    private ArrayList<String> errorList;
    private int option = 0;
    private boolean isRssFeed = false;

    public HTMLDataDownloader(MainActivity act, int opt){
        this.mainAct = act;
        this.progressDialog = new ProgressDialog(act);
        this.option = opt;
        this.closureList = null;
        this.errorList = null;
        this.twitterAlrtList = null;
    }

    @Override
    protected String doInBackground(Void... urls) {
        Document htmlDoc = null;
        Connection connection = null;

        // check if internet is available and if not don't proceed
        if(!AppConstants.isOnline(mainAct)){
            this.errorList = new ArrayList<String>();
            this.errorList.add(AppConstants.ERROR_TXT);
            this.closureList = null;
            this.twitterAlrtList = null;
            return null;
        }

        try {
            switch (option) {
                case AppConstants.PARSE_TTC_PGE:
                    connection = Jsoup.connect(AppConstants.TTC_CLOSURE_LINK);
                    connection.timeout(5000);
                    htmlDoc = connection.get();
                    htmlPrsr = new HTMLParserHandler(htmlDoc);
                    closureList = htmlPrsr.extractDetails();
                    break;
                case AppConstants.PARSE_TWITTER:
                    connection = Jsoup.connect(AppConstants.TTC_TWITTER);
                    connection.timeout(5000);
                    htmlDoc = connection.get();
                    htmlPrsr = new HTMLParserHandler(htmlDoc);
                    twitterAlrtList = htmlPrsr.parseTwitterPage();
                    break;
                default:
                    connection = Jsoup.connect(AppConstants.TTC_TWITTER);
                    connection.timeout(5000);
                    htmlDoc = connection.get();
                    htmlPrsr = new HTMLParserHandler(htmlDoc);
                    twitterAlrtList = htmlPrsr.parseTwitterPage();
                    break;
            }

        }catch (IOException ioe){
            errorList = new ArrayList<String>();
            errorList.add( ((twitterAlrtList == null) ? "Alerts" : "Closure") +
                            " list not available, please check if internet is available" +
                            " or try again later");
            Log.d("TTAAlerts_Error",ioe.getMessage());
            tryWithRssFeed();
        }catch (Exception e){
            e.printStackTrace();
            errorList = new ArrayList<String>();
            errorList.add( ((twitterAlrtList == null) ? "Alerts" : "Closure") +
                            " list not available, please check if internet is available" +
                            " or try again later");
            Log.d("TTAAlerts_Error",e.getMessage());
            tryWithRssFeed();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog != null) {
            switch (option) {
                case AppConstants.PARSE_TTC_PGE:
                    progressDialog.setMessage("Getting closure details...");
                    break;
                case AppConstants.PARSE_TWITTER:
                    progressDialog.setMessage("Getting alerts...");
                    break;
                default:
                    progressDialog.setMessage("Getting alerts...");
                    break;
            }
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    HTMLDataDownloader.this.cancel(true);
                }
            });
        }else{
            Log.d("TTAAlerts_Warning","Progress Dialog is null");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        switch (option) {
            case AppConstants.PARSE_TTC_PGE:
                showClosure();
                break;
            case AppConstants.PARSE_TWITTER:
                startSubwayAlertsFragment();
                break;
            default:
                startSubwayAlertsFragment();
                break;
        }

        if(progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    private void showClosure(){
        if(closureList != null || errorList != null ){
            mainAct.displayClosureDetails( (closureList == null
                    || closureList.isEmpty()) ? errorList : closureList);
        }
    }

    private void startSubwayAlertsFragment(){
        FragmentManager fgmntMngr = mainAct.getSupportFragmentManager();
        Bundle bdl = new Bundle();
        bdl.putSerializable("ttcSbwayAlertFeeds", twitterAlrtList);
        bdl.putBoolean("isRssFeed", isRssFeed);
        Fragment sbwayAlertFragment = new SubwayAlertsFragment();
        sbwayAlertFragment.setArguments(bdl);
        fgmntMngr.beginTransaction().replace(R.id.content_frame,
                sbwayAlertFragment).commit();
        isRssFeed = false; // reset flag once fragment starts
    }

    private void tryWithRssFeed() {
        if(twitterAlrtList == null || twitterAlrtList.isEmpty()){
            try {
                twitterAlrtList = loadXmlFromNetwork(AppConstants.TTC_ALERT_FEED_URL);
                isRssFeed = true;
            }catch (Exception e){
                Log.d("TTAAlerts_Error",e.getMessage());
            }
        }
    }

    // Load RSS feed from the TTC twitter page using TwitRSS.me
    // TTC alert page name : @TTCNotices

    private ArrayList<Feed> loadXmlFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        // Instantiate the parser
        RssFeedDOMParserHandler rssFeedXmlParser = new RssFeedDOMParserHandler();

        try {
            stream = downloadUrl(urlString);
            twitterAlrtList = rssFeedXmlParser.parse (stream);

        }catch (IOException pe){

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return twitterAlrtList;
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.connect();
        return conn.getInputStream();
    }
}
