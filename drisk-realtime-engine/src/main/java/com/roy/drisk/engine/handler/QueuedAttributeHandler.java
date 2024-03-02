package com.roy.drisk.engine.handler;

import com.roy.drisk.engine.context.EngineContext;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 *  * 后处理事件处理器基类，此类型的处理器在前处理时在<code>ContextAttributes</code>中申请相应的{@link Queue}，
 *  * 在引擎处理时，若调用{@link EngineContext#sendEvent(Object)}发送事件，则在后处理时对接收到的
 *  * 事件进行处理。
 */
public abstract class QueuedAttributeHandler<E> implements EngineContextHandler, ContextEventHandler<E> {
    /**
     * 取得<code>ContextAttributes</code>的Key值
     *
     * @return Key
     */
    public abstract String getAttributeKey();

    /**
     * 初始化<code>ContextAttributes</code>中的值
     *
     * @param context EngineContext
     */
    @Override
    public void before(final EngineContext context) {
        Queue<E> values = new ConcurrentLinkedQueue<>();
        context.attr().put(getAttributeKey(), values);
    }

    /**
     * 处理事件队列中的事件
     *
     * @param context EngineContext
     * @param values  事件队列
     */
    protected abstract void doAfterProcess(final EngineContext context, Queue<E> values);

    /**
     * 处理完成后是否清除事件队列
     *
     * @return boolean
     */
    protected boolean removeAttributesAfterProcess() {
        return false;
    }

    @SuppressWarnings("unchecked")
    private Queue<E> getAttrValue(final EngineContext context) {
        Object data = context.attr().get(getAttributeKey());
        if (data == null || !(data instanceof Queue)) {
            return null;
        }
        return (Queue<E>) data;
    }

    /**
     * 取事件队列，调用{@link #doAfterProcess(EngineContext, Queue)}进行实际处理
     *
     * @param context EngineContext
     */
    @Override
    public void after(final EngineContext context) {
        Queue<E> values = getAttrValue(context);
        if (values != null) {
            doAfterProcess(context, values);
        }
        if (removeAttributesAfterProcess()) {
            context.attr().remove(getAttributeKey());
        }

    }

    /**
     * 处理事件，加入待处理队列
     *
     * @param context EngineContext
     * @param item    事件
     */
    @Override
    public void handleEvent(final EngineContext context, E item) {
        Queue<E> values = getAttrValue(context);
        if (values != null) {
            values.add(item);
        }
    }
}
