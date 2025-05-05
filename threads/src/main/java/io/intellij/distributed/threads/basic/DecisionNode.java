package io.intellij.distributed.threads.basic;

import io.intellij.distributed.threads.basic.message.Accept;
import io.intellij.distributed.threads.basic.message.Accepted;
import io.intellij.distributed.threads.basic.message.Prepare;
import io.intellij.distributed.threads.basic.message.Promise;

/**
 * 决策节点
 *
 * @author tech@intellij.io
 * @since 2025-05-05
 */
public interface DecisionNode {

    /**
     * <b>接受者</b>保证三个承诺
     * <p>
     * 1. 如果准备请求的提案编号，<b>小于等于</b>接受者已经响应的准备请求的提案编号，那么接受者将承诺不响应这个准备请求
     * <p>
     * 2. 如果接受请求中的提案的提案编号，小于接受者已经响应的准备请求的提案编号，那么接受者将承诺不通过这个提案
     * <p>
     * 3. 如果接受者之前有通过提案，那么接受者将承诺，会在准备请求的响应中，包含已经通过的最大编号的提案信息
     *
     * @param prepare 准备请求
     * @return Promise 接受者的响应
     */
    Promise processPrepare(Prepare prepare);

    // 处理Accept(n,value)请求，返回 Accepted(n,value)
    Accepted processAccept(Accept accept);

}
