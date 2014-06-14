package com.technionrankerv1;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.serverapi.TechnionRankerAPI;

public class MainActivity extends SearchResults {
	private TextView errorM;
	private String username;
	private String password;
	public LinkedHashSet<String> coursesThatDidNotMeetInSpring2014 = new LinkedHashSet<String>();
	private Long currentProfessorId = null;
	private Course currentCourse = null;
	private HashMap<String, String> PLASTandPGRP = new HashMap<String, String>();
	private HashMap<String, Long> lookedUpProfessors = new HashMap<String, Long>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		errorM = (TextView) findViewById(R.id.textView1);
		final EditText passwordInput = (EditText) findViewById(R.id.editText2);
		passwordInput.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					setText();
					return true;
				}
				return false;
			}
		});

		Button loginButton = (Button) findViewById(R.id.button1);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Temporary code to test FragmentMainActivity:
				// Intent i = new Intent(MainActivity.this,
				// FragmentMainActivity.class);
				// i.putExtra("the username", "שלום");
				// startActivity(i);
				setText();
			}
		});

	}

	public void setText() {
		final EditText input1 = (EditText) findViewById(R.id.editText1);
		final EditText input2 = (EditText) findViewById(R.id.editText2);
		username = input1.getText().toString();
		password = input2.getText().toString();
		boolean shouldReturn = false;
		if (username != null && password != null) {
			// Log.d(getLocalClassName(), username);
			// Log.d(getLocalClassName(), password);
			if (username.length() < 9) {
				input1.setError("Student IDs should be 9 digits long.");
				shouldReturn = true;
			}
			if (password.length() < 8) {
				input2.setError("Passcodes should be 8 digits long.");
				shouldReturn = true;
			}
			if (shouldReturn) {
				errorM.setTextColor(getResources().getColor(R.color.red));
				errorM.setText("Please fix the errors and try again.");
				return;
			} else {
				errorM.setTextColor(getResources().getColor(R.color.gray));
				errorM.setText("Please wait.  Connecting to UG System.");
				doLogin();
			}
		}
	}

//	public void parseCatalogPages(String sem) throws IOException {
//		Document catalogDoc;
//		Document existingRatingDoc;
//		List<String> course_nums = null;
//
//		EditText startText = (EditText) findViewById(R.id.editTextStart);
//		EditText endText = (EditText) findViewById(R.id.editTextEnd);
//		
//		int start = Integer.parseInt(startText.getText().toString());
//		int end = Integer.parseInt(endText.getText().toString());
//		if (sem == "201302") {
//			course_nums= new ArrayList<String>(courseNumbers).subList(start, end);
//		}
//		else if (sem.equals("201301")) {
//			course_nums = new ArrayList<String>(coursesThatDidNotMeetInSpring2014);
//		}
//		else {
//			throw new IOException("Invalid semester.");
//		}
//		Long currProfessorId = null;
//		//Remember that this method is getting called twice so we're
//		//making x * 2 requests (winter and spring) and 2 database
//		//calls per request (GetProfessor and InsertCourse).
//		//The database guy recommend 100 database calls at a time.
//		Log.d(getLocalClassName(), "Total # of courses: " + courseNumbers.size());
//		Log.d(getLocalClassName(), "Size: " + course_nums.size() + " Start: " + start + " End: " + end);
//		// Limit the size for Spring semester - but not for Winter semester.
//		for (String courseNum : course_nums) {
//			//Raphi: Log.d(getLocalClassName(), curr++ + " / " + (curr + start));
//			String URL = "https://ug3.technion.ac.il/rishum/course?MK="
//					+ courseNum + "&CATINFO=&SEM=" + sem;
//			Connection.Response catalogRes = Jsoup
//					.connect(URL)
//					.userAgent(
//							"User-Agent: Mozilla/5.0 (Macintosh; Intel"
//									+ " Mac OS X 10.7; rv:29.0) Gecko/20100101 Firefox/29.0")
//					.maxBodySize(0).timeout(600000).execute();
//			catalogDoc = catalogRes.parse();
//			String catalogString = catalogDoc.toString();
//            String ParamCatalogString = catalogString;
//			int plastIndex = catalogString.indexOf("PLAST");
//			int regularProfessorIndex = catalogString.indexOf(">",
//					plastIndex) + 1;
//			int endRegularProfessorIndex = catalogString.indexOf(
//					"<", regularProfessorIndex);
//			String regularProfessorSubstring = catalogString
//					.substring(regularProfessorIndex,
//							endRegularProfessorIndex);
//			final int PROFESSORS_WOULD_BE_MUCH_FURTHER_DOWN_THAN_THIS = 100;
//			if (regularProfessorIndex < PROFESSORS_WOULD_BE_MUCH_FURTHER_DOWN_THAN_THIS
//					|| endRegularProfessorIndex < PROFESSORS_WOULD_BE_MUCH_FURTHER_DOWN_THAN_THIS) {
//				Log.d(courseNum,
//						"The regular professor is empty");
//				coursesThatDidNotMeetInSpring2014
//						.add(courseNum);
//			} else {
//				String[] regularProfessorSplitted = regularProfessorSubstring
//						.split(" ");
//				String professorResult = getHeadProf(regularProfessorSplitted, false);
//				professorResult = StringEscapeUtils.unescapeHtml4(professorResult);
//				if (professorResult == null) {
//					Log.d(courseNum,
//							"Check 2: the regular professor is empty.");
//					coursesThatDidNotMeetInSpring2014
//							.add(courseNum);
//				} else {
//						try {
//
//							if (lookedUpProfessors.containsKey(professorResult)) {
//								Log.d(getLocalClassName(), "The ProfessorID has already been gotten.");
//								currProfessorId = lookedUpProfessors.get(professorResult);
//							}
//							else {
//								GetProfessorClientAsync pas = new GetProfessorClientAsync();
//								List<Professor> p = pas.execute(professorResult).get();
//								if (p.size() > 0) {
//									currProfessorId = p.get(0).getId();
//								}
//								else {
//									continue;
//								}
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						} catch (ExecutionException e) {
//							e.printStackTrace();
//						}
//						
//		                int headParamterIndex = ParamCatalogString.indexOf("PLAST");
//		                String placeholder = ParamCatalogString.substring(
//		                        headParamterIndex - 7, headParamterIndex + 10);
//		                String PGRP = placeholder.substring(0, 2);
//		                String PLAST = placeholder.substring(13, 16);
//		                // Log.d(getLocalClassName(), "course num "
//		                // + courseNum + " PGRP " + PGRP + " PLAST "
//		                // + PLAST);
//		                ParamCatalogString = ParamCatalogString
//		                        .substring(headParamterIndex + 10);
//		                PLASTandPGRP.put(PLAST, PGRP);
//		                String weirdSem = null;
//		                if (sem.equals("201302")) {
//		                	weirdSem = "011302";
//		                }
//		                else {
//		                	weirdSem = "011301";
//		                }
//		                Connection.Response existingRatingRes = Jsoup
//		                        .connect(
//		                                "http://techmvs.technion.ac.il/cics/wmn/wmrns1x?PSEM="
//		                                        + sem + "&PSUB=315018&PGRP=" + PGRP
//		                                        + "&PLAST=" + PLAST)
//		                        .data("SGLID", "YES", "PSEM", weirdSem, "PSUB",
//		                                courseNum, "PGRP", PGRP, "PLAST", PLAST, "ID",
//		                                "922130141", "PASS", "32016463")
//		                        .method(Method.POST).execute();
//		                existingRatingDoc = existingRatingRes.parse();
//		                String existingRatingString = existingRatingDoc.toString();
//
//		                // Log.d(getLocalClassName(), existingRatingString);
//
//		                if (existingRatingString.contains("No lecture and no exercise")) {
//		                    Log.d(getLocalClassName(), "No lecture and no exercise");
//		                } else {
//		                    Log.d(getLocalClassName(),
//		                            "There are lecture and/or exercises");
//		                    if (existingRatingString.contains("<tr>")
//		                            && existingRatingString.length() > 350) {
//		                        int startOfRatings = existingRatingString
//		                                .indexOf("<tr>");
//		                        int endOfRatings = existingRatingString
//		                                .indexOf("<td class=\"l\">");
//		                        // Log.d(getLocalClassName(),startOfRatings+" "+endOfRatings);
//
//		                        // Log.d(getLocalClassName(),
//		                        // startOfRatings+" "+endOfRatings+" "+existingRatingString);
//		                        String ratingTable = existingRatingString.substring(
//		                                startOfRatings, endOfRatings);
//		                        String[] ratingArray = ratingTable.substring(13).split(
//		                                "</td>");
//
//		                        for (int r = 0; r <= 9; r++) {
//		                            if (r == 0) {
//		                                ratingArray[r] = ratingArray[r].trim()
//		                                        .substring(3).trim();
//		                            } else {
//		                                ratingArray[r] = ratingArray[r].trim()
//		                                        .substring(4).trim();
//		                            }
//		                            // Log.d(getLocalClassName(), r + " " +
//		                            // ratingArray[r]);
//		                        }
//
//		                        int numberOfRaters = 0;
//		                        double overall = 0.0;
//		                        double preparedness = 0.0;
//		                        double clarity = 0.0;
//		                        double interactivity = 0.0;
//
//		                        if (ratingArray.length == 11) {
//		                            numberOfRaters = Integer.parseInt(ratingArray[8]);
//		                            overall = Double.parseDouble(ratingArray[7]);
//		                            preparedness = Double.parseDouble(ratingArray[6]);
//		                            clarity = Double.parseDouble(ratingArray[4]);
//		                            interactivity = Double.parseDouble(ratingArray[1]);
//		                        } else if (ratingArray.length == 13) {
////		                            Log.d(getLocalClassName(),
////		                                    Arrays.toString(ratingArray));
//		                            String tempNumRate = ratingArray[10]
//		                                    .substring(5);
//		                            tempNumRate = tempNumRate.substring(tempNumRate.indexOf("<td>"));
//		                            tempNumRate = tempNumRate.substring(5);
//		                            numberOfRaters = Integer.parseInt(tempNumRate);
//		                            preparedness = Double.parseDouble(ratingArray[7]);
//		                            overall = Double.parseDouble(ratingArray[9]);
//		                            clarity = Double.parseDouble(ratingArray[5]);
//		                            interactivity = Double.parseDouble(ratingArray[2]);
//		                        } else {
//		                            Log.d(getLocalClassName(), "Array is not 11 or 13");
//		                        }
//		                        // print all data to screen
////		                        Log.d(getLocalClassName(), "number of raters = "
////		                                + numberOfRaters + " overall = " + overall
////		                                + " preparedness = " + preparedness
////		                                + " clarity = " + clarity + " interactivity = "
////		                                + interactivity);
//		                        if (currProfessorId == null) {
//		                        	Log.d(getLocalClassName(), "Rating has no professor reference.");
//		                        }
//		                        else {
//		                            ProfessorRating pr = new ProfessorRating(null,
//		                            		Long.valueOf(numberOfRaters),
//		                            		currProfessorId, overall, clarity,
//		                            		preparedness, interactivity);
//		                            InsertProfessorRatingClientAsync iprca = new InsertProfessorRatingClientAsync();
//		                            iprca.execute(pr);
//		                        }
//		                        // Log.d(getLocalClassName(), existingRatingString +
//		                        // "");
//		                        /*
//		                        existingRatingString = existingRatingString
//		                                .substring(endOfRatings);
//		                        existingRatingString = existingRatingString
//		                                .substring(existingRatingString.indexOf("<tr>"));
//		                        existingRatingString = existingRatingString
//		                                .substring(5);
//		                        if (existingRatingString.contains("<tr>")) {
//		                            existingRatingString = existingRatingString
//		                                    .substring(existingRatingString
//		                                            .indexOf("<tr>"));
//		                                            */
//		                        }
//		                        // Log.d(getLocalClassName(), existingRatingString);
//		                    }
//		                    // Log.d(getLocalClassName(), "course num "
//		                    // + courseNum + " PGRP " + PGRP + " PLAST "
//		                    // + PLAST);
//		                }
//					}
//				}
//			}

			/* Raphi: This code is for populating Courses and Professors
			int headProfessorIndex = catalogString
					.indexOf(" אחראים");
			int plastIndex = catalogString.indexOf("PLAST");
			int regularProfessorIndex = catalogString.indexOf(">",
					plastIndex) + 1;
			int endRegularProfessorIndex = catalogString.indexOf(
					"<", regularProfessorIndex);
			if (headProfessorIndex != -1) {
				String[] str1 = catalogString.substring(
						headProfessorIndex + 35,
						headProfessorIndex + 100).split(" ");
				//Log.d(getLocalClassName(), Arrays.toString(str1));
				// get the head prof english name
				String parsedHeadProfessor = getHeadProf(str1, true);
				parsedHeadProfessor = StringEscapeUtils.unescapeHtml4(parsedHeadProfessor);
				if (parsedHeadProfessor == null) {
					Log.d(courseNum, "The head professor is empty");
					String regularProfessorSubstring = catalogString
							.substring(regularProfessorIndex,
									endRegularProfessorIndex);
					final int PROFESSORS_WOULD_BE_MUCH_FURTHER_DOWN_THAN_THIS = 100;
					if (regularProfessorIndex < PROFESSORS_WOULD_BE_MUCH_FURTHER_DOWN_THAN_THIS
							|| endRegularProfessorIndex < PROFESSORS_WOULD_BE_MUCH_FURTHER_DOWN_THAN_THIS) {
						Log.d(courseNum,
								"The regular professor is empty");
						coursesThatDidNotMeetInSpring2014
								.add(courseNum);
					} else {
						String[] regularProfessorSplitted = regularProfessorSubstring
								.split(" ");
						//Log.d(getLocalClassName(), "Array: " + Arrays.toString(regularProfessorSplitted));
						String professorResult = getHeadProf(regularProfessorSplitted, false);
						professorResult = StringEscapeUtils.unescapeHtml4(professorResult);
						if (professorResult == null) {
							Log.d(courseNum,
									"Check 2: the regular professor is empty.");
							coursesThatDidNotMeetInSpring2014
									.add(courseNum);
						} else {
							String translatedRegularProfessor = hebrewTranslations
									.get(professorResult);
							if (translatedRegularProfessor == null) {
								insertProfessorGetProfessorAndInsertCourse(professorResult, courseNum);
								Log.d(courseNum,
										"Regular professor: no english name matches the hebrew name: "
												+ professorResult);
							} else {
							//	Log.d("Regular professor "
									//	+ courseNum,
									//	translatedRegularProfessor);
								getCourseAndInsertCourse(professorResult, courseNum);
							}
						}
					}
				} else { // head Prof exists
					Log.d(courseNum, "Head professor exists: " + parsedHeadProfessor);
					String translatedProfessor = hebrewTranslations
							.get(parsedHeadProfessor);
					if (translatedProfessor != null) {
						getCourseAndInsertCourse(parsedHeadProfessor, courseNum);
						//Log.d("Head professor translation:",
					//			translatedProfessor);
					} else {
						insertProfessorGetProfessorAndInsertCourse(parsedHeadProfessor, courseNum);
						Log.d(courseNum,
								"No english name matches the hebrew name: "
										+ parsedHeadProfessor);
					}
				}
			} else {
				Log.d(courseNum, "No head professor or regular professor exists.");
				coursesThatDidNotMeetInSpring2014.add(courseNum);
			}*/
		//Sam: Log.d(getLocalClassName(), PLASTandPGRP.toString());
	
	/* Raphi: For populating Courses and Professors
	public void insertProfessorGetProfessorAndInsertCourse(String hebrewName, String courseNum) {
		try {
			Professor p = new Professor(null, null, null, hebrewName, true);
			InsertProfessorClientAsync ipca = new InsertProfessorClientAsync();
			ipca.execute(p).get();
			getCourseAndInsertCourse(hebrewName, courseNum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void getCourseAndInsertCourse(String professorHebrewName, String courseNumber) {
		try {
			Long professorID;
			if (lookedUpProfessors.containsKey(professorHebrewName)) {
				Log.d(getLocalClassName(), "The ProfessorID has already been gotten.");
				professorID = lookedUpProfessors.get(professorHebrewName);
			}
			else {
				GetProfessorClientAsync pas = new GetProfessorClientAsync();
				List<Professor> p = pas.execute(professorHebrewName).get();
				professorID = p.get(0).getId();
			}
			Course c = courseNumbersToCourses.get(courseNumber);
			c.setProfessorID(professorID);
			InsertCourseClientAsync icca = new InsertCourseClientAsync();
			icca.execute(c);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	*/
	
	public void doLogin() {
		Thread downloadThread = new Thread() {
			public void run() {
				Document doc;
				Document doc1;
				try {
					
					Connection.Response res = Jsoup
							.connect("https://ug3.technion.ac.il/rishum/login")
							.data("OP", "LI", "UID", username, "PWD",
									password, "Login.x",
									"%D7%94%D7%AA%D7%97%D7%91%D7%A8")
							.method(Method.POST).execute();
					doc = res.parse();
					/* Raphi populating:
					parseCatalogPages("201302"); //Spring 2013/2014
					Log.d(getLocalClassName(), "Starting Winter semester.");
					parseCatalogPages("201301"); //Winter 2013/2014
					Log.d(getLocalClassName(), "Done (with existing rating pages).");
					coursesThatDidNotMeetInSpring2014.clear();*/
					// Log in part:
					int x = 1;
					// Log.d(getLocalClassName(), doc.toString().length() + "");
					if (doc.toString().length() < 4920) {
						// Log.d(getLocalClassName(), "fail");
						x = 0;
						reportError(x);
					}

					// Log.d(getLocalClassName(),doc.toString());
					// Log.d(getLocalClassName(), doc.toString().substring(4920,50
					// 4930));
					if (!doc.toString().substring(4609, 4618)
							.equals("error-msg")
							&& x == 1) {
						// myText.getText().toString());

						String temp[] = null;
						Log.d(getLocalClassName(), "Log in Successful");

						temp = doc.toString().substring(5325, 5350).split(" ");

						// Log.d(getLocalClassName(),
						// doc.toString().substring(5325, 5350));
						String name = "visitor";
						if (temp[2] != null && temp[3] != null)
							name = temp[2] + " " + temp[3];
						// Log.d(getLocalClassName(), name);

						// LinearLayout v = (LinearLayout) getLayoutInflater()
						// .inflate(R.layout.welcome_view, null);

						// final TextView myText = (TextView) v
						// .findViewById(R.id.textView23);
						// myText.setText("Welcome " + name + "!");
						// Log.d(getLocalClassName(), "in1");

						String rishum = res.cookie("rishum");
						String _ga = "GA1.3.1829128590.1396009585";
						// Log.d(getLocalClassName(),res.cookies().toString());
						// Log.d(getLocalClassName(), rishum);
						// Log.d(getLocalClassName(), _ga);
						Connection.Response res1 = Jsoup
								.connect(
										"http://techmvs.technion.ac.il:100/cics/wmn/wmnnut03?OP=WK&SEM=201302")
								.cookie("rishum", rishum).method(Method.POST)
								.cookie("_ga", _ga).execute();
						doc1 = res1.parse();
						// Log.d(getLocalClassName(), doc1.toString().length()
						// +"");
						 URL url=res1.url();
						 Log.d(getLocalClassName(), url.toString());
							String tempString=url.toString();
							String[] stringArray=new String[(int)((tempString.length()-50)/7)-2];
							int tempint=50;
							for(int i=0; i<(int)((tempString.length()-50)/7-2);i++){
								stringArray[i]=tempString.substring(tempint, tempint+6);
								tempint=tempint+8;
							}
						((ApplicationWithGlobalVariables) getApplication()).setStudentName(name);
						Intent i = new Intent(MainActivity.this,
								FragmentMainActivity.class);
						// NOTE: if you change this message, also change it
						// in SearchResults - onOptionsItemSelected().
						i.putExtra("the username", name);
						i.putExtra("courseList", stringArray);
						startActivity(i);
						// startActivity(new Intent(Login.this,
						// welcomeView.class));

					} else if (x != 0) {
						Log.d(getLocalClassName(), "Log in unsucessful");
						reportError(1);
						// Log.d(getLocalClassName(),
					}
				}
				catch (IOException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							errorM.setText("");
							Toast.makeText(getApplicationContext(), "Please check your"
									+ " Internet connection.", Toast.LENGTH_LONG).show();						  }
					});
					e.printStackTrace();
				} 
			}
			// OP=LI&UID=922130174&PWD=43150202&Login.x=%D7%94%D7%AA%D7%97%D7%91%D7%A8
			// 32016463
			// 203868179
			// 65374675
		};
		downloadThread.start();
	}

	protected void reportError(final int x) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				errorM.setTextColor(getResources().getColor(R.color.red));
				if (x == 1) {
					errorM.setText("Incorrect username or password. Please try again.");
				} else {
					errorM.setText("The Technion UG website is down. Please try again later.");
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
	}

//	String getHeadProf(String[] s, boolean startOn1) {
//
//		String name = "";
//
//		int x;
//		if (startOn1 == false) x = 0;
//		else x = 1;
//		for (int t = x; t < s.length; t++) {
//			// edit to add the different prefixes
//			if (!s[t].contains(" ")
//					&& !s[t].contains("פרופ")
//					&& !s[t].contains("חבר")
//					&& !s[t].contains("<")
//					&& !s[t].contains(">")
//					&& !s[t].contains("=")					
//					&& !s[t].contains("/")
//					&& !s[t].equals("מר")
//					&& !(s[t].contains("משנה") && !s[t-1].contains("דוד"))
//					&& !s[t].contains("ארכיטקט")
//					&& !(s[t].length() == 3 && s[t].substring(0, 2).contains(
//							"גב") && s[t].substring(2, 3).contains("."))
//					&& !(s[t].length() == 3 && s[t].substring(0, 2).contains(
//							"דר") && s[t].substring(2, 3).contains(".")))
//			{
//				if (s[t].contains("שיץ)")) {
//					//Handling Shlomo (Shitz) Shamai of Electrical Engineering (046733):
//					name = name + " " + StringEscapeUtils.unescapeHtml4("(&#1513;&#1497;&#1509;) &#1513;&#1502;&#1488;&#1497;");
//				}
//				else {
//					name = name + " " + s[t];
//				}
//				if (s[t].contains("<br)")) {
//					break;
//				}
//			}
//		}
//		name = name.trim();
//		if (name.length() == 0)
//			return null;
//		Pattern p = Pattern.compile("\\p{InHebrew}");
//		Matcher m = p.matcher(name.substring(0, 1));
//		if (m.matches()) {
//			return name;
//		}
//		return null;
//	}


	private class GetProfessorClientAsync extends
			AsyncTask<String, Void, List<Professor>> {
		private String currentHebrewName;
		public GetProfessorClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Professor> doInBackground(String... params) {
			String hebrewName = params[0];
			currentHebrewName = hebrewName;
			String encodedHebrewName = StringEscapeUtils.escapeJava(hebrewName);
			Professor lookup = new Professor(null, null, null, encodedHebrewName, true);
			List<Professor> result = new TechnionRankerAPI()
					.getProfessorByProfessorHebrewName(lookup);
			return result;
		}

		@Override
		protected void onPostExecute(List<Professor> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get of professor failed.");
			} else if (res.size() == 0) {
				Log.d(getLocalClassName(), "Get of professor returned empty.");
			} else {
				currentProfessorId = res.get(0).getId();
				Log.d(getLocalClassName(), "The professor id: "
						+ currentProfessorId + " - " + StringEscapeUtils.unescapeJava(res.get(0).getHebrewName()));
				lookedUpProfessors.put(currentHebrewName, currentProfessorId);
			}
		}
	}

	/* Raphi: code for populating courses and professors:
	private class InsertCourseClientAsync extends
			AsyncTask<Course, Void, String> {
		public InsertCourseClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Course... params) {
			String result = null;
			Course courseToAdd = params[0];
			Log.d(getLocalClassName(), courseToAdd.toString());
			List<Course> listToAdd = new ArrayList<Course>();
			listToAdd.add(courseToAdd);
			result = new TechnionRankerAPI().insertCourse(listToAdd)
					.toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Insert course failed.");
			} else {
				Log.d(getLocalClassName(), "Insert course: " + res);
			}
		}
	}

	private class InsertProfessorClientAsync extends
			AsyncTask<Professor, Void, String> {
		public InsertProfessorClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Professor... params) {
			String result = null;
			Professor professorToAdd = params[0];
			String encodedHebrewName = StringEscapeUtils.escapeJava(professorToAdd.getHebrewName());
			professorToAdd.setHebrewName(encodedHebrewName);
			Log.d(getLocalClassName(), "Inserting: " + professorToAdd.getHebrewName());
			List<Professor> lToAdd = new ArrayList<Professor>();
			lToAdd.add(professorToAdd);
			result = new TechnionRankerAPI().insertProfessor(
					lToAdd).toString();
			//This line ensures that no duplicate professors are inserted into the DB.
			hebrewTranslations.put(professorToAdd.getHebrewName(), "");
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Insert professor failed.");
			} else {
				Log.d(getLocalClassName(), "Insert professor: " + res);
			}
		}
	} */
	
	private class InsertProfessorRatingClientAsync extends
	AsyncTask<ProfessorRating, Void, String> {
		public InsertProfessorRatingClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ProfessorRating... params) {
			String result = null;
			ProfessorRating professorRatingToAdd = params[0];
			Log.d(getLocalClassName(), professorRatingToAdd.toString());
			result = new TechnionRankerAPI().insertProfessorRating(
					professorRatingToAdd).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Insert professor rating failed.");
			} else {
				Log.d(getLocalClassName(), "Insert professor rating: " + res);
			}
		}
	}
}
