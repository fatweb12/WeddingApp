package co.fatweb.com.wedding.DataObject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Asus-Amad-PC on 11/14/2017.
 */

public class Cat_Listing implements Serializable {

    @SerializedName("total_value") private String totalvalue;
    @SerializedName("business_id") private String businessid;
    @SerializedName("name") private String name;
    @SerializedName("content") private String content;
    @SerializedName("Img_url") private String imgUrl;
    @SerializedName("state") private String state;
    @SerializedName("street_addresses") private String adress;
    @SerializedName("ID") private String businessid1;
    @SerializedName("region") private String region;
    @SerializedName("post_title") private String posttitle;



    public String getBusinessid1() {
        return businessid1;
    }

    public void setBusinessid1(String businessid1) {
        this.businessid1 = businessid1;
    }



    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }


    public String getPostviews() {
        return postviews;
    }

    public void setPostviews(String postviews) {
        this.postviews = postviews;
    }

    @SerializedName("view") private String postviews;


    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    @SerializedName("category") private String cat_name;

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @SerializedName("postcodes") private String postcode;


    public String getTotalvalue() {
        return totalvalue;
    }

    public void setTotalvalue(String totalvalue) {
        this.totalvalue = totalvalue;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }


    @SerializedName("phone_number") private String phone;
    @SerializedName("mobile_number") private String mobilenum;
    @SerializedName("website_url") private String websiteurl;
    @SerializedName("suburb1") private String suburb;


 /* public List<Result> getResults() {
        return results;
    }*/


}
