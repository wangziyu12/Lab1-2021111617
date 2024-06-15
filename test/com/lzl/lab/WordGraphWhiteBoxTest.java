/**
 * 白盒测试类，用于测试WordGraph类的方法
 *
 * @Info:
 * - 1. 进行白盒测试，即测试人员了解WordGraph类中的方法的具体实现。
 * - 2. 测试目的是验证给定输入是否能产生预期的输出。
 * - 3. 测试范围包括calcShortestPath方法、queryBridgeWords方法、
 *        generateNewText方法和randomWalk方法。
 */
package com.lzl.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordGraphWhiteBoxTest {

    // 声明WordGraph类的实例，用于测试
    private WordGraph wordGraph;

    // 在每个测试方法执行前都会运行此方法，用于初始化环境
    @BeforeEach
    void buildGraph() {
        // 定义构建词图的文本
        String txt = "explore strange new worlds to seek out new life and new lzl";
        // 创建WordGraph实例并构建词图
        wordGraph = new WordGraph();
        wordGraph.buildGraph(txt);
    }

    // 测试queryBridgeWords方法
    @Test
    void queryBridgeWords() {
        // 测试两个词之间存在桥接词的情况
        assertEquals("The bridge words from explore to new are: strange",
                wordGraph.queryBridgeWords("explore", "new"));
        // 测试特殊情况和边界条件
        assertEquals("No bridge words from to to new!",
                wordGraph.queryBridgeWords("to", "new"));
        assertEquals("Neither enter nor tje is in the graph!",
                wordGraph.queryBridgeWords("enter", "tje"));
    }

    // 测试generateNewText方法
    @Test
    void generateNewText() {
        // 测试生成新文本的功能
        assertEquals("explore strange new worlds to seek out new life and new lzl",
                wordGraph.generateNewText("explore new worlds to seek new life new lzl"));
    }

    // 测试calcShortestPath方法
    @Test
    void calcShortestPath() {
        // 测试计算两个词之间的最短路径
        assertEquals("The shortest path(s) from explore to lzl with length 3 are:\n" +
                        "explore -> strange -> new -> lzl\n",
                wordGraph.calcShortestPath("explore", "lzl"));
        // 测试其他路径
        assertEquals("The shortest path(s) from explore to new with length 2 are:\n" +
                        "explore -> strange -> new\n",
                wordGraph.calcShortestPath("explore", "new"));
        assertEquals("The shortest path(s) from explore to worlds with length 3 are:\n" +
                        "explore -> strange -> new -> worlds\n",
                wordGraph.calcShortestPath("explore", "worlds"));
    }

    // 测试randomWalk方法
    @Test
    void randomWalk() {
        // 测试随机游走功能，确保每次调用都能产生非空字符串
        String walk1 = wordGraph.randomWalk();
        String walk2 = wordGraph.randomWalk();
        String walk3 = wordGraph.randomWalk();

        assertNotNull(walk1); // 断言结果不为null
        assertFalse(walk1.isEmpty()); // 断言结果不为空字符串
        assertNotNull(walk2);
        assertFalse(walk2.isEmpty());
        assertNotNull(walk3);
        assertFalse(walk3.isEmpty());
    }
}