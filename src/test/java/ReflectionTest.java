import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionTest {

    private String stringName = "stringValue";
    private int integer = 0;

    public void extractStringFromField(Class<?> classToInspect, Object o) throws IllegalAccessException {
        Field[] allFields = classToInspect.getDeclaredFields();

        for (Field field : allFields) {
            if (field.getType().isAssignableFrom(String.class)) {
                System.out.println("field.getModifiers(): " + field.getModifiers());
                System.out.println("Fields: " + Modifier.toString(field.getModifiers())); // modifiers
                System.out.println("field.getType().getName(): " + field.getType());  //type var name
                System.out.println("field.getName(): " + field.getName());        //real var name
                field.setAccessible(true);                                //set readable
                System.out.println("field.get(o): " + field.get(o));  //get var values
                field.set(o,"NewstringValue");
                System.out.println("field.get(o): " + field.get(o));  //get var values
                System.out.println("field.toString(): " + field.toString());     //get "String" values
            }
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        ReflectionTest reflectionTest = new ReflectionTest();
        System.out.println("reflectionTest.stringName Before: " + reflectionTest.stringName);
        System.out.println("------------------------------------------------");
        reflectionTest.extractStringFromField(ReflectionTest.class, reflectionTest);
        System.out.println("------------------------------------------------");
        System.out.println("reflectionTest.stringName After: " + reflectionTest.stringName);

    }
}
