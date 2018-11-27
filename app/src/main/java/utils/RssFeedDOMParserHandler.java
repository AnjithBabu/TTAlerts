package utils;

import android.util.Xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import pojos.Feed;

public class RssFeedDOMParserHandler {

    private Feed feed;
    private String feedText;
    private static final String ns = null;
    private ArrayList<Feed> rssFeeds;
    private SimpleDateFormat formatPubDate = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");

    public RssFeedDOMParserHandler(){
        rssFeeds = new ArrayList<Feed>();
    }

    //Instantiate the parser

    public ArrayList<Feed> parse(InputStream in) throws IOException{

        try {
           readFeed(in);

        }catch ( ParserConfigurationException | IOException
                    | SAXException | ParseException     exp) {

            exp.printStackTrace();

        }finally {
            in.close();
        }

        return rssFeeds;
    }

    private void readFeed(InputStream in)
            throws ParserConfigurationException, IOException, SAXException, ParseException{

        Element eElement;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(in);
        doc.getDocumentElement().normalize();


        NodeList nList = doc.getElementsByTagName("item");
        rssFeeds = rssFeeds == null ? new ArrayList<Feed>() : rssFeeds;

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                feed     = new Feed();
                eElement = (Element) nNode;

                feed.setTitle(
                        eElement.getElementsByTagName
                                ("title").item(0).getTextContent());
                feed.setDescription(
                        eElement.getElementsByTagName
                                ("description").item(0).getTextContent());
                feed.setPubDate(
                        formatPubDate.parse(
                                eElement.getElementsByTagName
                                        ("pubDate").item(0).getTextContent()));
                rssFeeds.add(feed);
            }
        }
    }
}
