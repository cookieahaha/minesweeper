#!/bin/sh

# CLASS=Matrix
# CLASS=UI
CLASS=GridPanel

java -classpath target/classes me.kukkii.minesweeper.${CLASS} $*
