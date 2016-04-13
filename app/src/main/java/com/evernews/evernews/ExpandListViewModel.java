package com.evernews.evernews;
/**An adapter to set out list views**/
public class ExpandListViewModel {
    String title;
    String desc;
    String icon;

    /**
     * @param title
     * @param icon
     */
    public ExpandListViewModel(String title, String icon, String desc) {
        super();
        this.title = title;
        this.icon = icon;
        this.desc = desc;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setDesc(String title) {
        this.desc = desc;
    }
}
