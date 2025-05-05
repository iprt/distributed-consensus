package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Prepare;
import io.intellij.distributed.threads.basic.message.Promise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * MsgWrapper
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class MsgData {
    private MsgType type;
    private Object data;

    public static long getBroadcastPrepareMsg(MsgData msg) {
        if (msg.getType() != MsgType.BROADCAST_PREPARE) {
            throw new IllegalArgumentException("Message type is not BROADCAST_PREPARE");
        }
        return (Long) msg.getData();
    }

    public static Prepare getPrepareMsg(MsgData msg) {
        if (msg.getType() != MsgType.PREPARE) {
            throw new IllegalArgumentException("Message type is not PREPARE");
        }
        return (Prepare) msg.getData();
    }

    public static Promise getPromiseMsg(MsgData msg) {
        if (msg.getType() != MsgType.PROMISE) {
            throw new IllegalArgumentException("Message type is not PROMISE");
        }
        return (Promise) msg.getData();
    }

    public static Accept getBroadcastAcceptMsg(MsgData msg) {
        if (msg.getType() != MsgType.BROADCAST_ACCEPT) {
            throw new IllegalArgumentException("Message type is not BROADCAST_ACCEPT");
        }
        return (Accept) msg.getData();
    }

    public static Accept getAcceptMsg(MsgData msg) {
        if (msg.getType() != MsgType.ACCEPT) {
            throw new IllegalArgumentException("Message type is not ACCEPT");
        }
        return (Accept) msg.getData();
    }

    public static Accepted getAcceptedMsg(MsgData msg) {
        if (msg.getType() != MsgType.ACCEPTED) {
            throw new IllegalArgumentException("Message type is not ACCEPTED");
        }
        return (Accepted) msg.getData();
    }

}
