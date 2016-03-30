package com.evernews.evernews;


public class ItemObjectS {

    private String newsImage;
    private String newsTitle;
    private String newsName;
    private String newsID;
    private String categoryID;
    private String fullText;
    private String newsURL;
    private String newsSummary;

    public ItemObjectS(String newsImage, String newsTitle, String newsName,String newsID,String categoryID,String fullText,String newsURL ,String newsSummary) {
        this.newsImage = newsImage;
        this.newsTitle = newsTitle;
        this.newsName = newsName;
        this.newsID = newsID;
        this.categoryID = categoryID;
        this.fullText = fullText;
        this.newsURL = newsURL;
        this.newsSummary=newsSummary;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public String getnewsTitle() {
        return newsTitle;
    }

    public String getnewsName() {
        return newsName;
    }

    public String getNewsID() {
        return newsID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getFullText() {
        return fullText;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public String getnewsSummary(){return newsSummary;}
}
