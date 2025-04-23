package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.Accept;
import io.intellij.distributed.threads.basic.Accepted;
import io.intellij.distributed.threads.basic.NodeOperator;
import io.intellij.distributed.threads.basic.Prepare;
import io.intellij.distributed.threads.basic.Promise;
import io.intellij.distributed.threads.basic.paxos.BasicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * NodeThreadAdapter
 * <p>
 * 专门处理线程间的适配器
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class NodeThreadAdapter extends Thread implements NodeThread {
    private final Logger log;

    private final NodeOperator nodeOperator;
    private final BlockingQueue<MsgWrapper> msgQueue;

    private volatile boolean running = true;

    public NodeThreadAdapter(String name, List<String> all) {
        super("NodeThread-" + name);
        this.nodeOperator = new BasicNode(name, all);
        this.log = LoggerFactory.getLogger("NodeThreadAdapter-" + name);
        this.msgQueue = new LinkedBlockingQueue<>();
        NodeThreadManager.getInstance().registerNodeThread(this);
    }

    @Override
    public BlockingQueue<MsgWrapper> getMsgQueue() {
        return this.msgQueue;
    }

    @Override
    public void startThread() {
        this.start();
    }

    @Override
    public void stopThread() {
        this.running = false;
    }

    @Override
    public List<String> otherNodes() {
        return nodeOperator.otherNodes();
    }

    @Override
    public void run() {
        log.info("NodeThreadAdapter start");
        while (running) {
            try {
                MsgWrapper take = msgQueue.take();
                if (take.getType() == MsgType.STOP) {
                    log.info("NodeThreadAdapter stop message received");
                    break;
                }

                switch (take.getType()) {
                    case BROADCAST_PREPARE:
                        Prepare bPrepare = MsgWrapper.getPrepareMsg(take);
                        broadcastPrepare(bPrepare);
                        break;
                    case PREPARE:
                        Prepare prepare = MsgWrapper.getPrepareMsg(take);
                        handlePrepare(prepare);
                        break;
                    case PROMISE:
                        Promise promise = MsgWrapper.getPromiseMsg(take);
                        handlePromise(promise);
                        break;
                    case BROADCAST_ACCEPT:
                        Accept bAccept = MsgWrapper.getBroadcastAcceptMsg(take);
                        broadcastAccept(bAccept);
                        break;
                    case ACCEPT:
                        Accept accept = MsgWrapper.getAcceptMsg(take);
                        handleAccept(accept);
                        break;
                    case ACCEPTED:
                        Accepted accepted = MsgWrapper.getAcceptedMsg(take);
                        handleAccepted(accepted);
                        break;
                    default:
                        log.error("Unknown message type");
                }

            } catch (InterruptedException e) {
                log.error("NodeThreadAdapter interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.info("NodeThreadAdapter stop");
    }

    @Override
    public void broadcastPrepare(Prepare prepare) {
        NodeThreadManager.getInstance().broadcastPrepare(prepare, this.getName());
    }

    @Override
    public Promise handlePrepare(Prepare prepare) {
        Promise promise = this.nodeOperator.handlePrepare(prepare);
        NodeThreadManager.getInstance().responsePromise(this.getName(), promise);
        return null;
    }

    @Override
    public void handlePromise(Promise promise) {
        this.nodeOperator.handlePromise(promise);
    }

    @Override
    public void broadcastAccept(Accept accept) {
        NodeThreadManager.getInstance().broadcastAccept(accept, this.getName());
    }

    @Override
    public Accepted handleAccept(Accept accept) {
        Accepted accepted = this.nodeOperator.handleAccept(accept);
        NodeThreadManager.getInstance().responseAccepted(this.getName(), accepted);
        return null;
    }

    @Override
    public void handleAccepted(Accepted accepted) {
        this.nodeOperator.handleAccepted(accepted);
    }

}
