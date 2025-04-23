package io.intellij.distributed.threads.basic;

/**
 * Promise
 *
 * @author tech@intellij.io
 * @since 2025-04-22
 */
public record Promise(long id, String value, String from) {
}
