/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package com.harry.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.harry.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 类/接口描述
 *
 * @author Harry
 * @date 2016/8/15.
 */
public class RxJavaActivity extends Activity {

    private static final String tag = "RxJava";

    @OnClick(R.id.rxjava)
    void startRx() {
        observable.subscribe(subscriber);
    }

    @OnClick(R.id.just)
    void just() {
        Observable.just("Hello World")
            .map(new Func1<String, String>() {

            @Override
            public String call(String s) {
                return "subscriber: " + s;
            }
            })
            .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    @OnClick(R.id.scheduler)
    void scheduler() {
        Observable.just(1, 2, 3, 4)
            .subscribeOn(Schedulers.io())//subcribe 事件发生线程
            .observeOn(AndroidSchedulers.mainThread())//observe 事件消费线程，指定主线程
            .subscribe(
            new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    Log.e(tag, "number=" + integer+"/"+Thread.currentThread());
                }
            });
    }

    @OnClick(R.id.from)
    void from() {
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable = Observable.from(words);
        observable.subscribe(subscriber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
    }

    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("Hello");
            subscriber.onNext("Hi");
            subscriber.onNext("Alpha");
            subscriber.onCompleted();
        }
    });
    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onNext(String s) {
            Log.d(tag, "subscriber Item: " + s);
        }

        @Override
        public void onCompleted() {
            Log.d(tag, "Completed!subscriber");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(tag, "Error!subscriber");
        }
    };
}


