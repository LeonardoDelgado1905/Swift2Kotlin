public class VisitorKotlin<T> extends KotlinParserBaseVisitor<T>{


    int nested_level = 0;

    void print_tabs(){
        for(int i = 0; i < nested_level; ++i){
            System.out.print("\t");
        }
    }

    @Override
    public T visitExpression(KotlinParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public T visitRangeExpression(KotlinParser.RangeExpressionContext ctx) {
        if(ctx.RANGE().size() > 0){
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
        for (int i = 0; i < ctx.unaryPrefix().size();i++){
            System.out.print(ctx.unaryPrefix().get(i).getText()+" ");
        }
        //System.out.print(ctx.postfixUnaryExpression().getText());
        if(ctx.postfixUnaryExpression() != null){
            visitPostfixUnaryExpression(ctx.postfixUnaryExpression());
        }
        return null;
    }

    @Override
    public T visitPostfixUnaryExpression(KotlinParser.PostfixUnaryExpressionContext ctx) {
        if(ctx.primaryExpression().jumpExpression() != null){
            visitJumpExpression(ctx.primaryExpression().jumpExpression());
        }
        else{
            System.out.print(ctx.getText());
            //return super.visitPostfixUnaryExpression(ctx);
        }
        return null;
    }

    @Override
    public T visitJumpExpression(KotlinParser.JumpExpressionContext ctx) {
        print_tabs();
        if(ctx.RETURN() != null){
            System.out.print("return ");
        }
        if(ctx.THROW() != null){
            System.out.print("throw ");
        }
        if(ctx.CONTINUE() != null){
            System.out.print("continue");
        }
        if(ctx.BREAK() != null){
            System.out.print("break");
        }
        if(ctx.expression() != null){
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
        System.out.print(" "+ctx.getText()+" ");
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
        if(ctx.modifiers() != null){
            System.out.print(ctx.modifiers().getText() + " ");
        }
        if(ctx.VAL() != null){
            System.out.print("let ");
        }
        if(ctx.VAR() != null){
            System.out.print("var ");
        }
        if(ctx.multiVariableDeclaration() != null){
            System.out.print(ctx.multiVariableDeclaration().getText() + " ");
        }
        if(ctx.variableDeclaration() != null){
            System.out.print(ctx.variableDeclaration().getText() + " ");
        }
        if(ctx.typeConstraints() != null){
            System.out.print(ctx.typeConstraints().getText()+ " ");
        }
        if(ctx.ASSIGNMENT() != null){
            System.out.print("= ");
        }
        if(ctx.expression() != null){
            System.out.print(ctx.expression().getText() + " ");
        }
        if(ctx.propertyDelegate() != null){
            System.out.print(ctx.propertyDelegate().getText() + " ");
        }
        boolean hasGetter = ctx.getter() != null, hasSetter = ctx.setter() != null;
        if(hasGetter){
            System.out.println("{");
            print_tabs();
            nested_level++;
            visitGetter(ctx.getter());
            if(!hasSetter){
                System.out.println("}");
            }
        }
        if(ctx.setter() != null){
            if(!hasGetter){
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
        if(ctx.ASSIGNMENT() != null){
            System.out.print("return ");
            visitExpression(ctx.expression());
            System.out.println();
        }
        if(ctx.block() !=  null){
            visitBlock(ctx.block());
        }
        return null;
    }

    @Override
    public T visitAssignment(KotlinParser.AssignmentContext ctx) {

        if(ctx.directlyAssignableExpression() != null){
            visitDirectlyAssignableExpression(ctx.directlyAssignableExpression());
            System.out.print(" = ");
            visitExpression(ctx.expression());
        } else{
            visitAssignableExpression(ctx.assignableExpression());
            visitAssignmentAndOperator(ctx.assignmentAndOperator());
            visitExpression(ctx.expression());
        }
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
}

