import java.util.List;

public class VisitorKotlin<T> extends KotlinParserBaseVisitor<T>{


    int nested_level = 0;

    void print_tabs() {
        for (int i = 0; i < nested_level; ++i) {
            System.out.print("\t");
        }
    }

    @Override
    public T visitExpression(KotlinParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public T visitRangeExpression(KotlinParser.RangeExpressionContext ctx) {
        if (ctx.RANGE().size() > 0) {
            visitAdditiveExpression(ctx.additiveExpression().get(0));
            System.out.print("...");
            visitAdditiveExpression(ctx.additiveExpression().get(1));
            return null;
        }
        visitAdditiveExpression(ctx.additiveExpression().get(0));
        return null;
    }

    @Override
    public T visitPrefixUnaryExpression(KotlinParser.PrefixUnaryExpressionContext ctx) {
        for (int i = 0; i < ctx.unaryPrefix().size(); i++) {
            System.out.print(ctx.unaryPrefix().get(i).getText() + " ");
        }
        //System.out.print(ctx.postfixUnaryExpression().getText());
        if (ctx.postfixUnaryExpression() != null) {
            visitPostfixUnaryExpression(ctx.postfixUnaryExpression());
        }
        return null;
    }

    @Override
    public T visitPostfixUnaryExpression(KotlinParser.PostfixUnaryExpressionContext ctx) {
        visitPrimaryExpression(ctx.primaryExpression());
        if (ctx.postfixUnarySuffix() != null) {
            for (int i = 0; i < ctx.postfixUnarySuffix().size(); i++) {
                System.out.print(ctx.postfixUnarySuffix(i).getText() + " ");
            }
        }
        return null;
    }

    @Override
    public T visitPrimaryExpression(KotlinParser.PrimaryExpressionContext ctx) {
        if (ctx.literalConstant() != null ||
                ctx.stringLiteral() != null ||
                ctx.simpleIdentifier() != null ||
                ctx.callableReference() != null ||
                ctx.functionLiteral() != null ||
                ctx.objectLiteral() != null ||
                ctx.collectionLiteral() != null ||
                ctx.thisExpression() != null ||
                ctx.superExpression() != null) {
            System.out.println(ctx.getText());
        } else if (ctx.parenthesizedExpression() != null) {
            visitParenthesizedExpression(ctx.parenthesizedExpression());
        } else if (ctx.ifExpression() != null) {
            visitIfExpression(ctx.ifExpression());
        } else if (ctx.whenExpression() != null) {
            visitWhenExpression(ctx.whenExpression());
        } else if (ctx.tryExpression() != null) {
            visitTryExpression(ctx.tryExpression());
        } else if (ctx.jumpExpression() != null) {
            visitJumpExpression(ctx.jumpExpression());
        } else {
            System.out.println("Error");
        }
        return null;
    }

    @Override
    public T visitParenthesizedExpression(KotlinParser.ParenthesizedExpressionContext ctx) {
        System.out.print("( ");
        visitExpression(ctx.expression());
        System.out.println(" )");
        return null;
    }

    @Override
    public T visitWhenExpression(KotlinParser.WhenExpressionContext ctx) {
        System.out.print("switch ");
        visitExpression(ctx.expression());
        System.out.println(" {");
        if (ctx.whenEntry() != null) {
            for (int i = 0; i < ctx.whenEntry().size(); i++) {
               visitWhenEntry(ctx.whenEntry(i));
                System.out.println();
            }
        }
        System.out.println(" }");
        return null;
    }

    @Override
    public T visitWhenEntry(KotlinParser.WhenEntryContext ctx) {
        if(ctx.whenCondition() != null){
            for(int i = 0; i < ctx.whenCondition().size(); i++){
                if(ctx.whenCondition(i).expression() != null){
                    visitExpression(ctx.whenCondition(i).expression());
                }else if(ctx.whenCondition(i).rangeTest() != null){
                    visitRangeTest(ctx.whenCondition(i).rangeTest());
                }else{
                    return null;
                    //TODO
                }
                if(i != ctx.whenCondition().size() - 1){
                    System.out.print(", ");
                }else{
                    System.out.println(":");
                }
            }
            if(ctx.controlStructureBody().block() != null){
                visitStatements(ctx.controlStructureBody().block().statements());
            }else{
                visitStatement(ctx.controlStructureBody().statement());
            }
        }else{
            System.out.println("default:");
            if(ctx.controlStructureBody().block() != null){
                visitStatements(ctx.controlStructureBody().block().statements());
            }else{
                visitStatement(ctx.controlStructureBody().statement());
            }
        }
        return null;
    }

    @Override
    public T visitRangeTest(KotlinParser.RangeTestContext ctx) {
        System.out.print(ctx.inOperator().getText()+ " ");
        visitExpression(ctx.expression());
        return null;
    }

    @Override
    public T visitJumpExpression(KotlinParser.JumpExpressionContext ctx) {
        print_tabs();
        if (ctx.RETURN() != null) {
            System.out.print("return ");
        }
        if (ctx.THROW() != null) {
            System.out.print("throw ");
        }
        if (ctx.CONTINUE() != null) {
            System.out.print("continue");
        }
        if (ctx.BREAK() != null) {
            System.out.print("break");
        }
        if (ctx.expression() != null) {
            visitExpression(ctx.expression());
            System.out.println();
        }
        return null;
    }

    @Override
    public T visitAdditiveOperator(KotlinParser.AdditiveOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitMultiplicativeOperator(KotlinParser.MultiplicativeOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitAsOperator(KotlinParser.AsOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitPrefixUnaryOperator(KotlinParser.PrefixUnaryOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitPostfixUnaryOperator(KotlinParser.PostfixUnaryOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitMemberAccessOperator(KotlinParser.MemberAccessOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitComparisonOperator(KotlinParser.ComparisonOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitInOperator(KotlinParser.InOperatorContext ctx) {
        System.out.print(" " + ctx.getText() + " ");
        return null;
    }

    @Override
    public T visitEqualityOperator(KotlinParser.EqualityOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    @Override
    public T visitAssignmentAndOperator(KotlinParser.AssignmentAndOperatorContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }


    @Override
    public T visitPropertyDeclaration(KotlinParser.PropertyDeclarationContext ctx) {
        if (ctx.modifiers() != null) {
            System.out.print(ctx.modifiers().getText() + " ");
        }
        if (ctx.VAL() != null) {
            System.out.print("let ");
        }
        if (ctx.VAR() != null) {
            System.out.print("var ");
        }
        if (ctx.multiVariableDeclaration() != null) {
            System.out.print(ctx.multiVariableDeclaration().getText() + " ");
        }
        if (ctx.variableDeclaration() != null) {
            System.out.print(ctx.variableDeclaration().getText() + " ");
        }
        if (ctx.typeConstraints() != null) {
            System.out.print(ctx.typeConstraints().getText() + " ");
        }
        if (ctx.ASSIGNMENT() != null) {
            System.out.print("= ");
        }
        if (ctx.expression() != null) {
            System.out.print(ctx.expression().getText() + " ");
        }
        if (ctx.propertyDelegate() != null) {
            System.out.print(ctx.propertyDelegate().getText() + " ");
        }
        boolean hasGetter = ctx.getter() != null, hasSetter = ctx.setter() != null;
        if (hasGetter) {
            System.out.println("{");
            print_tabs();
            nested_level++;
            visitGetter(ctx.getter());
            if (!hasSetter) {
                System.out.println("}");
            }
        }
        if (ctx.setter() != null) {
            if (!hasGetter) {
                System.out.println("{");
                nested_level++;
                print_tabs();
            }
            visitSetter(ctx.setter());
            nested_level--;
            print_tabs();
            System.out.print("}");
            nested_level--;
        }
        System.out.println();
        return null;
    }

    @Override
    public T visitGetter(KotlinParser.GetterContext ctx) {
        print_tabs();
        System.out.println("get {");
        nested_level++;
        visitFunctionBody(ctx.functionBody());
        nested_level--;
        print_tabs();
        System.out.println("}");
        return null;
    }

    @Override
    public T visitSetter(KotlinParser.SetterContext ctx) {
        print_tabs();
        System.out.println("set {");
        nested_level++;
        visitFunctionBody(ctx.functionBody());
        nested_level--;
        print_tabs();
        System.out.println("}");
        return super.visitSetter(ctx);
    }

    @Override
    public T visitFunctionBody(KotlinParser.FunctionBodyContext ctx) {
        print_tabs();
        if (ctx.ASSIGNMENT() != null) {
            System.out.print("return ");
            visitExpression(ctx.expression());
            System.out.println();
        }
        if (ctx.block() != null) {
            visitBlock(ctx.block());
        }
        return null;
    }

    @Override
    public T visitAssignment(KotlinParser.AssignmentContext ctx) {
        if (ctx.directlyAssignableExpression() != null) {
            visitDirectlyAssignableExpression(ctx.directlyAssignableExpression());
            System.out.print(" = ");
            visitExpression(ctx.expression());
        } else {
            visitAssignableExpression(ctx.assignableExpression());
            visitAssignmentAndOperator(ctx.assignmentAndOperator());
        }
        visitExpression(ctx.expression());
        System.out.println();
        return null;
    }

    @Override
    public T visitDirectlyAssignableExpression(KotlinParser.DirectlyAssignableExpressionContext ctx) {
        visitSimpleIdentifier(ctx.simpleIdentifier());
        return null;
    }


    @Override
    public T visitSimpleIdentifier(KotlinParser.SimpleIdentifierContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }
    
    @Override
    public T visitControlStructureBody(KotlinParser.ControlStructureBodyContext ctx) {
        return super.visitControlStructureBody(ctx);
    }

    @Override
    public T visitIfExpression(KotlinParser.IfExpressionContext ctx) {
        System.out.print("if");
        //printNL(ctx.NL());
        System.out.print("(");
        //printNL(ctx.NL());
        visitExpression(ctx.expression());
        //printNL(ctx.NL());
        if(ctx.controlStructureBody(0) != null){
            nested_level++;
            visitControlStructureBody(ctx.controlStructureBody(0));
            if(ctx.controlStructureBody(1) != null){
                System.out.print("else");
                nested_level++;
                visitControlStructureBody(ctx.controlStructureBody(1));
            }
        }else{
            System.out.print("else");
            nested_level++;
            visitControlStructureBody(ctx.controlStructureBody(0));
        }

        return null;
    }
}

