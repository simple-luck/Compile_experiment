package cn.edu.hitsz.compiler.lexer;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.io.*;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * TODO: 实验一: 实现词法分析
 * <br>
 * 你可能需要参考的框架代码如下:
 *
 * @see Token 词法单元的实现
 * @see TokenKind 词法单元类型的实现
 */
public class LexicalAnalyzer {
    private final SymbolTable symbolTable;
    private char[] buffer1;
    private List<Token> tokens = new ArrayList<>();
    // 定义关键字列表
    private final Set<String> keywords;
    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.keywords=new HashSet<>(Arrays.asList("return", "int", "if", "else", "while"));
    }


    /**
     * 从给予的路径中读取并加载文件内容
     *
     * @param path 路径
     */
    public void loadFile(String path) {
        // TODO: 词法分析前的缓冲区实现
        // 可自由实现各类缓冲区
        // 或直接采用完整读入方法
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);

            }
            buffer1 = content.toString().toCharArray();
            System.out.println("Loaded file with length: " + buffer1.length); // 调试输出
        } catch (IOException e) {
            System.out.println("Failed to load file: " + path); // 调试输出
            e.printStackTrace();
        }
        //throw new NotImplementedException();
    }

    /**
     * 执行词法分析, 准备好用于返回的 token 列表 <br>
     * 需要维护实验一所需的符号表条目, 而得在语法分析中才能确定的符号表条目的成员可以先设置为 null
     */
    public void run() {
        // TODO: 自动机实现的词法分析过程
        int i = 0;
        while (i < buffer1.length) {
            char currentChar = buffer1[i];
            if (Character.isWhitespace(currentChar)) {
                i++;
                continue;
            }

            if (Character.isLetter(currentChar) || currentChar == '_') {
                StringBuilder identifier = new StringBuilder();
                while (i < buffer1.length && (Character.isLetterOrDigit(buffer1[i]) || buffer1[i] == '_')) {
                    identifier.append(buffer1[i]);
                    i++;
                }
                String identifierStr = identifier.toString();

                // 判断是否为关键字
                if (keywords.contains(identifierStr)) {
                    tokens.add(Token.simple(identifierStr));
                } else {
                    tokens.add(Token.normal("id", identifierStr));
                    if(!symbolTable.has(identifierStr)){
                        symbolTable.add(identifierStr); // 添加标识符到符号表
                    }
                }
                continue;
            }


            // 处理数字常量
            if (Character.isDigit(currentChar)) {
                StringBuilder number = new StringBuilder();
                while (i < buffer1.length && Character.isDigit(buffer1[i])) {
                    number.append(buffer1[i]);
                    i++;
                }
                tokens.add(Token.normal("IntConst", number.toString()));
                continue;
            }

            // 处理运算符和标点符号
            switch (currentChar) {
                case '=':
                    tokens.add(Token.simple("="));
                    i++;
                    break;
                case ',':
                    tokens.add(Token.simple(","));
                    i++;
                    break;
                case '+':
                    tokens.add(Token.simple("+"));
                    i++;
                    break;
                case '-':
                    tokens.add(Token.simple("-"));
                    i++;
                    break;
                case '*':
                    tokens.add(Token.simple("*"));
                    i++;
                    break;
                case '/':
                    tokens.add(Token.simple("/"));
                    i++;
                    break;
                case '(':
                    tokens.add(Token.simple("("));
                    i++;
                    break;
                case ')':
                    tokens.add(Token.simple(")"));
                    i++;
                    break;
                case ';':
                    tokens.add(Token.simple("Semicolon"));
                    i++;
                    break;
                default:
                    // 处理未知字符
                    i++;
                    break;
            }
        }
        tokens.add(Token.eof());
        //throw new NotImplementedException();
    }

    /**
     * 获得词法分析的结果, 保证在调用了 run 方法之后调用
     *
     * @return Token 列表
     */
    public Iterable<Token> getTokens() {
        // TODO: 从词法分析过程中获取 Token 列表
        // 词法分析过程可以使用 Stream 或 Iterator 实现按需分析
        // 亦可以直接分析完整个文件
        // 总之实现过程能转化为一列表即可
        //throw new NotImplementedException();

        return  this.tokens;
    }

    public void dumpTokens(String path) {
        //System.out.println("Dumping tokens to file: " + path); // 调试输出
        List<String> lines = StreamSupport.stream(getTokens().spliterator(), false)
                .map(Token::toString)
                .toList();
        FileUtils.writeLines(
            path,
           lines
        );
        //System.out.println("Tokens: " + lines.size()); // 检查 tokens 列表的大小
    }


}
