package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.Accept;
import io.intellij.distributed.threads.basic.Accepted;
import io.intellij.distributed.threads.basic.NodeOperator;
import io.intellij.distributed.threads.basic.Prepare;
import io.intellij.distributed.threads.basic.Promise;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NodeThreadManager
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
class NodeThreadManager {
    private static final NodeThreadManager INSTANCE = new NodeThreadManager();
    @Getter
    private final Map<String, NodeThread> nodeThreadMap = new ConcurrentHashMap<>();

    public void registerNodeThread(NodeThread nodeThread) {
        nodeThreadMap.put(nodeThread.getName(), nodeThread);
    }

    public void broadcast(MsgType msgType, Object msg, String from) {
        nodeThreadMap.forEach((name, nodeThread) -> {
            if (!name.equals(from)) {
                nodeThread.addMsg(msgType, msg);
            }
        });
    }

    public void broadcastPrepare(Prepare prepare, String from) {
        this.broadcast(MsgType.BROADCAST_PREPARE, prepare, from);
    }

    public void broadcastAccept(Accept accept, String from) {
        this.broadcast(MsgType.BROADCAST_ACCEPT, accept, from);
    }

    public void responsePromise(String from, Promise promise) {
        nodeThreadMap.get(from).addMsg(MsgType.PROMISE, promise);
    }

    public void responseAccepted(String from, Accepted accepted) {
        nodeThreadMap.get(from).addMsg(MsgType.ACCEPTED, accepted);
    }

    public void stopAll() {
        for (NodeOperator nodeThread : nodeThreadMap.values()) {
            if (nodeThread instanceof NodeThread thread) {
                thread.stopThread();
            }
        }
    }

    public static NodeThreadManager getInstance() {
        return INSTANCE;
    }

}
