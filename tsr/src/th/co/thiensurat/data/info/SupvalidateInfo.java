package th.co.thiensurat.data.info;

import java.util.List;

public class SupvalidateInfo {
    public String contno;
    public String effdate;
    public String customername;
    public String productname;
    public String productmodel;
    public String productserial;
    public String statusComment;
    public int statusId;
    public List<SupvalidateItemInfo> supvalidateItemInfoList;

    public String getContno() {
        return contno;
    }

    public void setContno(String contno) {
        this.contno = contno;
    }

    public String getEffdate() {
        return effdate;
    }

    public void setEffdate(String effdate) {
        this.effdate = effdate;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductmodel() {
        return productmodel;
    }

    public void setProductmodel(String productmodel) {
        this.productmodel = productmodel;
    }

    public String getProductserial() {
        return productserial;
    }

    public void setProductserial(String productserial) {
        this.productserial = productserial;
    }

    public List<SupvalidateItemInfo> getSupvalidateItemInfoList() {
        return supvalidateItemInfoList;
    }

    public void setSupvalidateItemInfoList(List<SupvalidateItemInfo> supvalidateItemInfoList) {
        this.supvalidateItemInfoList = supvalidateItemInfoList;
    }

    public String getStatusComment() {
        return statusComment;
    }

    public void setStatusComment(String statusComment) {
        this.statusComment = statusComment;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
