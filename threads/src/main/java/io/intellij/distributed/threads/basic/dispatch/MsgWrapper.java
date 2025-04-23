package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.Accept;
import io.intellij.distributed.threads.basic.Accepted;
import io.intellij.distributed.threads.basic.Prepare;
import io.intellij.distributed.threads.basic.Promise;
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
public class MsgWrapper {
    private MsgType type;
    private Object data;

    public static Prepare getBroadcastPrepareMsg(MsgWrapper msg) {
        if (msg.getType() != MsgType.BROADCAST_PREPARE) {
            throw new IllegalArgumentException("Message type is not BROADCAST_PREPARE");
        }
        return (Prepare) msg.getData();
    }

    public static Prepare getPrepareMsg(MsgWrapper msg) {
        if (msg.getType() != MsgType.PREPARE) {
            throw new IllegalArgumentException("Message type is not PREPARE");
        }
        return (Prepare) msg.getData();
    }

    public static Promise getPromiseMsg(MsgWrapper msg) {
        if (msg.getType() != MsgType.PROMISE) {
            throw new IllegalArgumentException("Message type is not PROMISE");
        }
        return (Promise) msg.getData();
    }

    public static Accept getBroadcastAcceptMsg(MsgWrapper msg) {
        if (msg.getType() != MsgType.BROADCAST_ACCEPT) {
            throw new IllegalArgumentException("Message type is not BROADCAST_ACCEPT");
        }
        return (Accept) msg.getData();
    }

    public static Accept getAcceptMsg(MsgWrapper msg) {
        if (msg.getType() != MsgType.ACCEPT) {
            throw new IllegalArgumentException("Message type is not ACCEPT");
        }
        return (Accept) msg.getData();
    }

    public static Accepted getAcceptedMsg(MsgWrapper msg) {
        if (msg.getType() != MsgType.ACCEPTED) {
            throw new IllegalArgumentException("Message type is not ACCEPTED");
        }
        return (Accepted) msg.getData();
    }

}
