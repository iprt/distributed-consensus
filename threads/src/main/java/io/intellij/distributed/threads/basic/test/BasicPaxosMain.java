package io.intellij.distributed.threads.basic.test;

import io.intellij.distributed.threads.basic.dispatch.NodeAdapter;
import io.intellij.distributed.threads.basic.dispatch.ThreadNode;

import java.util.List;

/**
 * BasicPaxosMain
 *
 * @author tech@intellij.io
 * @since 2025-04-23
 */
public class BasicPaxosMain {

    public static void main(String[] args) {
        List<String> nodeNames = List.of("A", "B", "C");

        for (String nodeName : nodeNames) {
            ThreadNode threadNode = new NodeAdapter(nodeName, nodeNames);
            threadNode.startThread();
        }
    }

}
