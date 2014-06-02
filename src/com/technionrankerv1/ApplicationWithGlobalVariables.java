package com.technionrankerv1;

import android.app.Application;

public class ApplicationWithGlobalVariables extends Application {
	
    private int ratingsSubmitted = 0;
    private boolean loggedIn = false;

	/**
	 * @return the ratingsSubmitted
	 */
	public int getRatingsSubmitted() {
		return ratingsSubmitted;
	}

	/**
	 * @param ratingsSubmitted the ratingsSubmitted to set
	 */
	public void setRatingsSubmitted(int ratingsSubmitted) {
		this.ratingsSubmitted = ratingsSubmitted;
	}
	
	public void incrementRatingsSubmitted() {
		this.ratingsSubmitted++;
	}
	
	/**
	 * 
	 * @return Whether the user submitted fewer than the ratings threshold.
	 */
	public boolean canSubmitRatings() {
		int RATINGS_THRESHOLD = 3;
		return ratingsSubmitted < RATINGS_THRESHOLD;
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

}
