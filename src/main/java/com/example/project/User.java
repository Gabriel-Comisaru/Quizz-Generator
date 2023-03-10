package com.example.project;

import java.io.*;

public class User {
    private String username = "";

    private String password = "";

    private int score = 0;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User() {}

    public static void createUser(String[] args) {
        if (args.length == 1) {
            System.out.print("{'status':'error','message':'Please provide username'}");
            return;
        }
        if (args.length == 2) {
            System.out.print("{'status':'error','message':'Please provide password'}");
            return;
        }
        String user;
        user = args[1].split("'")[1];
        try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Users.csv"))) {
            String line;
            line = br.readLine();
            while (line != null) {
                String word = line.split(",")[0];
                if (word.equals(user)) {
                    System.out.println("{'status':'error','message':'User already exists'}");
                    return;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String password;
        password = args[2].split("'")[1];
        try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Users.csv", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("{'status':'ok','message':'User created successfully'}");
    }
    public User verifyCredentials(String[] args) {
        User user = new User();
        if (args.length == 1 || args.length == 2 || !(args[1].split(" ")[0]).equals("-u") || !(args[2].split(" ")[0]).equals("-p")) {
            System.out.print("{'status':'error','message':'You need to be authenticated'}");
            return null;
        } else {
            String username = (args[1].split(" ")[1]).substring(1, args[1].split(" ")[1].length() - 1);
            String password = (args[2].split(" ")[1]).substring(1, args[2].split(" ")[1].length() - 1);
            int gasit = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Users.csv"))) {
                String line;
                line = br.readLine();
                while (line != null) {
                    String UN = line.split(",")[0];
                    String PW = line.split(",")[1];
                    if (UN.equals(username) && PW.equals(password)) {
                        gasit = 1;
                        user = new User(UN, PW);
                        break;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (gasit == 0) {
                System.out.println("{'status':'error','message':'Login failed'}");
                return null;
            }
        }
        return user;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }
    protected String getUsername() {
        return username;
    }
    protected String getPassword() {
        return password;
    }
}
