package pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Feed implements Serializable{

    private String title;
    private String description;
    private Date pubDate;
    private String taggedStations;
    private boolean isStationOnePresent;
    private boolean isStationTwoPresent;
    private boolean isStationThreePresent;
    private boolean isStationFourPresent;
    private Map<String,Object> alertTypeMap;
    private int line = -1;
    private boolean accessible;

    public Feed(){}

    public Feed(String title, String description, Date pubDate) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaggedStations() {
        return taggedStations;
    }

    public void setTaggedStations(String taggedStations) {
        this.taggedStations = taggedStations;
    }

    public boolean isStationOnePresent() {
        return isStationOnePresent;
    }

    public void setStationOnePresent(boolean stationOnePresent) {
        isStationOnePresent = stationOnePresent;
    }

    public boolean isStationTwoPresent() {
        return isStationTwoPresent;
    }

    public void setStationTwoPresent(boolean stationTwoPresent) {
        isStationTwoPresent = stationTwoPresent;
    }

    public boolean isStationThreePresent() {
        return isStationThreePresent;
    }

    public void setStationThreePresent(boolean stationThreePresent) {
        isStationThreePresent = stationThreePresent;
    }

    public boolean isStationFourPresent() {
        return isStationFourPresent;
    }

    public void setStationFourPresent(boolean stationFourPresent) {
        isStationFourPresent = stationFourPresent;
    }

    public Map<String, Object> getAlertTypeMap() {
        return alertTypeMap;
    }

    public void setAlertTypeMap(Map<String, Object> alertTypeMap) {
        this.alertTypeMap = alertTypeMap;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

}
