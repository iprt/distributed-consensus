package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.NodeOperator;

import java.util.concurrent.BlockingQueue;

/**
 * NodeThread
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public interface NodeThread extends NodeOperator {

    void startThread();

    void stopThread();

    BlockingQueue<MsgWrapper> getMsgQueue();

    default void addMsg(MsgType msgType, Object msg) {
        getMsgQueue().add(new MsgWrapper(msgType, msg));
    }
}
