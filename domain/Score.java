package domain;

import java.io.Serializable;

public class Score implements Serializable{

    private String name;
    private int score;

    public Score() {
        this.name = "";
        this.score = -1;
    } // contructor

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int sizeInBytes() {
        return this.name.length() * 2 + 8;
    }

} // end class
