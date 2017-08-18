package com.biu.modulebase.binfenjiari.datastructs;

import android.os.Environment;

import com.biu.modulebase.binfenjiari.BuildConfig;

import java.io.File;


public class Constant {

    /** Log Tag **/
    public static String TAG = "XiangQu";
    /** 本地缓存文件名 **/
    public static String PREFERENCE_NAME = "Biu_xq_Pref";

    public static String APP_NAME = "XiangQu";
    public static String APPNAME = "享去";
    public static String APK_NAME = "xiangqu.apk";
    // ***************************↓↓↓↓↓↓↓接口默认传参↓↓↓↓↓↓↓***********************************
    /** 安卓通讯渠道号 **/
    public static String ANDROID_CHANNEL = "200";
    /** 数据交互的平台验证 **/
    public static String KEY = "";
    /** 通讯版本 **/
    public static String VERSION = "1.0";

    public static String START_TIME = "1900-12-30 00:00:00";
    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的AlipayConfig基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String PARTNER = "";
    //	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串 可以是邮箱、手机号或者对应的2008开头的纯16位数字
    public static String SELLER = "";
    // 商户的私钥 - 用户自己上传的私钥
    public static String ALI_RSA_PRIVATE = "";

    // 支付宝的公钥，无需修改该值 - 支付宝合作者提供的默认的公钥，不是自己上传的那个公钥
    public static String ALI_RSA_PUBLIC = "";

    public static String s="signature007";

    /** 微信分享 **/
    public static final String WEIXIN_AppID = "wx013aad9217dedd99";
    public static final String WEIXIN_AppSecret = "c8ad8b1d626b309c3f3e3b7b271b11e7";
    /**QQ/QZone**/
    public static final String QQ_AppID = "1104758682";
    public static final String QQ_AppKEY = "h97lgfazyRUzXJKy";
    /**Sina**/
    public static final String SINA_AppID = "3443149913";
    public static final String SINA_AppKEY = "2d6bac14bc37989170ba9ab6214f06c3";

    // ***************************↓↓↓↓↓↓↓本地缓存路径↓↓↓↓↓↓↓***********************************
    /************************************** 存储根路径 **************************************/
    public static final String STORAGE_HOME_PATH = Environment.getExternalStorageDirectory() + File.separator;
    /************************************** APP缓存路径 **************************************/
    public static final String SZTW_CACHE_PATH = STORAGE_HOME_PATH + APP_NAME;
    /**************************************
     * APP 错误日志存储路径
     **************************************/
    public static final String ERROR_LOG_PATH = STORAGE_HOME_PATH + APP_NAME + File.separator + "crash"
            + File.separator;
    /**************************************
     * APP 本地图片ImageLoader缓存路径
     **************************************/
    public static final String IMAGE_LOADER_CACHE_PATH = APP_NAME + File.separator + "imgCache";

    public static final String MY_IMAGE_LOADER_PATH = STORAGE_HOME_PATH + APP_NAME + File.separator + "mImg";

    // ***************************↓↓↓↓↓↓↓数据库↓↓↓↓↓↓↓*******************************************

    // ***************************↓↓↓↓↓↓↓接口↓↓↓↓↓↓↓*********************************************

//        public static final String BASE_URL = "http://112.74.219.38:8080/";
////    /**服務器主url**/
//    public static final String BASE_URL ="http://xq.cz001.com.cn:8080/";
    /**服務器接口主url**/
    public static final String SERVERURL = BuildConfig.BASE_URL +"xiangqu/app";
    /**上传文件url**/
    public static final String UPLOAD_URL= BuildConfig.BASE_URL+"xiangqu/appfile";
    /**图片根路径**/
    public static final String IMG_URL =BuildConfig.BASE_URL+"imgs/";  //正式
//    public static final String IMG_URL =BuildConfig.BASE_URL+"/";//测试

    /**上传错误日志**/
    public static final String UPLOAD_ERROR_LOG_URL =BuildConfig.BASE_URL+"/xiangqu/appunifiedfile?modelname=app_errorUpload";

    // －－－－－－－－－－－－－－－－－－－登录注册模块 Reg－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_REG ="Reg";
    /** 请求验证码 **/
    public static final String ACTION_VERIFICATION_CODE = "app_sendmobile";
    /** 注册 **/
    public static final String ACTION_REGISTER = "app_register";
    /** 登陆 **/
    public static final String ACTION_LOGIN = "app_login";
    /** 获取用户资料 **/
    public static final String ACTION_GET_USERINFO = "user_userInfo";
    /** 忘记密码 **/
    public static final String ACTION_FORGET_PWD = "app_findPassword";
    /** 上传Umeng devToken **/
    public static final String ACTION_UP_UMENG_TOKEN ="user_up_umeng";
    /** 登出 **/
    public static final String ACTION_LOGOUT = "user_app_logout";
    //  －－－－－－－－－－－－－－－－－－－公共模块－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_COMMON ="Commonality";
    /** 获取区域列表 **/
    public static final String ACTION_GET_AREA_LIST = "app_findDistrictList";
    /** 加载静h5界面**/
    public static final String ACTION_LOAD_STATIC_HTML = "app_findHtml";
    /** 获取举报原因 **/
    public static final String ACTION_GET_REPORT_REASON = "app_findReportList";
    /** 举报 **/
    public static final String ACTION_REPORT= "user_report";
    /** 获取分享信息 **/
    public static final String ACTION_GET_SHARE_INFO= "app_Share";
    /**   **/
    public static final String ACTION_FEEDBACK ="user_idea";
    //  －－－－－－－－－－－－－－－－－－－首页模块－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_BANNER ="Banner";
    public static final String MODEL_HOME="Index";
    /** 获取Banner列表 **/
    public static final String ACTION_BANNER ="app_findBannerList";
    /** 根据id获取banner详情 **/
    public static final String ACTION_BANNER_DETAIL ="app_findBannerInfo";
    /** 首页 **/
    public static final String ACTION_HOME_INFO= "app_newindex";
    public static final String ACTION_HOME_INFO2= "app?model=Index&action=app_newindex";
    //  －－－－－－－－－－－－－－－－－－－活动模块－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_ACTIVITY ="Activity";

    public static final String MODEL_ACTIVITY_COMMENT="Comment";

    public static final String MODEL_ACTIVITY_COMMENT_DETAIL="Reply";

    /** 活动首页 **/
    public static final String ACTION_ACTIVITY_HOME= "app_findActivityList";
    /** 活动详情 **/
    public static final String ACTION_ACTIVITY_DETAIL= "app_findActivityInfo";
    /** 获取活动评论列表 **/
    public static final String ACTION_ACTIVITY_COMMENT_LIST= "app_findActivityCommentList";
    /** 活动主评论回复列表（评论详情） **/
    public static final String ACTION_ACTIVITY_COMMENT_DETAIL= "app_findActivityReplyList";
    /** 评论活动 **/
    public static final String ACTION_ACTIVITY_COMMENT= "user_commentActivity";
    /** 活动 删除评论 **/
    public static final String ACTION_ACTIVITY_COMMENT_DELETE= "user_deleteActivityComment";
    /** 活动 回复评论 **/
    public static final String ACTION_ACTIVITY_COMMENT_REPLY= "user_replyCommentActivity";
    /** 活动 删除回复 **/
    public static final String ACTION_ACTIVITY_COMMENT_REPLY_DELETE= "user_deleteActivityReply";
    /** 活动 点赞 **/
    public static final String ACTION_ACTIVITY_LIKE= "user_likeActivity";
    /** 活动 收藏 **/
    public static final String ACTION_ACTIVITY_COLLECT= "biu_activity_collect";
    /** 活动 报名 **/
    public static final String ACTION_ACTIVITY_JOIN= "user_joinActivity";
    /**获取评价项列表**/
    public static final String ACTION_GET_EVALUATE_LIST="app_findActivityEvaluateList";
    //  －－－－－－－－－－－－－－－－－－－基地模块－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_PLACE ="Base";

    public static final String MODEL_PLACE_COMMENT="Base_comment";

    public static final String MODEL_PLACE_COMMENT_DETAIL="Base_reply";

    /** 基地首页 **/
    public static final String ACTION_PLACE_HOME= "app_findBaseList";
    /** 基地详情 **/
    public static final String ACTION_PLACE_DETAIL= "app_findBaseInfo";
    /** 获取基地评论列表 **/
    public static final String ACTION_PLACE_COMMENT_LIST= "app_findBaseCommentList";
    /** 基地主评论回复列表（评论详情） **/
    public static final String ACTION_PLACE_COMMENT_DETAIL= "app_findBaseReplyList";
    /** 评论基地 **/
    public static final String ACTION_PLACE_COMMENT= "user_commentBase";
    /** 基地 删除评论 **/
    public static final String ACTION_PLACE_COMMENT_DELETE= "user_deleteBaseComment";
    /** 基地 回复评论 **/
    public static final String ACTION_PLACE_COMMENT_REPLY= "user_replyCommentBase";
    /** 基地 删除回复 **/
    public static final String ACTION_PLACE_COMMENT_REPLY_DELETE= "user_deleteBaseReply";
    /** 基地 点赞 **/
    public static final String ACTION_PLACE_LIKE= "user_likeBase";
    /** 基地 收藏 **/
    public static final String ACTION_PLACE_COLLECT= "user_collectBase";
    /** 根据基地获取活动列表 **/
    public static final String ACTION_GET_ACTIVITY_BY_PLACE= "app_findActivityListForBase";
    //  －－－－－－－－－－－－－－－－－－交流模块－－－－－－－－－－－－－－－－－－－－
    //新鲜事
    public static final String MODEL_NEWS ="News";
    /** 新鲜事首页 **/
    public static final String ACTION_NEWS_HOME= "app_findNewsList";
    /** 新鲜事详情 **/
    public static final String ACTION_NEWS_DETAIL ="app_findNewsInfo";
    /** 获取新鲜事评论列表 **/
    public static final String ACTION_NEWS_COMMENT_LIST ="app_findNewsCommentList";
    /** 获取新鲜事主评论回复列表 **/
    public static final String ACTION_NEWS_COMMENT_REPLY_LIST ="app_findNewsReplyList";
    /** 发布新鲜事 **/
    public static final String ACTION_NEWS_PUBLISH ="user_releaseNews";
    /** 新鲜事点赞 **/
    public static final String ACTION_NEWS_LIKE="user_likeNews";
    /** 评论新鲜事 **/
    public static final String ACTION_NEWS_COMMENT="user_commentNews";
    /** 删除新鲜事 **/
    public static final String ACTION_NEWS_DELETE="user_deleteNews";
    /** 删除评论 **/
    public static final String ACTION_NEWS_COMMENT_DELETE= "user_deleteNewsComment";
    /** 回复评论 **/
    public static final String ACTION_NEWS_COMMENT_REPLY= "user_replyCommentNews";
    /** 删除回复 **/
    public static final String ACTION_NEWS_COMMENT_REPLY_DELETE= "user_deleteNewsReply";
    //问答
    public static final String MODEL_QUESTION ="Questions";
    /** 问答首页 **/
    public static final String ACTION_QUESTION_HOME= "app_findQuestionsList";
    /** 问答详情 **/
    public static final String ACTION_QUESTION_DETAIL ="app_findQuestionsInfo";
    /** 获取问答评论列表 **/
    public static final String ACTION_QUESTION_COMMENT_LIST ="app_findQuestionsCommentList";
    /** 获取问答主评论回复列表 **/
    public static final String ACTION_QUESTION_COMMENT_REPLY_LIST ="app_findQuestionsReplyList";
    /** 评论问答 **/
    public static final String ACTION_QUESTION_COMMENT="user_commentQuestions";
    /** 删除问答 **/
    public static final String ACTION_QUESTION_DELETE="user_deleteQuestions";
    /** 发布问答 **/
    public static final String ACTION_QUESTION_PUBLISH ="user_releaseQuestions";
    /** 删除评论 **/
    public static final String ACTION_QUESTION_COMMENT_DELETE= "user_deleteQuestionsComment";
    /** 回复评论 **/
    public static final String ACTION_QUESTION_COMMENT_REPLY= "user_replyCommentQuestions";
    /** 删除回复 **/
    public static final String ACTION_QUESTION_COMMENT_REPLY_DELETE= "user_deleteQuestionsReply";
    //投票
    public static final String MODEL_VOTE ="Vote";
    /** 投票列表 **/
    public static final String ACTION_VOTE_HOME ="app_findVoteList";
    /** 投票详情 **/
    public static final String ACTION_VOTE_DETAIL ="app_findVoteInfo";
    /** 搜索投票 **/
    public static final String ACTION_VOTE_SEARCH ="app_findVoteProject";
    /** 进行投票 **/
    public static final String ACTION_DO_VOTE ="user_applyVote";
    /** 资讯详情 **/
    public static final String ACTION_VOTE_FINDINFOMATIONINFO = "app_findInfomationInfo";
    /** 投票项详情 **/
    public static final String ACTION_VOTE_FINDVOTEPROJECTINFO = "app_findVoteProjectInfo";

    //圈子
    public static final String MODEL_CIRCLE="Circle";
    /**圈子首页**/
    public static final String ACTION_CIRCLE_MAIN="app_findCircleIndex";
    /**获取圈子一级大类**/
    public static final String ACTION_CIRCLE_FIRST_CLASS="app_findBigCirleList";
    /**根据一级大类获取圈子**/
    public static final String ACTION_GET_CIRCLE_LIST="app_findCircleList";
    /**圈子搜索**/
    public static final String ACTION_SEARCH_CIRCLE="app_searchCircleList";
    /**圈子详情**/
    public static final String ACTION_GET_CIRCLE_DETAIL="app_findCircleInfo";
    /**加入退出圈子**/
    public static final String ACTION_JOIN_CIRCLE="user_joinCircle";
    /**创建圈子**/
    public static final String ACTION_CREATE_CIRCLE="user_addCircle";
    /**获取敏感词汇**/
    public static final String ACTION_GET_KEY_LIST="app_findKeyList";

    //我的圈子
    public static final String MODEL_CIRCLE_MANAGE="CircleManage";
    /**获取我的圈子一级大类**/
    public static final String ACTION_MY_CIRCLE_FIRST_CLASS="user_findMyBigcircleList";
    /**根据一级大类获取我的圈子**/
    public static final String ACTION_GET_MY_CIRCLE_LIST="user_findMyCircleList";
    /**我的圈子搜索**/
    public static final String ACTION_SEARCH_MY_CIRCLE="user_searchMyCircleList";
    /**进入圈子管理获取圈子信息**/
    public static final String ACTION_GET_MY_CIRCLE_DETAIL="user_managerCircle";
    /**新增公告**/
    public static final String ACTION_ADD_NOTICE="user_addAnnounce";
    /**获取公告列表**/
    public static final String ACTION_GET_NOTICE_LIST="user_findAnnounceList";
    /**删除公告**/
    public static final String ACTION_DELETE_NOTICE="user_deleteAnnounce";
    /**获取圈子成员列表(搜索成员)**/
    public static final String ACTION_GET_MEMEBER_LIST="user_findCircleUserList";
    /**获取圈子黑名单*/
    public static final String ACTION_GET_CIRCLE_BLACK_LIST="user_findCircleBlacklist";
    /**设置取消精华帖**/
    public static final String ACTION_SET_ESSENCE_POST="user_essencePost";
    /**设置取消推荐帖**/
    public static final String ACTION_SET_RECOMMEND_POST="user_commendPost";
    /**成员操作**/
    public static final String ACTION_MEMEBER_OPERATE="user_circleUserManager";
    /**修改圈子**/
    public static final String ACTION_UPDATE_CIRCLE="user_editCircle";


    //帖子
    public static final String MODEL_POST="Circle_Post";
    /**根据圈子id获取帖子列表**/
    public static final String ACTION_GET_POST_LIST="app_findPostList";
    /**发布帖子**/
    public static final String ACTION_POST_PUBLISH="user_releasePost";
    /**获取精华帖**/
    public static final String ACTION_GET_ESSENCE_POST_LIST="app_findEssencePostList";
    /**搜索帖子**/
    public static final String ACTION_POST_SEARCH="app_searchPostList";
    /**帖子详情**/
    public static final String ACTION_POST_DETAIL="app_postInfo";
    /** 帖子点赞 **/
    public static final String ACTION_POST_LIKE="user_likePost";
    /** 获取帖子评论列表 **/
    public static final String ACTION_POST_COMMENT_LIST ="app_findPostCommentList";
    /** 获取帖子主评论回复列表 **/
    public static final String ACTION_POST_COMMENT_REPLY_LIST ="app_findPostReplyList";
    /** 评论帖子 **/
    public static final String ACTION_POST_COMMENT="user_commentPost";
    /** 删除帖子 **/
    public static final String ACTION_POST_DELETE="user_deletePost";
    /** 删除评论 **/
    public static final String ACTION_POST_COMMENT_DELETE= "user_deletePostComment";
    /** 回复评论 **/
    public static final String ACTION_POST_COMMENT_REPLY= "user_replyCommentPost";
    /** 删除回复 **/
    public static final String ACTION_POST_COMMENT_REPLY_DELETE= "user_deletePostReply";

    //我的消息模块
    public static final String MODEL_MESSAGE="Message";
    /** 我的消息首页 **/
    public static final String ACTION_MESSAGE_INDEX ="user_messageIndex";
    /** 查看各个具体的消息列表 **/
    public static final String ACTION_GET_MESSAGE_LIST ="user_messageList";
    /** 查看消息**/
    public static final String ACTION_SEE_MESSAGE ="user_showMessage";
    /** 删除单个消息**/
    public static final String ACTION_DELETE_MESSAGE ="user_deleteSingleMessage";
    /** 清空所有消息**/
    public static final String ACTION_DELETE_ALL_MESSAGE ="user_deleteAllMessage";

    //  －－－－－－－－－－－－－－－－－－個人中心－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_PERSONAL ="PeopleInfo";
    public static final String MODEL_SCHOOL ="School";
    public static final String MODEL_COLLECTION ="Collection";
    /**删除预约model**/
    public static final String MODEL_DELETE_MY_ACTIVITY="Appointment";
    /** 修改头像 **/
    public static final String ACTION_UPDATE_HEAD= "user_updateHead";
    /** 获取学校列表 **/
    public static final String ACTION_GET_SCHOOL_LIST= "app_schoolList";
    /** 获取年级列表 **/
    public static final String ACTION_GET_GRADE_LIST= "app_gradeList";
    /** 修改用户信息 **/
    public static final String ACTION_UPDATE_USER_INFO= "user_updateUserInfo";
    /** 获取我的活动 **/
    public static final String ACTION_GET_MY_ACTIVITY="user_findMyActivityList";
    /**删除我预约的活动**/
    public static final String ACTION_DELETE_MY_ACTIVITY="user_deleteReservationActivity";
    /** 获取我的收藏 **/
    public static final String ACTION_GET_MY_COLLECTION="user_myCollect";
    /**评价**/
    public static final String MODEL_EVALUATE ="Evaluate";
    public static final String ACTION_EVALUATE ="user_commentJoinActivityList";

    /**他人个人中心**/
    public static final String MODEL_OTHER_INFO ="PeopleInfo";
    public static final String ACTION_GET_OTHER_INFO ="app_peopleInfo";



    //  －－－－－－－－－－－－－－－－－－排行－－－－－－－－－－－－－－－－－－－－
    public static final String MODEL_RANK ="Rank";
    /** 获取所有排行列表 **/
    public static final String ACTION_GET_ALL_RANK= "app_findRankingList";
    /** 获取好友排行列表 **/
    public static final String ACTION_GET_FRIEND_RANK= "user_findRankingList";

    public static final String MODEL_PAY="Pay";
    public static final String ACTION_GET_PAY_INFO= "user_findPayInfo";

    //版本更新
    public static final String MODEL_UPDATE="CircleManage";
    public static final String ACTION_UPDATE = "version_up";

    //  －－－－－－－－－－－－－－－－－－举报模块类型－－－－－－－－－－－－－－－－－－－－
    /**基地**/
    public static final int REPORT_PLACE=1;
    /**活动**/
    public static final int REPORT_ACTIVITY=2;
    /**新鲜事**/
    public static final int REPORT_NEWS=3;
    /**问答**/
    public static final int REPORT_QUESTION=4;
    /**视听**/
    public static final int REPORT_AUDIO=5;
    /**帖子**/
    public static final int REPORT_POST=6;
    /**基地举报**/
    public static final int REPORT_PLACE_COMMENT=7;
    /**活动举报**/
    public static final int REPORT_ACTIVITY_COMMENT=8;
    /**新鲜事举报**/
    public static final int REPORT_NEWS_COMMENT=9;
    /**问答举报**/
    public static final int REPORT_QUESTION_COMMENT=10;
    /**视听举报**/
    public static final int REPORT_AUDIO_COMMENT=11;
    /**帖子举报**/
    public static final int REPORT_POST_COMMENT=12;
    //  －－－－－－－－－－－－－－－－－－获取分享信息类型－－－－－－－－－－－－－－－－－－－－
    /**个人中心**/
    public static final int SHARE_PERSONAL_CENTER=1;
    /**基地**/
    public static final int SHARE_PLACE=2;
    /**活动**/
    public static final int SHARE_ACTIVITY=3;
    /**新鲜事**/
    public static final int SHARE_NEWS=4;
    /**投票**/
    public static final int SHARE_VOTE=5;
    /**问答**/
    public static final int SHARE_QUESTION=6;
    /**视频、语音**/
    public static final int SHARE_AUDIO=7;
    /**图文**/
    public static final int SHARE_PIC_AND_TEXT=8;
    /**帖子**/
    public static final int SHARE_POST=9;
    /**投票资讯**/
    public static final int SHARE_VOTE_BREAF_INFO=11;
    /**新投票详情**/
    public static final int SHARE_VOTE_PROJECT_INFO=10;
    /**banner**/
    public static final int SHARE_BANNER=12;
    /**投票项**/
    public static final int SHARE_VOTE_PROJECT=13;
    //  －－－－－－－－－－－－－－－－－－搜索类型－－－－－－－－－－－－－－－－－－－－
    public static final String SEARCH_TAG ="SEARCH_TAG";
    /**搜索活动**/
    public static final String SEARCH_EVENT ="SEARCH_EVENT";
    /**搜索基地**/
    public static final String SEARCH_JIDI ="SEARCH_JIDI";
    /**搜索视听**/
    public static final String SEARCH_AUDIO ="SEARCH_AUDIO";
    /**搜索投票**/
    public static final String SEARCH_VOTE ="SEARCH_VOTE";
    /**搜索所有圈子**/
    public static final String SEARCH_CIRCLE_ALL ="SEARCH_CIRCLE_ALL";
    /**搜索我的圈子**/
    public static final String SEARCH_CIRCLE_MY ="SEARCH_CIRCLE_MY";
    /**搜索帖子**/
    public static final String SEARCH_POST ="SEARCH_POST";
    //  －－－－－－－－－－－－－－－－－－删除类型－－－－－－－－－－－－－－－－－－－－
    public static final String DELETE="DELETE";
    /**新鲜事**/
    public static final String DELETE_NEWS="DELETE_NEWS";
    /**问答**/
    public static final String DELETE_QUESTION="DELETE_QUESTION";
    /**帖子**/
    public static final String DELETE_POST="DELETE_POST";
    //  －－－－－－－－－－－－－－－－－－圈子成员操作－－－－－－－－－－－－－－－－－－－－
    /**转让圈子**/
    public static final String MEMEBER_OPERATE_TRANSFER_CIRCLE ="1";
    /**设置管理员**/
    public static final String MEMEBER_OPERATE_SET_MANAGER ="2";
    /**取消管理员**/
    public static final String MEMEBER_OPERATE_CANCLE_MANAGER ="3";
    /**移除成员**/
    public static final String MEMEBER_OPERATE_MOVE_OUT ="4";
    /**拉黑*/
    public static final String MEMEBER_OPERATE_PULL_BLACK ="5";
    /**取消拉黑*/
    public static final String MEMEBER_OPERATE_CANCLE_PULL_BLACK ="6";

    /** 图片单选 **/
    public static final String SINGLE_CHOICE_IMG = "SINGLE_CHOICE_IMG";
    /** ListView加载数据类型(刷新) **/
    public static final int LIST_REFRESH = 1;
    /** ListView加载数据类型(加载更多) **/
    public static final int LIST_LOAD_MORE = 2;
    /** 拍照 **/
    public static final int CAPTURE_PHOTO = 3;
    /** 相册选择 **/
    public static final int CHOICE_PHOTO = 4;
    /** 图片压缩 **/
    public static final int COMPRESS_IMG = 5;
    /** 图片裁剪 **/
    public static final int CROP_IMG = 6;
    /** 上传的照片最大数 **/
    public static final int PREVIEW_IMG_MAX_NUM = 9;

    /**图片种类，用于加载图片是url拼接*/
    public static final String IMG_SOURCE="source";
    public static final String IMG_COMPRESS="compress";
    public static final String IMG_THUMBNAIL="thumbnail";

    //----------------------获取静态html url 类型------------------------------
    /**用户协议**/
    public static final String LOAD_POLICY_HTML ="2";
    /**发帖必读**/
    public static final String LOAD_POST_READ_HTML ="4";


    /**
     * Id to identify a camera permission request.
     */
    public static final int REQUEST_CAMERA = 100;


    public static final String KEY_TYPE="type";
    public static final String KEY_ID="id";
    public static final String KEY_ID_CHILD="id_child";
    public static final String KEY_POSITION="position";
    public static final String KEY_PARENT_POSITION="parent_position";
    public static final String KEY_CHILD_POSITION="child_position";
    public static final String KEY_DATA="data";
    public static final String KET_IS_DELETE_ALL="key_delete_all";
    public static final String KEY_ARGS="key_args";
    public static final String KEY_NAME_ARGS="key_name_args";
    public static final String KET_VALUE_ARGS="key_value_args";
    public static final String KEY_TO_NAME="to_name";
    public static final String KEY_MODEL="model";
    public static final String KEY_ACTION="action";
    public static final String KEY_COMMENT="comment";
    public static final String KEY_REPLY_COUNT="reply_count";
    public static final String KEY_COMMENT_CHANGED_COUNT="comment_changed_count";
    public static final String KEY_COMMENT_OPREATE_TYPE="comment_operate_type";
    public static final String KEY_UPLOAD_DATA="data";

    public static final String KEY_REPORT_TYPE="report_type";

    public static final String KEY_VIDEO_URL="video_url";
    public static final String KEY_VIDEO_PRE_SEEK_TO_POSITION="video_pre_seek_to_position";

    public static final String KEY_MODEL_COMMENT_DETAIL="model_comment_detail";
    public static final String KEY_ACTION_COMMENT_DETAIL="action_comment_detail";

    public static final String KEY_SEARCH_ARGS="search_args";
    public static final String KEY_SEARCH_ARGS_TITLE="search_args_title";
    public static final String KEY_SEARCH_ARGS_DISTRICTID="search_args_districtId";

    public static final String
            KEY_MODEL_DELETE_COMMENT_COMMENT_DETAIL="model_delete_comment_comment_detail";
    public static final String
            KEY_ACTION_DELETE_COMMENT_COMMENT_DETAIL="action_delete_comment_comment_detail";

    public static final String
            KEY_MODEL_DELETE_REPLY_COMMENT_DETAIL="model_delete_reply_comment_detail";
    public static final String
            KEY_ACTION_DELETE_REPLY_COMMENT_DETAIL="action_delete_reply_comment_detail";


    public static final String KEY_LOCATION_LATITUDE="location_latitude";
    public static final String KEY_LOCATION_LONGITUDE="location_longitude";
    public static final String KEY_MAP_TARGET="map_target";


    public static final int REQUEST_COMMENT_CHANGED_STATUS=8;

    //---------------------------------av--------------------------------------
    public static final String MODEL_AV ="Vedio";
    /** 视听列表 **/
    public static final String ACTION_AV_LIST= "app_findVedioList";
    /** 视听详情 **/
    public static final String ACTION_AV_DETAIL= "app_findVedioInfo";

    /** 视听评论列表 **/
    public static final String ACTION_AV_COMMENT_LIST= "app_findVedioCommentList";

    /** 视听主评论详情 **/
    public static final String ACTION_AV_COMMENT_DETAIL= "app_findVedioReplyList";

    /** 评论视听**/
    public static final String ACTION_AV_COMMENT= "user_commentVedio";

    /** 回复视听**/
    public static final String ACTION_AV_REPLY= "user_replyCommentVedio";

    /** 删除评论**/
    public static final String ACTION_AV_DELETE_COMMENT= "user_deleteVedioComment";

    /** 删除回复**/
    public static final String ACTION_AV_DELETE_REPLY= "user_deleteVedioReply";

    /** 点赞**/
    public static final String ACTION_AV_LIKE= "user_likeVedio";

    /** 收藏**/
    public static final String ACTION_AV_COLLECT= "user_collectVedio";
}
