package by.it.group310902.nazarov.lesson01.lesson13;

import java.util.*;

public class GraphA {
    private Map<String, ArrayList<String>> elements = new HashMap<>();

    // Конструктор,на основе входных данных.граф
    public GraphA(Scanner input) {
        for (var connections : input.nextLine().split(", ")) {
            // Разделяем каждое ребро на вершину и ее смежные вершины (формат "A -> B").
            var nodes = connections.split(" -> ");
            if (!elements.containsKey(nodes[0]))
                elements.put(nodes[0], new ArrayList<>());
            // Добавляем смежную вершину в список.
            elements.get(nodes[0]).add(nodes[1]);
        }
        input.close();
    }

    // Метод для сортировки смежных списков каждой вершины в порядке убывания.
    public GraphA sort() {
        for (var i : elements.values())
            i.sort((a, b) -> b.compareTo(a));
        return this;
    }

    // Переопределение метода toString для представления графа в виде строки.
    public String toString() {
        var sb = new StringBuilder();
        var stack = new Stack<String>();
        var visited = new HashSet<String>();

        // Запускаем DFS для каждой вершины графа.
        for (var node : elements.keySet())
            if (!visited.contains(node))
                dfs(node, visited, stack);

        // Формируем строку из содержимого стека.
        sb.append(stack.pop());
        while (!stack.empty())
            sb.append(' ').append(stack.pop());

        return sb.toString();
    }

    // Рекурсивный метод Depth-First Search (DFS).
    private void dfs(String node, Set<String> visited, Stack<String> stack) {
        visited.add(node);
        // Проходим по всем смежным вершинам текущей.
        if (elements.get(node) != null)
            for (var next : elements.get(node))
                if (!visited.contains(next))
                    dfs(next, visited, stack); 
        stack.push(node); // После обработки всех смежных вершин добавляем текущую вершину в стек.
    }

    public static void main(String[] args) {
        // Читаем входные данные и выводим граф, отсортированный в топологическом порядке.
        System.out.print(new GraphA(new Scanner(System.in)).sort());
    }
}
