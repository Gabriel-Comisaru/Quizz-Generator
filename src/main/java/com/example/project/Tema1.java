package com.example.project;
import java.io.*;
import java.text.DecimalFormat;

public class Tema1 {
	public static void main(final String[] args)
	{
		int id = 1;
		if(args == null) {
			System.out.print("Hello world!");
		}
		else {
			// switch handler pentru fiecare functie
			switch (args[0]) {
				case "-create-user":
						User.createUser(args);
						break;
				case "-create-question":
						Question.createQuestion(args, id);
						break;
				case "-get-question-id-by-text": {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					if (args.length < 4) {
						System.out.println("{'status':'error','message':'No question text provided'}");
					} else {
						String text = (args[3].split("'")[1]);
						int id_q = 0;
						// cautam id-ul intrebarii
						try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
							String line;
							line = br.readLine();
							while (line != null) {
								String text1 = line.split(",")[0];
								if (text1.equals(text)) {
									id_q = Integer.parseInt(line.split(":")[1]);
									break;
								}
								line = br.readLine();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (id_q == 0) {
							System.out.println("{'status':'error','message':'Question does not exist'}");
						} else {
							System.out.println("{'status':'ok','message':'" + id_q + "'}");
						}
					}
					break;
				}
				case "-get-all-questions" : {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					int lines_no = 0;
					// calculam numarul de intrebari
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
						while (br.readLine() != null) {
							lines_no++;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					// array unde vom stoca intrebarile
					Question[] questions = new Question[lines_no];
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
						String line;
						line = br.readLine();
						while (line != null) {
							String text = line.split(",")[0];
							int id_q = Integer.parseInt(line.split(":")[1]);
							Question q = new Question(text, id_q);
							questions[id_q - 1] = q;
							line = br.readLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (questions.length > 0) {
						System.out.print("{'status':'ok','message':'[");
						for (int i = 0; i < questions.length; i++) {
							if (i == questions.length - 1) {
								System.out.print(questions[i].toStringEnd());
							} else {
								System.out.print(questions[i].toString());
							}
						}
						System.out.print("]'}");
					}
					break;
				}
				case "-create-quizz" :
						Quizz.createQuizz(args);
						break;
				case "-get-quizz-by-name" : {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					String name = (args[3].split("'")[1]);
					//cautam quizz-ul
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Quizz.csv"))) {
						String line;
						line = br.readLine();
						while (line != null) {
							String quizz_name1 = line.split("@")[1];
							if (quizz_name1.equals(name)) {
								int id_q = Integer.parseInt(line.split(":")[1]);
								System.out.println("{'status':'ok','message':'" + id_q + "'}");
								return;
							}
							line = br.readLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("{'status':'error','message':'Quizz does not exist'}");
					break;
				}
				case "-get-all-quizzes" : {
					User user = new User();
					user = user.verifyCredentials(args);
					Quizz q = new Quizz();
					if (user == null)
						return;
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Quizz.csv"))) {
						String line;
						line = br.readLine();
						int next;
						if (line != null) {
							System.out.print("{'status':'ok','message':'[");
						}
						while (line != null) {
							next = 0;
							String user_quizz = (line.split("@")[0]).substring(1, line.split(" ")[0].length());
							if (user_quizz.equals(user.getUsername())) {
								String quizz_name = line.split("@")[1];
								int id_q = Integer.parseInt(line.split(":")[1]);
								next = 1;
								q.setId(id_q);
								q.setName(quizz_name);
								if ((line = br.readLine()) != null) {
									System.out.print(q);
								} else {
									System.out.print(q.toStringEnd());
								}
							}
							if (next == 0) {
								line = br.readLine();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("]'}");
					break;
				}
				case "-get-quizz-details-by-id" : {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					String quizz_id = (args[3].split("'")[1]);
					Quizz quizz = new Quizz();
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Quizz.csv"))) {
						String line;
						line = br.readLine();
						while (line != null) {
							if (line.split(":")[1].equals(quizz_id)) {
								String quizz_name = line.split("@")[1];
								String questions_no = line.split("@")[2];
								Question[] questions = new Question[questions_no.split(",").length - 1];
								String questions_string = questions_no.split(":")[0];
								for (int i = 0; i < questions_no.split(",").length - 1; i++) {
									String question_body = questions_string.split(",")[i];
									String question_text = question_body.split(";")[0];
									String question_type = question_body.split(";")[1];
									int question_id = Integer.parseInt((question_body.split(";")[2]).substring(0, 1));
									questions[i] = new Question(question_text, question_id, question_type);
								}
								quizz = new Quizz(user, questions, quizz_name, Integer.parseInt(quizz_id));
								break;
							}
							line = br.readLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.print("{'status':'ok','message':'[");
					int question_id;
					int id_q = 1;
					for (Question q : quizz.getQuestions()) {
						question_id = q.getId();
						System.out.print("{\"question-name\":\"" + q.getText() + "\", \"question_index\":\"" + q.getId() + "\", \"question_type\":\"" + q.getType() + "\", \"answers\":\"[");
						try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
							String line;
							line = br.readLine();
							while (line != null) {
								if (line.split(",")[0].equals(q.getText())) {
									for (int i = 2; i < line.split(",").length - 1; i++) {
										if (i == line.split(",").length - 2) {
											System.out.print("{\"answer_name\":\"" + (line.split(",")[i]).split("-")[0] + "\", \"answer_id\":\"" + id_q + "\"}");
										} else {
											System.out.print("{\"answer_name\":\"" + (line.split(",")[i]).split("-")[0] + "\", \"answer_id\":\"" + id_q + "\"}, ");
										}
										id_q++;
									}
								}
								line = br.readLine();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (question_id == (quizz.getQuestions()).length) {
							System.out.print("]\"}");
						} else {
							System.out.print("]\"}, ");
						}
					}
					System.out.println("]'}");
					break;
				}
				case "-delete-quizz-by-id" : {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					if (args.length == 3) {
						System.out.print("{'status':'error','message':'No quizz identifier was provided'}");
						return;
					}
					// deschidem fisierul Quizz si unul temporar
					File inputFile = new File("./src/main/java/com/example/project/Quizz.csv");
					File tempFile = new File("./src/main/java/com/example/project/Quizz_temp.csv");

					try {
						BufferedReader reader = new BufferedReader(new FileReader(inputFile));
						BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
						// aflam id-ul quizz-ului pe care vrem sa il stergem
						String lineToRemove = (args[3].split(" ")[1]).substring(1, args[3].split(" ")[1].length() - 1);
						String currentLine;
						int gasit = 0;
						// citim fiecare chestionar din fisierul Quizz si l copiem in fisierul temporar mai putin chestionarul pe care vrem sa l stergem
						while ((currentLine = reader.readLine()) != null) {
							String DeletedLine = currentLine.split(":")[1];
							if (DeletedLine.equals(lineToRemove)) {
								gasit = 1;
								continue;
							}
							writer.write(currentLine + System.getProperty("line.separator"));
						}
						writer.close();
						reader.close();
						// stergem fisierul initial si fisierul temporar devine fisierul initial
						boolean succesful = inputFile.delete();
						if (succesful) {
							succesful = tempFile.renameTo(inputFile);
						}
						if (!succesful)
							return;
						if (gasit == 0) {
							System.out.println("{'status':'error','message':'No quiz was found'}");
						} else {
							System.out.println("{'status':'ok','message':'Quizz deleted successfully'}");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case "-submit-quizz" : {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					if (args.length == 3) {
						System.out.print("{'status':'error','message':'No quizz identifier was provided'}");
						return;
					}
					String quizz_id = (args[3].split("'")[1]);
					int gasit = 0;
					Quizz quizz = new Quizz();
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Quizz.csv"))) {
						String line;
						line = br.readLine();
						while (line != null) {
							String quizz_id1 = line.split(":")[1];
							if (quizz_id1.equals(quizz_id)) {
								if ((((line.split("@")[0]).split(" ")[0]).substring(1)).equals(user.getUsername())) {
									System.out.println("{'status':'error','message':'You cannot answer your own quizz'}");
									return;
								}
								String quizz_text = line.split("@")[1];
								Question[] quizz_questions = new Question[(line.split(",").length) - 1];
								for (int i = 0; i < line.split(",").length - 1; i++) {
									quizz_questions[i] = new Question(((line.split("@")[2]).split(",")[i]).split(";")[0]);
								}
								gasit = 1;
								quizz = new Quizz(user, quizz_questions, quizz_text, Integer.parseInt(quizz_id));
								break;
							}
							line = br.readLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (gasit == 0) {
						System.out.println("{'status':'error','message':'No quiz was found'}");
						return;
					} else {
						int next;
						float score = 0.0f;
						int out_of_range;
						Question[] questions = quizz.getQuestions();
						for (int i = 0; i < args.length - 4; i++) {
							String answer = (args[i + 4].split("'")[1]);
							out_of_range = 1;
							for (int j = 0; j < questions.length; j++) {
								next = 0;
								// in functie de id-ul raspunsului pe care il da utilizatorul mergem la intrebarea specifica
								// ex: daca id-ul rasp e 3 si prima intrebare are 2 rasp, trecem la a doua intrebare(id-ul 3 e primul raspuns de la a 2a intrebare)
								if (out_of_range == 0) {
									j--;
								}
								try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Question.csv"))) {
									String line;
									line = br.readLine();
									out_of_range = 0;
									while (line != null) {
										if (line.split(",")[0].equals(questions[j].getText())) {
											if (((line.split(",")).length - 3) < Integer.parseInt(answer)) {
												out_of_range = 1;
												int answer_int = Integer.parseInt(answer);
												answer_int -= ((line.split(",")).length - 3);
												answer = Integer.toString(answer_int);
												break;
											}
											float pondere_corect = 0.0f;
											float pondere_gresit = 0.0f;
											for (int k = 2; k < line.split(",").length - 1; k++) {
												if (((line.split(",")[k]).split("-")[1]).equals("1")) {
													pondere_corect++;
												} else
													pondere_gresit++;
											}
											pondere_corect = 1 / pondere_corect;
											pondere_gresit = 1 / pondere_gresit;
											String answer_verif = (line.split(",")[Integer.parseInt(answer) + 1]).split("-")[1];
											if (answer_verif.equals("1")) {
												next = 1;
												score += pondere_corect;
												break;
											} else {
												next = 1;
												score -= pondere_gresit;
												break;
											}
										}
										line = br.readLine();
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
								if (next == 1) {
									break;
								}
							}
						}
						score = (score / questions.length) * 100;
						// cazul in care scorul este negativ
						if (score < 0) {
							score = 0;
						}
						// rotunjim scorul
						DecimalFormat f = new DecimalFormat("##");
						System.out.println("{'status':'ok','message': '" + f.format(score) + " points'}");
						user.setScore((int) score);
					}
					int scris = 0;
					int prima_linie = 0;
					try (
							BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/com/example/project/Solutions.csv"));
							BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/java/com/example/project/Solutions.csv", true));
							BufferedWriter bw = new BufferedWriter(writer);
							PrintWriter out = new PrintWriter(bw)) {
						String line = reader.readLine();
						while (line != null) {
							prima_linie = 1;
							if (line.split(",")[0].equals(user.getUsername())) {
								for (int i = 1; i < line.split(",").length; i += 3) {
									if (line.split(",")[i].equals(quizz.getId())) {
										System.out.println("{'status':'error','message': 'You already submitted this quizz'}");
										return;
									}
								}
								out.print("," + quizz.getId() + "," + quizz.getName() + "," + user.getScore());
								scris = 1;
							}
							line = reader.readLine();
						}
						if (scris == 0 && prima_linie == 0) {
							out.println(user.getUsername() + "," + quizz.getId() + "," + quizz.getName() + "," + user.getScore());
						} else if (scris == 0) {
							out.print(user.getUsername() + "," + quizz.getId() + "," + quizz.getName() + "," + user.getScore());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case "-get-my-solutions" : {
					User user = new User();
					user = user.verifyCredentials(args);
					if (user == null)
						return;
					try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/example/project/Solutions.csv"))) {
						String line;
						line = br.readLine();
						System.out.print("{'status':'ok','message':'");
						while (line != null) {
							String username = line.split(",")[0];
							if (username.equals(user.getUsername())) {
								for (int i = 1; i < line.split(",").length; i += 3) {
									if (i + 3 == line.split(",").length) {
										System.out.print("[{\"quiz-id\" : \"" + line.split(",")[i] + "\", \"quiz-name\" : \"" + line.split(",")[i + 1] +
												"\", \"score\" : \"" + line.split(",")[i + 2] + "\", \"index_in_list\" : \"" + (i / 3 + 1) + "\"}]");
									} else {
										System.out.print("[{\"quiz-id\" : \"" + line.split(",")[i] + "\", \"quiz-name\" : \"" + line.split(",")[i + 1] +
												"\", \"score\" : \"" + line.split(",")[i + 2] + "\", \"index_in_list\" : \"" + (i / 3 + 1) + "\"}], ");
									}
								}
							}
							line = br.readLine();
						}
						System.out.println("'}");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case "-cleanup-all" : {
					try {
						PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/Users.csv");
						writer.print("");
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/Question.csv");
						writer.print("");
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/Quizz.csv");
						writer.print("");
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/Solutions.csv");
						writer.print("");
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}