package io.intellij.distributed.threads.basic;

import java.util.List;

/**
 * Consensus
 *
 * @author tech@intellij.io
 * @since 2025-04-25
 */
public interface Consensus {
    Consensus getConsensus();

    default String getName() {
        return getConsensus().getName();
    }

    default List<String> otherNodes() {
        return getConsensus().otherNodes();
    }

    default long getProposalId() {
        return getConsensus().getProposalId();
    }

    default void setProposalId(long proposalId) {
        getConsensus().setProposalId(proposalId);
    }

    default String getProposalValue() {
        return getConsensus().getProposalValue();
    }

    default void setProposalValue(String proposalValue) {
        getConsensus().setProposalValue(proposalValue);
    }

}
