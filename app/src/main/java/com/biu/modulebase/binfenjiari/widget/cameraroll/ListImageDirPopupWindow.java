package com.biu.modulebase.binfenjiari.widget.cameraroll;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.adapter.CommonAdapter;
import com.biu.modulebase.binfenjiari.adapter.ViewHolder;
import com.biu.modulebase.binfenjiari.model.ImageFloderVO;

import java.util.List;

public class ListImageDirPopupWindow extends
		BasePopupWindowForListView<ImageFloderVO> {
    private ListView mListDir;

    public ListImageDirPopupWindow(int width, int height,
	    List<ImageFloderVO> datas, View convertView) {
	super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
	mListDir = (ListView) findViewById(R.id.id_list_dir);
	mListDir.setAdapter(new CommonAdapter<ImageFloderVO>(context, mDatas,
		R.layout.select_img_list_dir_item) {
	    @Override
	    public void convert(ViewHolder helper, ImageFloderVO item) {
		helper.setText(R.id.id_dir_item_name, item.getName());
		helper.setImageByUrl(R.id.id_dir_item_image,
			item.getFirstImagePath());
		helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
	    }
	});
    }

    public interface OnImageDirSelected {
	void selected(ImageFloderVO floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
	this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
	mListDir.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {

		if (mImageDirSelected != null) {
		    mImageDirSelected.selected(mDatas.get(position));
		}
	    }
	});
    }

    @Override
    public void init() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
	// TODO Auto-generated method stub
    }

}
