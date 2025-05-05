package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.BasicNodeOperator;
import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Prepare;
import io.intellij.distributed.threads.basic.message.Promise;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NodeManager
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class NodeManager {
    private static final NodeManager INSTANCE = new NodeManager();
    @Getter
    private final Map<String, ThreadNode> nodeThreadMap = new ConcurrentHashMap<>();

    public void registerNodeThread(ThreadNode threadNode) {
        nodeThreadMap.put(threadNode.getName(), threadNode);
    }

    public void broadcast(MsgType msgType, Object msg, String from) {
        nodeThreadMap.forEach((name, threadNode) -> {
            if (!name.equals(from)) {
                threadNode.addMsg(msgType, msg);
            }
        });
    }

    void broadcastPrepare(Prepare prepare) {
        this.broadcast(MsgType.BROADCAST_PREPARE, prepare, prepare.from());
    }

    void broadcastAccept(Accept accept, String from) {
        this.broadcast(MsgType.BROADCAST_ACCEPT, accept, from);
    }

    void responsePromise(String from, Promise promise) {
        nodeThreadMap.get(from).addMsg(MsgType.PROMISE, promise);
    }

    void responseAccepted(String from, Accepted accepted) {
        nodeThreadMap.get(from).addMsg(MsgType.ACCEPTED, accepted);
    }

    public void stopAll() {
        for (BasicNodeOperator nodeThread : nodeThreadMap.values()) {
            if (nodeThread instanceof ThreadNode thread) {
                thread.stopThread();
            }
        }
    }

    public static NodeManager get() {
        return INSTANCE;
    }

}
