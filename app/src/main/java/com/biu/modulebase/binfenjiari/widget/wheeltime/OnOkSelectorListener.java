package com.biu.modulebase.binfenjiari.widget.wheeltime;


/**
 * 监听Wheel Dialog选择的Ok按钮
 * 
 * @author Lee
 *
 */
public interface OnOkSelectorListener {
    /**
     * 时间选择回调
     * 
     * @param wheelMain
     */
    public void onOkSelector(WheelMain wheelMain);

    /**
     * 城市选择回调
     * 
     * @param wheelMain
     * @param Tag
     *            Dialog弹出类型
     */
    public void onOkSelector(CityMain wheelMain);

    /**
     * 一级级联菜单选择回调
     * 
     * @param wheelMain
     * @param Tag
     *            Dialog弹出类型
     */
    public void onOkSelector(EmotionMain wheelMain);

}
