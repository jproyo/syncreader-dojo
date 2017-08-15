# Syncreader demo

[![Build Status](https://travis-ci.org/jproyo/syncreader-dojo.svg?branch=master)](https://travis-ci.org/jproyo/syncreader-dojo.svg?branch=master)

This is a syncrhonizer file reader demo app

## Requirements

- It contains 2 threads and each of one read from a different text file. One text file contains a number (for instance 1, 2, 3, ... ) and the other file a string (for instance Monday, Tuesday, or whatever).

- The thread that manages numbers read the file each 2 sec. while the thread for strings every 1 sec.

- The application shows the combination of the two files in a label.

### Notes

*label* is going to be assumed as if both entries matching files should be grouped somehow. For example:

- if file1 contains:

    1
    2
    3

- and file2 contains:

    monday
    tuesday
    wedenesday

- label should be in some Object container:

        1 - monday
        2 - tuesday
        3 - wedenesday


## Prerequisites
    - JDK 8
    - Maven 3.0.4 or newer


## Run the solution

        To run the solution you should execute maven command as follow

        > mvn clean install

        Enjoy it!!!!!!!!!!!
