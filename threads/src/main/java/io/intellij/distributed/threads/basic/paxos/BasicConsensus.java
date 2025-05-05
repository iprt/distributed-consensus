package io.intellij.distributed.threads.basic.paxos;

import io.intellij.distributed.threads.basic.Consensus;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * BasicConsensus
 *
 * @author tech@intellij.io
 * @since 2025-05-05
 */
public class BasicConsensus implements Consensus {
    private final String name;
    private final List<String> otherNodes;

    private final AtomicLong proposalId = new AtomicLong(-1);
    private final AtomicReference<String> proposalValue = new AtomicReference<>(null);

    public BasicConsensus(String name, List<String> otherNodes) {
        this.name = name;
        this.otherNodes = otherNodes;
    }

    @Override
    public Consensus getConsensus() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> otherNodes() {
        return this.otherNodes;
    }

    @Override
    public long getProposalId() {
        return proposalId.get();
    }

    @Override
    public void setProposalId(long proposalId) {
        this.proposalId.set(proposalId);
    }

    @Override
    public String getProposalValue() {
        return proposalValue.get();
    }

    @Override
    public void setProposalValue(String proposalValue) {
        this.proposalValue.set(proposalValue);
    }
}
