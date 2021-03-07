# UCalendar
JavaFX 15 Calendar Application Using Maven build Tool

## Requirments
1.Download and Install [Maven](https://maven.apache.org/download.cgi).

2.Download [JDK 11 or later](http://jdk.java.net/) for your operating system. 

3.Ensure `JAVA_HOME` is set to the JDK installation directory. Reference-Guide ([Linux, Mac, Windows](https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux))


## Overview

U-Calendar is a desktop calendar application specifically designed for students to facilitate managing multiple courses. 
U-Calendar not only provides the user with an optimized schedule and considers various constraints, but it also integrates 
a grade calculator for each course to assist users in calculating their current mark before going into the final exam.
With U-Calendar, it is easier to create courses and add a proper class schedule that starts and ends with the semester. 
U-Calendar understands most student needs. U-Calendar is designed to be the all-in-one stop for students to plan their 
day, manage their schedule, and find information about their courses.

## How to run the application

### Windows/Linux/Mac

If you run on Windows,Linux or Mac, follow these steps:

    cd UCalendar
    mvn clean javafx:run

## How to build an executable version of UCalendar

### Linux / Mac

If you build on Linux or Mac, follow these steps:

    cd UCalendar
    mvn clean javafx:jlink
    target/UCalendar/bin/launcher

### Windows

If you run on Windows, follow these steps:

    cd UCalendar
    mvn clean javafx:jlink
    target\UCalendar\bin\launcher

### TODO List

Ayman :
- Main Application
- Login Screen
- SidePanel
- Task View 
- Week View
- Month View 
- Add Event
- Create Grid
- Edit / Delete events

Moe : Edited Add Event (adding the new tables)

Kamel : Create The Calculator 

