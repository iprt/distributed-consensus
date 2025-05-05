package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.BasicNodeOperator;

import java.util.concurrent.BlockingQueue;

/**
 * ThreadNode
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public interface ThreadNode extends BasicNodeOperator {

    void startThread();

    void stopThread();

    BlockingQueue<MsgData> getMsgQueue();

    default void addMsg(MsgType msgType, Object msg) {
        getMsgQueue().add(new MsgData(msgType, msg));
    }
}
