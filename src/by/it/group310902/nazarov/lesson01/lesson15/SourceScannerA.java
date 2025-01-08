package by.it.group310902.nazarov.lesson01.lesson15;


import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {
    public static void main(String[] args) {
        // Получаем путь к папке src в текущем рабочем каталоге проекта
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileEntry> fileEntries = new ArrayList<>();
        // Создаём список для хранения информации о файлах
        try {
            // Рекурсивно обходим все файлы в папке src, ищем файлы с расширением .java
            Files.walk(Path.of(srcDir))
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Читаем содержимое файла
                            String content = Files.readString(path);

                            // Исключаем тесты
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                String processed = processContent(content);
                                if (!processed.isEmpty()) {
                                    int sizeInBytes = processed.getBytes().length;
                                    String relativePath = Path.of(srcDir).relativize(path).toString();
                                    fileEntries.add(new FileEntry(relativePath, sizeInBytes));
                                }
                            }
                        } catch (MalformedInputException e) {
                            System.err.println("Malformed input: " + path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            // Сортировка файлов
            fileEntries.sort(Comparator.comparingInt((FileEntry f) -> f.size)
                    .thenComparing(f -> f.path));

            // Вывод результатов
            for (FileEntry entry : fileEntries) {
                System.out.println(entry.size + " " + entry.path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processContent(String content) {// Метод для обработки содержимого файла
        StringBuilder sb = new StringBuilder();// Используем StringBuilder для формирования текста
        String[] lines = content.split("\n");

        for (String line : lines) {
            line = line.strip();
            // Пропускаем строки, начинающиеся с "package" или "import"
            if (line.startsWith("package") || line.startsWith("import")) {
                continue;
            }
            sb.append(line).append("\n");
        }

        String processed = sb.toString();
        return removeNonPrintable(processed);
    }

    private static String removeNonPrintable(String text) {
        // Метод для удаления непечатаемых символов в тексте
        int start = 0, end = text.length();
        // Находим первый символ, который не является непечатаемым
        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    static class FileEntry {
        String path;
        int size;
        // Вложенный класс для хранения информации о файлах
        public FileEntry(String path, int size) {
            // Конструктор класса
            this.path = path;
            this.size = size;
        }
    }
}