package org.openjfx;

import java.text.DecimalFormat;

public class tableEntry {
    String subevent;
    double grade;
    double outOf;
    double weight;
    String achieved;

    public tableEntry(String subevent, double grade, double outOf, double weight){
        this.subevent=subevent;
        this.grade= grade;
        this.outOf=outOf;
        this.weight=weight;

        DecimalFormat df = new DecimalFormat("#.##");
        achieved= df.format(grade/outOf*weight);
    }

    public String getSubevent() {
        return subevent;
    }

    public void setSubevent(String subevent) {
        this.subevent = subevent;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getOutOf() {
        return outOf;
    }

    public void setOutOf(double outOf) {
        this.outOf = outOf;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAchieved() {
        return achieved;
    }

    public void setAchieved(String achieved) { this.achieved = achieved; }
}