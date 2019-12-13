package com.zoom.netmiddleframework.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ResendManager {

    private static volatile ResendManager mResendManager;

    //用于排队
    private LinkedBlockingQueue<HttpCallBack> mHttpCallBackQueue;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private boolean mIsAllowed = false;
    private boolean mIsStart = false;
    private long mRsendNum = 0;

    private ResendManager() {
        mHttpCallBackQueue = new LinkedBlockingQueue<>();
        mThreadPoolExecutor = new ThreadPoolExecutor(1, 1, 0,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5), (RejectedExecutionHandler) null);

    }

    public static ResendManager getInstance() {
        synchronized (ResendManager.class) {
            if (mResendManager == null) {
                mResendManager = new ResendManager();
            }
        }
        return mResendManager;
    }

    public void init(boolean isAllowResend, long mRsendNum) {
        setAllowed(isAllowResend);
        this.mRsendNum = mRsendNum;
    }

    public synchronized void addResendData(HttpCallBack mHttpCallBack) {

        if (!ResendManager.getInstance().getAllowed()) {
            return;
        }

        /**
         * 如果未启动则启动重发模块
         */
        mHttpCallBackQueue.add(mHttpCallBack);

        if (!isStart()) {
            mThreadPoolExecutor.execute(thread);
        }
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            startResendManager();
        }
    });

    public void startResendManager() {

        //标注启动状态
        setStart(true);

        while(true) {
            if (!HttpRequestManager.getInstance().getNetConnectStatus()) {
                setStart(false);
                break;
            }
            try {
                //3秒取一次
                HttpCallBack mHttpCallBack = mHttpCallBackQueue.take();
                if (mHttpCallBack != null) {
                    //为0时默认为不限次数
                    if (mRsendNum != 0) {
                        if (mHttpCallBack.mResendData.getResendNum() > mRsendNum) {
                            continue;
                        }
                        mHttpCallBack.mResendData.setResendNum(mHttpCallBack.mResendData.getResendNum() + 1);
                    }
                } else {
                    continue;
                }
                sendResendData(mHttpCallBack);
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendResendData(HttpCallBack mHttpCallBack) {
       HttpRequestManager.getInstance().sendRequest(mHttpCallBack);
    }

    public boolean getAllowed() {
        return mIsAllowed;
    }

    public void setAllowed(boolean isAllowed) {
        mIsAllowed = isAllowed;
    }

    private boolean isStart() {
        return mIsStart;
    }

    private void setStart(boolean isStart) {
        mIsStart = isStart;
    }
}

