package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import com.abdevs.project.ttcalerts.R;
import com.abdevs.project.ttcalerts.SubwayAlertsFragment;

public class AppConstants {

    public static int RSS_FEED = 1;
    public static int TWTTR_FEED = 2;
    public static final int CREATE_SETTGS = 3;
    public static final int UPDATE_SETTGS = 4;
    public static final int PARSE_TTC_PGE = 5;
    public static final int PARSE_TWITTER = 6;

    public static String EAST_PREFIX = "E";
    public static String WEST_PREFIX = "W";
    public static String WEST_CAMELCASE = "West";
    public static String EAST_CAMELCASE = "East";
    public static String ACCESSIB_WORD = "Accessible";
    public static String SERVICE_ALRT = "Service alert";
    public static String ACCESSBLE_ALRT = "Accessible alert";
    public static String ST_CLAIR_WEST_WORD_TBR = "Clair West";
    public static String ST_CLAIR_WEST_WORD_BR = "ClairWest";
    public static String NON_SUBWAY_ALERT = "Non-subway alert";

    public static String CHUNKFIVE_FONT = "fonts/Chunkfive.otf";
    public static String QUICKSAND_FONT = "fonts/Quicksand-Bold.otf";
    public static String ALLER_FONT = "fonts/Aller_Rg.ttf";
    public static String LORA_REG_FONT = "fonts/Lora-Regular.ttf";
    public static String OXYGN_BLD_FONT = "fonts/Oxygen-Bold.ttf";
    public static String OXYGN_REG_FONT = "fonts/Oxygen-Regular.ttf";
    public static String NUTON_REG_FONT = "fonts/Neuton-Regular.ttf";
    public static String ALLERTA_REG_FONT = "fonts/Allerta-Regular.ttf";
    public static String ANTON_REG_FONT = "fonts/Anton-Regular.ttf";
    public static String BREESERIF_REG_FONT = "fonts/BreeSerif-Regular.ttf";
    public static String CRETE_REG_FONT = "fonts/CreteRound-Regular.ttf";
    public static String LOGO_REG_FONT = "fonts/BalooBhaijaan-Regular.ttf";

    public static String IS_RED_OR_GREEN = "IsRedOrGreen";
    public static String RSN = "Reason";
    public static String MSG = "Message";
    public static String LINE_WRD = "Line";
    public static String ALERT_LIST_DATEFRMAT = "EEE MMM dd, YYYY";
    public static String ALERT_LIST_TMEFRMT = "hh:mm aa";
    public static String HTML_PRSR_DATETIMEFRMT = "hh:mm aa - dd MMM yyyy";

    public static String[] MONTHS = {"January", "February", "March", "April", "May",
                                     "June", "July", "August", "September", "October",
                                     "November", "December"};

    public static String TTC_CLOSURE_LINK = "https://www.ttc.ca/Service_Advisories/" +
            "Subway_closures/index.jsp";
    public static String TTC_TWITTER = "https://twitter.com/TTCnotices";
    public static String TTC_ALERT_FEED_URL = "https://twitrss.me/" +
            "twitter_user_to_rss/?user=TTCNotices";

    public static String RSS_FEED_COMMENTS = "Instant alerts not available, showing rss feed results." +
                                        " Note: Rss feed alerts might not "+
                                        "show recent service alerts";
    public static String ERROR_TXT = "Device is offline or an error occurred, please try later.";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void createUpcomingDialog(Activity act){

        if(act != null) {

            Typeface upcomingPopUpContentFnt = Typeface.createFromAsset
                    (act.getAssets(),
                            AppConstants.BREESERIF_REG_FONT);

            Typeface upcomingPopUpHeadFnt = Typeface.createFromAsset
                    (act.getAssets(),
                            AppConstants.ANTON_REG_FONT);

            // Display the selected item text on TextView
            Dialog popUpDialog = new Dialog(act);
            popUpDialog.setContentView(R.layout.upcoming_pop);

            TextView upcomingPopUpText = popUpDialog.findViewById(R.id.upcomingPopUpDesc);
            TextView upcomingPopUpHead = popUpDialog.findViewById(R.id.upcomingPopUpHead);

            upcomingPopUpText.setTypeface(upcomingPopUpContentFnt);
            upcomingPopUpHead.setTypeface(upcomingPopUpHeadFnt);

            popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popUpDialog.show();
        }
    }
}
