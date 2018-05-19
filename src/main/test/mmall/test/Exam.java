package mmall.test;

public class Exam {
    private String field1="私有属性";
    public String field2="公有属性";
    public void fun1(){
        System.out.println("fun1:这是一个public访问权限方法");
    }

    private void fun2(){
        System.out.println("fun2:这是一个private访问权限方法");
    }

    private void fun3(String arg){
        System.out.println("fun3:这是一个private访问权限且带参数的方法，参数为："+arg);
    }
}
