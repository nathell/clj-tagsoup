[![Clojars Project](http://clojars.org/clj-tagsoup/clj-tagsoup/latest-version.svg)](http://clojars.org/clj-tagsoup/clj-tagsoup)

clj-tagsoup
===========

This is a HTML parser for Clojure, somewhat akin to Common Lisp's
[cl-html-parse].  It is a wrapper around the [TagSoup] Java SAX
parser, but has a DOM interface.  It is buildable by [Leiningen].

Usage
-----

The two main functions defined by clj-tagsoup are `parse` and `parse-string`.
The first one can take anything accepted by clojure.java.io's [reader] function
except for a `Reader`,
while the second can parse HTML from a string.

The resulting HTML tree is a vector, consisting of:

 1. a keyword representing the tag name,
 2. a map of tag attributes (mapping keywords to strings),
 3. children nodes (strings or vectors of the same format).

This is the same format as used by [hiccup], thus the output of `parse` is
appropriate to pass to hiccup.

There are also utility accessors (`tag`, `attributes`, `children`).

clj-tagsoup will automatically use the correct encoding to parse the file if
one is specified in either the HTTP headers (if the argument to `parse` is an
URL object or a string representing one) or a `<meta http-equiv="...">` tag.

clj-tagsoup is meant to parse HTML tag soup, but, in practice, nothing
prevents you to use it to parse arbitrary (potentially malformed)
XML. The `:xml` keyword argument causes clj-tagsoup to take into
consideration the XML header when detecting the encoding.

There are two other options for parsing XML:

 * `parse-xml` just invokes `clojure.xml/parse` with TagSoup, so
   the output format is compatible with `clojure.xml` and is not
   the one described above.
 * `lazy-parse-xml` (introduced in clj-tagsoup 0.3.0) returns a 
   lazy sequence of `Event` records defined by `clojure.data.xml`,
   similarly to the `source-seq` function from that library.

Example
-------

*project.clj*:
```clojure
(defproject clj-tagsoup-example "0.0.1"
  :dependencies [[clj-tagsoup/clj-tagsoup "0.3.0"]])
```

`lein repl`:

```clojure
(use 'pl.danieljanus.tagsoup)
=> nil

(parse "http://example.com")
=> [:html {}
          [:head {}
                 [:title {} "Example Web Page"]]
          [:body {}
                 [:p {} "You have reached this web page by typing \"example.com\",\n\"example.net\",\n  or \"example.org\" into your web browser."]
                 [:p {} "These domain names are reserved for use in documentation and are not available \n  for registration. See "
                     [:a {:shape "rect", :href "http://www.rfc-editor.org/rfc/rfc2606.txt"} "RFC \n  2606"]
                     ", Section 3."]]]
```
                         
FAQ
---

 * Why not just use [Enlive]?
   
   Truth be told, I wrote clj-tagsoup prior to discovering Enlive, which is an excellent library. That said,
   I believe clj-tagsoup has its niche. Here is an _à la carte_ list of differences between the two:
  
   - Enlive is a full-blown templating library; clj-tagsoup just parses HTML (and XML).
   - Unlike Enlive, clj-tagsoup's `parse` function goes out of its way to return parsed data in a proper
     encoding. It will detect the `<meta http-equiv="...">` tag in your data and reinterpret the input
     stream to the indicated encoding as needed.
   - clj-tagsoup boasts a way to lazily parse XML with TagSoup.
   
 * What's with the dependency on stax-utils?
 
   It's for `lazy-parse-xml`. It's needed because that function uses [clojure.data.xml], which under the hood
   uses the StAX API. TagSoup is a SAX parser, so a bridge between the two parsing APIs is needed.

   If you don't use `lazy-parse-xml`, you can optionally exclude stax-utils from your project.clj, like this:
   
        :dependencies [[clj-tagsoup "0.3.0" :exclusions [net.java.dev.stax-utils/stax-utils]]]

Author
------

clj-tagsoup was written by [Daniel Janus].

 [cl-html-parse]: http://www.cliki.net/CL-HTML-Parse
 [clojure.data.xml]: https://github.com/clojure/data.xml
 [reader]: http://richhickey.github.com/clojure-contrib/branch-1.1.x/duck-streams-api.html#clojure.contrib.duck-streams/reader
 [Daniel Janus]: http://danieljanus.pl
 [Enlive]: http://github.com/cgrand/enlive
 [TagSoup]: http://home.ccil.org/~cowan/XML/tagsoup/
 [Leiningen]: http://github.com/technomancy/leiningen
 [hiccup]: http://github.com/weavejester/hiccup
