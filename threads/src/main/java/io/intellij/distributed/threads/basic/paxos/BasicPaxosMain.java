package io.intellij.distributed.threads.basic.paxos;

import io.intellij.distributed.threads.basic.dispatch.NodeThreadAdapter;
import io.intellij.distributed.threads.basic.dispatch.NodeThread;

import java.util.List;

/**
 * Main
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class BasicPaxosMain {

    public static void main(String[] args) {
        List<String> nodeNames = List.of("A", "B", "C");

        for (String nodeName : nodeNames) {
            NodeThread nodeThread = new NodeThreadAdapter(nodeName, nodeNames);
            nodeThread.startThread();
        }
    }

}
