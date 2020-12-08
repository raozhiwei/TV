package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/12/3
 * Description:
 */
public class Commentback {
    int up;
    int down;

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    @Override
    public String toString() {
        return "Commentback{" +
                "up=" + up +
                ", down=" + down +
                '}';
    }

    public void setDown(int down) {
        this.down = down;
    }

    public Commentback() {
    }

    public Commentback(int up, int down) {
        this.up = up;
        this.down = down;
    }
}
