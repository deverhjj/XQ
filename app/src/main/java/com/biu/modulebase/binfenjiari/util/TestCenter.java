package com.biu.modulebase.binfenjiari.util;

import android.net.Uri;
import android.view.View;

import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/14.
 */
public class TestCenter {
    private static final String TAG = "TestCenter";

    public static final Uri URI_VIDEO_LOCAL=Uri.parse("android.resource://"+"com.biu" +
            ".xq/raw/video_sample_1");

    public static final String[] tempTabs={"老师","小学生","杀手","收拾旧山河","撒阿斯顿"};

    public interface ClickCallbacks{
        void onClicked(int pos,View target);
    }
    public static void setClickListener(final ClickCallbacks callbacks,View... views) {
        for (int i = 0; i < views.length; i++) {
            final int finalI = i;
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callbacks!=null) {
                       callbacks.onClicked(finalI,v);
                    }
                }
            });
        }
    }


    public static List<ParentListItem> getCommentTestData() {
        List<ParentListItem> parentItems = new ArrayList<>();

//        for (int i = 0; i < 6; i++) {
//            CardDetailParentItem cardDetailParentItem = new CardDetailParentItem();
//            parentItems.add(cardDetailParentItem);
//
//            if (i == 0) {
//                cardDetailParentItem.setCard(true);
//                continue;
//            }
//
//            if (i%2==0) {
//                List<CardDetailChildItem> cardDetailChildItems = new ArrayList<>();
//                for (int j = 0; j < 3; j++) {
//                    CardDetailChildItem childItem = new CardDetailChildItem();
//                    cardDetailChildItems.add(childItem);
//                }
//                cardDetailParentItem.setHasChildComment(true);
//                //cardDetailParentItem.setCardDetailChildItems(cardDetailChildItems);
//            }
//
//        }

        return parentItems;
    }
}
