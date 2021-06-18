public class MyVisitor<T> extends Swift3BaseVisitor<T> {

    int nested_level = 0;

    void print_tabs(){
        for(int i = 0; i < nested_level; ++i){
            System.out.print("\t");
        }
    }

    boolean is_range(String s){
        return s.indexOf("..") != -1;
    }


    @Override
    public T visitSwitch_statement(Swift3Parser.Switch_statementContext ctx) {
        System.out.println("when ( " + ctx.expression().getText() + " ){");
        if(ctx.switch_cases() != null){
            nested_level++;
            visitSwitch_cases(ctx.switch_cases());
            nested_level--;
        }
        System.out.println("}");

        return null;
    }


    @Override
    public T visitSwitch_cases(Swift3Parser.Switch_casesContext ctx) {
        return super.visitSwitch_cases(ctx);
    }

    @Override
    public T visitSwitch_case(Swift3Parser.Switch_caseContext ctx) {
        print_tabs();
        if(ctx.default_label() != null){
            System.out.print("else -> ");
        }
        else{
            visitCase_label(ctx.case_label());
        }
        System.out.println(ctx.statements().getText());
        return null;
    }

    @Override
    public T visitCase_label(Swift3Parser.Case_labelContext ctx) {
        if(is_range(ctx.case_item_list().getText())){
            System.out.print("in ");
            int inf, sup;
            if(ctx.case_item_list().getText().indexOf("<") != -1){
                String[] splt = ctx.case_item_list().getText().split("\\.\\.<");
                boolean vacios = splt[0].equals("") || splt[1].equals("");
                if(splt.length == 2 && !vacios){
                    inf = Integer.parseInt(splt[0]);
                    sup = Integer.parseInt(splt[1]) - 1;
                    System.out.print(inf + ".." + sup);
                }else if(splt.length == 1 || vacios){
                    if(splt[0].equals("") && vacios) splt[0] = splt[1];
                    sup = Integer.parseInt(splt[1]) - 1;
                    String INF = "-9223372036854775807";
                    System.out.print(INF + ".." + sup);
                }
            }
            else{
                String init = ctx.case_item_list().getText();
                String[] splt = init.split("\\.\\.\\.",2);
                boolean vacios = splt[0].equals("") || splt[1].equals("");
                if(splt.length == 2 && !vacios){
                    inf = Integer.parseInt(splt[0]);
                    sup = Integer.parseInt(splt[1]);
                    System.out.print(inf + ".." + sup);
                }else if(splt.length == 1 || vacios){
                    if(splt[0].equals("") && vacios) splt[0] = splt[1];
                    String INF = "9223372036854775807";
                    if(ctx.case_item_list().getText().charAt(0) == '.'){
                        INF = "-" + INF;
                        System.out.print(INF + ".." + splt[0]);
                    }
                    else{
                        System.out.print(splt[0] + ".." + INF);
                    }
                }
            }
        }
        else{
            System.out.print(ctx.case_item_list().getText());
        }
        System.out.print(" -> ");
        return null;
    }

    @Override
    public T visitCase_item_list(Swift3Parser.Case_item_listContext ctx) {
        return super.visitCase_item_list(ctx);
    }

    @Override
    public T visitDefault_label(Swift3Parser.Default_labelContext ctx) {
        return super.visitDefault_label(ctx);
    }

    @Override
    public T visitWhere_clause(Swift3Parser.Where_clauseContext ctx) {
        return super.visitWhere_clause(ctx);
    }

    @Override
    public T visitWhere_expression(Swift3Parser.Where_expressionContext ctx) {
        return super.visitWhere_expression(ctx);
    }

    @Override
    public T visitLabeled_statement(Swift3Parser.Labeled_statementContext ctx) {
        return super.visitLabeled_statement(ctx);
    }

    @Override
    public T visitStatement_label(Swift3Parser.Statement_labelContext ctx) {
        return super.visitStatement_label(ctx);
    }

    @Override
    public T visitLabel_name(Swift3Parser.Label_nameContext ctx) {
        return super.visitLabel_name(ctx);
    }

    @Override
    public T visitControl_transfer_statement(Swift3Parser.Control_transfer_statementContext ctx) {
        return super.visitControl_transfer_statement(ctx);
    }
}
