package cn.imrhj.newlogin.model;

/**
 * Created by rhj on 16/4/17.
 */
public interface NetWorkInfoInterface {
    String getNetWorkType();

    String getSSID();

    String getName();

    boolean isThreeSSID();

    boolean isCanLoginSSID();
}
