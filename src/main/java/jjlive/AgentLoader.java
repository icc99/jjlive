package jjlive;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import jjlive.Classpath;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Iterator;

public class AgentLoader {
    /**
     * @param args <jarpath1>[<File.pathSeparator><jarpath2>...] [pid]
     *
     * jarpath is the absolute path this directory:
     *
     * {{{
     * jarpath/
     *   scalive.jar
     *
     *   scala-library-2.11.0.jar
     *   scala-compiler-2.11.0.jar
     *   scala-reflect-2.11.0.jar
     *
     *   [Other Scala versions]
     * }}}
     */
    public static void main(String[] args) throws Exception {
//        if (args.length != 1 && args.length != 2) {
//            System.out.println("Arguments: <jarpath1>[<File.pathSeparator><jarpath2>...] [pid]");
//            return;
//        }

        addToolsDotJarToClasspath();

        new Client().openREPL();
//        String jarpaths = args[0];
//        String pid      = args[1];
//        loadAgent(jarpaths, pid);
    }

    /**
     * com.sun.tools.attach.VirtualMachine is in tools.jar, which is not in
     * classpath by default:
     *
     * {{{
     * jdk/
     *   bin/
     *     java
     *     javac
     *   jre/
     *     java
     *   lib/
     *     tools.jar
     * }}}
     */
    private static void addToolsDotJarToClasspath() throws Exception {
        String path = System.getProperty("java.home") + "/../lib/tools.jar";
        Classpath.addPath((URLClassLoader) ClassLoader.getSystemClassLoader(), path);
    }

}
