package com.biu.modulebase.binfenjiari.model;

import java.io.Serializable;

/**
 * Created by jhj_Plus on 2016/1/13.
 */
public class CardDetailChildItem implements Serializable {
    private static final String TAG = "CardDetailChildItem";

    private ReplyItem mReplyItem;

    public ReplyItem getReplyItem() {
        return mReplyItem;
    }

    public void setReplyItem(ReplyItem replyItem) {
        mReplyItem = replyItem;
    }
}
