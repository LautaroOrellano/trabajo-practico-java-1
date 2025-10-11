package clases.entidades.users;

import enums.Rol;

public class Customer extends User {

    private long dni;
    private long number;
    private String address;
    private int age;

    public Customer(String name, String lastName, String email, String password,
                    long dni, long number, String address, int age) {
        super(name, lastName, email, password, Rol.CUSTOMER);
        this.dni = dni;
        this.number = number;
        this.address = address;
        this.age = age;
    }

    @Override
    public void getMenu() {
        System.out.println("Menu Usuario: ");
        System.out.println("[1] Ver producto");
        System.out.println("[2] Buscar producto");
        System.out.println("[3] Generar orden");
        System.out.println("[4] Ver mi orden");
        System.out.println("[5] Ver catalogo");
        System.out.println("[6] Ver mi carrito");
        System.out.println("[7] Agregar a mi carrito");
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "dni=" + dni +
                ", number=" + number +
                ", addres='" + address + '\'' +
                ", age=" + age +
                "} " + super.toString();
    }
}
