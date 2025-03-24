public class Oop {

    String name;
    int age;
    String address;

    public Oop(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static void main(String[] args) {
        Oop student = new Oop("Ronjohnson Owuor", 20, "427rues1200x");
        System.out.println(student.getName());

    }
}

/* class name should be the same as the constructor name */


