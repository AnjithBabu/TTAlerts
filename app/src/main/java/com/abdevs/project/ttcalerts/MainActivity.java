package com.abdevs.project.ttcalerts;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Set;

import pojos.Settings;
import utils.AppConstants;
import utils.AsyncRouter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int rssFeedOrTwitter = 0;
    private Dialog popUpDialog = null;
    final String appPackageName = getPackageName();
    // getPackageName() from Context or Activity object


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {

            /*FragmentManager fgmntMngr = this.getSupportFragmentManager();
            Fragment loginFragment = new LoginFragment();
            fgmntMngr.beginTransaction().replace(R.id.content_frame,
                    loginFragment).commit();*/

            AsyncRouter.connectToDownloadHTMLFeeds(this,AppConstants.PARSE_TWITTER);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(popUpDialog != null){
            popUpDialog.dismiss();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ttc_alerts) {

            // connector class that call TwittRss parsing class (RSSDataDownloader)
            // once rss is parsed it then calls the fragment to show results.

           // AsyncRouter.connectToDownloadRssFeederTask(this);

           // AsyncRouter.connectToDownloadTwitterFeed(this);

            AsyncRouter.connectToDownloadHTMLFeeds(this,AppConstants.PARSE_TWITTER);

        } else if (id == R.id.nav_go_alerts) {

            AppConstants.createUpcomingDialog(this);

        } else if (id == R.id.nav_settings) {

            AppConstants.createUpcomingDialog(this);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //TODO:
    public void onRssFeedBtnClick(View view){
        rssFeedOrTwitter = AppConstants.RSS_FEED;
        //AsyncRouter.readAndWriteToAppStorage(this, AppConstants.CREATE_SETTGS);
    }
    //TODO:
    public void onTwttrFeedBtnClick(View view){
        rssFeedOrTwitter = AppConstants.TWTTR_FEED;
        //AsyncRouter.readAndWriteToAppStorage(this);
    }

    public void onClosureBtnClick(View view){

        AsyncRouter.connectToDownloadClosure(this, AppConstants.PARSE_TTC_PGE);
    }

    public void onFilterBtnClick(View view) {

        AppConstants.createUpcomingDialog(this);
    }

    public void onPlayStoreImageClick(View view){
        try {
            /*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                    appPackageName)));*/
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/" +
                    "store/apps/details?id=" + "com.facebook.katana")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/" +
                    "store/apps/details?id=" + "com.facebook.katana")));
        }
    }

    public int getOption(){
        return rssFeedOrTwitter;
    }

    public void displayClosureDetails(ArrayList<String> closureList){

        // Display the selected item text on TextView
        popUpDialog = new Dialog(this);
        popUpDialog.setContentView(R.layout.closure_popup);
        TextView closureContent = popUpDialog.findViewById(R.id.closureContent);
        TextView closureHead = popUpDialog.findViewById(R.id.closureHead);
        String innerContent = "";
        String previousMonth = "";

        closureHead.setTypeface(Typeface.createFromAsset(this.getAssets(),
                AppConstants.OXYGN_BLD_FONT));
        closureContent.setTypeface(Typeface.createFromAsset(this.getAssets(),
                AppConstants.OXYGN_REG_FONT));

        for(String detail:closureList){

            if(getMonth(detail, previousMonth) != null){

                previousMonth = getMonth(detail, previousMonth);
                innerContent = innerContent + "\n---------------------------------------\n"
                                                  +"  "+ previousMonth +
                                              "\n---------------------------------------\n";
            }

            innerContent = innerContent + "\n" +detail +"\n";
        }

        closureContent.setText(innerContent);

        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    private String getMonth(String txt, String previousMonth){

        for(String monthName: AppConstants.MONTHS){
            if(txt.toUpperCase().contains(monthName.toUpperCase())
                    && previousMonth != monthName){
                return monthName;
            }
        }
        return null;
    }
}
