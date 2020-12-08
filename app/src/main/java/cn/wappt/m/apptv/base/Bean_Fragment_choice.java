package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/9/18
 * Description:
 */
public class Bean_Fragment_choice {
    String name;
    String imgUrl;
    String listUrl;

    public Bean_Fragment_choice(String name, String imgUrl, String listUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.listUrl = listUrl;
    }

    public Bean_Fragment_choice() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
}
