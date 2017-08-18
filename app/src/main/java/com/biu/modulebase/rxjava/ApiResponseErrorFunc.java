//package com.biu.modulebase.rxjava;
//
//
//
///**
// * @author Lee
// * @Title: {标题}
// * @Description:{拦截Api Excaption 让BaseSubscriber统一处理}
// * @date 2017/1/11
// */
//public  class ApiResponseErrorFunc<T> implements Func1<Throwable, Observable<T>> {
//        @Override
//        public Observable<T> call(Throwable t) {
//            return Observable.error(ExceptionHandle.handleException(t));
//        }
//
//
//}
