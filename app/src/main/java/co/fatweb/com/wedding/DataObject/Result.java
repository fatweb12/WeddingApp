package co.fatweb.com.wedding.DataObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("postcodes")
    @Expose
    private String postcode;



    @SerializedName("total_value")@Expose private String totalvalue;
    @SerializedName("business_id")@Expose private String businessid;

    @SerializedName("content")@Expose private String content;
    @SerializedName("state")@Expose private String state;
    @SerializedName("street_addresses")@Expose private String adress;
    @SerializedName("ID")@Expose private String businessid1;
    @SerializedName("region")@Expose private String region;
    @SerializedName("post_title")@Expose private String posttitle;

    @SerializedName("phone_number")@Expose private String phone;
    @SerializedName("mobile_number")@Expose private String mobilenum;
    @SerializedName("website_url")@Expose private String websiteurl;
    @SerializedName("category")@Expose private String cat_name;
    @SerializedName("view")@Expose private String postviews;

    public String getPostviews() {
        return postviews;
    }

    public void setPostviews(String postviews) {
        this.postviews = postviews;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBusinessid1() {
        return businessid1;
    }

    public void setBusinessid1(String businessid1) {
        this.businessid1 = businessid1;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
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

    @SerializedName("suburb1")@Expose private String suburb;



    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @SerializedName("Img_url")
    @Expose
    private String imgurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }





    /**
     *
     * @return
     * The posterPath


    /**
     *
     * @return
     * The id
     */

    /**
     *
     * @return
     * The originalTitle
     */

    /**
     *
     * @return
     * The originalLanguage
     */


    /**
     *
     * @return
     * The voteAverage
     */

}