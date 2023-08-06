package org.example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class NumberTest {
    @Test
    public void testNumber() throws Exception {
        //定义脚本
        String script="println(\"Hello, AviatorScript!\");";
        //编译
        Expression exp4 = AviatorEvaluator.getInstance().compile(script);
        //执行
        exp4.execute();

        //获取路径
        ClassPathResource resource = new ClassPathResource("script/number.av");
        String scriptPath = resource.getPath();
        //编译
        Expression exp = AviatorEvaluator.getInstance().compileScript(scriptPath);
        //执行
        exp.execute();

        String expression = "a-(b-c) > 100";
        Expression compiledExp = AviatorEvaluator.compile(expression);
        //上下文
        double a=100.3,b=45,c= -199.100;
        Map<String, Object> context=new HashMap<>();
        context.put("a",a);
        context.put("b",b);
        context.put("c",c);
        //通过注入的上下文执行
        Boolean result = (Boolean) compiledExp.execute(context);
        System.out.println(result);
    }
}
