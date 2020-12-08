package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/10/29
 * Description:
 */
public class DownloadVideoTaskItemBase {
    private String name;
    private String imageurl;
    private String url;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public DownloadVideoTaskItemBase() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public DownloadVideoTaskItemBase(String name, String imageurl, String url) {
        this.name = name;
        this.imageurl = imageurl;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownloadVideoTaskItemBase{" +
                "name='" + name + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", url='" + url + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
