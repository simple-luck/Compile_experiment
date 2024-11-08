package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.*;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;
import cn.edu.hitsz.compiler.symtab.SymbolTable;

import java.util.Objects;
import java.util.Stack;

// TODO: 实验三: 实现语义分析
public class SemanticAnalyzer implements ActionObserver {
    private Stack<Symbol> semanticStack=new Stack<>();
    private SymbolTable symbolTable;



    @Override
    public void whenAccept(Status currentStatus) {
        // TODO: 该过程在遇到 Accept 时要采取的代码动作
        //throw new NotImplementedException();
    }


    @Override
    public void whenReduce(Status currentStatus, Production production) {
        // TODO: 该过程在遇到 reduce production 时要采取的代码动作
        //throw new NotImplementedException();
        Symbol token1,token2;
        Symbol nonTerminal;
        switch (production.index()){

            case 4->{//S -> D id;
                // 从语义栈中弹出两个元素（D 和 id），获得 D 的 type 并设置到符号表中的 id
                token1=semanticStack.pop();//id
                token2=semanticStack.pop();//D

                // 将 id 的 type 信息存入符号表
                symbolTable.get(token1.token.getText()).setType(token2.type);

                // S 不需要携带信息，压入空记录占位
                nonTerminal=new Symbol(production.head());
                nonTerminal.type=null;
                semanticStack.push(nonTerminal);
            }
            case 5->{//D -> int;
                // 1. 弹出 `int` 符号，并获取其 type
                token1=semanticStack.pop();
                nonTerminal=new Symbol(production.head());
                // 2. 将 `int` 的 type 赋给 `D`
                nonTerminal.type=token1.type;
                // 3. 将 D 的 type 信息压入语义栈
                semanticStack.push(nonTerminal);
            }
            default ->{
                for(int i=0;i<production.body().size();i++){
                    semanticStack.pop();
                }
                semanticStack.push(new Symbol(production.head()));
            }
        }

    }

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        // TODO: 该过程在遇到 shift 时要采取的代码动作
        //throw new NotImplementedException();
        Symbol curSymbol=new Symbol(currentToken);

        if(Objects.equals(currentToken.getKindId(),"int")){
            curSymbol.type=SourceCodeType.Int;
        }else{
            curSymbol.type=null;
        }
        semanticStack.push(curSymbol);
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        // TODO: 设计你可能需要的符号表存储结构
        // 如果需要使用符号表的话, 可以将它或者它的一部分信息存起来, 比如使用一个成员变量存储
        //throw new NotImplementedException();
        this.symbolTable = table;

    }
}

