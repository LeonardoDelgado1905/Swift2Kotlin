import java.util.ArrayList;
import java.util.List;

public class VisitorKotlin<T> extends KotlinParserBaseVisitor<T> {

    int nested_level = 0;
    String parameters = "";

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
        if (ctx.primaryExpression().simpleIdentifier() != null) {
            boolean interesting_thing =(ctx.postfixUnarySuffix() != null && ctx.postfixUnarySuffix().size() > 0 /*&& ctx.postfixUnarySuffix(0).callSuffix() != null*/) || ctx.getText().contains("--") || ctx.getText().contains("++");
            if (interesting_thing) {
                print_tabs();
            }
            visitPrimaryExpression(ctx.primaryExpression());
            boolean just_print = ctx.primaryExpression().getText().equals("print");
            boolean print_endline = ctx.primaryExpression().getText().equals("println");
            if (ctx.postfixUnarySuffix() != null) {
                for (int i = 0; i < ctx.postfixUnarySuffix().size(); ++i) {
                    String val = ctx.postfixUnarySuffix(i).getText();
                    if(just_print && i == ctx.postfixUnarySuffix().size() - 1){
                        if(val.length() >= 2)  val = val.substring(0, val.length() - 1) + ", terminator:\"\")";
                    }
                    System.out.print(val);
                }
            }
            if(interesting_thing)System.out.println();
        } else {
            visitPrimaryExpression(ctx.primaryExpression());
            if (ctx.postfixUnarySuffix() != null) {
                for (int i = 0; i < ctx.postfixUnarySuffix().size(); i++) {
                    System.out.print(ctx.postfixUnarySuffix(i).getText() + " ");
                }
            }
        }
        return null;
    }

    @Override
    public T visitPrimaryExpression(KotlinParser.PrimaryExpressionContext ctx) {
        if (ctx.literalConstant() == null && ctx.stringLiteral() == null && ctx.thisExpression() == null && ctx.simpleIdentifier() == null) {
            print_tabs();
        }
        if (
                ctx.stringLiteral() != null ||
                        ctx.callableReference() != null ||
                        ctx.functionLiteral() != null ||
                        ctx.simpleIdentifier() != null ||
                        ctx.objectLiteral() != null ||
                        ctx.collectionLiteral() != null ||
                        ctx.superExpression() != null ) {
            String val = ctx.getText();
            if(ctx.getText().equals("println")) val = "print";
            System.out.print(val);
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
        } else if (ctx.thisExpression() != null) {
            System.out.print("self");
        } else if (ctx.literalConstant() != null) {
            if (ctx.literalConstant().getText().equals("null")) {
                System.out.print("nil");
            } else {
                System.out.print(ctx.literalConstant().getText());
            }
        } else {
            System.out.println("Error");
        }
        return null;
    }

    @Override
    public T visitTryExpression(KotlinParser.TryExpressionContext ctx) {
        System.out.println("try {");
        nested_level++;
        visitBlock(ctx.block());
        nested_level--;
        if(ctx.catchBlock().size() > 0){
            for(int i = 0; i < ctx.catchBlock().size(); i++){
                System.out.print("}");
                visitCatchBlock(ctx.catchBlock(i));
                System.out.println();
            }
            if(ctx.finallyBlock() != null){
                visitFinallyBlock(ctx.finallyBlock());
            }
        }else{
            visitFinallyBlock(ctx.finallyBlock());
        }

        return null;
    }

    @Override
    public T visitCatchBlock(KotlinParser.CatchBlockContext ctx) {
        System.out.print(" catch (");

        for(int i = 0; i < ctx.annotation().size(); i++){
            System.out.print(ctx.annotation(i).getText());
        }

        System.out.print(ctx.simpleIdentifier().getText());
        System.out.print(" : ");
        System.out.print(ctx.userType().getText());
        System.out.println(") {");
        nested_level++;
        visitBlock(ctx.block());
        nested_level--;
        System.out.println("}");

        return null;
    }

    @Override
    public T visitParenthesizedExpression(KotlinParser.ParenthesizedExpressionContext ctx) {
        System.out.print("(");
        visitExpression(ctx.expression());
        System.out.println(")");
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
        System.out.println("}");
        return null;
    }

    @Override
    public T visitWhenEntry(KotlinParser.WhenEntryContext ctx) {
        if (ctx.whenCondition() != null && ctx.whenCondition().size() > 0) {
            for (int i = 0; i < ctx.whenCondition().size(); i++) {
                System.out.print("case ");
                if (ctx.whenCondition(i).expression() != null) {
                    visitExpression(ctx.whenCondition(i).expression());
                } else if (ctx.whenCondition(i).rangeTest() != null) {
                    visitRangeTest(ctx.whenCondition(i).rangeTest());
                } else {
                    return null;
                    //TODO
                }
                if (i != ctx.whenCondition().size() - 1) {
                    System.out.print(", ");
                } else {
                    System.out.println(":");
                    nested_level++;
                }
            }
            if (ctx.controlStructureBody().block() != null) {
                visitStatements(ctx.controlStructureBody().block().statements());
            } else {
                visitStatement(ctx.controlStructureBody().statement());
            }
            nested_level--;
        } else {
            System.out.println("default:");
            nested_level++;
            if (ctx.controlStructureBody().block() != null) {
                visitStatements(ctx.controlStructureBody().block().statements());
            } else {
                visitStatement(ctx.controlStructureBody().statement());
            }
        }
        return null;
    }

    @Override
    public T visitRangeTest(KotlinParser.RangeTestContext ctx) {
        System.out.print(ctx.inOperator().getText() + " ");
        visitExpression(ctx.expression());
        return null;
    }

    @Override
    public T visitJumpExpression(KotlinParser.JumpExpressionContext ctx) {
        if (ctx.RETURN() != null) {
            System.out.print("return ");
            if (ctx.expression() != null) {
                visitExpression(ctx.expression());
            }
            System.out.println();
        } else if (ctx.THROW() != null) {
            System.out.print("throw ");
            visitExpression(ctx.expression());
            System.out.println();
        } else if (ctx.CONTINUE() != null) {
            System.out.print("continue");
        } else if (ctx.BREAK() != null) {
            System.out.print("break");
        }
        return null;
    }

    @Override
    public T visitWhileStatement(KotlinParser.WhileStatementContext ctx) {
        print_tabs();
        System.out.print("while ");
        visitExpression(ctx.expression());
        if (ctx.controlStructureBody() != null) {
            System.out.println(" {");
            nested_level++;
            visitControlStructureBody(ctx.controlStructureBody());
            nested_level--;
            print_tabs();
            System.out.println("}");
        } else {
            System.out.println(";");
        }
        return null;
    }


    @Override
    public T visitElvis(KotlinParser.ElvisContext ctx) {
        System.out.print(" ?? ");
        return super.visitElvis(ctx);
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
        print_tabs();
        if (ctx.modifiers() != null) {
            visitModifiers(ctx.modifiers());
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
            visitVariableDeclaration(ctx.variableDeclaration());

            if (ctx.modifiers() != null) {
                for (int i = 0; i < ctx.modifiers().modifier().size(); i++) {
                    if (ctx.modifiers().modifier(i).getText().equals("lateinit")) {
                        System.out.print("!");
                        break;
                    }
                }
            }
        }
        if (ctx.typeConstraints() != null) {
            System.out.print(ctx.typeConstraints().getText() + " ");
        }
        if (ctx.ASSIGNMENT() != null) {
            System.out.print(" = ");
        }
        if (ctx.expression() != null) {
            System.out.print(ctx.expression().getText() + " ");
        }
        if (ctx.propertyDelegate() != null) {
            System.out.print(" by ");
            visitExpression(ctx.propertyDelegate().expression());
        }
        boolean hasGetter = ctx.getter() != null, hasSetter = ctx.setter() != null;
        if (hasGetter) {
            System.out.println("{");
            print_tabs();
            nested_level++;
            visitGetter(ctx.getter());
            if (!hasSetter) {
                nested_level--;
                print_tabs();
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
    public T visitVariableDeclaration(KotlinParser.VariableDeclarationContext ctx) {
        if (ctx.annotation() != null) {
            for (int i = 0; i < ctx.annotation().size(); ++i) {
                visitAnnotation(ctx.annotation(0));
            }
        }
        visitSimpleIdentifier(ctx.simpleIdentifier());

        if (ctx.type_() != null) {
            System.out.print(" : ");
            visitType_(ctx.type_());
        }

        return null;
    }

    @Override
    public T visitType_(KotlinParser.Type_Context ctx) {
        if (ctx.typeReference() != null && ctx.typeReference().userType() != null && ctx.typeReference().userType().simpleUserType() != null) {
            List<KotlinParser.SimpleUserTypeContext> sut = ctx.typeReference().userType().simpleUserType();
            for (int i = 0; i < sut.size(); ++i) {
                if (sut.get(i).simpleIdentifier().getText().equals("List")) {
                    System.out.print("Array" + sut.get(i).typeArguments().getText() + " ");
                } else if (sut.get(i).simpleIdentifier().getText().equals("Map")) {
                    System.out.print("Dictionary" + sut.get(i).typeArguments().getText() + " ");
                } else {
                    System.out.print(sut.get(i).getText());
                }
            }
        } else {
            System.out.print(ctx.getText());
        }
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
        return null;
    }

    @Override
    public T visitFunctionBody(KotlinParser.FunctionBodyContext ctx) {
        if (ctx.ASSIGNMENT() != null) {
            print_tabs();
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
        print_tabs();
        if (ctx.directlyAssignableExpression() != null) {
            visitDirectlyAssignableExpression(ctx.directlyAssignableExpression());
            System.out.print(" = ");
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
        if (ctx.postfixUnaryExpression() != null) {
            visitPostfixUnaryExpression(ctx.postfixUnaryExpression());
            System.out.print(ctx.assignableSuffix().getText());
        } else {
            visitSimpleIdentifier(ctx.simpleIdentifier());
        }
        return null;
    }

    @Override
    public T visitSimpleIdentifier(KotlinParser.SimpleIdentifierContext ctx) {
        print_tabs();
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
        System.out.print("(");
        visitExpression(ctx.expression());
        System.out.println("){");
        if (ctx.controlStructureBody(0) != null) {
            nested_level++;
            visitControlStructureBody(ctx.controlStructureBody(0));
            nested_level--;
            if (ctx.controlStructureBody(1) != null) {
                print_tabs();
                System.out.println("}else{");
                nested_level++;
                visitControlStructureBody(ctx.controlStructureBody(1));
                nested_level--;
            }
        } else {
            print_tabs();
            System.out.print("else");
            nested_level++;
            visitControlStructureBody(ctx.controlStructureBody(0));
            nested_level--;
        }
        print_tabs();
        System.out.println("}");

        return null;
    }

    @Override
    public T visitDoWhileStatement(KotlinParser.DoWhileStatementContext ctx) {
        System.out.println("repeat{");
        nested_level++;
        visitControlStructureBody(ctx.controlStructureBody());
        nested_level--;
        System.out.print("}while ");
        visitExpression(ctx.expression());
        System.out.println();
        return null;
    }

    @Override
    public T visitForStatement(KotlinParser.ForStatementContext ctx) {
        print_tabs();
        System.out.print("for ");
        visitVariableDeclaration(ctx.variableDeclaration());
        System.out.print(" in ");
        visitExpression(ctx.expression());
        System.out.println("{");
        nested_level++;
        visitControlStructureBody(ctx.controlStructureBody());
        nested_level--;
        print_tabs();
        System.out.println("}");
        return null;
    }

    @Override
    public T visitModifier(KotlinParser.ModifierContext ctx) {
        if (!ctx.getText().equals("lateinit")) {
            System.out.print(ctx.getText() + " ");
        }
        return null;
    }

    @Override
    public T visitAnnotation(KotlinParser.AnnotationContext ctx) {
        System.out.print(ctx.getText() + " ");
        return null;
    }

    @Override
    public T visitPrimaryConstructor(KotlinParser.PrimaryConstructorContext ctx) {
        if (ctx.CONSTRUCTOR() != null) {
            System.out.print("init ");

        }
        visitClassParameters(ctx.classParameters());
        return null;
    }

    @Override
    public T visitClassParameters(KotlinParser.ClassParametersContext ctx) {
        System.out.print("(");
        if (ctx.classParameter() != null) {
            visitClassParameter(ctx.classParameter().get(0));
            if (ctx.classParameter().size() > 1) {
                for (int i = 1; i < ctx.classParameter().size(); i++) {
                    System.out.print(", ");
                    visitClassParameter(ctx.classParameter().get(i));
                }
            }
        }
        System.out.print(")");
        return null;
    }

    @Override
    public T visitClassParameter(KotlinParser.ClassParameterContext ctx) {
        if (ctx.modifiers() != null) {
            visitModifiers(ctx.modifiers());
        }
        if (ctx.VAL() != null) {
            System.out.print("let ");
        }
        if (ctx.VAR() != null) {
            System.out.print("var ");
        }

        visitSimpleIdentifier(ctx.simpleIdentifier());
        System.out.print(ctx.type_().getText());
        if (ctx.expression() != null) {
            System.out.print(" = ");
            visitExpression(ctx.expression());
        }
        return null;
    }

    @Override
    public T visitClassBody(KotlinParser.ClassBodyContext ctx) {
        System.out.println("{");
        nested_level++;
        visitClassMemberDeclarations(ctx.classMemberDeclarations());
        nested_level--;
        System.out.println("}");
        return null;
    }

    @Override
    public T visitClassMemberDeclarations(KotlinParser.ClassMemberDeclarationsContext ctx) {
        for (int i = 0; i < ctx.classMemberDeclaration().size(); i++) {
            //print_tabs();
            visitClassMemberDeclaration(ctx.classMemberDeclaration(i));
            if (ctx.semis(i) != null) {
                visitSemis(ctx.semis(i));
            }
        }
        return null;
    }

    @Override
    public T visitClassMemberDeclaration(KotlinParser.ClassMemberDeclarationContext ctx) {
        return super.visitClassMemberDeclaration(ctx);
    }

    @Override
    public T visitAnonymousInitializer(KotlinParser.AnonymousInitializerContext ctx) {
        print_tabs();
        System.out.print("init");
        if (!parameters.equals("")) {
            System.out.print(parameters);
        }
        System.out.print("{");
        System.out.println();
        nested_level++;
        print_tabs();
        visitBlock(ctx.block());
        nested_level--;
        System.out.println();
        print_tabs();
        System.out.println("}");
        return null;
    }

    @Override
    public T visitClassDeclaration(KotlinParser.ClassDeclarationContext ctx) {
        if (ctx.modifiers() != null) {
            visitModifiers(ctx.modifiers());
        }
        if (ctx.CLASS() != null) {
            System.out.print("class ");
        }
        if (ctx.INTERFACE() != null) {
            System.out.print("protocol ");
        }

        visitSimpleIdentifier(ctx.simpleIdentifier());
        if (ctx.typeParameters() != null) {
            System.out.print(ctx.typeParameters().getText());
        }

        if (ctx.primaryConstructor() != null) {
            parameters += " " + ctx.primaryConstructor().getText();
        }

        if (ctx.delegationSpecifiers() != null) {
            System.out.print(" : ");
            System.out.print(ctx.delegationSpecifiers().getText());
        }

        if (ctx.classBody() != null) {
            visitClassBody(ctx.classBody());
        }

        // Declararlo dentro del body
        //visitPrimaryConstructor(ctx.primaryConstructor());

        return null;
    }

    public T visitFunctionDeclaration(KotlinParser.FunctionDeclarationContext ctx) {
        print_tabs();
        if (ctx.modifiers() != null) {
            visitModifiers(ctx.modifiers());
        }
        System.out.print("func ");
        if (ctx.typeParameters() != null) {
            System.out.print(ctx.typeParameters().getText() + " ");
        }
        if (ctx.receiverType() != null) {
            System.out.print(ctx.receiverType().getText() + " ");
        }
        System.out.print(ctx.simpleIdentifier().getText());
        boolean parenthesize = ctx.functionValueParameters().getText().charAt(0) != '(';
        if (parenthesize) System.out.print("(");
        System.out.print(ctx.functionValueParameters().getText());
        if (parenthesize) System.out.print(")");
        if (ctx.type_() != null) {
            if (!ctx.type_().getText().equals("Unit")) {
                System.out.print("-> " + ctx.type_().getText());
            }
        }
        if (ctx.typeConstraints() != null) {
            System.out.print(ctx.typeConstraints().getText());
        }
        if (ctx.functionBody() != null) {
            System.out.println("{");
            nested_level++;
            visitFunctionBody(ctx.functionBody());
            nested_level--;
            print_tabs();
            System.out.println("}");
        } else {
            System.out.println();
        }
        return null;
    }
}

