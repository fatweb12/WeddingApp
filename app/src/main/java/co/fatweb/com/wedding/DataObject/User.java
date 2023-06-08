package co.fatweb.com.wedding.DataObject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Asus-Amad-PC on 11/14/2017.
 */

public class User implements Serializable {

    @SerializedName("user_id") private String id;

    @SerializedName("password") private String password;

    @SerializedName("email") private String emailAddress;
    @SerializedName("username") private String username;
    @SerializedName("phone") private String bphn;
    @SerializedName("category") private String bcategory;
    @SerializedName("name") private String businessname;

    @SerializedName("website") private String website;
    @SerializedName("first_name") private String firstname;
    @SerializedName("last_name") private String lastname;
    @SerializedName("nickname") private String nickname;
    @SerializedName("about_us") private String about;
    @SerializedName("message") private String message;

    public String getUser_phn() {
        return user_phn;
    }

    public void setUser_phn(String user_phn) {
        this.user_phn = user_phn;
    }

    @SerializedName("address") private String baddress;
    @SerializedName("suburb") private String bsuburb;
    @SerializedName("city") private String bcity;
    @SerializedName("postcode") private String bpostcode;
    @SerializedName("user_phone") private String user_phn;

    public String getOld_pas() {
        return old_pas;
    }

    public void setOld_pas(String old_pas) {
        this.old_pas = old_pas;
    }

    public String getNew_pas() {
        return new_pas;
    }

    public void setNew_pas(String new_pas) {
        this.new_pas = new_pas;
    }

    public String getId_pas() {
        return id_pas;
    }

    public void setId_pas(String id_pas) {
        this.id_pas = id_pas;
    }

    @SerializedName("old_password") private String old_pas;

    @SerializedName("new_password") private String new_pas;
    @SerializedName("ID") private String id_pas;

    @SerializedName("total_value") private String totalvalue;

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getGmail_id() {
        return gmail_id;
    }

    public void setGmail_id(String gmail_id) {
        this.gmail_id = gmail_id;
    }

    @SerializedName("business_id") private String businessid;
    @SerializedName("content") private String content;
    @SerializedName("Img_url") private String imgUrl;
    @SerializedName("phone_number") private String phone;
    @SerializedName("mobile_number") private String mobilenum;
    @SerializedName("fbid") private String fb_id;
    @SerializedName("gmailid") private String gmail_id;

    public String getPostviews() {
        return postviews;
    }

    public void setPostviews(String postviews) {
        this.postviews = postviews;
    }

    @SerializedName("website_url") private String websiteurl;
    @SerializedName("view") private String postviews;


    @SerializedName("location") private String location;



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @SerializedName("suburb1") private String suburb;

    public String getCat_postcode() {
        return cat_postcode;
    }

    public void setCat_postcode(String cat_postcode) {
        this.cat_postcode = cat_postcode;
    }

    @SerializedName("postcodes") private String cat_postcode;


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

    @SerializedName("state") private String state;
    @SerializedName("street_addresses") private String adress;
    @SerializedName("region") private String region;

    public String getBphn() {
        return bphn;
    }

    public void setBphn(String bphn) {
        this.bphn = bphn;
    }

    public String getBcategory() {
        return bcategory;
    }

    public void setBcategory(String bcategory) {
        this.bcategory = bcategory;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBaddress() {
        return baddress;
    }

    public void setBaddress(String baddress) {
        this.baddress = baddress;
    }

    public String getBsuburb() {
        return bsuburb;
    }

    public void setBsuburb(String bsuburb) {
        this.bsuburb = bsuburb;
    }

    public String getBcity() {
        return bcity;
    }

    public void setBcity(String bcity) {
        this.bcity = bcity;
    }

    public String getBpostcode() {
        return bpostcode;
    }

    public void setBpostcode(String bpostcode) {
        this.bpostcode = bpostcode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }



    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    @SerializedName("android_version") private String androidVersion;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
