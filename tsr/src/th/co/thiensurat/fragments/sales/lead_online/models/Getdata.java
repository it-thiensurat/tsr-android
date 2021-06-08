package th.co.thiensurat.fragments.sales.lead_online.models;

public class Getdata {
    String CusName;
    String id;
    String CreateDate;
    String CustomerName;
    String Tel;
    String Email;
    String IDLine;
    String IDProvince;

    String Province;
    String Product;
    String Details;
    String Picture;
    String Channel;
    String StatusWork;
    String CodeStamp;
    long idl;

    public String getCusName() {
        return CusName;
    }

    public void setCusName(String cusName ) {
        CusName = cusName;
    }
//**
    public String getId() {
    return id;
}

    public long getidl(long id) {
        return idl;
    }

    public String getCreateDate() {
        return CreateDate;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public String getTel() {
        return Tel;
    }
    public String getEmail() {
        return Email;
    }
    public String getIDLine() {
        return IDLine;
    }
    public String getProduct() {
        return Product;
    }
    public String getProvince() {
        return Province;
    }
    public String getIDProvince() {
        return IDProvince;
    }
    public String getDetails() {
        return Details;
    }
    public String getPicture() {
        return Picture;
    }
    public String getChannel() {
        return Channel;
    }
    public String getStatusWork(){
        return StatusWork;
    }
    public String getCodeStamp(){
        return CodeStamp;
    }

    public void setId(String noid ) {
        id = noid;
    }
    public void setidl(long idl ) {
        idl = idl;
    }
    public void setCreateDate(String createDate ) {
        CreateDate = createDate;
    }
    public void setCustomerName(String customerName ) {
        CustomerName = customerName;
    }
    public void setTel(String tel ) {
        Tel = tel;
    }
    public void setEmail(String email ) {
        Email = email;
    }
    public void setIDLine(String idline ) {
        IDLine = idline;
    }
    public void setProvince(String province ) {
        Province = province;
    }
    public void setIDProvince(String idprovince ) {
        IDProvince = idprovince;
    }
    public void setProduct(String product ) {
        Product = product;
    }
    public void setDetails(String details ) {
        Details = details;
    }
    public void setPicture(String picture ) {
        Picture = picture;
    }
    public void setChannel(String channel ) {
        Channel = channel;
    }
    public String setStatusWork(String statuswork1){
        return StatusWork =statuswork1;
    }
    public String setCodeStamp(String codestamp){
        return CodeStamp =codestamp;
    }
}
