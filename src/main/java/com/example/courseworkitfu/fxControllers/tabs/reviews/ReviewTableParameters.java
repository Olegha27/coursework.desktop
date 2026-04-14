package com.example.courseworkitfu.fxControllers.tabs.reviews;

import lombok.Getter;

@Getter
public class ReviewTableParameters {
    private final int id;
    private final String reviewer;
    private final String target;
    private final String title;
    private final int rating;
    private final String text;
    private final String reviewDate;

    public ReviewTableParameters(int id, String reviewer, String target, String title, int rating, String text, String reviewDate) {
        this.id = id;
        this.reviewer = reviewer;
        this.target = target;
        this.title = title;
        this.rating = rating;
        this.text = text;
        this.reviewDate = reviewDate;
    }

}