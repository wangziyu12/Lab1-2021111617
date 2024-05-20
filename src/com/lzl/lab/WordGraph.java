package com.lzl.lab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class WordGraph {
    private Map<String, List<String>> adjacencyList;    // 存储图的邻接表
    private Map<String, Integer> edgeWeights;           // 存储边的权重

    // 构造函数，初始化邻接表和边权重的映射
    public WordGraph() {
        this.adjacencyList = new HashMap<>();
        this.edgeWeights = new HashMap<>();
    }

    // 构建图
    public void buildGraph(String text) {
        // 使用正则表达式按空白字符分割输入文本，得到单词数组。
        String[] words = text.split("\\s+");
        // 遍历单词数组，除了最后一个单词，因为它没有后继单词。
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].toLowerCase();
            String word2 = words[i + 1].toLowerCase();
            // 检查邻接表中是否已经包含当前单词，如果不包含，则添加。
            if (!adjacencyList.containsKey(word1)) {
                adjacencyList.put(word1, new ArrayList<>());
            }
            // 将下一个单词添加到当前单词的邻接列表中。
            adjacencyList.get(word1).add(word2);

            // 构造表示边的字符串。
            String edge = word1 + " -> " + word2;
            // 更新这条边的权重，如果这条边已存在，则权重加一，否则初始权重为1。
            edgeWeights.put(edge, edgeWeights.getOrDefault(edge, 0) + 1);
        }
        // 处理文本中的最后一个单词，确保它也在邻接表中。
        String lastWord = words[words.length - 1].toLowerCase();
        if (!adjacencyList.containsKey(lastWord)) {
            adjacencyList.put(lastWord, new ArrayList<>());
        }
    }

    // 返回图的邻接表。邻接表存储每个节点及其邻接节点的信息。
    public Map<String, List<String>> getAdjacencyList() {
        return adjacencyList;
    }

    // 返回图中所有边的权重。权重表存储每条边及其对应的权重。
    public Map<String, Integer> getEdgeWeights() {
        return edgeWeights;
    }

    // 查询并返回从word1到word2的桥接词。
    public String queryBridgeWords(String word1, String word2) {
        // 检查邻接表中是否存在word1和word2。如果任一单词不存在，则返回错误消息。
//        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
//            // 使用三元运算符判断是哪个单词不存在，并构造相应的错误消息。
//            return "No " + (adjacencyList.containsKey(word1) ? "word2" : "word1") + " in the graph!";
//        }
        boolean word1Exists = adjacencyList.containsKey(word1);
        boolean word2Exists = adjacencyList.containsKey(word2);

        if (!word1Exists || !word2Exists) {
            if (!word1Exists && !word2Exists) {
                return "Neither " + word1 + " nor " + word2 + " is in the graph!";
            } else if (!word1Exists) {
                return "No " + word1 + " in the graph!";
            } else {
                return "No " + word2 + " in the graph!";
            }
        }

        // 创建一个列表来存储桥接词。
        List<String> bridgeWords = new ArrayList<>();
        // 遍历word1的所有邻接词。
        for (String neighbor : adjacencyList.get(word1)) {
            // 检查每个邻接词的邻接表中是否包含word2。
            if (adjacencyList.get(neighbor).contains(word2)) {
                // 如果包含，说明这个邻接词是一个桥接词，添加到列表中。
                bridgeWords.add(neighbor);
            }
        }

        // 检查是否找到了任何桥接词。
        if (bridgeWords.isEmpty()) {
            // 如果没有找到桥接词，返回相应的错误消息。
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            // 如果找到了桥接词，构造一个包含所有桥接词的字符串。
            StringBuilder result = new StringBuilder("The bridge words from " + word1 + " to " + word2 + " are: ");
            for (int i = 0; i < bridgeWords.size(); i++) {
                // 添加每个桥接词到结果字符串中。
                result.append(bridgeWords.get(i));
                // 在桥接词之间添加逗号作为分隔符，最后一个桥接词后不添加逗号。
                if (i < bridgeWords.size() - 1) {
                    result.append(", ");
                }
            }
            // 返回构造好的结果字符串。
            return result.toString();
        }
    }


    // 定义一个方法，用于生成新文本，这个方法接受一个字符串作为输入。
    public String generateNewText(String inputText) {

        // 使用正则表达式按一个或多个空格分割输入文本，得到单词数组。
        String[] words = inputText.split("\\s+");

        // 创建一个新的列表，用于存储生成的新文本中的单词。
        List<String> newTextWords = new ArrayList<>();

        // 遍历单词数组，除了最后一个单词，因为它没有后继单词。
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];

            // 将当前单词添加到新文本的单词列表中。
            newTextWords.add(word1);

            // 调用 queryBridgeWords 方法查询当前单词和下一个单词之间的桥接词。
            String bridgeWords = queryBridgeWords(word1, word2);

            // 检查返回的字符串是否表示有桥接词（即，返回结果不是以"No"开始的）。
            if (!bridgeWords.startsWith("No")) {

                // 计算桥接词描述字符串的起始索引。
                int startIndex = ("The bridge words from " + word1 + " to " + word2 + " are: ").length();

                // 截取实际桥接词部分。
                String bridgeWordsPart = bridgeWords.substring(startIndex);

                // 将桥接词字符串分割成列表。
                List<String> bridgeWordList = Arrays.asList(bridgeWordsPart.split(", "));

                // 创建一个随机数生成器。
                Random random = new Random();
                // 随机选择一个桥接词添加到新文本的单词列表中。
                String bridgeWord = bridgeWordList.get(random.nextInt(bridgeWordList.size()));

                // 将选中的桥接词添加到列表中。
                newTextWords.add(bridgeWord);
            }
        }

        // 将原始文本的最后一个单词添加到新文本的单词列表中。
        newTextWords.add(words[words.length - 1]);

        // 使用空格将新文本的单词列表连接成一个完整的字符串，并返回。
        return String.join(" ", newTextWords);
    }

    // 计算最短路径
    public String calcShortestPath(String word1, String word2) {
        // 检查 word1 和 word2 是否在图中
        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
            // 如果 word1 或 word2 不在图中，则返回相应的错误消息
            return "图中没有" + (adjacencyList.containsKey(word1) ? "word2" : "word1") + "!";
        }

        // 初始化广度优先搜索的队列
        Queue<String> queue = new LinkedList<>();
        // 初始化用于跟踪访问过的节点的映射
        Map<String, Boolean> visited = new HashMap<>();
        // 初始化用于跟踪到每个节点的最短路径长度的映射
        Map<String, Integer> shortestPathLength = new HashMap<>();
        // 初始化用于跟踪最短路径中每个节点的前置节点的映射
        Map<String, String> predecessors = new HashMap<>();

        // 将所有节点的最短路径长度初始化为无穷大
        for (String node : adjacencyList.keySet()) {
            shortestPathLength.put(node, Integer.MAX_VALUE);
            visited.put(node, false);
        }

        // 从 word1 开始广度优先搜索
        queue.offer(word1);
        visited.put(word1, true);           // 将起始节点 word1 标记为已访问
        shortestPathLength.put(word1, 0);   // 将起始节点 word1 的最短路径长度初始化为 0

        // 执行广度优先搜索
        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            List<String> neighbors = adjacencyList.get(currentWord);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.getOrDefault(neighbor, false)) {
                        visited.put(neighbor, true);
                        int weight = edgeWeights.get(currentWord + " -> " + neighbor);
                        shortestPathLength.put(neighbor, shortestPathLength.get(currentWord) + weight);
                        predecessors.put(neighbor, currentWord);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // 从 word1 到 word2 检索最短路径
        List<List<String>> shortestPaths = new ArrayList<>();
        int shortestLength = shortestPathLength.get(word2);// 获取从起始节点 word1 到目标节点 word2 的最短路径长度
        if (shortestLength == Integer.MAX_VALUE) {
            // 如果找不到最短路径，则返回相应的错误消息
            return "从 " + word1 + " 到 " + word2 + " 没有最短路径！";
        } else {
            List<String> path = new ArrayList<>();// 存储从 word2 到 word1 的最短路径
            String currentWord = word2;
            while (currentWord != null) {
                // 从 word2 开始，沿着最短路径反向回溯，直到到达 word1
                path.add(0, currentWord);// 将当前节点添加到路径的开头
                currentWord = predecessors.get(currentWord);// 获取当前节点的前置节点，继续回溯
            }
            shortestPaths.add(path);
        }

        // 输出最短路径
        StringBuilder result = new StringBuilder();
        result.append("从 " + word1 + " 到 " + word2 + " 的最短路径长度为 " + shortestLength + " 是：\n");
        for (List<String> path : shortestPaths) {
            // 将最短路径添加到结果中
            result.append(String.join(" -> ", path)).append("\n");
        }
        return result.toString();
    }

    // 计算并打印给定单词 word 与图中所有其他单词之间的最短路径及其长度
    public void printShortestDistancesFromWord(String word) {
        // 遍历每个单词，并计算其与给定单词之间的最短距离
        for (String otherWord : adjacencyList.keySet()) {
            if (!otherWord.equals(word)) { // 跳过给定单词本身
                // 调用 calcShortestPath 计算最短路径，并打印结果
                String shortestPathResult = calcShortestPath(word, otherWord);
                System.out.println(shortestPathResult);

            }
        }
    }

    // 随机游走
    public String randomWalk() {
        // 随机选择一个起始节点
        Random random = new Random();
        List<String> nodes = new ArrayList<>(adjacencyList.keySet()); // 获取图中所有节点的列表
        String currentNode = nodes.get(random.nextInt(nodes.size())); // 随机选择一个起始节点

        StringBuilder result = new StringBuilder(); // 用于构建随机游走的结果字符串
        result.append(currentNode).append(" "); // 将起始节点添加到结果中

        Set<String> visitedEdges = new HashSet<>(); // 用于跟踪已访问的边

        while (adjacencyList.containsKey(currentNode) && !adjacencyList.get(currentNode).isEmpty()) {
            // 随机选择下一个节点
            List<String> neighbors = adjacencyList.get(currentNode); // 获取当前节点的所有邻居节点
            String nextNode = neighbors.get(random.nextInt(neighbors.size())); // 随机选择一个邻居节点

            // 检查该边是否已经被访问过
            String edge = currentNode + " -> " + nextNode; // 构建当前边的字符串表示
            if (visitedEdges.contains(edge)) { // 如果该边已被访问过，结束随机游走
                result.append(nextNode).append(" "); // 将下一个节点添加到结果中
                break;
            }

            // 将下一个节点添加到结果中
            result.append(nextNode).append(" ");

            // 标记该边为已访问
            visitedEdges.add(edge);

            // 移动到下一个节点
            currentNode = nextNode;

            // 检查当前节点是否没有出边
            if (!adjacencyList.containsKey(currentNode) || adjacencyList.get(currentNode).isEmpty()) {
                break; // 如果当前节点没有出边，结束随机游走
            }
        }

        return result.toString().trim(); // 返回随机游走的结果字符串
    }

    // 将给定的内容追加写入到指定文件中，并在控制台上打印成功或失败的信息
    public void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, true)) { // 使用 FileWriter 打开文件进行追加写入操作
            writer.write(content + System.lineSeparator()); // 将内容加上系统的行分隔符写入文件
            System.out.println("Random walk result has been written to file: " + filePath); // 打印成功写入的消息
        } catch (IOException e) { // 捕获可能的 IO 异常
            System.err.println("Error writing random walk result to file: " + e.getMessage()); // 打印错误消息
        }
    }



}