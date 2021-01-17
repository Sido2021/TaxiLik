package com.taxilik;

public class User {
    int id ,type;
    String firstName , lastName , phone,image ,email ;

    public User(int id, String firstName, String lastName, String phone,String image, int type,String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.type = type;
        this.image = image;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName(){
       return firstName+" "+lastName ;
    }

}
