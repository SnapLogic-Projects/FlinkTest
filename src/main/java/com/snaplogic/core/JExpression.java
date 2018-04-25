package com.snaplogic.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.EnvironmentScope;
import com.snaplogic.expression.ExpressionUtil;
import com.snaplogic.expression.GlobalScope;
import com.snaplogic.expression.SnapLogicExpression;
import org.apache.flink.types.Row;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static sl.EvaluatorUtils.DEFAULT_VALUE_HANDLER;


public class JExpression implements Serializable {
    private static final LoadingCache<String, SnapLogicExpression> PARSE_TREE_CACHE =
            CacheBuilder.newBuilder()
                    .softValues()
                    .build(new CacheLoader<String, SnapLogicExpression>() {
                        @Override
                        public SnapLogicExpression load(final String key) throws Exception {
                            return ExpressionUtil.compile(key);
                        }
                    });
    private static final GlobalScope GLOBAL_SCOPE = new GlobalScope();
    private static String expression;
    private static SnapLogicExpression snapLogicExpression;
    private static ScopeStack scopes;
    private HashMap<String, Object> jsonData;
    private HashMap<String, Object> envParam;

    private JExpression(){ }

    private static JExpression instance = new JExpression();

    public static JExpression getInstance() {
        return instance;
    }

    public void InitializtionEnvData(Map<String,Object> envData){
        this.jsonData = new HashMap<String, Object>();
        this.envParam = new HashMap<String, Object>();
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new GlobalScope());
        if (envData != null) {
            scopeStack.push(new EnvironmentScope(envData));
        }
        this.scopes = scopeStack;
    }

    public void setExpression(String expression){
        this.expression = expression;
    }

    public boolean eval(Row row) throws Throwable {
        snapLogicExpression = PARSE_TREE_CACHE.get(expression);
        ScopeStack scopeStack;
        if (scopes != null && scopes.getClass() == ScopeStack.class) {
            scopeStack = scopes;
        } else {
            scopeStack = new ScopeStack();
            if (scopes != null) {
                scopeStack.pushAllScopes(scopes);
            } else {
                scopeStack.push(GLOBAL_SCOPE);
            }
        }
        return (Boolean) snapLogicExpression.evaluate(row, scopeStack, DEFAULT_VALUE_HANDLER);

    }
}
