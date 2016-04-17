package cn.imrhj.newlogin.model;

/**
 * Created by rhj on 16/4/17.
 */
public interface NetWorkInfoInterface {
    enum NetWorkStatus{
        NOT_CONNECT,
        WIFI,
        OTHER
    }

    NetWorkStatus getNetWorkType();

    String getSSID();

    String getName();
}
