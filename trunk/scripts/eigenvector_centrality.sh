#!/bin/sh

MAVEN_OPTS='-Xms512m -Xmx3072m -server'

mvn compile exec:java -Dexec.mainClass="twittercrawler.reader.EigenvectorCentrality" -o -Dexec.classpathScope=runtime > eigenvector.log.txt
