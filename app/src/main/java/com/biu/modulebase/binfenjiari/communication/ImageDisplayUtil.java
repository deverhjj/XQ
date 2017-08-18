package com.biu.modulebase.binfenjiari.communication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * @author Lee
 * @Title: {图片加载类 使用开源框架 image-loader}
 * @Description:{描述}
 * @date 2016/4/12
 */
public class ImageDisplayUtil {

    /**
     * 个人中心头像
     **/
    public static final int DISPLAY_PERSONAL_HEADER = 0;
    /**
     * 头像
     **/
    public static final int DISPLAY_HEADER = 1;
    /**
     * 圆形图片
     **/
    public static final int DISPLAY_ROUND_IMAGE = 2;
    /**
     * 小图片  30*30 或更小
     **/
    public static final int DISPLAY_SMALL_IMAGE = 3;
    /**
     * 普通大小图片  120*120 左右
     **/
    public static final int DISPLAY_COMMON_IMAGE = 4;
    /**
     * 大长图 Banner、宽度占满屏幕的图片等
     **/
    public static final int DISPLAY_BIG_IMAGE = 5;

    public static void displayImage(String url, ImageView imageView) {
        MyApplication.getInstance().getImgLoader().displayImage(url, imageView);
    }

    /**
     * @param url       图片url
     * @param imageView
     * @param tag       图片类型
     */
    public static void displayImage(String imgType, String url, ImageView imageView, int tag) {
        String targetUrl = Utils.getString(url);
        String urlArray[] = targetUrl.split("/");
        StringBuilder urlBuilder = new StringBuilder();
        //url处理
        if (urlArray.length >= 2)
            urlBuilder.append(Constant.IMG_URL).append(urlArray[0]).append("/").append(imgType).append("/").append(urlArray[1]);
        DisplayImageOptions options;
        switch (tag) {
            case DISPLAY_PERSONAL_HEADER:
                options = getPersonalHeaderOptions();
                break;
            case DISPLAY_HEADER:
                options = getHeaderOptions();
                break;
            case DISPLAY_ROUND_IMAGE:
                options = getRoundImageOptions();
                break;
            case DISPLAY_SMALL_IMAGE:
                options = getSmallImageOptions();
                break;
            case DISPLAY_COMMON_IMAGE:
                options = getCommonImageOptions();
                break;
            case DISPLAY_BIG_IMAGE:
                options = getLargeImageOptions();
                break;
            default:
                options = getDefalutOptions();
        }
        MyApplication.getInstance().getImgLoader().displayImage(urlBuilder.toString(), imageView, options);
    }

    /**
     * 获取图片控件宽高  宽度为屏幕宽度
     * @return
     */
    public static int[] getWidthHeigh(int imageViewWidth, String url){
        int[] scrsize = {0,0};

        String targetUrl = Utils.getString(url);
        String urlArray[] = targetUrl.split("/");
        StringBuilder urlBuilder = new StringBuilder();
        //url处理
        String size = null;
        if (urlArray.length >= 3)
            size = urlArray[2];

        if(TextUtils.isEmpty(size))
            return scrsize;

//        Utils.getScreenWidth(act);
        String[] imageSize = size.split("\\*");
        int screenWidth = imageViewWidth;
        double picWidth = Utils.isDouble(imageSize[0]);
        double picHeight = Utils.isDouble(imageSize[1]);

        return getWidthHeigh(screenWidth,picWidth,picHeight);

    }

    /**
     * 获取图片控件宽高
     * @return
     */
    public static int[] getWidthHeigh(int allScreenSize,double picWidth,double picHeight){

        int showHeight = (int)((allScreenSize * picHeight)/picWidth);
        return new int[]{allScreenSize, showHeight};
    }

    public static void resumeTask() {
        MyApplication.getInstance().getImgLoader().resume();
    }

    public static void pauseTask() {
        MyApplication.getInstance().getImgLoader().pause();
    }

    public static void stopTask() {
        MyApplication.getInstance().getImgLoader().stop();
    }


    /**
     * 默认的Options
     *
     * @return
     */
    protected static DisplayImageOptions getDefalutOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(0) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(0)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(0) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型//
                .delayBeforeLoading(100)
                // .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    /**
     * 个人中心头像Options
     *
     * @return
     */
    protected static DisplayImageOptions getPersonalHeaderOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.personal_header) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.personal_header)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.personal_header) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型//
                .delayBeforeLoading(0)
                // .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    /**
     * 头像Options
     *
     * @return
     */
    protected static DisplayImageOptions getHeaderOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.header_pic) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.header_pic)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.header_pic) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型//
                .delayBeforeLoading(100)
                // .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    protected static DisplayImageOptions getRoundImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.circle_pic) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.circle_pic)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.circle_pic) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型//
                // .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    protected static DisplayImageOptions getSmallImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(null) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(null)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(null) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型//
                .delayBeforeLoading(100).resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    protected static DisplayImageOptions getCommonImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.common_pic) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.common_pic)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.common_pic) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型//
                // .delayBeforeLoading(300)
                // .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    protected static DisplayImageOptions getLargeImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.banner_pic) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.banner_pic)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.banner_pic) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .delayBeforeLoading(300)
                // .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                // .displayer(new RoundedBitmapDisplayer(5))
                .build();// 构建完成

        return options;
    }

    /**
     * 加载圆形图片
     **/
    public static void LoadCircleImg(final Context context, String imgType, String src, final ImageView imageView) {
        String targetUrl = Utils.getString(src);
        String urlArray[] = targetUrl.split("/");
        StringBuilder urlBuilder = new StringBuilder();
        //url处理
        if (urlArray.length >= 2)
            urlBuilder.append(Constant.IMG_URL).append(urlArray[0]).append("/").append(imgType).append("/").append(urlArray[1]);

        Glide.with(context).load(urlBuilder.toString()).asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.circle_pic)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


}
