#!/bin/sh

MAVEN_OPTS='-Xms512m -Xmx2048m'

mvn compile exec:java -Dexec.mainClass="twittercrawler.io.Controller" -Dexec.classpathScope=runtime #> crawl.log.txt
