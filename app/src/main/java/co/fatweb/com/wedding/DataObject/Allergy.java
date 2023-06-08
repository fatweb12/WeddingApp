package co.fatweb.com.wedding.DataObject;

import com.google.gson.annotations.SerializedName;


/**
 * Created by aamad on 1/22/2018.
 */

public class Allergy {


    @SerializedName("id") private String id;
    @SerializedName("name") private String name;

    public String getSlug() {
        return Slug;
    }

    public void setSlug(String slug) {
        Slug = slug;
    }

    @SerializedName("slug") private String Slug;

    public String getCat_image() {
        return Cat_image;
    }

    public void setCat_image(String cat_image) {
        Cat_image = cat_image;
    }

    @SerializedName("image") private String Cat_image;










    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
