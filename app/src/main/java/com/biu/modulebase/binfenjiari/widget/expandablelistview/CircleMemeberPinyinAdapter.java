package com.biu.modulebase.binfenjiari.widget.expandablelistview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.model.CircleMemeberVO;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子成员ExpendListViewAdapter
 */
public class CircleMemeberPinyinAdapter extends BaseExpandableListAdapter {

    private BaseFragment fragment;

    // 字符串
    private List<CircleMemeberVO> strList;

    private AssortPinyinFriendsList assort = new AssortPinyinFriendsList();

    /**成员操作类型  1转让圈子 2设置管理员 3取消管理员 4移除成员 5拉黑 6取消拉黑**/
    private String operateType;
    /**
     * checkbox 最多可选人数
     *  转让圈子：1
     *  设置管理员：最多两个管理员 2-(adminList.size()-1)>0?2-(adminList.size()-1):0
     *  删除圈子成员 ：无限制   且 管理员只能操作普通成员
     *  不允许再次加入：无限制  且 管理员只能操作普通成员
     *  -10:  无限大
     * **/
    private int checkNum ;
    private String mCircleId;
    private List<CircleMemeberVO> checkedList = new ArrayList<>();

    private LayoutInflater inflater;
    // 中文排序
    private LanguageComparator_CN cnSort = new LanguageComparator_CN();

    public CircleMemeberPinyinAdapter(BaseFragment fragment, List<CircleMemeberVO> list, String operateType,String mCircleId) {
        super();
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(fragment.getActivity());
        this.strList = list;
        this.operateType =operateType;
        this.mCircleId = mCircleId;
        if (list == null) {
            list = new ArrayList<>();
        } else {
            sort();
        }
    }

    private void sort() {
        // 分类
        for (CircleMemeberVO str : strList) {
            assort.getHashList().add(str);
        }
        assort.getHashList().sortKeyComparator(cnSort);

    }

    public CircleMemeberVO getChild(int group, int child) {
        return assort.getHashList().getValueIndex(group, child);
    }

    public long getChildId(int group, int child) {
        return child;
    }

    public View getChildView(final int group, final int child, boolean arg2,
                             View contentView, ViewGroup arg4) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_friend_child, null);
        }
        CircleMemeberVO bean =assort.getHashList().getValueIndex(group, child);
        ImageView header = (ImageView) contentView.findViewById(R.id.header);
        ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,bean.getUser_pic(),header,ImageDisplayUtil.DISPLAY_HEADER);
        TextView textView = (TextView) contentView.findViewById(R.id.name);
        textView.setText(assort.getHashList().getValueIndex(group, child).getUser_name());
        final CheckBox checkBox =(CheckBox) contentView.findViewById(R.id.check);
        ImageView delete =(ImageView) contentView.findViewById(R.id.delete);
        if(!Utils.isEmpty(operateType)){
            if(operateType.equals(Constant.MEMEBER_OPERATE_TRANSFER_CIRCLE)||operateType.equals(Constant.MEMEBER_OPERATE_SET_MANAGER)||operateType.equals(Constant.MEMEBER_OPERATE_PULL_BLACK)){
                //转让圈子||设置管理员//不允许再加入
                checkBox.setChecked(false);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CircleMemeberVO bean = (CircleMemeberVO) getChild(group,child);
                        if(isChecked){
                            addCheckList(bean,buttonView);
                        }else{
                            removeCheckListById(bean);
                        }

                    }
                });
                delete.setVisibility(View.GONE);
            }else if(operateType.equals(Constant.MEMEBER_OPERATE_MOVE_OUT)){//删除圈子成员
                checkBox.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleMemeberVO bean = (CircleMemeberVO) getChild(group,child);
                        List<String> userIds = new ArrayList<String>();
                        userIds.add( bean.getId());
                        OtherUtil.memeberOperate(fragment, mCircleId, userIds, operateType, new OtherUtil.MemeberOperateCallback() {
                            @Override
                            public void onsuccess(String typenim) {
                                fragment.showTost("移除成功",0);
                                removeChild(group,child);
                            }
                        });
                    }
                });
            }

        }else{
            checkBox.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
        return contentView;
    }

    public void addCheckList(CircleMemeberVO bean,CompoundButton buttonView ){
        //检查可check数量
        if(checkNum ==-10||checkedList.size()<checkNum){//-10代表无限制
            checkedList.add(bean);
        }else{
            buttonView.setChecked(false);
            fragment.showTost("已达最大操作数",0);
        }
    }

    public void removeCheckListById(CircleMemeberVO bean){
        for(int i=0;i<checkedList.size();i++){
            if(checkedList.get(i).getId().equals(bean.getId())){
                checkedList.remove(i);
                break;
            }
        }
    }

    public int getChildrenCount(int group) {
        return assort.getHashList().getValueListIndex(group).size();
    }

    public Object getGroup(int group) {
        return assort.getHashList().getValueListIndex(group);
    }

    public int getGroupCount() {
        return assort.getHashList().size();
    }

    public long getGroupId(int group) {
        return group;
    }

    public View getGroupView(int group, boolean arg1, View contentView,
                             ViewGroup arg3) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_friend_parent, null);
            contentView.setClickable(true);
        }
        TextView textView = (TextView) contentView.findViewById(R.id.name);
        TextView num = (TextView) contentView.findViewById(R.id.num);
        textView.setText(assort.getFirstChar(assort.getHashList()
                .getValueIndex(group, 0).getUser_name()));
        num.setText("("+getChildrenCount(group)+"人)");
        // 禁止伸展
        return contentView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    public AssortPinyinFriendsList getAssort() {
        return assort;
    }

    public void removeChild(int group,int child){
        CircleMemeberVO bean = (CircleMemeberVO) getChild(group,child);
        for(int i =0;i<strList.size();i++){
            if(strList.get(i).getId().equals(bean.getId())){
                strList.remove(i);
                break;
            }
        }
        assort.getHashList().clear();
        sort();
        notifyDataSetChanged();
    }

    public void removeChilds(List<CircleMemeberVO> lists){
        for(int j =0;j<lists.size();j++){
            CircleMemeberVO bean = (CircleMemeberVO) lists.get(j);
            for(int i =0;i<strList.size();i++){
                if(strList.get(i).getId().equals(bean.getId())){
                    strList.remove(i);
                    break;
                }
            }
        }
        assort.getHashList().clear();
        sort();
        notifyDataSetChanged();
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
        // ...TODO 清除之前选的数据
        checkedList.clear();

        notifyDataSetChanged();
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public List<CircleMemeberVO> getCheckedList() {
        return checkedList;
    }

}
