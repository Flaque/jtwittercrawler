#!/bin/sh

rm -rf data/*
rm -rf nohup.out

mvn clean compile -Dmaven.test.skip=true -o

echo 'cleaned :-)'
