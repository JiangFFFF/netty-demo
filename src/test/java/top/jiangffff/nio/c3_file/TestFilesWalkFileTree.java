package top.jiangffff.nio.c3_file;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WalkFileTree 文件树
 *
 * @author JiangHuifeng
 * @create 2023-10-19-22:21
 */
public class TestFilesWalkFileTree {
    public static void main(String[] args) throws IOException {
//        method1();
//        method2();
//        Files.delete(Paths.get("/Users/jianghuifeng/Desktop/t1"));
        Files.walkFileTree(Paths.get("/Users/jianghuifeng/Desktop/t1"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                System.out.println(file);
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                System.out.println("<===退出 " + dir);
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }

        });
    }

    private static void method2() throws IOException {
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("/Users/jianghuifeng/IdeaProjects"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith("jar")) {
                    System.out.println(file);
                    jarCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("jarCount: " + jarCount);
    }

    /**
     * 遍历与计数
     *
     * @throws IOException
     */
    private static void method1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("/Users/jianghuifeng/Downloads/jdk-17.0.6.jdk/Contents"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("====>" + dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("dir count " + dirCount);
        System.out.println("dir count " + fileCount);
    }
}
