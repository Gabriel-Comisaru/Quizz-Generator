package com.example.project;

import java.io.*;

public class Question {
    private final String text;
    private final int id;
    private String type;

    public Question(String text, int id, String type) {
        this.text = text;
        this.id = id;
        this.type = type;
    }

    public Question(String text, int id) {
        this.text = text;
        this.id = id;
    }
    public Question(String text) {
        this.text = text;
        this.id = 0;
    }
    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }
    public int getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static void createQuestion(String[] args, int id) {
        User user = new User();
        user = user.verifyCredentials(args);
        if (user == null)
            return;
        String user1 = args[3];
        String parameter = user1.split(" ")[0];
        String text;
        if (parameter.equals("-text")) {
            text = user1.substring(7, user1.length() - 1);
            try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
                String line;
                line = br.readLine();
                while (line != null) {
                    String word_s = line.split(",")[0];
                    String word = line.substring(0, word_s.length());
                    if (word.equals(text)) {
                        System.out.println("{'status':'error','message':'Question already exists'}");
                        return;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Question.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.print(text);
                out.print(",");
                out.print(args[4].split(" ")[1].substring(1, args[4].split(" ")[1].length() - 1));
                out.print(",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("{'status':'error','message':'No question text provided'}");
            return;
        }
        parameter = args[4].split(" ")[0];
        String type;
        if (parameter.equals("-type")) {
            type = args[4].split("'")[1];
            if (type.equals("single") || type.equals("multiple")) {
                if (args.length == 5) {
                    System.out.print("{'status':'error','message':'No answer provided'}");
                    return;
                }
                int i = 0, j;
                int single = 0;
                while (!(parameter = args[5 + i].split(" ")[0]).equals("")) {
                    if (args.length - 5 > 5 * 2) {
                        System.out.print("{'status':'error','message':'More than 5 answers were submitted'}");
                        return;
                    }
                    j = i;
                    if (parameter.length() > 10) {
                        System.out.print("{'status':'error','message':'Answer " + (j + 1) + " has no answer description'}");
                        return;
                    }
                    String answer = args[5 + i].split("'")[1];
                    String answer_sv;
                    try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
                        String line;
                        String save = "";
                        while ((line = br.readLine()) != null) {
                            save = line;
                        }
                        int cont = 2;
                        if (!save.equals("")) {
                            while (!(answer_sv = save.split(",", -1)[cont]).equals("")) {
                                String answer_sv2 = answer_sv.split("-")[0];
                                if (answer_sv2.equals(answer)) {
                                    System.out.println("{'status':'error','message':'Same answer provided more than once'}");
                                    return;
                                }
                                cont++;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String is_correct = args[5 + i + 1].split(" ")[0];
                    if (is_correct.length() < 10) {
                        System.out.print("{'status':'error','message':'Answer " + (j + 1) + " has no answer correct flag'}");
                        return;
                    }
                    String correct = (args[5 + i + 1].split(" ")[1]).substring(1, args[5 + i + 1].split(" ")[1].length() - 1);
                    if (correct.equals("1")) {
                        single++;
                    }
                    try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Question.csv", true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        out.print(answer);
                        out.print("-");
                        out.print(correct);
                        out.print(",");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i += 2;
                    if (5 + i >= args.length) {
                        break;
                    }
                }
                if (single > 1 && type.equals("single")) {
                    System.out.print("{'status':'error','message':'Single correct answer question has more than one correct answer'}");
                    return;
                }
                if (i == 2) {
                    System.out.print("{'status':'error','message':'Only one answer provided'}");
                    return;
                }
                System.out.print("{'status':'ok','message':'Question added successfully'}");
                try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
                    while (br.readLine() != null) {
                        id++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (FileWriter fw = new FileWriter("./src/main/java/com/example/project/Question.csv", true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.print(":" + (id - 1));
                    out.println("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "{\"question_id\" : \"" + this.getId() + "\", \"question_name\" : \"" + this.getText() + "\"}, ";
    }

    public String toStringEnd() {
        return "{\"question_id\" : \"" + this.getId() + "\", \"question_name\" : \"" + this.getText() + "\"}";
    }
}
