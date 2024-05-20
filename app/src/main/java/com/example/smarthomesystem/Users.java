package com.example.smarthomesystem;

public class Users {

    String username,pass, email,phone,birthdate,country,city,photoUrl;

    public Users(){

    }

    public Users(String user, String pass, String mail, String phone, String birthdate, String country, String city) {
        this.username = user;
        this.pass = pass;
        this.email = mail;
        this.phone = phone;
        this.birthdate = birthdate;
        this.country = country;
        this.city = city;

    }
    public String getphotoUrl() {
        return photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
