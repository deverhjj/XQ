package com.biu.modulebase.binfenjiari.widget.wheeltime;


import android.content.Context;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.Utils;

import java.util.List;



/**
 * 一级级联菜单通用类
 * @author Lee
 *
 */
public class EmotionMain  implements OnWheelChangedListener {

    /**
     * 一级WheelView控件
     */
    private WheelView emotion;

    /**
     * 所有数据集
     */
    private List<String> emotions;
    
    /**
     * 当前所选情感的名称
     */
    private String selectedData = null;

    private Context context;
    private View view;
    
    private int currentPosition =0;

    public View getView() {
	return view;
    }

    public void setView(View view) {
	this.view = view;
    }

    public EmotionMain(Context context, View view) {
	super();
	this.context = context;
	this.view = view;
    }

    /**
     * 初始化城市选择器
     */
    public void initCityPicker(List<String> emotions) {
	emotion = (WheelView) view.findViewById(R.id.biu_select_city_province);

	this.emotions = emotions;

    selectedData = emotions.get(0);

	emotion.setAdapter(new ArrayWheelAdapter<String>(context, emotions));

	// 添加change事件
	emotion.addChangingListener(this);

	emotion.setCurrentItem(0);

	setTextSize();

    }

    /**
     * 设置字体大小
     */
    public void setTextSize() {
	int textSize = 0;
	if (Utils.getScreenHeight(view.getContext()) < 800) {
	    textSize = (int) view.getResources().getDimension(
		    R.dimen.text_size_18);
	} else {
	    textSize = (int) view.getResources().getDimension(
		    R.dimen.text_size_20);
	}
	emotion.TEXT_SIZE = textSize;
    }

    /**
     * change事件的处理
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
	if (wheel == emotion) {
        selectedData = emotions.get(newValue);
	    currentPosition =  newValue;
	}
    }
    /** 获取一级菜单所对应的对象 */
    public String getSelectedData() {
        return selectedData;
    }

    /**
     * 返回当前选择的item positon
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

   
    

}
