package com.example.project;

import java.io.*;

public class Quizz {

    private Question[] questions;
    private String name;
    private int id;

    private String is_completed = "False";
    public Quizz(){}
    public Quizz(User user, Question[] questions, String name, int id) {
        this.questions = questions;
        this.name = name;
        this.id = id;
    }
    public void setCompleted(String is_completed) {
        this.is_completed = is_completed;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return Integer.toString(id);
    }
    Question[] getQuestions() {
        return questions;
    }

    public static void createQuizz(String[] args) {
        User user = new User();
        user = user.verifyCredentials(args);
        if (user == null)
            return;
        if ((args[3].split(" ")[0]).equals("-name")) {
            String quizz_name = (args[3].split("'")[1]);
            try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Quizz.csv"))) {
                String line;
                line = br.readLine();
                while (line != null) {
                    String quizz_name1 = line.split("@")[1];
                    if (quizz_name1.equals(quizz_name)) {
                        System.out.println("{'status':'error','message':'Quizz name already exists'}");
                        return;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Quizz.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.print("(" + user.getUsername() + " " + user.getPassword() + ")@" + quizz_name + "@");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("{'status':'error','message':'No quizz text provided'}");
            return;
        }
        int lines_no = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
            while (br.readLine() != null) {
                lines_no++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Question[] questions = new Question[lines_no];
        try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
            String line;
            line = br.readLine();
            while (line != null) {
                String text = line.split(",")[0];
                String type = line.split(",")[1];
                int id_q = Integer.parseInt(line.split(":")[1]);
                Question q = new Question(text, id_q, type);
                questions[id_q - 1] = q;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int question_no = args.length - 4;
        if (question_no > 10) {
            System.out.print("{'status':'error','message':'Quizz has more than 10 questions'}");
            return;
        }
        for (int i = 0; i < question_no; i++) {
            if (questions.length - 1 < i) {
                System.out.print("{'status':'error','message':'Question ID for question " + (i + 1) + " does not exist'}");
                return;
            }
            Question q = questions[i];
            try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Quizz.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.print(q.getText() + ";" + q.getType() + ";" + q.getId() + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int id_quizz = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Quizz.csv"))) {
            while (br.readLine() != null) {
                id_quizz++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Quizz.csv", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.print(":" + (id_quizz) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("{'status':'ok','message':'Quizz added succesfully'}");
    }
    @Override
    public String toString() {
        return "{\"quizz_id\" : \"" + this.getId() + "\", \"quizz_name\" : \"" + this.getName() + "\", \"is_completed\" : \"" + this.is_completed + "\"}, ";
    }
    public String toStringEnd() {
        return "{\"quizz_id\" : \"" + this.getId() + "\", \"quizz_name\" : \"" + this.getName() + "\", \"is_completed\" : \"" + this.is_completed + "\"}";
    }
}