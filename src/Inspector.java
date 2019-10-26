import java.lang.reflect.*;
import java.util.HashMap;

public class Inspector {

    private int numIndents = 0;
    private HashMap<Integer, String> modifiers = null;

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        if (modifiers == null) initModifiers();

        System.out.println("======================================================");
        String identifierName = c.isInterface() ? "Interface " : "Class ";
        System.out.println(getSpacing(numIndents, true) + "• " + identifierName + "Name: " + c.getSimpleName());
        System.out.println("• Super Class: " + c.getSuperclass().getSimpleName());

        Class currClass = c.getSuperclass();
        while (currClass != null && !currClass.getSimpleName().equals("Object")) {
            numIndents++;
            printInfo(currClass, obj, recursive);
            currClass = currClass.getSuperclass();
        }

        if (!c.getSuperclass().getSimpleName().equals("Object"))
            System.out.println("------------------------------------------------------");

        // print interfaces
        Class[] interfaces = c.getInterfaces();
        System.out.println("• Interfaces: " + formatArrWithSeparator(interfaces));

        // recurse interfaces
        if (interfaces.length > 0) {
            int i = 0;
            currClass = interfaces[i];
            numIndents = 0;
            while (true) {
                numIndents++;
                printInfo(currClass, obj, recursive);
                currClass = currClass.getSuperclass();

                if (currClass == null || currClass.getSimpleName().equals("Object")) {
                    i++;
                    if (i >= interfaces.length) break;
                    else {
                        currClass = interfaces[i];
                    }
                }
            }
            System.out.println("------------------------------------------------------");
        }

        printConstructors(c, 0);
        printMethods(c, 0);
        printFields(c, obj, 0, recursive);
    }

    private void printConstructors(Class c, int numIndents) {
        Constructor[] constructors = c.getDeclaredConstructors();
        for (Constructor cons : constructors) {
            System.out.print(getSpacing(numIndents, true));
            System.out.println("• Constructor name: " + cons.getName());
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Parameter types: " + formatArrWithSeparator(cons.getParameterTypes()));
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Modifiers: " + modifiers.get(cons.getModifiers()));
        }
    }

    private void printMethods(Class c, int numIndents) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            System.out.print(getSpacing(numIndents, true));
            System.out.println("• Method name: " + method.getName());
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Exceptions thrown: " + formatArrWithSeparator(method.getExceptionTypes()));
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Parameter types: " + formatArrWithSeparator(method.getParameterTypes()));
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Return type: " + method.getReturnType().getSimpleName());
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Modifiers: " + modifiers.get(method.getModifiers()));
        }
    }

    private void printFields(Class c, Object obj, int numIndents, boolean recursive) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            System.out.print(getSpacing(numIndents, true));
            System.out.println("• Field name: " + field.getName());
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Field type: " + field.getType().getSimpleName());
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Modifiers: " + modifiers.get(field.getModifiers()));
            field.setAccessible(true);
            try {
                System.out.print(getSpacing(numIndents, true));
                if (field.getType().isArray()) {
                    System.out.println("  Array values: " + getArrayValues(field.get(obj)));
                } else {
                    System.out.println("  Current value: " + field.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    String getArrayValues(Object obj) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (true) {
            try {
                builder.append(Array.get(obj, i));
                builder.append(", ");
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
            i++;
        }

        return builder.toString();
    }

    String getSpacing(int numIndents, boolean isTab) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numIndents; i++) {
            if (isTab) builder.append("\t");
            else builder.append(" ");
        }
        return builder.toString();
    }

    String formatArrWithSeparator(Class[] classes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < classes.length; i++) {
            builder.append(classes[i].getSimpleName());
            if (i != classes.length - 1) builder.append(", ");
        }
        String result = builder.toString();
        if (result.isEmpty()) return "none";
        else return result;
    }

    private void printInfo(Class currClass, Object obj, boolean recursive) {
        System.out.println("------------------------------------------------------");
        String identifierName = currClass.isInterface() ? "Interface " : "Class ";
        System.out.println(getSpacing(numIndents, true) + "• " + identifierName + "Name: " + currClass.getSimpleName());
        String superClassName = currClass.getSuperclass() == null ? "none" : currClass.getSuperclass().getSimpleName();
        System.out.println(getSpacing(numIndents, true) + "• Super Class: " + superClassName);
        printConstructors(currClass, numIndents);
        printMethods(currClass, numIndents);
        printFields(currClass, obj, numIndents, recursive);
    }

    private void initModifiers() {
        modifiers = new HashMap<>();
        modifiers.put(Modifier.PUBLIC, "PUBLIC");
        modifiers.put(Modifier.PRIVATE, "PRIVATE");
        modifiers.put(Modifier.PROTECTED, "PROTECTED");
        modifiers.put(Modifier.STATIC, "STATIC");
        modifiers.put(Modifier.FINAL, "FINAL");
        modifiers.put(Modifier.SYNCHRONIZED, "SYNCHRONIZED");
        modifiers.put(Modifier.VOLATILE, "VOLATILE");
        modifiers.put(Modifier.TRANSIENT, "TRANSIENT");
        modifiers.put(Modifier.NATIVE, "NATIVE");
        modifiers.put(Modifier.INTERFACE, "INTERFACE");
        modifiers.put(Modifier.STRICT, "STRICT");
    }
}