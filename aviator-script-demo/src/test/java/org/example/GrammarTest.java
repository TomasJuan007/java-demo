package org.example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class GrammarTest {
    @Test
    public void testGrammar() throws Exception {
        //获取路径
        ClassPathResource resource = new ClassPathResource("script/grammar.av");
        String scriptPath = resource.getPath();
        //编译
        Expression exp = AviatorEvaluator.getInstance().compileScript(scriptPath);
        //执行
        exp.execute();
    }
}
