
package com.abdevs.project.ttcalerts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pojos.Feed;
import utils.AppConstants;
import utils.AsyncRouter;


public class SubwayAlertsFragment extends Fragment{

    private ArrayList<Feed> ttcAlertsFeed = null;
    private Typeface itemDescFont = null;
    private Typeface listHeadFont = null;
    private Typeface listItemHead = null;
    private String[] stationList = {"Bathurst Accessible","Bay","Bayview Accessible",
                                    "Bessarion Accessible","Bloor-Yonge Accessible",
                                    "Broadview Accessible","Castle Frank","Chester","Christie"
                                    ,"College","Coxwell Accessible","Davisville Accessible",
                                    "Don Mills Accessible","Donlands","Downsview Park Accessible",
                                    "Dufferin Accessible","Dundas Accessible","Dundas West Accessible",
                                    "Dupont","Eglinton Accessible","Eglinton West Accessible","Ellesmere","Finch Accessible","Finch West Accessible","Glencairn","Greenwood","High Park","Highway 407 Accessible","Islington","Jane Accessible","Keele","Kennedy Accessible","King","Kipling Accessible","Lansdowne","Lawrence","Lawrence East","Lawrence West Accessible","Leslie Accessible","Main Street Accessible","McCowan","Midland","Museum","North York Centre Accessible","Old Mill","Osgoode Accessible","Ossington Accessible","Pape Accessible","Pioneer Village Accessible","Queen Accessible","Queen's Park Accessible","Rosedale","Royal York","Runnymede","Scarborough Centre Accessible","Sheppard-Yonge Accessible","Sheppard West Accessible","Sherbourne","Spadina","St Andrew Accessible","St Clair Accessible","St Clair West Accessible","St George Accessible","St Patrick","Summerhill","Union Accessible",
            "Vaughan Metropolitan Centre Accessible","Victoria Park Accessible","Warden","Wellesley","Wilson",
                                    "Woodbine Accessible","York Mills Accessible","York University Accessible","Yorkdale"};
    private String[] lineOneStationList = {"Finch","North York Centre","Sheppard–Yonge","York Mills",
                                            "Lawrence","Eglinton","Davisville","St. Clair",
                                            "Summerhill","Rosedale","Bloor–Yonge",
                                            "Wellesley","College","Dundas","Queen","King","Union",
                                            "St. Andrew","Osgoode","St. Patrick","Queen's Park",
                                            "Museum","St. George","Spadina","Dupont","St. Clair West",
                                            "Eglinton West","Glencairn","Lawrence West","Yorkdale",
                                            "Wilson","Sheppard West","Downsview Park","Finch West",
                                            "York University","Pioneer Village","Highway 407",
                                            "Vaughan Metro Centre"};
    private String[] lineTwoStationList = {"Kipling","Islington","Royal York",
                                            "Old Mill","Jane","Runnymede","High Park","Keele","Dundas West",
                                            "Lansdowne","Dufferin","Ossington","Christie","Bathurst","Bay",
                                            "Sherbourne","Castle Frank","Broadview","Chester","Pape","Donlands",
                                            "Greenwood","Coxwell","Woodbine",
                                            "Main Street","Victoria Park","Warden","Kennedy"};

    private String[] lineThreeStationList = {"Lawrence East","Ellesmere","Midland",
                                            "Scarborough Centre","McCowan"};
    private String[] listFourStationList = {"Bayview","Bessarion","Leslie","Don Mills"};

    private ArrayList<Feed> filteredList = null;
    private boolean isStationOnePresent = false;
    private boolean isStationTwoPresent = false;
    private boolean isStationThreePresent = false;
    private boolean isStationFourPresent = false;
    private String[] green = {"resumed", "back in service"};
    private String[] red = {"No service", "out of service", "collision", "delays",
                            "delay", "detour", "holding"};
    private String[] lines = {"Line 1:", "Line 2:", "Line 3:", "Line 4:"};
    private String[] accessible = {"elevator"};
    private ListView alertList = null;
    private SwipeRefreshLayout alertListSwipe;
    private FragmentActivity parentActivity;
    private Dialog popUpDialog = null;

    /**
     *  TODO: Filter Option android
     *
     *  1. Get keywords and List from JSON
     *  2. Handle Twitter Authentication
     *
     *  Future Feature
     *  1. Add filter to the application
     */

      // TODO private String[] keywords = {"Station"};


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get the alert list set using fragments getter method
        ttcAlertsFeed = (ArrayList<Feed>) getArguments().get("ttcSbwayAlertFeeds");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subway_alerts, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentActivity = this.getActivity();
        listHeadFont = Typeface.createFromAsset(SubwayAlertsFragment.this.getActivity().getAssets(),
                AppConstants.CHUNKFIVE_FONT);
        TextView alertListHead =  (TextView) this.getActivity().findViewById(R.id.listHeading);
        TextView noConn = (TextView) this.getActivity().findViewById(R.id.noConnection);
        LinearLayout errorLayout =  (LinearLayout) this.getActivity().findViewById(R.id.errorLayout);
        alertListHead.setTypeface(listHeadFont);
        alertList = (ListView) this.getActivity().findViewById(R.id.alertListView);

        //TODO filterList(ttcAlertsFeed);

        // check if feed is empty, then show error if empty

        if(ttcAlertsFeed == null || ttcAlertsFeed.isEmpty() || ttcAlertsFeed.size() <2 ) {
            alertList.setVisibility(View.GONE);
            alertListHead.setVisibility(View.GONE);
        }else{
            // validate feed and store required information to each alert
            validateAlert(ttcAlertsFeed);
            buildAlertList();
            noConn.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
            if((boolean)getArguments().get("isRssFeed")){
                Toast.makeText(getContext(),AppConstants.RSS_FEED_COMMENTS,
                        Toast.LENGTH_LONG).show();
            }
        }

        alertListSwipe = this.getActivity().findViewById(R.id.alertListSwipe);
        alertListSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncRouter.connectToDownloadHTMLFeeds(parentActivity, AppConstants.PARSE_TWITTER);
                alertListSwipe.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(popUpDialog != null){
            popUpDialog.dismiss();
        }
    }

    /**
     * Validate the alert list and updates properties of Feed objects
     *
     * @param alertFeeds
     */
    private void validateAlert(ArrayList<Feed> alertFeeds){

        String stationName = null;

        for(Feed currentFeed:alertFeeds) {

            // find a assign tagged stations
            if(currentFeed.getTitle() != null) {
                stationName = currentFeed.getTitle().replace(AppConstants.WEST_PREFIX,
                                                                AppConstants.WEST_CAMELCASE);
                stationName = stationName.replace(AppConstants.ST_CLAIR_WEST_WORD_TBR,
                                                    AppConstants.ST_CLAIR_WEST_WORD_BR);
                currentFeed.setTaggedStations(getStation
                        (stationName.replace(AppConstants.EAST_PREFIX,
                                                AppConstants.EAST_CAMELCASE)));

                currentFeed.setTaggedStations( currentFeed.getTaggedStations() == null ?
                        AppConstants.NON_SUBWAY_ALERT : currentFeed.getTaggedStations() );

                checkLine(lines, currentFeed);
                currentFeed.setAlertTypeMap(isRedOrGreen(currentFeed));
            }
        }
    }

    private void buildAlertList(){

        ArrayAdapter<Feed> alertAdptr = new AlertListAdapter();
        alertList.setAdapter(alertAdptr);

        // Set an item click listener for ListView
        alertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get the selected item text from ListView
            Feed selectedFeed = (Feed) parent.getItemAtPosition(position);

            //create
            Typeface popUpContentFnt = Typeface.createFromAsset
                    (SubwayAlertsFragment.this.getActivity().getAssets(),
                            AppConstants.QUICKSAND_FONT);

            Typeface popUpHeadFnt = Typeface.createFromAsset
                    (SubwayAlertsFragment.this.getActivity().getAssets(),
                            AppConstants.BREESERIF_REG_FONT);

            Typeface popUpFtrFnt = Typeface.createFromAsset
                    (SubwayAlertsFragment.this.getActivity().getAssets(),
                            AppConstants.CRETE_REG_FONT);

            // Display the selected item text on TextView
            popUpDialog = new Dialog(SubwayAlertsFragment.this.getActivity());
            popUpDialog.setContentView(R.layout.alert_popup);

            TextView popUpText = popUpDialog.findViewById(R.id.popUpDesc);
            TextView popUpHead = popUpDialog.findViewById(R.id.popUpHead);
            TextView popUpFtr  = popUpDialog.findViewById(R.id.popUpFoot);

            popUpText.setTypeface(popUpContentFnt);
            popUpHead.setTypeface(popUpHeadFnt);
            popUpFtr.setTypeface(popUpFtrFnt);
            popUpText.setText(selectedFeed.getTitle());
            popUpFtr.setText(selectedFeed.getTaggedStations());

            popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popUpDialog.show();
            }
        });
    }

    // TODO filter feature
    /*private void filterList(ArrayList<Feed> ttcAlertsFeed){
       int position = 0;
       filteredList = new ArrayList<Feed>();

       for (Feed currentFeed:ttcAlertsFeed){

           if (checkKeyWord(currentFeed.getTitle().trim())){
               filteredList.add(currentFeed);
           }
           position = position + 1;
       }
    }

    private boolean checkKeyWord(String text){
        for(String key:keywords){
            if(text.contains(key)){
                return true;
            }
        }
        return false;
    }*/

    /**
     *
     */
    private class AlertListAdapter extends ArrayAdapter<Feed>{

        public AlertListAdapter() {
            super(SubwayAlertsFragment.this.getActivity(), R.layout.list_item, ttcAlertsFeed);

            itemDescFont = Typeface.createFromAsset
                    (SubwayAlertsFragment.this.getActivity().getAssets(),
                            AppConstants.QUICKSAND_FONT);
            listItemHead = Typeface.createFromAsset
                    (SubwayAlertsFragment.this.getActivity().getAssets(),
                            AppConstants.CHUNKFIVE_FONT);
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent){

            String taggedStations;
            String feedTitle;
            View itemView = convertView;
            SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.ALERT_LIST_DATEFRMAT);
            SimpleDateFormat timeFormat = new SimpleDateFormat(AppConstants.ALERT_LIST_TMEFRMT);

            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.list_item,parent,false);
            }

            // initialize variables needed
            Feed currentFeed = ttcAlertsFeed.get(position);
            feedTitle = currentFeed.getTitle();
            TextView contentTxtView = (TextView)itemView.findViewById(R.id.alertContent);
            TextView listTitleTxtView = (TextView) itemView.findViewById(R.id.listTitleTxtView);
            TextView listTagsTxtView = (TextView) itemView.findViewById(R.id.tagText);
            ImageView titleImgView = (ImageView) itemView.findViewById(R.id.headImage);
            ImageView itemImgView = (ImageView) itemView.findViewById(R.id.itemImage);
            TextView listLineTxtView = (TextView) itemView.findViewById(R.id.listLineTxt);
            TextView alertDateTxtView = (TextView) itemView.findViewById(R.id.alertDate);


            listLineTxtView.setText(AppConstants.LINE_WRD);
            contentTxtView.setTypeface(itemDescFont);
            contentTxtView.setText(feedTitle.substring(0,35)+"......");

            if(currentFeed.getTaggedStations() != null) {
                listTagsTxtView.setText(currentFeed.getTaggedStations());
            }else{
                listTagsTxtView.setText("");
            }

            if(currentFeed.getTaggedStations() != null &&
                    currentFeed.getTaggedStations().
                            equalsIgnoreCase(AppConstants.NON_SUBWAY_ALERT)
                    && currentFeed.getLine() == -1){
                listLineTxtView.setText("");
                itemImgView.setBackgroundResource(R.drawable.bell);
                listTagsTxtView.setText("");
                listLineTxtView.setText(AppConstants.SERVICE_ALRT);
            }else if(currentFeed.getLine() == 1){
                itemImgView.setBackgroundResource(R.drawable.line1);
            }else if(currentFeed.getLine() == 2){
                itemImgView.setBackgroundResource(R.drawable.line2);
            }else if(currentFeed.getLine() == 3){
                itemImgView.setBackgroundResource(R.drawable.line3);
            }else if(currentFeed.getLine() == 4){
                itemImgView.setBackgroundResource(R.drawable.line4);
            }else if(currentFeed.isAccessible()){
                itemImgView.setBackgroundResource(R.drawable.accessible);
                listLineTxtView.setText(AppConstants.ACCESSBLE_ALRT);
            }else{
                listLineTxtView.setText("");
                itemImgView.setBackgroundResource(R.drawable.bell);
                listTagsTxtView.setText("");
                listLineTxtView.setText(AppConstants.SERVICE_ALRT);
            }

            Map<String,Object> alertTypeMap = currentFeed.getAlertTypeMap();

            if((boolean)alertTypeMap.get(AppConstants.IS_RED_OR_GREEN)) {
                titleImgView.setBackgroundResource(R.drawable.item_prob);
                listTitleTxtView.setText((String) alertTypeMap.get("Reason"));
                listTitleTxtView.setTextColor(Color.parseColor("#C0392B"));
            }else{
                titleImgView.setBackgroundResource(R.drawable.item_info);
                listTitleTxtView.setText((String) alertTypeMap.get("Reason"));
                listTitleTxtView.setTextColor(Color.parseColor("#145A32"));
            }

            alertDateTxtView.setText(
                    dateFormat.format(currentFeed.getPubDate())+" at " +
                            timeFormat.format(currentFeed.getPubDate()));

            listTitleTxtView.setTypeface(listItemHead);

            return itemView;
        }
    }

    /**
     *
     * @param text
     * @return
     */
    private String getStation(String text){

        String station = "";
        Pattern p = null;
        Matcher m = null;

        for(String key:stationList){

            p = Pattern.compile(".*\\b"+ ((key.replace(AppConstants.ACCESSIB_WORD,"")).trim().
                    replace(AppConstants.ST_CLAIR_WEST_WORD_TBR,
                                AppConstants.ST_CLAIR_WEST_WORD_BR)) +"\\b.*");
            m = p.matcher(text);

            if(m.find()){
                station = station+" - "+key.replace(AppConstants.ACCESSIB_WORD,
                                                        "").trim();
            }
        }
        return station.trim().equalsIgnoreCase("") ? null : station;
    }

    /**
     *
     * @param currentFeed
     * @returnAppConstants.
     */

    private Map<String,Object> isRedOrGreen(Feed currentFeed){

        Map<String, Object>  alertTypeMap = new HashMap<String, Object>();

        for(String key:red){

            if(currentFeed.getTitle().toUpperCase().contains(key.toUpperCase())){
                currentFeed.setAccessible(isAccessibleAlert(currentFeed));
                alertTypeMap.put(AppConstants.RSN,toCamelCase(key));
                alertTypeMap.put(AppConstants.IS_RED_OR_GREEN, true);
                return alertTypeMap;
            }
        }

        for(String key:green){
            if(currentFeed.getTitle().toUpperCase().contains(key.toUpperCase())){
                alertTypeMap.put(AppConstants.RSN,toCamelCase(key));
                alertTypeMap.put(AppConstants.IS_RED_OR_GREEN, false);
                return alertTypeMap;
            }
        }
        alertTypeMap.put(AppConstants.RSN,AppConstants.MSG);
        alertTypeMap.put(AppConstants.IS_RED_OR_GREEN, false);
        return alertTypeMap;
    }

    /**
     *
     * @param init
     * @return
     */
    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }

    /**
     * @param lineList
     */
    private void checkLine(String[] lineList, Feed alertFeed){

        int index = 1;

        for(String key:lineList){
            if(alertFeed.getTitle().toUpperCase().contains(key.toUpperCase())){
                alertFeed.setLine(index);
                return;
            }
            index = index + 1;
        }
    }

    private boolean isAccessibleAlert(Feed currentFeed){
        for(String acesbleKyWrd:accessible){
            if(currentFeed.getTitle().toUpperCase().contains(acesbleKyWrd.toUpperCase())){
                return true;
            }
        }
        return false;
    }

}
