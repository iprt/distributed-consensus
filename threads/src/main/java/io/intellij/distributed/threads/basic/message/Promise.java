package io.intellij.distributed.threads.basic.message;

/**
 * Promise
 *
 * @author tech@intellij.io
 * @since 2025-04-22
 */
public record Promise(long proposalId, String value, String from) {
}
