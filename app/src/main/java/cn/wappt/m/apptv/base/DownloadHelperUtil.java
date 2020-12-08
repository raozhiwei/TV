package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/11/17
 * Description:
 */
public class DownloadHelperUtil {

    private  String download_name;  //名称
    private  String download_url;  //url
    private  String download_urlimage;  //图片

    public DownloadHelperUtil() {
    }



    public String getDownload_name() {
        return download_name;
    }

    public void setDownload_name(String download_name) {
        this.download_name = download_name;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getDownload_urlimage() {
        return download_urlimage;
    }

    public void setDownload_urlimage(String download_urlimage) {
        this.download_urlimage = download_urlimage;
    }

    public DownloadHelperUtil( String download_name, String download_url, String download_urlimage) {

        this.download_name = download_name;
        this.download_url = download_url;
        this.download_urlimage = download_urlimage;
    }

    @Override
    public String toString() {
        return "DownloadHelperUtil{" +
                "download_name='" + download_name + '\'' +
                ", download_url='" + download_url + '\'' +
                ", download_urlimage='" + download_urlimage + '\'' +
                '}';
    }
}
