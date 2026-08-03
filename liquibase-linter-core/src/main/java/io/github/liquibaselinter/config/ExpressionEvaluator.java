package io.github.liquibaselinter.config;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.ObjectContext;
import org.apache.commons.jexl3.introspection.JexlPermissions;

public final class ExpressionEvaluator {

    private static final JexlEngine ENGINE = new JexlBuilder()
        .permissions(JexlPermissions.RESTRICTED.compose("+liquibase.**", "+io.github.liquibaselinter.**"))
        .create();

    private ExpressionEvaluator() {}

    public static JexlExpression compile(String expression) {
        return ENGINE.createExpression(expression);
    }

    public static boolean evaluateBoolean(JexlExpression expression, Object root) {
        return (boolean) expression.evaluate(new ObjectContext<>(ENGINE, root));
    }

    public static String evaluateString(JexlExpression expression, Object root) {
        return (String) expression.evaluate(new ObjectContext<>(ENGINE, root));
    }
}
