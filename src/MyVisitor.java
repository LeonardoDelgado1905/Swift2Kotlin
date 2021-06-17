public class MyVisitor<T> extends Swift3BaseVisitor<T> {
    @Override
    public T visitSwitch_statement(Swift3Parser.Switch_statementContext ctx) {
        System.out.println("when ( " + ctx.expression() + " ){");

        System.out.println("}");
        return super.visitSwitch_statement(ctx);
    }
}
