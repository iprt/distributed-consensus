package io.intellij.distributed.threads.basic.paxos;

import io.intellij.distributed.threads.basic.BasicNodeOperator;
import io.intellij.distributed.threads.basic.Consensus;
import io.intellij.distributed.threads.basic.DecisionNode;
import io.intellij.distributed.threads.basic.ProposalNode;
import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Prepare;
import io.intellij.distributed.threads.basic.message.Promise;

import java.util.List;

/**
 * BasicNode
 *
 * @author tech@intellij.io
 * @since 2025-05-05
 */
public class BasicNode implements BasicNodeOperator {
    private final Consensus consensus;
    private final ProposalNode proposalNode;
    private final DecisionNode decisionNode;

    public BasicNode(String name, List<String> otherNodes) {
        this.consensus = new BasicConsensus(name, otherNodes);
        proposalNode = new BasicProposalNode(consensus);
        decisionNode = new BasicDecisionNode(consensus);
    }

    @Override
    public Consensus getConsensus() {
        return this.consensus;
    }

    @Override
    public Promise processPrepare(Prepare prepare) {
        return decisionNode.processPrepare(prepare);
    }

    @Override
    public Accepted processAccept(Accept accept) {
        return decisionNode.processAccept(accept);
    }

    @Override
    public void handlePromise(Promise promise) {
        proposalNode.handlePromise(promise);
    }

    @Override
    public void handleAccepted(Accepted accepted) {
        proposalNode.handleAccepted(accepted);
    }

}
