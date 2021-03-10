package org.openjfx;

public class tableEntry {
    String subevent;
    double grade;
    double outOf;
    double weight;
    double achieved;

    public tableEntry(String subevent, double grade, double outOf, double weight){
        this.subevent=subevent;
        this.grade=grade;
        this.outOf=outOf;
        this.weight=weight;

        achieved= (grade/outOf)*weight;
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

    public double getAchieved() {
        return achieved;
    }
}
