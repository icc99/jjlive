import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * <p>
 * <p>
 * </p>
 * <b>Creation Time:</b> 2016年03月30日
 *
 * @author Alex
 * @since dwf 1.1
 */
public class NashornTest {
    public static int age = 10;
    private static String name = "Alex";

    @Test
    public void testEval() throws Exception {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine("-scripting");
        Object ret = engine.eval("var a = 10");
//        System.out.println(ret + ret.getClass().toString());
//        System.out.println(engine.eval("$a"));
    }
    public static void heelo() {
        System.out.println("hello");
    }
    public static int get() {
        return 10;
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            Thread.sleep(100);
        }
    }
}
