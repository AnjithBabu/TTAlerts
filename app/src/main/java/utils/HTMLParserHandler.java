package utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pojos.Feed;

public class HTMLParserHandler {

    private ArrayList<String> closureList;
    private Document jsonDoc;
    private SimpleDateFormat formatPubDate = new SimpleDateFormat
                                                (AppConstants.HTML_PRSR_DATETIMEFRMT);

    public HTMLParserHandler(Document jsonDoc){
        this.jsonDoc = jsonDoc;
    }

    public ArrayList<String> extractDetails(){

        if(jsonDoc != null) {

            Element mainContentDiv = jsonDoc.select("div.main-content").first();
            Elements closureHrefs = mainContentDiv.select("a[href]");
            closureList = new ArrayList<String>();

            for (Element e : closureHrefs) {
                closureList.add(e.text());
            }

            return closureList;

        }else{
            return null;
        }
    }

    public ArrayList<Feed> parseTwitterPage() throws ParseException{

        ArrayList<Feed> twitterFeeds = null;
        Elements divContents = null;
        Elements divContentHdr = null;
        Elements divContentBdy = null;
        Element contentHdrSmallFirst = null;
        Element dateTime = null;
        String tempDate = null;


        if(jsonDoc != null) {
            twitterFeeds = new ArrayList<>();
            divContents = jsonDoc.select("div.content");

            for(Element el:divContents){
                divContentHdr = el.select("div.stream-item-header");
                divContentBdy = el.select("div.js-tweet-text-container");

                if(divContentHdr != null && divContentBdy != null){
                    Feed feedObj = new Feed();
                    contentHdrSmallFirst = divContentHdr.select("small.time").first();
                    dateTime = contentHdrSmallFirst.select("a[href]").first();
                    //tempDate = dateTime.attr("title").replaceFirst("-\\W7\\W","07");
                    feedObj.setPubDate( formatPubDate.parse(dateTime.attr("title")));
                    feedObj.setTitle(divContentBdy.first().text());
                    feedObj.setDescription(divContentBdy.first().text());
                    // twitter server is 3 hours behind, so add 3 hours to current time
                    feedObj.getPubDate().setTime( feedObj.getPubDate().getTime() + 3600 * 3000 );
                    twitterFeeds.add(feedObj);
                }
            }

            return twitterFeeds;
        }else{
            return null;
        }
    }
}
