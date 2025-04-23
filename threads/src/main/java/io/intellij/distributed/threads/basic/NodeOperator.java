package io.intellij.distributed.threads.basic;

import java.util.List;

/**
 * NodeOperator
 *
 * @author tech@intellij.io
 * @since 2025-04-22
 */
public interface NodeOperator {

    // node name
    String getName();

    List<String> otherNodes();

    // 构造新的提案，广播Prepare请求
    default void broadcastPrepare(Prepare prepare) {
    }

    // 处理Prepare请求，返回 Promise(n,value)
    // 两种情况，1.首次设值 2.非首次设值
    Promise handlePrepare(Prepare prepare);

    // 处理Promise请求
    void handlePromise(Promise promise);

    // 广播Accept请求
    default void broadcastAccept(Accept accept) {
    }

    // 处理Accept请求，返回 Accepted(n,value)
    Accepted handleAccept(Accept accept);

    // 处理Accepted请求，达成共识
    void handleAccepted(Accepted accepted);

}
