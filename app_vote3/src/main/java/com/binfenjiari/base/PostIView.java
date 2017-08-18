package com.binfenjiari.base;

/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/8
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface PostIView {

    void showPostPrepareUi();

    void showPostSuccessUi();

    void showPostFailureUi(AppExp exp);

    void clearPostUi();
}
