package org.example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class FunctionTest {
    @Test
    public void testGrammar() throws Exception {
        //获取路径
        ClassPathResource resource = new ClassPathResource("script/function.av");
        String scriptPath = resource.getPath();
        //编译
        Expression exp = AviatorEvaluator.getInstance().compileScript(scriptPath);
        //执行
        exp.execute();

        //通通创建一个AviatorEvaluator的实例
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        //注册函数
        instance.addFunction(new AddFunction());
        //执行ab脚本,脚本里调用自定义函数
        Double result = (Double) instance.execute("add(1, 2)");
        //输出结果
        System.out.println(result);
    }
}
