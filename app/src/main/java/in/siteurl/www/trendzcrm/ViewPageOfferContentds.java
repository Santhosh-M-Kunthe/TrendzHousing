package in.siteurl.www.trendzcrm;

/**
 * Created by SiteURL on 3/10/2018.
 */

public class ViewPageOfferContentds {

    String offerID, offerName, offferDesc, offerImageURL;

    public ViewPageOfferContentds(String offerID, String offerName, String offferDesc, String offerImageURL) {
        this.offerID = offerID;
        this.offerName = offerName;
        this.offferDesc = offferDesc;
        this.offerImageURL = offerImageURL;
    }

    public String getOfferID() {
        return offerID;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getOffferDesc() {
        return offferDesc;
    }

    public String getOfferImageURL() {
        return offerImageURL;
    }
}
