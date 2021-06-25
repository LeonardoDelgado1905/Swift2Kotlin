public class VisitorKotlin<T> extends KotlinParserBaseVisitor<T>{

    @Override
    public T visitExpression(KotlinParser.ExpressionContext ctx) {
        System.out.println();
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
        System.out.print(ctx.postfixUnaryExpression().getText());
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
}

