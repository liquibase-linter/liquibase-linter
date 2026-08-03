package io.github.liquibaselinter.config;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.ObjectContext;
import org.apache.commons.jexl3.introspection.JexlPermissions;

public final class Expression {

    private static final JexlEngine ENGINE = new JexlBuilder()
        .permissions(JexlPermissions.RESTRICTED.compose("+liquibase.**", "+io.github.liquibaselinter.**"))
        .create();

    private final JexlExpression jexlExpression;

    private Expression(JexlExpression jexlExpression) {
        this.jexlExpression = jexlExpression;
    }

    public static Expression compile(String expression) {
        return new Expression(ENGINE.createExpression(expression));
    }

    public boolean evaluateBoolean(Object root) {
        return (boolean) jexlExpression.evaluate(new ObjectContext<>(ENGINE, root));
    }

    public String evaluateString(Object root) {
        return (String) jexlExpression.evaluate(new ObjectContext<>(ENGINE, root));
    }
}
