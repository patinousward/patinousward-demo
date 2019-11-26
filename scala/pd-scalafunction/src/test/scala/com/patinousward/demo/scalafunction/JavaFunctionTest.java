package scala.com.patinousward.demo.scalafunction;

import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

public class JavaFunctionTest {

    public void test01(){
        Function<String, String> metho01 = JavaFunctionTest::metho01;
        BiFunction<JavaFunctionTest, String, String> metho02 = JavaFunctionTest::metho02;
        //static方法，不能使用this::methodname  比如this::metho01 会报编译错误
        //非static 方法，即可使用this::methodname,也可使用类名::methodname 不过返回的函数入参不一样
        Function<String, String> metho022 = this::metho02;
    }

    public static String metho01(String params){
        return null;
    }

    public String metho02(String params){
        return null;
    }
}
