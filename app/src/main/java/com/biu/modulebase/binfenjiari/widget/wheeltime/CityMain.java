package com.biu.modulebase.binfenjiari.widget.wheeltime;

import android.content.Context;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.model.CityVO;
import com.biu.modulebase.binfenjiari.model.SchoolVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CityMain implements OnWheelChangedListener {

    	/**String格式的json数据源**/
    private String jsonDataString;
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONObject mJsonObj;
    /**
     * 省的WheelView控件
     */
    private WheelView mProvince;
    /**
     * 市的WheelView控件
     */
    private WheelView mCity;
    /**
     * 区的WheelView控件
     */
    private WheelView mArea;

    /**
     * 所有省
     */
    private ArrayList<CityVO> mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, ArrayList<SchoolVO>> mCitisDatasMap = new HashMap<String, ArrayList<SchoolVO>>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, ArrayList<CityVO>> mAreaDatasMap = new HashMap<String, ArrayList<CityVO>>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    /**
     * 当前省的id
     */
    private String mCurrentProviceId;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前市的id
     */
    private String mCurrentCityId;
    /**
     * 当前区的名称
     */
    private String mCurrentAreaName = "";
    /**
     * 当前区的id
     */
    private String mCurrentAreaId;

    private Context context;
    private View view;
    /** 当前选择城市类型 默认为显示三级目录 **/
    private CITY_TYPE type = CITY_TYPE.LEVEL_THREE;

    /**
     * 选择城市类型
     */
    public enum CITY_TYPE {
	/** 只显示一级城市 **/
	LEVEL_ONE,
	/** 只显示一、二级城市 **/
	LEVEL_TWO,
	/** 显示一、二三级城市 **/
	LEVEL_THREE;

    }

    public View getView() {
	return view;
    }

    public void setView(View view) {
	this.view = view;
    }

    public CityMain(Context context, View view) {
	super();
	this.context = context;
	this.view = view;
    }

    public CityMain(Context context, View view, CITY_TYPE type,String jsonDataString) {
	super();
	this.context = context;
	this.view = view;
	this.type = type;
	this.jsonDataString =jsonDataString;
    }

    /**
     * 初始化城市选择器
     */
    public void initCityPicker() {
	// initJsonData();
	// 省
	mProvince = (WheelView) view
		.findViewById(R.id.biu_select_city_province);
	mCity = (WheelView) view.findViewById(R.id.biu_select_city_city);
	mArea = (WheelView) view.findViewById(R.id.biu_select_city_area);

	try {
	    initCityDatas(jsonDataString);
	} catch (JSONException e) {
	    e.printStackTrace();
	}
	if (mProvinceDatas == null) {
	    mProvinceDatas = new ArrayList<CityVO>();
	}
	mProvince.setAdapter(new ArrayListWheelAdapter<CityVO>(context,
		mProvinceDatas));

	if (type.equals(CITY_TYPE.LEVEL_ONE)) {
	    mCity.setVisibility(View.GONE);
	    mArea.setVisibility(View.GONE);
	} else if (type.equals(CITY_TYPE.LEVEL_TWO)) {
	    mArea.setVisibility(View.GONE);
	}

	// 添加change事件
	mProvince.addChangingListener(this);
	// 添加change事件
	mCity.addChangingListener(this);
	// 添加change事件
	mArea.addChangingListener(this);

	mProvince.setCurrentItem(0);
	mCity.setCurrentItem(0);
	mArea.setCurrentItem(0);
	mCurrentProviceName = mProvinceDatas.get(0).getName();
	mCurrentProviceId = mProvinceDatas.get(0).getId();
	updateProvinces();
	updateCities();
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
	mProvince.TEXT_SIZE = textSize;
	mCity.TEXT_SIZE = textSize;
	mArea.TEXT_SIZE = textSize;
    }

    /**
     * change事件的处理
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
	if (wheel == mProvince) {
	    updateProvinces();
	} else if (wheel == mCity) {
	    updateCities();
	} else if (wheel == mArea) {
	    updateAreas();

	}
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     * @param allCityList String 格式json字符串
     * @throws JSONException
     */
    private void initCityDatas(String allCityList) throws JSONException {
	JSONArray jsonArray = new JSONArray(allCityList);
	if(jsonArray.length() == 0)
	    return;
	mProvinceDatas = new ArrayList<CityVO>();
	for (int i = 0; i < jsonArray.length(); i++) {
	    JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
	    CityVO province = JSONUtil.fromJson(jsonP.toString(), CityVO.class);
//	     String province = jsonP.getString("p");// 省名字
	    mProvinceDatas.add(province);
//	    JSONArray jsonCs = null;
//	    try {
//		jsonCs = jsonP.getJSONArray("city_list");
//	    } catch (Exception e1) {
//		continue;
//	    }
	    ArrayList<SchoolVO> mCitiesDatas = new ArrayList<SchoolVO>();// 当前省下的说有市
	    mCitiesDatas.addAll(province.getClassList());
//	    for (int j = 0; j < mCitiesDatas.size(); j++) {
//		JSONObject jsonCity = jsonCs.getJSONObject(j);
//		CityVO city = JsonUtil.fromJson(jsonCity.toString(),
//			CityVO.class);
//		// String city = jsonCity.getString("n");// 市名字
//		mCitiesDatas.add(city);
//		JSONArray jsonAreas = null;
//		try {
//		    jsonAreas = jsonCity.getJSONArray("city_list");
//		} catch (Exception e) {
//		    continue;
//		}
//
//		ArrayList<CityVO> mAreasDatas = new ArrayList<CityVO>();// 当前市的所有区
//		for (int k = 0; k < jsonAreas.length(); k++) {
//		    JSONObject JSONArea = jsonCs.getJSONObject(k);
//		    CityVO area = JsonUtil.fromJson(JSONArea.toString(),
//			    CityVO.class);
//		    // String area =
//		    // jsonAreas.getJSONObject(k).getString("s");// 区域的名称
//		    mAreasDatas.add(area);
//		}
//
//		mAreaDatasMap.put(city.getCity_name(), mAreasDatas);
//	    }
	    ArrayList<CityVO> mAreasDatas = new ArrayList<CityVO>();// 当前市的所有区
	    mAreaDatasMap.put(province.getName(), mAreasDatas);
	    mCitisDatasMap.put(province.getName(), mCitiesDatas);
	}
	mJsonObj = null;
    }

    /**
     * 根据当前的区信息
     */
    private void updateAreas() {
	int pCurrent = mArea.getCurrentItem();
	ArrayList<CityVO> areas = mAreaDatasMap.get(mCurrentCityName) == null ? new ArrayList<CityVO>()
		: mAreaDatasMap.get(mCurrentCityName);
	if (areas.size() < 1) {
	    return;
	}
	mCurrentAreaName = areas.get(pCurrent).getName();
	mCurrentAreaId = areas.get(pCurrent).getId();
    }

    // private DataDictionaryControl dataDictionaryControl;

    /**
     * 根据当前的市信息，更新区WheelView的信息
     */
    private void updateCities() {
	int pCurrent = mCity.getCurrentItem();
	ArrayList<SchoolVO> citys = mCitisDatasMap.get(mCurrentProviceName) == null ? new ArrayList<SchoolVO>()
		: mCitisDatasMap.get(mCurrentProviceName);
	if (citys.size() < 1) {
	    return;
	}
	mCurrentCityName = citys.get(pCurrent).getName();
	mCurrentCityId = citys.get(pCurrent).getId();
	ArrayList<CityVO> areas = mAreaDatasMap.get(mCurrentCityName);
	if (areas == null) {
	    areas = new ArrayList<CityVO>();
	}
	mArea.setAdapter(new ArrayListWheelAdapter<CityVO>(context, areas));
	mArea.setCurrentItem(0);
    }
    
    

    /**
     * 根据当前的省信息，更新市WheelView的信息
     */
    private void updateProvinces() {
	int pCurrent = mProvince.getCurrentItem();
	mCurrentProviceName = mProvinceDatas.get(pCurrent).getName();
	mCurrentProviceId = mProvinceDatas.get(pCurrent).getId();
	ArrayList<SchoolVO> cities = mCitisDatasMap.get(mCurrentProviceName);
	if (cities == null) {
	    cities = new ArrayList<SchoolVO>();
	}
	mCity.setAdapter(new ArrayListWheelAdapter<SchoolVO>(context, cities));
	mCity.setCurrentItem(0);
	updateAreas();

    }

    /**
     * 获取当前城市全名
     * 
     * @return
     */
    public String getCurrentAllName() {
	StringBuilder allName = new StringBuilder();
	String oneName = getOneName();
	String twoName = getTwoName();
	String threeName = getThreeName();
	if (type.equals(CITY_TYPE.LEVEL_ONE)) {
	    if (!oneName.equals("----"))
		allName = allName.append(oneName);
	} else if (type.equals(CITY_TYPE.LEVEL_TWO)) {
	    if (!oneName.equals("----"))
		allName = allName.append(oneName);
	    if (!twoName.equals("----"))
		allName.append("-").append(twoName);
	} else if (type.equals(CITY_TYPE.LEVEL_THREE)) {
	    if (!oneName.equals("----"))
		allName = allName.append(oneName);
	    if (!twoName.equals("----"))
		allName.append("-").append(twoName);
	    if (!threeName.equals("----"))
		allName = allName.append("-").append(threeName);
	}
	return allName.toString();

    }

    public String showChoose() {
	return mCurrentProviceName + mCurrentCityName + mCurrentAreaName;
    }

    /** 获取一级菜单的名称 */
    public String getOneName() {
	return mCurrentProviceName;
    }

    /** 获取二级菜单的名称 */
    public String getTwoName() {
	return mCurrentCityName;
    }

    /** 获取三级菜单的名称 */
    public String getThreeName() {
	return mCurrentAreaName;
    }

    /**
     * 获取当前一级菜单对应的Code
     * 
     * @return
     */
    public String getCurentProviceId() {

	return mCurrentProviceId;
    }

    /**
     * 获取当前二级菜单对应的Code
     * 
     * @return
     */
    public String getCurentCityId() {
	return mCurrentCityId;
    }

    /**
     * 获取当前三级菜单对应的Code
     * 
     * @return
     */
    public String getCurentAreaId() {
	return mCurrentAreaId;
    }

    public CITY_TYPE getType() {
	return type;
    }

    public void setType(CITY_TYPE type) {
	this.type = type;
    }
}
