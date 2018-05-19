package mmall.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test02 {
    public static void main(String args[]){
        Exam e=new Exam();
        try {
            Field field1 = e.getClass().getDeclaredField("field1");
            Field field2 = e.getClass().getDeclaredField("field2");field1.setAccessible(true);
            System.out.println("field1: "+field1.get(e));
            field1.set(e,"重新设置一个field1值");
            System.out.println("field1: "+field1.get(e));
            System.out.println("field2: "+field2.get(e));
            field2.set(e,"重新设置一个field2值");
            System.out.println("field2: "+field2.get(e));
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

        try {

            Method method1 = e.getClass().getDeclaredMethod("fun1");
            method1.invoke(e);

            Method method2 = e.getClass().getDeclaredMethod("fun2");
            method2.setAccessible(true);
            method2.invoke(e);

            Method method3 = e.getClass().getDeclaredMethod("fun3",String.class);
            method3.setAccessible(true);
            method3.invoke(e,"fun3的参数");
        } catch (NoSuchMethodException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
