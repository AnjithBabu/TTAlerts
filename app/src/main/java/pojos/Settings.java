package pojos;

import java.io.Serializable;

public class Settings implements Serializable {

    private int feedType;

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }
}
