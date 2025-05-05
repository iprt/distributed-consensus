package io.intellij.distributed.threads.basic.message;

import java.util.Date;

/**
 * Prepare 消息
 *
 * @author tech@intellij.io
 * @since 2025-04-22
 */
public record Prepare(long proposalId, String from) {
    /**
     * Prepare 消息
     */
    public static Prepare create(String from) {
        return new Prepare(new Date().getTime(), from);
    }

    /**
     * Prepare 消息
     *
     * @param proposalId 提案ID
     * @param from       发送者
     */
    public static Prepare create(long proposalId, String from) {
        return new Prepare(proposalId, from);
    }

}
