package th.co.thiensurat.fragments.sales.lead_online.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

public class Getdata {
    String CusName;
    String id;
    String CreateDate;
    String CustomerName;
    String Tel;
    String Province;
    String Product;
    String Details;
    Uri Picture;
    String Channel;
    String StatusWork;
    String CodeStamp;


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
    public String getCreateDate() {
        return CreateDate;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public String getTel() {
        return Tel;
    }
    public String getProduct() {
        return Product;
    }
    public String getProvince() {
        return Province;
    }
    public String getDetails() {
        return Details;
    }
    public Uri getPicture() {
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
    public void setCreateDate(String createDate ) {
        CreateDate = createDate;
    }
    public void setCustomerName(String customerName ) {
        CustomerName = customerName;
    }
    public void setTel(String tel ) {
        Tel = tel;
    }
    public void setProvince(String province ) {
        Province = province;
    }
    public void setProduct(String product ) {
        Product = product;
    }
    public void setDetails(String details ) {
        Details = details;
    }
    public void setPicture(Uri picture ) {
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
