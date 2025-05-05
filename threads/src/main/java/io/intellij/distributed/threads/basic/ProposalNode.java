package io.intellij.distributed.threads.basic;

import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Promise;

/**
 * 提案节点
 *
 * @author tech@intellij.io
 * @since 2025-05-05
 */
public interface ProposalNode {

    // 广播Prepare请求
    default void broadcastPrepare(long proposalId) {
    }

    // 处理Promise请求
    void handlePromise(Promise promise);

    // 广播Accept请求
    default void broadcastAccept(Accept accept) {
    }

    // 处理Accepted请求，达成共识
    void handleAccepted(Accepted accepted);
}
