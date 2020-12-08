package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/10/20
 * Description:
 */
public class SortLateral {
    String sortName;
    int sortId;

    public SortLateral() {
    }

    public SortLateral(String sortName, int sortId) {
        this.sortName = sortName;
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Override
    public String toString() {
        return "SortLateral{" +
                "sortName='" + sortName + '\'' +
                ", sortId='" + sortId + '\'' +
                '}';
    }
}
