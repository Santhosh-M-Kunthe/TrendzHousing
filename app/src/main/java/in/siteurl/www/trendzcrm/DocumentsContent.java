package in.siteurl.www.trendzcrm;

import java.io.Serializable;

/**
 * Created by SiteURL on 2/27/2018.
 */

public class DocumentsContent implements Serializable {

    String imageURL;
    String docID;
    String docName;
    String tableName;
    String docType;
    String association_id;

    public DocumentsContent(String imageURL, String docName) {

        //initalize all variables
        this.docName = docName;
        this.imageURL = imageURL;

    }

    public DocumentsContent(String docName, String imageURL, String association_id, String tableName, String docType) {

        //initalize all variables
        this.docName = docName;
        this.imageURL = imageURL;
        this.association_id = association_id;
        this.tableName = tableName;
        this.docType = docType;
    }

    String docPath;

    public DocumentsContent(String imageURL) {

        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDocID() {
        return docID;
    }

    public String getDocName() {
        return docName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocPath() {
        return docPath;
    }
}
