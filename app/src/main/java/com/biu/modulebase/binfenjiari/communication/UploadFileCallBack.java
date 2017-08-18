//package com.binfenjiari.communication;
//
//import org.apache.http.Header;
//
///**
// * 上传文件回调
// *
// * @author Lee
// *
// */
//public interface UploadFileCallBack {
//    /**
//     * 上传成功回调
//     *
//     * @param statusCode
//     *            状态码
//     * @param headers
//     *            头
//     * @param responseBody
//     *            返回信息
//     */
//    public abstract void onSuccess(int statusCode, Header[] headers,
//                                   byte[] responseBody);
//
//    /**
//     * 上传过程回调
//     *
//     * @param bytesWritten
//     *            已上传大小
//     * @param totalSize
//     *            文件总大小
//     */
//    public abstract void onProgress(int bytesWritten, int totalSize);
//
//    /**
//     * 上传失败回调
//     *
//     * @param statusCode
//     * @param headers
//     * @param responseBody
//     * @param error
//     */
//    public abstract void onFailure(int statusCode, Header[] headers,
//                                   byte[] responseBody, Throwable error);
//
//    /**
//     * 重新上传回调
//     *
//     * @param retryNo
//     */
//    public abstract void onRetry(int retryNo);
//}
