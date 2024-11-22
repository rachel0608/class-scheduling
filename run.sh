#!/bin/bash

# Compile the Java program
javac *.java

# Check if compilation succeeded
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

# Run the Java program with the provided arguments
java StudentAssignment "$1" "$2" "$3"

# Check if the Java program executed successfully
if [ $? -ne 0 ]; then
    echo "Error while running the Java program. Exiting."
    exit 1
fi