package io.intellij.distributed.threads.basic.dispatch;

import io.intellij.distributed.threads.basic.BasicNodeOperator;
import io.intellij.distributed.threads.basic.Consensus;
import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Prepare;
import io.intellij.distributed.threads.basic.message.Promise;
import io.intellij.distributed.threads.basic.paxos.BasicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * NodeAdapter 线程间的适配器
 *
 * <p>
 * 处理广播请求
 * <p>
 * 代理basicNode的请求
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class NodeAdapter extends Thread implements ThreadNode {
    private final Logger log;

    private final BasicNodeOperator basicNode;
    private final BlockingQueue<MsgData> msgQueue;

    private volatile boolean running = true;

    public NodeAdapter(String name, List<String> all) {
        super("NodeThread-" + name);
        this.basicNode = new BasicNode(name, all);
        this.log = LoggerFactory.getLogger("NodeThreadAdapter-" + name);
        this.msgQueue = new LinkedBlockingQueue<>();
        NodeManager.get().registerNodeThread(this);
    }

    @Override
    public BlockingQueue<MsgData> getMsgQueue() {
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
    public Consensus getConsensus() {
        return basicNode;
    }

    @Override
    public void run() {
        log.info("NodeThreadAdapter start");
        while (running) {
            try {
                MsgData take = msgQueue.take();
                if (take.getType() == MsgType.STOP) {
                    log.info("NodeThreadAdapter stop message received");
                    break;
                }

                switch (take.getType()) {
                    case BROADCAST_PREPARE:
                        long proposalId = MsgData.getBroadcastPrepareMsg(take);
                        broadcastPrepare(proposalId);
                        break;
                    case PREPARE:
                        Prepare prepare = MsgData.getPrepareMsg(take);
                        processPrepare(prepare);
                        break;
                    case PROMISE:
                        Promise promise = MsgData.getPromiseMsg(take);
                        handlePromise(promise);
                        break;
                    case BROADCAST_ACCEPT:
                        Accept bAccept = MsgData.getBroadcastAcceptMsg(take);
                        broadcastAccept(bAccept);
                        break;
                    case ACCEPT:
                        Accept accept = MsgData.getAcceptMsg(take);
                        processAccept(accept);
                        break;
                    case ACCEPTED:
                        Accepted accepted = MsgData.getAcceptedMsg(take);
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

    /**
     * 广播Prepare请求, 对于于提案ID的处理
     *
     * @param proposalId 提案ID
     */
    @Override
    public void broadcastPrepare(long proposalId) {
        long currentProposalId = getProposalId();

        // 当前的提案ID小于等于0，说明是第一次广播Prepare请求
        if (currentProposalId <= 0) {
            setProposalId(proposalId);
            NodeManager.get().broadcastPrepare(Prepare.create(basicNode.getName()));
            return;
        }

        if (proposalId <= currentProposalId) {
            // 接受的提案ID <= 当前的提案ID,说明是旧的提案
            log.info("节点{} | 提案ID{}小于等于当前的提案ID{}，不需要广播Prepare请求", basicNode.getName(), proposalId, currentProposalId);
        } else {
            log.info("节点{} | 提案ID{}大于当前的提案ID{}，广播Prepare请求", basicNode.getName(), proposalId, currentProposalId);
            // 接受的提案ID大于当前的提案ID，说明是新的提案
            NodeManager.get().broadcastPrepare(Prepare.create(basicNode.getName()));
        }

    }

    @Override
    public Promise processPrepare(Prepare prepare) {
        Promise promise = this.basicNode.processPrepare(prepare);
        if (promise != null) {
            // 消息回复
            NodeManager.get().responsePromise(this.getName(), promise);
        }
        return null;
    }

    @Override
    public void handlePromise(Promise promise) {

    }

    @Override
    public void broadcastAccept(Accept accept) {
        NodeManager.get().broadcastAccept(accept, this.getName());
    }

    @Override
    public Accepted processAccept(Accept accept) {
        Accepted accepted = this.basicNode.processAccept(accept);
        NodeManager.get().responseAccepted(this.getName(), accepted);
        return null;
    }

    @Override
    public void handleAccepted(Accepted accepted) {
        this.basicNode.handleAccepted(accepted);
    }

}
