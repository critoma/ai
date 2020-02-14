!#/bin/sh

echo "Try compiling everything...."

javac -d . mwa/data/*.java
javac -d . mwa/gui/*.java
javac -d . mwa/ai/genetic/*.java
javac -d . mwa/ai/neural/*.java
javac -d . mwa/ai/nlp/*.java
javac -d . mwa/agent/*.java


