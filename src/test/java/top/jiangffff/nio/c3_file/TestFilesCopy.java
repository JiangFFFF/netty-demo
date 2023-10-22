package top.jiangffff.nio.c3_file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 拷贝多级目录
 *
 * @author JiangHuifeng
 * @create 2023-10-21-09:16
 */
public class TestFilesCopy {
    public static void main(String[] args) throws IOException {
        String source = "/Users/jianghuifeng/Desktop/杂乱代码";
        String target = "/Users/jianghuifeng/Desktop/杂乱代码11";

        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                // 是一个目录
                if (Files.isDirectory(path)) {
                    Files.createDirectories(Paths.get(targetName));
                } else if (Files.isRegularFile(path)) {
                    // 普通文件
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
