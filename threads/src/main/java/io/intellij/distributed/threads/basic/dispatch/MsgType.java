package io.intellij.distributed.threads.basic.dispatch;

/**
 * MsgType
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public enum MsgType {
    BROADCAST_PREPARE,
    PREPARE,
    PROMISE,
    BROADCAST_ACCEPT,
    ACCEPT,
    ACCEPTED,
    // thread stop
    STOP
}
