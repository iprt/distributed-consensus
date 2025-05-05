package io.intellij.distributed.threads.basic.paxos;

import io.intellij.distributed.threads.basic.Consensus;
import io.intellij.distributed.threads.basic.ProposalNode;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BasicProposalNode 提案节点逻辑
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class BasicProposalNode implements Consensus, ProposalNode {
    private final Consensus consensus;
    private final Logger log;

    // 广播prepare后接收到的promise
    // 后续需要clear
    private final Map<Long, List<Promise>> promiseMap = new ConcurrentHashMap<>();

    public BasicProposalNode(Consensus consensus) {
        this.consensus = consensus;
        this.log = LoggerFactory.getLogger("BasicNode-" + consensus.getName());
    }

    // 满足多数人的原则，二阶段提交的提交节点，一定是作为提案节点
    // 提案节点可能接收到的决策节点Promise的返回集合
    // 1. 多数决策节点已经达成共识的值 超过 1/2 的 Promise(n,value)
    // 2. 多数决策节点还没有达成共识的值 超过 1/2 的 Promise(n,null)
    @Override
    public void handlePromise(Promise promise) {
        log.info("Promise message received: {}", promise);
        // todo
    }

    @Override
    public void handleAccepted(Accepted accepted) {
        log.info("Accepted message received: {}", accepted);
    }

    @Override
    public Consensus getConsensus() {
        return this.consensus;
    }
}