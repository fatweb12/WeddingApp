package co.fatweb.com.wedding.DataObject;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TopRatedMovies {

    @SerializedName("data")
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("total_value")
    @Expose
    private String totalResults;


    private json json;

    public json getJson() {
        return json;
    }

    public void setJson(json json) {
        this.json = json;
    }

    public String location;
    public String category;
    public int page;





    public TopRatedMovies(String location, String category, int page) {
        this.location = location;
        this.category = category;
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }


    public void setResults(List<Result> results) {
        this.results = results;
    }


    public String getTotalResults() {
        return totalResults;
    }


    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }


}