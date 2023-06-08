package co.fatweb.com.wedding.Model;

public class Home {
    public String serviceTitle;
    public String serviceContent;
    public String serviceContent1;
    public int serviceImageId;
    public String serviceImg;

    public Home(String serviceTitle, String web_content, int ic_web_dev, String ic_img, String web_content1) {
        this.serviceTitle = serviceTitle;
        this.serviceContent = web_content;
        this.serviceContent1 = web_content1;
        this.serviceImageId = ic_web_dev;
        this.serviceImg = ic_img;

    }
}
