RSS GOSSIP
==========

This is a very, very simple Clojure application to read the contents of an RSS feed and look for a key phrase.

This project is based on [rssgossip](https://github.com/dogriffiths/rssgossip) written in Python and has been adapted to Clojure as a learning project.

## Usage

For example, to look for stories about 'NASA' in the RSS 2.0 sample feed, you can do this:

```bash
export RSS_FEED=https://www.rssboard.org/files/sample-rss-2.xml
lein run 'nasa'
```

Why does the script configure the feed using an environment variable? Because the original Python module was written as
an example program for the book [Head First C](http://shop.oreilly.com/product/0636920015482.do) (great book for learning C btw). The book
needed an example external program that required command line arguments as well as environment variables.

If you want the script to search more than one feed, set `RSS_FEED` to a list of space-separated URLs.

The search string can be a regular expression. So:

```bash
lein run 'mars|jupiter|saturn'
```

will find stories containing any of the three planet names.

## Building

This project uses [Leiningen](https://leiningen.org/) for dependency management and building.

To build the project:

```bash
lein compile
```

To run the application:

```bash
lein run <search-term>
```

## Example

Using the RSS 2.0 sample feed to search for any space-related content:

```bash
export RSS_FEED=https://www.rssboard.org/files/sample-rss-2.xml
lein run 'space|satellite|orbit'
```
