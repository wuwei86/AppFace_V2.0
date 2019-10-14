package com.atisz.appface.utils;


import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 *
 * @author wuwei
 * @date 2019/3/6
 */

/*使用Rxjava2.0实现eventbus的功能，原本是想使用eventbus取代广播功能，后来发现使用eventbus需要导入eventbus库
为了进来少的导入第三方库，所以使用Rxjava来实现这个功能(Rxjava也是第三方库)*/

/*这个功能可以使用到线程中，接收线程中的数据*/

/*有背压处理（Backpressure）的 Rxbus*/
public class RxBusUtil {

    private final FlowableProcessor<Object> mBus;

    private RxBusUtil() {
        mBus = PublishProcessor.create().toSerialized();
    }

    private static class Holder {
        private static RxBusUtil instance = new RxBusUtil();
    }

    public static RxBusUtil getInstance() {
        return Holder.instance;
    }

    public void post(@NonNull Object obj) {
        mBus.onNext(obj);
    }

    public <T> Flowable<T> register(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public Flowable<Object> register() {
        return mBus;
    }

    public void unregisterAll() {
        //会将所有由mBus生成的Flowable都置completed状态后续的所有消息都收不到了
        mBus.onComplete();
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }
}
