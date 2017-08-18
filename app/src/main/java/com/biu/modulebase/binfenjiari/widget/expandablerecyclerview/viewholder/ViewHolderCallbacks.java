package com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jhj_Plus on 2016/3/22.
 */
public interface ViewHolderCallbacks {

    int[] getNeedRegisterClickListenerChildViewIds();

    void onClick(RecyclerView.ViewHolder viewHolder, View view, int position, int adapterPosition,
            int parentPosition, int parentAdapterPosition);

    void bindData(RecyclerView.ViewHolder viewHolder, Object data);
}
