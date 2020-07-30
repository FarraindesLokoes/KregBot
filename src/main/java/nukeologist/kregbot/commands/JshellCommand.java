package nukeologist.kregbot.commands;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import jdk.jshell.execution.JdiExecutionControlProvider;
import jdk.jshell.execution.LocalExecutionControl;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControlProvider;
import jdk.jshell.spi.ExecutionEnv;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import org.objectweb.asm.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class JshellCommand {

    private static final Map<Long, JShell> SHELLS = new HashMap<>();

    private static final List<Pattern> blacklist;
    private static final List<Pattern> whitelist;

    private static Pattern add(final String pattern) {
        return Pattern.compile(pattern);
    }

    static {
        blacklist = List.of(
                add("sun"),
                add("jdk"),
                add("java.lang.reflect"),
                add("java.lang.invoke"),
                add("java.util.concurrent"),
                add("nukeologist.kregbot"),
                add("java.lang.ProcessBuilder"),
                add("java.lang.ProcessHandle"),
                add("java.lang.Runtime"),
                add("io.github.classgraph"),
                add("java.io"),
                add("net.dv8tion"),
                add("org.slf4j"),
                //add("java.lang.System#exit"),
                add("java.lang.Thread"),
                add("java.lang.System")
                //add("java.lang.Thread#sleep"),
                //add("java.lang.Thread#wait"),
                //add("java.lang.Thread#notify"),
                //add("java.lang.Thread#currentThread"),
                //add("java.lang.Thread#start")
        );
        whitelist = List.of(
                add("java.lang.invoke.CallSite"),
                add("java.util.EnumSet"),
                add("java.util.EnumMap"),
                add("java.lang.CharacterName"),
                add("java.text.DecimalFormatSymbols")
                //add("java.util.concurrent.atomic"),
                //add("java.util.concurrent.Concurrent.*"),
                //add("java.util.concurrent..*Queue"),
                //add("java.util.concurrent.CopyOnWrite.*"),
                //add("java.util.concurrent.ThreadLocalRandom.*")
        );
    }

    private static JShell getOrCreate(long id) {
        JShell shell = SHELLS.get(id);
        if (shell == null) {
            shell = create();
            SHELLS.put(id, shell);
        }
        return shell;
    }

    private static JShell create() {
        return JShell
                .builder()
                .executionEngine(new SandboxProvider(), Map.of())
                .build();
    }

    @Command(value = "jshell", type = ContextType.GUILD)
    public static void shell(Context ctx) {
        final var raw = ctx.getMessage().getContentRaw();
        final var ev = raw.substring(raw.indexOf("jshell") + "jshell ".length());
        final var shell = getOrCreate(ctx.getMember().getGuild().getIdLong());
        try {
            var list = shell.eval(ev);
            for (SnippetEvent e : list) {
                final var value = e.value();
                if (value != null && !"".equals(value)) ctx.send(value);
            }
        } catch (UnsupportedOperationException e) {
            //e.printStackTrace();
            ctx.send("Error: tried to access a " + e.getMessage());
        }
    }

    @CommandHelp("jshell")
    public static void shellHelp(Context ctx) {
        ctx.send("Run a Jshell evaluation. Each server has it's own. Example: !jshell 9 + 10");
    }

    private static boolean matches(List<Pattern> patterns, String input) {
        return patterns.stream().anyMatch(pattern -> pattern.matcher(input).matches());
    }


    private static boolean isBlocked(String input) {
        return matches(blacklist, input) && !matches(whitelist, input);
    }

    private static boolean isWhitelisted(String input) {
        return matches(whitelist, input);
    }

    private static class SandboxProvider implements ExecutionControlProvider {

        private final JdiExecutionControlProvider provider = new JdiExecutionControlProvider();

        @Override
        public String name() {
            return "sandbox";
        }

        @Override
        public ExecutionControl generate(ExecutionEnv env, Map<String, String> parameters) throws Throwable {
            ExecutionControl exControl = provider.generate(env, parameters);
            SandboxExecutionControl sandControl = new SandboxExecutionControl();

            return (ExecutionControl) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class[]{ExecutionControl.class},
                    new SandboxProxy(sandControl, exControl)
            );
        }
    }

    private static class SandboxExecutionControl extends LocalExecutionControl {
        @Override
        public void load(ClassBytecodes[] cbcs)
                throws ClassInstallException, NotImplementedException, EngineTerminationException {
            for (ClassBytecodes bytecodes : cbcs) {
                var classReader = new ClassReader(bytecodes.bytecodes());
                classReader.accept(new ClassVisitor(Opcodes.ASM7) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                     String signature,
                                                     String[] exceptions) {
                        return new FilteringMethodVisitor();
                    }
                }, 0);
            }

            super.load(cbcs);
        }


        private boolean isPackageOrParentBlocked(String sanitizedPackage) {
            if (sanitizedPackage == null || sanitizedPackage.isEmpty()) {
                return false;
            }
            if (isBlocked(sanitizedPackage)) {
                return true;
            }

            int nextDot = sanitizedPackage.lastIndexOf('.');

            return nextDot >= 0 && isPackageOrParentBlocked(sanitizedPackage.substring(0, nextDot));
        }


        private class FilteringMethodVisitor extends MethodVisitor {

            private FilteringMethodVisitor() {
                super(Opcodes.ASM7);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
                                        boolean isInterface) {
                checkAccess(owner, name);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                checkAccess(owner, name);
            }

            private void checkAccess(String owner, String name) {
                String sanitizedClassName = sanitizeClassName(owner);

                if (isBlocked(sanitizedClassName)) {
                    throw new UnsupportedOperationException("naughty class: " + sanitizedClassName);
                }
                if (isBlocked(sanitizedClassName + "#" + name)) {
                    throw new UnsupportedOperationException(
                            "naughty method: " + sanitizedClassName + "#" + name
                    );
                }

                if (isWhitelisted(sanitizedClassName) || isWhitelisted(sanitizedClassName + "#" + name)) {
                    return;
                }

                if (isPackageOrParentBlocked(sanitizedClassName)) {
                    throw new UnsupportedOperationException("naughty package: " + sanitizedClassName);
                }
            }

            private String sanitizeClassName(String owner) {
                return owner.replace("/", ".");
            }

            @Override
            public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
                                               Object... bootstrapMethodArguments) {
            }
        }
    }

    private static class SandboxProxy implements InvocationHandler {

        private SandboxExecutionControl target;
        private ExecutionControl exControl;

        private SandboxProxy(SandboxExecutionControl target,
                             ExecutionControl exControl) {
            this.target = target;
            this.exControl = exControl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("load".equals(method.getName())
                    && method.getParameterTypes()[0] == ExecutionControl.ClassBytecodes[].class
                    && args.length != 0) {

                target.load((ExecutionControl.ClassBytecodes[]) args[0]);
            }

            try {
                return method.invoke(exControl, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }
}
