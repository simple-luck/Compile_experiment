import java.io.File;

public class FileChecker {

    public static void main(String[] args) {
        // 替换为你想检查的文件路径
        String filePath = "D:/Compile/template/data/in/grammar.txt";

        // 创建 File 对象
        File file = new File(filePath);

        // 检查文件是否存在
        if (file.exists()) {
            System.out.println("文件存在: " + file.getAbsolutePath());
        } else {
            System.out.println("文件不存在: " + file.getAbsolutePath());
        }

        // 检查是否为文件而不是目录
        if (file.isFile()) {
            System.out.println("这是一个文件。");
        } else if (file.isDirectory()) {
            System.out.println("这是一个目录。");
        } else {
            System.out.println("文件路径既不是文件也不是目录。");
        }
    }
}
