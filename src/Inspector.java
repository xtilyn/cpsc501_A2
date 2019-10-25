import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
        System.out.println("Name: " + c.getSimpleName());
        ArrayList<String> superClasses = new ArrayList<>();
        System.out.println("Super Class: " + c.getSuperclass().getSimpleName());

        Class currClass = c.getSuperclass();
        while (currClass != null && !currClass.getSimpleName().equals("Object")) {
            numIndents++;
            System.out.println("------------------------------------------------------");
            System.out.println(getSpacing(numIndents, true) + "Name: " + currClass.getSimpleName());
            System.out.println(getSpacing(numIndents, true) + "Super Class: " + currClass.getSuperclass().getSimpleName());
            printConstructors(currClass, numIndents);
            printMethods(currClass, numIndents);
            printFields(currClass, numIndents, recursive);
            currClass = currClass.getSuperclass();
        }
    }

    private void printConstructors(Class c, int numIndents) {
        Constructor[] constructors = c.getConstructors();
        int n = constructors.length;
        for (int i = 0; i < n; i++) {
            Constructor cons = constructors[i];
            System.out.print(getSpacing(numIndents, true));
            System.out.println("Constructor name: " + cons.getName());
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Parameter types: " + printArrWithSeparator(cons.getParameterTypes()));
            System.out.print(getSpacing(numIndents, true));
            System.out.println("  Modifiers: " + modifiers.get(cons.getModifiers()));
        }
    }

    private void printMethods(Class c, int numIndents) {
        // TODO
    }

    private void printFields(Class c, int numIndents, boolean recursive) {
        // TODO
    }

    private String getSpacing(int numIndents, boolean isTab) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numIndents; i++) {
            if (isTab) builder.append("\t");
            else builder.append(" ");
        }
        return builder.toString();
    }

    private String printArrWithSeparator(Class[] classes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < classes.length; i++) {
            builder.append(classes[i].getSimpleName());
            if (i < classes.length) builder.append(", ");
        }
        String result = builder.toString();
        if (result.isEmpty()) return "none";
        else return result;
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