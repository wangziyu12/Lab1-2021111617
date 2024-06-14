package com.lzl.lab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// 测试类，用于测试WordGraph类与Graphviz库交互的功能
class GraphvizTest {

    // 定义一个字符串，用于构建词图
    private final String txt = "i do not love you";
    // 创建WordGraph类的实例，用于操作词图
    private final WordGraph wordGraph = new WordGraph();

    /**
     * 测试方法：展示有向图
     * 利用给定的文本构建词图，并使用Graphviz库展示它
     */
    @Test
    void showDirectedGraph() {
        // 构建词图
        wordGraph.buildGraph(txt);
        // 使用Graphviz库展示构建的词图
        Graphviz.showDirectedGraph(wordGraph);
    }

    /**
     * 测试方法：生成带有最短路径的图形
     * 利用给定的文本构建词图，计算两个词之间的最短路径，
     * 并使用Graphviz库生成包含该路径的图形文件
     */
    @Test
    void generateGraphWithShortestPath() {
        // 构建词图
        wordGraph.buildGraph(txt);

        // 定义要查找最短路径的两个词
        String word1 = "explore";
        String word2 = "lzl";

        // 计算两个词之间的最短路径
        String result = wordGraph.calcShortestPath(word1, word2);

        // 定义输出文件的名称，格式为 "词1_to_词2.png"
        String outputFileName = word1 + "_to_" + word2 + ".png";

        // 使用Graphviz库生成包含最短路径的图形文件
        Graphviz.generateGraphWithShortestPath(wordGraph, result, outputFileName, "png");
    }
}