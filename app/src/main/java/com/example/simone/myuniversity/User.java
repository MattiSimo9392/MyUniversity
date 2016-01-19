package com.example.simone.myuniversity;

/**
 * Created by Simone on 13/01/2016.
 */
public class User {private
String matricola;
    String password;

    public User(){                      // Iniializzo le variabili
        matricola = "";
        password = "";
    }

    public User (String a , String b){          // costruttore con input
        matricola =  a;
        password = b;
    }

    public String getMatricola(){
        return matricola;
    }

    public String getPassword(){
        return password;
    }

    public void setMatricola(String a){
        matricola = a;
    }

    public void setPassword(String b){
        password = b;
    }

}
