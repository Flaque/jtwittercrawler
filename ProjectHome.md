This code will let you crawl the Twitter API and build a social network of ties (following, follower). You can then perform social network analysis, like centrality (see twittercrawler.reader.EigenvectorCentrality.java).

For performance, the crawl status of the users is kept in memory (UserCrawlerStatuses.java) and the crawled edges are written to the FS (EdgeWriter.java) but you could use a DB alternatively. I have been crawling at a rate of ~ 250k users / day (using a whitelisted API key) on a single machine.

To get started, use the provided script (sh scripts/crawl.sh) or run the class Controller.java directly from your IDE. You need to setup some seed users in Controller.java and your Twitter API password in TwitterClient.java.

This is a work in progress, just contact me if you have any questions...