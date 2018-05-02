package in.siteurl.www.trendzcrm;

/**
 * Created by SiteURL on 2/26/2018.
 */

public class FlatContents {

    String unitId, unitName, projectId, blockID, floor, value, customerID, saleValue, recievedAmount;
    String balanceAmaount, size, unitStatus, createdAt;

    public FlatContents(String unitId, String unitName, String projectId, String blockID, String floor, String value, String customerID, String saleValue, String recievedAmount, String balanceAmaount, String size, String unitStatus, String createdAt) {

        //initialize all variable
        this.unitId = unitId;
        this.unitName = unitName;
        this.projectId = projectId;
        this.blockID = blockID;
        this.floor = floor;
        this.value = value;
        this.customerID = customerID;
        this.saleValue = saleValue;
        this.recievedAmount = recievedAmount;
        this.balanceAmaount = balanceAmaount;
        this.size = size;
        this.unitStatus = unitStatus;
        this.createdAt = createdAt;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getBlockID() {
        return blockID;
    }

    public String getFloor() {
        return floor;
    }

    public String getValue() {
        return value;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getSaleValue() {
        return saleValue;
    }

    public String getRecievedAmount() {
        return recievedAmount;
    }

    public String getBalanceAmaount() {
        return balanceAmaount;
    }

    public String getSize() {
        return size;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
