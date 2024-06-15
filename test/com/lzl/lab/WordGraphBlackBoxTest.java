/**
 * 黑盒测试类，用于测试WordGraph类的方法
 *
 * @Info:
 * - 1. 进行黑盒测试，即测试人员不了解WordGraph类中的方法的具体实现细节。
 * - 2. 只知道规格文档，基于输入输出的关系进行测试。
 * - 3. 测试范围包括calcShortestPath方法、queryBridgeWords方法、
 *        generateNewText方法和randomWalk方法。
 */
package com.lzl.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordGraphBlackBoxTest {

    // 声明WordGraph类的实例，用于测试
    private WordGraph wordGraph;

    // 在每个测试方法执行前都会运行此方法，用于初始化环境
    @BeforeEach
    void setup() {
        wordGraph = new WordGraph();
        // 使用固定的文本构建词图，为接下来的测试做准备
        wordGraph.buildGraph("explore strange new worlds to seek out new life and new lzl");
    }

    // 测试queryBridgeWords方法
    @Test
    void testQueryBridgeWords() {
        // 正常情况：测试两个词之间存在桥接词的情况
        assertEquals("The bridge words from explore to new are: strange",
                wordGraph.queryBridgeWords("explore", "new"));
        // 异常情况：测试两个词都不在图中的情况
        assertEquals("Neither enter nor tje is in the graph!",
                wordGraph.queryBridgeWords("enter", "tje"));
        // 边界情况：测试两个词相邻但没有桥接词的情况
        assertEquals("No bridge words from life to lzl!",
                wordGraph.queryBridgeWords("life", "lzl"));
    }

    // 测试generateNewText方法
    @Test
    void testGenerateNewText() {
        // 正常情况：使用桥接词生成新文本
        assertEquals("explore strange new worlds to seek out new life and new lzl",
                wordGraph.generateNewText("explore new worlds to seek new life new lzl"));
        // 异常情况：没有桥接词，直接返回原文本
        assertEquals("seek out new life", wordGraph.generateNewText("seek out new life"));
    }

    // 测试calcShortestPath方法
    @Test
    void testCalcShortestPath() {
        // 正常情况：测试计算两个词之间的最短路径
        assertEquals("The shortest path(s) from explore to lzl with length 3 are:\n" +
                        "explore -> strange -> new -> lzl\n",
                wordGraph.calcShortestPath("explore", "lzl"));
        // 异常情况：测试一个或两个词不在图中的情况
        assertEquals("No word2 in the graph!", wordGraph.calcShortestPath("explore", "unknown"));
    }

    // 测试randomWalk方法
    @Test
    void testRandomWalk() {
        // 多次运行随机游走，确保每次结果都非空
        for (int i = 0; i < 10; i++) {
            String walk = wordGraph.randomWalk();
            assertNotNull(walk);  // 断言结果不为null
            assertFalse(walk.isEmpty());  // 断言结果不为空字符串
        }
    }
}