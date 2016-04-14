package com.evernews.evernews;
/**An adapter to set out list views**/
public class ExpandListViewModel {
    String title;
    String desc;
    String icon;


    private String channelLogo;
    private String channelTitle;
    private String channelMeta;
    private String channelRSSID;
    private String categoryType;
    private String channelRSSURL;

    public ExpandListViewModel(String channelLogo, String channelTitle, String channelMeta,String channelRSSID,String categoryType,String channelRSSURL) {
        super();
        this.channelLogo = channelLogo;
        this.channelTitle = channelTitle;
        this.channelMeta = channelMeta;
        this.channelRSSID = channelRSSID;
        this.categoryType = categoryType;
        this.channelRSSURL = channelRSSURL;
    }


    public String getChannelLogo() {
        return channelLogo;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getChannelMeta() {
        return channelMeta;
    }

    public String getChannelRSSID() {
        return channelRSSID;
    }

    public String getChannelRSSURL() {
        return channelRSSURL;
    }

    public String getCategoryType() {
        return categoryType;
    }



    public void  setChannelLogo() {
        this.channelLogo=channelLogo;
    }

    public void  setChannelTitle() {
        this.channelTitle=channelTitle;
    }

    public void  setChannelMeta() {
        this.channelMeta=channelMeta;
    }

    public void  setChannelRSSID() {
        this.channelRSSID=channelRSSID;
    }

    public void  setChannelRSSURL() {
        this.channelRSSURL=channelRSSURL;
    }

    public void  setCategoryType() {
        this.categoryType=categoryType;
    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    /**
//     * @return the icon
//     */
//    public String getIcon() {
//        return icon;
//    }
//
//    /**
//     * @param icon
//     *            the icon to set
//     */
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    /**
//     * @param title
//     *            the title to set
//     */
//    public void setDesc(String title) {
//        this.desc = desc;
//    }
}
