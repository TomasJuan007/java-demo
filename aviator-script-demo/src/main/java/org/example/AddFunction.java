package org.example;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

public class AddFunction extends AbstractFunction {

    /**
     * 函数调用
     * @param env 当前执行的上下文
     * @param arg1 第一个参数
     * @param arg2 第二个参数
     * @return 函数返回值
     */
    @Override
    public AviatorObject call(Map<String, Object> env,
                              AviatorObject arg1, AviatorObject arg2) {
        Number left = FunctionUtils.getNumberValue(arg1, env);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        //将两个参数进行相加
        return new AviatorDouble(left.doubleValue() + right.doubleValue());
    }

    /**
     * 函数的名称
     * @return 函数名
     */
    public String getName() {
        return "add";
    }
}
