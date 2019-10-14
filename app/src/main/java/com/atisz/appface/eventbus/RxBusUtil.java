package com.atisz.appface.eventbus;


import com.luck.picture.lib.rxbus2.RxBus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 *
 * @author wuwei
 * @date 2019/3/6
 */

/*使用Rxjava1.0实现eventbus的功能，原本是想使用eventbus取代广播功能，后来发现使用eventbus需要导入eventbus库
为了进来少的导入第三方库，所以使用Rxjava来实现这个功能(Rxjava也是第三方库)*/

/*这个功能可以使用到线程中，接收线程中的数据*/
public class RxBusUtil {
    private Subject bus;

    RxBusUtil() {
        bus = new SerializedSubject(PublishSubject.create());
    }

    private static class BusSingleHolder {
        private static final RxBusUtil Instance = new RxBusUtil();
    }

    //单例模式
    public static RxBusUtil getInstance() {
        return BusSingleHolder.Instance;
    }

    /**
     * 发送消息 即调用所有观察者的onNext()方法
     * @param obj
     */
    public void send(Object obj) {
        bus.onNext(obj);//这里调用的是所有存在的observaber的onNext()方法
    }

    public <T> Observable<T> getObservable(Class<T> eventType) {
        return bus.ofType(eventType);//filter(..).cast(..)  包含过滤和转换类型两个动作，只发送出指定类型的消息
    }
}
