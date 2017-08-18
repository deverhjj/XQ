package com.biu.modulebase.binfenjiari.widget.expandablelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.model.CircleMemeberVO;

import java.util.ArrayList;
import java.util.List;

public class FriendsPinyinAdapter extends BaseExpandableListAdapter {

    // 字符串
    private List<CircleMemeberVO> strList;

    private AssortPinyinFriendsList assort = new AssortPinyinFriendsList();

    private Context context;

    private LayoutInflater inflater;
    // 中文排序
    private LanguageComparator_CN cnSort = new LanguageComparator_CN();

    public FriendsPinyinAdapter(Context context, List<CircleMemeberVO> list) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.strList = list;
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
        TextView textView = (TextView) contentView.findViewById(R.id.name);
        textView.setText(assort.getHashList().getValueIndex(group, child)
                .getUser_name());

        return contentView;
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

}
