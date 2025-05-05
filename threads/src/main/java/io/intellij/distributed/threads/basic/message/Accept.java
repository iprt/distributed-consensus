package io.intellij.distributed.threads.basic.message;

/**
 * Accept
 *
 * @author tech@intellij.io
 * @since 2025-04-22
 */
public record Accept(long id, String value, String from) {

}
