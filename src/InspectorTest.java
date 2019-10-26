import org.junit.jupiter.api.Test;

public class InspectorTest {

    @Test
    public void shouldPrintArrayValuesCorrectly() {
        int[] sample = {1,2,3,4,5};
        Inspector inspector = new Inspector();
        String result = inspector.getArrayValues(sample);
        assert (result.equals("1, 2, 3, 4, 5, "));
    }

    @Test
    public void shouldGiveCorrectTabIndents() {
        int numberOfIndents = 4;
        Inspector inspector = new Inspector();
        String result = inspector.getSpacing(numberOfIndents, true);
        assert (result.equals("\t\t\t\t"));
    }

    @Test
    public void shouldPrintClassSimpleNamesWithCommaSeparator() {
        Class[] classes = new Class[]{ClassA.class, ClassB.class, ClassC.class, ClassD.class};
        Inspector inspector = new Inspector();
        String result = inspector.formatArrWithSeparator(classes);
        System.out.println(result);
        assert (result.equals("ClassA, ClassB, ClassC, ClassD"));
    }

}
