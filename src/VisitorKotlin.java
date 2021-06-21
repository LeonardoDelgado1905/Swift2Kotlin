public class VisitorKotlin<T> extends KotlinParserBaseVisitor<T>{

    @Override
    public T visitExpression(KotlinParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public T visitRangeExpression(KotlinParser.RangeExpressionContext ctx) {
        System.out.print(ctx.additiveExpression().get(0).getText());
        System.out.print("...");
        System.out.println(ctx.additiveExpression().get(ctx.RANGE().size()).getText());
        return null;
    }
}
