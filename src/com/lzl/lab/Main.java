/**
 * @Author: LZL
 * @Date: 2024/5/14
 * @编译命令：javac -encoding UTF-8 -d out src/com/lzl/lab/*.java
 * @运行命令：java -cp out com.lzl.lab.Main
 */

package com.lzl.lab;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

//      TODO: 从命令行接受参数，找不到生成的.dot文件;还不能写中文，什么鬼
//            已解决：-encoding UTF-8;dot文件是因为后边写了个删除临时文件的谜之操作

        String filePath = "./src/test.txt";     // 指定WordGraph类将要读取和处理的数据文件的位置
        WordGraph wordGraph = new WordGraph();  // 创建WordGraph实例


        try {
            String processedContent = readFileContent(filePath);
            wordGraph.buildGraph(processedContent);     // 使用处理后的内容构建图结构
            Graphviz.showDirectedGraph(wordGraph);      // 显示构建的有向图

            boolean exit = false;
            Scanner scanner = new Scanner(System.in);
            while (!exit) {
                System.out.println("Choose an option:");
                System.out.println("1. Query Bridge Words");
                System.out.println("2. Generate New Text");
                System.out.println("3. Calculate Shortest Path");
                System.out.println("4. Calculate Shortest Path from one word");
                System.out.println("5. Random Walk");
                System.out.println("6. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        queryBridgeWords(wordGraph, scanner);
                        break;
                    case 2:
                        generateNewText(wordGraph, scanner);
                        break;
                    case 3:
                        calculateShortestPath(wordGraph, scanner);
                        break;
                    case 4:
                        printShortestDistancesFromWord(wordGraph, scanner);
                        break;
                    case 5:
                        randomWalk(wordGraph, scanner);
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }

    }

    /**
     * 读取指定文件的内容并进行处理。
     * @param filePath 文件的路径。
     * @return 处理后的文件内容。
     * @throws IOException 如果读取文件时出现错误。
     */
    private static String readFileContent(String filePath) throws IOException {
        // 创建StringBuilder对象，用于高效拼接字符串
        StringBuilder content = new StringBuilder();
        // 使用BufferedReader读取文件，提高读取效率
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        // 循环读取文件的每一行，直到文件结束
        while ((line = reader.readLine()) != null) {
            // 将读取的每行内容追加到StringBuilder对象，并添加换行符
            content.append(line).append("\n");
        }
        // 关闭文件读取流
        reader.close();

        // 将读取到的内容转换为一个字符串，然后执行以下处理步骤：
        // 1. 替换所有非字母和非空白字符为单个空格。
        // 2. 替换连续的空白字符为单个空格。
        // 3. 将所有字符转换为小写。
        String processedContent = content.toString().replaceAll("[^a-zA-Z\\s]", " ")
                .replaceAll("\\s+", " ")
                .toLowerCase();

        // 返回处理后的文本内容
        return processedContent;
    }

    /**
     * 查询两个单词之间的桥接词。
     * @param wordGraph 用于管理和查询单词图的对象。
     * @param scanner 用于从控制台接收用户输入的扫描器对象。
     */
    private static void queryBridgeWords(WordGraph wordGraph, Scanner scanner) {
        System.out.println("Enter two words to find bridge words:");
        String word1 = scanner.nextLine();
        String word2 = scanner.nextLine();
        // 调用WordGraph对象的queryBridgeWords方法，查询两个单词之间的桥接词
        String bridgeWords = wordGraph.queryBridgeWords(word1, word2);
        // 输出查询结果
        System.out.println(bridgeWords);
        // System.out.println("Bridge words between " + word1 + " and " + word2 + ": " + bridgeWords);
    }

    /**
     * 基于用户输入的文本，使用WordGraph中的单词关系生成新的文本。
     * @param wordGraph WordGraph对象，提供生成新文本所需的单词图处理功能。
     * @param scanner Scanner对象，用于从控制台接收用户输入。
     */
    private static void generateNewText(WordGraph wordGraph, Scanner scanner) {
        System.out.println("Enter the source text:");
        // 使用Scanner对象读取用户从控制台输入的整行文本
        String sourceText = scanner.nextLine();
        // 调用WordGraph对象的generateNewText方法，根据原始文本生成新文本
        String newText = wordGraph.generateNewText(sourceText);
        System.out.println("Generated new text: " + newText);
    }

    /**
     * 计算并展示两个单词之间在单词图中的最短路径。
     * @param wordGraph WordGraph对象，提供对单词图的操作和查询功能。
     * @param scanner Scanner对象，用于从控制台接收用户输入。
     */
    private static void calculateShortestPath(WordGraph wordGraph, Scanner scanner) {
        System.out.println("Enter two words to calculate shortest path:");
        String word1 = scanner.nextLine();
        String word2 = scanner.nextLine();
        // 调用WordGraph对象的calcShortestPath方法，计算两个单词之间的最短路径
        String shortestPath = wordGraph.calcShortestPath(word1, word2);
        // 输出计算出的最短路径
        System.out.println(shortestPath);
        // 根据单词生成图形文件的名称
        String outputFileName = word1 + "_to_" + word2 + ".png";
        // 调用Graphviz的generateGraphWithShortestPath方法，生成包含最短路径的图形，并保存为PNG文件
        Graphviz.generateGraphWithShortestPath(wordGraph, shortestPath, outputFileName, "png");
        // System.out.println("Shortest path between " + word1 + " and " + word2 + ": " + shortestPath);
    }

    /**
     * 打印从一个单词到图中所有其他单词的最短路径。
     * @param wordGraph WordGraph对象，用于处理单词图数据。
     * @param scanner Scanner对象，用于从控制台读取用户输入。
     */
    private static void printShortestDistancesFromWord(WordGraph wordGraph, Scanner scanner){
        System.out.println("Enter one word to calculate shortest path:");
        String word = scanner.nextLine();
        // 使用WordGraph对象的printShortestDistancesFromWord方法来打印从输入的单词到图中所有其他单词的最短路径
        wordGraph.printShortestDistancesFromWord(word);
    }

    /**
     * 执行图上的随机游走。
     * @param wordGraph WordGraph对象，用于图数据的操作。
     * @param scanner Scanner对象，用于从控制台读取用户输入。
     */
    private static void randomWalk(WordGraph wordGraph, Scanner scanner) {
        System.out.println("Starting random walk...");
        // 调用WordGraph对象的randomWalk方法进行随机游走，并获取游走结果
        String randomWalkResult = wordGraph.randomWalk();
        // 输出随机游走的结果
        System.out.println("Random walk result: " + randomWalkResult);
        // 将随机游走的结果保存到文件中
        wordGraph.writeToFile("./src/random_walk_result.txt", randomWalkResult);
    }


}
