package jjlive;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URLClassLoader;

public class Server {
    // Load this Scala version if Scala has not been loaded in the target process
    private static  NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
    private ScriptEngine engine = factory.getScriptEngine("-scripting");

    public void serve(Socket client, String jar) throws Exception {
        InputStream  in  = client.getInputStream();
        OutputStream out = client.getOutputStream();

        InputStream oldIn  = System.in;
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;

        System.setIn(in);
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out));

        try {
            String source = Data.read(in);
            String ret = engine.eval(source)+"\n";
            out.write(ret.getBytes());
            out.flush();
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
            System.setErr(oldErr);
            System.out.println("[Scalive] REPL closed");
            client.close();
        }
    }

    private static void addJarsToURLClassLoader(URLClassLoader cl, String[] jarpaths) throws Exception {
        // Try scala-library first
//        Classpath.findAndAddJar(cl, jarpaths, "scala-library-" + DEFAULT_SCALA_VERSION, "scala.AnyVal");

        // So that we can get the actual Scala version being used
//        String version = Classpath.getScalaVersion(cl);
//
//        Classpath.findAndAddJar(cl, jarpaths, "scala-reflect-"  + version, "scala.reflect.runtime.JavaUniverse");
//        Classpath.findAndAddJar(cl, jarpaths, "scala-compiler-" + version, "scala.tools.nsc.interpreter.ILoop");
//
//        Classpath.findAndAddJar(cl, jarpaths, "scalive", "scalive.Repl");
    }
}
