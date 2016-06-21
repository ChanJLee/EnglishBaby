#!/bin/bash
protoc -I=./ --java_out=./app/src/main/java ./EnglishBook.proto