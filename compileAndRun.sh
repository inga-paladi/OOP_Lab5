#!/bin/bash

javac -d classes *.java
pushd classes &> /dev/null
java OOP_LAB5
popd &> /dev/null
