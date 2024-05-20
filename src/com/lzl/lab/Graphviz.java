package com.lzl.lab;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graphviz {

    private static final String TEMP_DIR = "C:\\Users\\王紫瑜\\Desktop\\软件工程\\experiment\\lab1\\lab1\\lab1\\temp";
// 定义临时文件存储目录

    private static final String DOT_EXECUTABLE_PATH = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
// 定义 Graphviz 中 dot 可执行文件的路径

    public static void generateGraph(String dotSource, String outputFilePath, String imageFormat) throws IOException {
        // 生成图像的主方法，接受 DOT 源码、输出文件路径和图像格式作为参数
        File dotFile = writeDotSourceToFile(dotSource);
        // 将 DOT 源码写入临时文件，并返回该临时文件对象
        if (dotFile != null) {
            // 如果 DOT 文件生成成功
            convertDotToImage(dotFile, outputFilePath, imageFormat);
            // 调用方法将 DOT 文件转换为图像
            // dotFile.delete(); // 删除临时 DOT 文件
        } else {
            System.err.println("Error: Failed to generate DOT file.");
            // 如果 DOT 文件生成失败，打印错误信息
        }
    }

    private static File writeDotSourceToFile(String dotSource) throws IOException {
        // 将 DOT 源码写入临时文件的方法
        File tempDotFile = Files.createTempFile(Paths.get(TEMP_DIR), "graph", ".dot").toFile();
        // 在指定目录下创建一个临时文件，用于存储 DOT 源码

        try (FileWriter writer = new FileWriter(tempDotFile)) {
            writer.write(dotSource);
            // 使用 FileWriter 将 DOT 源码写入临时文件
        }
        return tempDotFile;
        // 返回临时文件对象
    }

    private static void convertDotToImage(File dotFile, String outputFilePath, String imageFormat) throws IOException {
        // 将 DOT 文件转换为图像的方法
        String[] command = {DOT_EXECUTABLE_PATH, "-T" + imageFormat, dotFile.getAbsolutePath(), "-o", outputFilePath};
        // 构建命令数组，包含 dot 可执行文件路径、图像格式、输入 DOT 文件路径和输出图像文件路径
        ProcessBuilder builder = new ProcessBuilder(command);
        // 使用 ProcessBuilder 创建进程
        Process process = builder.start();
        // 启动进程
        try {
            int exitCode = process.waitFor();
            // 等待进程执行完成，并获取退出码
            if (exitCode != 0) {
                System.err.println("Error: Failed to convert DOT to image. Exit code: " + exitCode);
                // 如果退出码不为0，打印错误信息
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 捕获中断异常并恢复中断状态
            System.err.println("Error: Interrupted while converting DOT to image.");
            // 打印中断错误信息
        }
    }


    public static void showDirectedGraph(WordGraph wordGraph) {
        // 获取图的边权重映射
        Map<String, Integer> edgeWeights = wordGraph.getEdgeWeights();
        // 生成 DOT 源码
        String dotSource = generateDotSource(wordGraph.getAdjacencyList(), edgeWeights);

        String outputFilePath = "./picture/graph.png"; // 输出图像文件路径
        String imageFormat = "png"; // 图像格式

        try {
            // 调用 Graphviz.generateGraph 方法生成图像
            Graphviz.generateGraph(dotSource, outputFilePath, imageFormat);
            System.out.println("Graph generated successfully: " + outputFilePath); // 打印成功信息
        } catch (IOException e) {
            System.err.println("Error generating graph: " + e.getMessage()); // 打印错误信息
        }
    }

    private static String generateDotSource(Map<String, List<String>> adjacencyList, Map<String, Integer> edgeWeights) {
        // 生成 DOT 源码的方法
        StringBuilder dotSource = new StringBuilder();
        dotSource.append("digraph G {\n"); // 添加 DOT 语言的开头

        // 添加节点及其标签
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            dotSource.append("\t\"" + node + "\" [label=\"" + node + "\"];\n");
        }

        // 添加边及其权重
        Set<String> processedEdges = new HashSet<>(); // 用于跟踪已处理的边以避免重复
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            List<String> adjacentNodes = entry.getValue();
            for (String adjNode : adjacentNodes) {
                String edge = node + " -> " + adjNode;
                int weight = edgeWeights.getOrDefault(edge, 1); // 从 edgeWeights 映射中获取边的权重，默认值为 1
                if (!processedEdges.contains(edge)) {
                    dotSource.append("\t\"" + node + "\" -> \"" + adjNode + "\" [label=\"" + weight + "\"];\n");
                    processedEdges.add(edge); // 将边标记为已处理
                }
            }
        }

        dotSource.append("}"); // 添加 DOT 语言的结尾
        return dotSource.toString(); // 返回生成的 DOT 源码
    }

    public static void generateGraphWithShortestPath(WordGraph wordGraph, String shortestPath, String outputFileName, String imageFormat) {
        // 生成包含最短路径的图像的方法
        Map<String, Integer> edgeWeights = wordGraph.getEdgeWeights();
        String dotSource = generateDotSourceWithShortestPath(wordGraph.getAdjacencyList(), edgeWeights, shortestPath);
        String outputFilePath = "./picture/" + outputFileName; // 输出图像文件路径

        try {
            generateGraph(dotSource, outputFilePath, imageFormat); // 调用 generateGraph 方法生成图像
            System.out.println("Graph with shortest path generated successfully: " + outputFilePath); // 打印成功信息
        } catch (IOException e) {
            System.err.println("Error generating graph with shortest path: " + e.getMessage()); // 打印错误信息
        }
    }

    private static String generateDotSourceWithShortestPath(Map<String, List<String>> adjacencyList, Map<String, Integer> edgeWeights, String shortestPath) {
        // 生成包含最短路径的 DOT 源码的方法
        StringBuilder dotSource = new StringBuilder();
        dotSource.append("digraph G {\n"); // 添加 DOT 语言的开头

        // 添加节点及其标签
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            dotSource.append("\t\"" + node + "\" [label=\"" + node + "\"];\n");
        }

        // 添加边及其权重，并突出显示最短路径中的边
        Set<String> processedEdges = new HashSet<>(); // 用于跟踪已处理的边以避免重复
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            List<String> adjacentNodes = entry.getValue();
            for (String adjNode : adjacentNodes) {
                String edge = node + " -> " + adjNode;
                int weight = edgeWeights.getOrDefault(edge, 1); // 从 edgeWeights 映射中获取边的权重，默认值为 1
                if (!processedEdges.contains(edge)) {
                    if (shortestPath.contains(edge)) {
                        // 如果边在最短路径中，设置颜色为红色
                        dotSource.append("\t\"" + node + "\" -> \"" + adjNode + "\" [label=\"" + weight + "\", color=\"red\"];\n");
                    } else {
                        dotSource.append("\t\"" + node + "\" -> \"" + adjNode + "\" [label=\"" + weight + "\"];\n");
                    }
                    processedEdges.add(edge); // 将边标记为已处理
                }
            }
        }

        dotSource.append("}"); // 添加 DOT 语言的结尾
        return dotSource.toString(); // 返回生成的 DOT 源码
    }


}

