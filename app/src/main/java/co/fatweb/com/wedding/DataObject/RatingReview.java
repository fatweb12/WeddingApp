package co.fatweb.com.wedding.DataObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aamad on 1/22/2018.
 */

public class RatingReview {



    @SerializedName("comment_ID") private String commentid;
    @SerializedName("comment_author") private String comment_author;

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getComment_author() {
        return comment_author;
    }

    public void setComment_author(String comment_author) {
        this.comment_author = comment_author;
    }

    public String getComment_email() {
        return comment_email;
    }

    public void setComment_email(String comment_email) {
        this.comment_email = comment_email;
    }

    public String getCommenturl() {
        return commenturl;
    }

    public void setCommenturl(String commenturl) {
        this.commenturl = commenturl;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getCommentuserid() {
        return commentuserid;
    }

    public void setCommentuserid(String commentuserid) {
        this.commentuserid = commentuserid;
    }

    public String getRatingcounnt() {
        return ratingcounnt;
    }

    public void setRatingcounnt(String ratingcounnt) {
        this.ratingcounnt = ratingcounnt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SerializedName("comment_author_email") private String comment_email;
    @SerializedName("comment_author_url") private String commenturl;
    @SerializedName("comment_content") private String commentcontent;

    @SerializedName("user_id") private String commentuserid;
    @SerializedName("rating") private String ratingcounnt;


    public User user;




}
