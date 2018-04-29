package edu.andr.xyzyx.MyUtil;

/**
 * Created by asus on 2018/4/15.
 */

public class ClockBean {
    private long encrypt;
    private long decrypt;
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time+1;
    }

    public long getEncrypt() {
        return encrypt;
    }

    public ClockBean() {
    }

    public ClockBean(long start, long end, long finish) {
        this.encrypt = end-start;
        this.decrypt = finish-end;
    }

    public void setEncrypt(long encrypt) {
        this.encrypt = encrypt;
    }

    public long getDecrypt() {
        return decrypt;
    }

    public void setDecrypt(long decrypt) {
        this.decrypt = decrypt;
    }
}
