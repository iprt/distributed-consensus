package io.intellij.distributed.threads.basic.paxos;

import io.intellij.distributed.threads.basic.Consensus;
import io.intellij.distributed.threads.basic.DecisionNode;
import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Prepare;
import io.intellij.distributed.threads.basic.message.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDecisionNode 决策节点逻辑
 *
 * @author tech@intellij.io
 * @since 2025-05-05
 */
public class BasicDecisionNode implements Consensus, DecisionNode {
    private final Consensus consensus;
    private final Logger log;

    public BasicDecisionNode(Consensus consensus) {
        this.consensus = consensus;
        this.log = LoggerFactory.getLogger("BasicNode-" + consensus.getName());
    }

    /**
     * <b>接受者</b>保证三个承诺（角色：提案节点+决策节点）
     * <p>
     * 1. 如果接收到的准备请求(Prepare)的提案编号，<b>小于等于</b>接受者已经响应的准备请求的提案编号，那么接受者将承诺不响应这个准备请求
     * <p>
     * 2. 如果接受请求中的提案的提案编号，小于接受者已经响应的准备请求的提案编号，那么接受者将承诺不通过这个提案
     * <p>
     * 3. 如果接受者之前有通过提案，那么接受者将承诺，会在准备请求的响应中，包含已经通过的最大编号的提案信息
     *
     * @param prepare 准备请求
     * @return Promise 接受者的响应
     */
    @Override
    public Promise processPrepare(Prepare prepare) {
        String from = prepare.from();
        long receivedProposalId = prepare.proposalId();
        log.info("Prepare message received|from={}|proposalId={}", from, receivedProposalId);
        // 提案节点判断的优先级 大于 决策节点判断的优先级

        // 作为决策节点：接收者首次接收到提案编号，直接通过
        if (consensus.getProposalId() < 0) {
            consensus.setProposalId(receivedProposalId);
            return new Promise(receivedProposalId, null, getName());
        }

        if (this.getProposalValue() == null) {    // 作为决策节点：还没有设置值
            // 首次设值 提案ID的处理，两阶段提交的关键，准备阶段+设置值的阶段
            if (receivedProposalId <= getProposalId()) {
                // 这种情况是作为提案节点广播了提案ID
                log.info("节点{} 广播了提案ID={} | 接收到旧的提案ID={}", getName(), getProposalId(), receivedProposalId);
                return null;
            } else {
                // 这种情况是作为提案节点广播了提案ID，但是还没有设置值，接收到了新的提案ID
                consensus.setProposalId(receivedProposalId);
                return new Promise(receivedProposalId, null, getName());
            }

        } else { // 作为决策节点： 存在设置的值（已经达成共识）
            return new Promise(getProposalId(), getProposalValue(), getName());
        }
    }

    @Override
    public Accepted processAccept(Accept accept) {
        setProposalId(accept.id());
        setProposalValue(accept.value());
        return new Accepted(accept.id(), accept.value(), getName());
    }

    @Override
    public Consensus getConsensus() {
        return this.consensus;
    }
}
