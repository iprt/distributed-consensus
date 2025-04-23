package io.intellij.distributed.threads.basic.paxos;

import io.intellij.distributed.threads.basic.Accept;
import io.intellij.distributed.threads.basic.Accepted;
import io.intellij.distributed.threads.basic.NodeOperator;
import io.intellij.distributed.threads.basic.Prepare;
import io.intellij.distributed.threads.basic.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * BasicThreadNode
 * <p>
 * 真实的Basic Paxos 的逻辑处理
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class BasicNode implements NodeOperator {
    private final Logger log;
    private final String name;
    private final List<String> otherNodes;

    public BasicNode(String name, List<String> all) {
        this.log = LoggerFactory.getLogger("BasicNode-" + name);
        this.name = name;
        this.otherNodes = all.stream().filter(node -> !node.equals(name)).toList();
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
    public Promise handlePrepare(Prepare prepare) {
        log.info("Prepare message received: {}", prepare);
        // todo
        return null;
    }

    @Override
    public void handlePromise(Promise promise) {
        log.info("Promise message received: {}", promise);
        // todo
    }

    @Override
    public Accepted handleAccept(Accept accept) {
        log.info("Accept message received: {}", accept);
        // todo
        return null;
    }

    @Override
    public void handleAccepted(Accepted accepted) {
        log.info("Accepted message received: {}", accepted);
        // todo
    }

}
