package com.roy.drisk.connector.service;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 实现此接口的类需要可以判断本身是否已经关闭。
 */
public interface ClosedStatusAware {
    boolean isClosed();
}
