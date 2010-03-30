clj-tagsoup
===========

This is a HTML parser for Clojure, somewhat akin to Common Lisp's
[cl-html-parse].  It is a wrapper around the [TagSoup] Java SAX
parser, but has a DOM interface.  It is buildable by [Leiningen].

Usage
-----
The two main functions defined by clj-tagsoup are `parse` and `parse-string`.
The first one can take anything accepted by clojure.contrib's [reader] function,
while the second can parse HTML from a string.

The resulting HTML tree is a vector, consisting of:

 1. a keyword representing the tag name,
 2. a map of tag attributes (mapping keywords to strings),
 3. children nodes (strings or vectors of the same format).

There are also utility accessors (`tag`, `attributes`, `children`).

Example
-------

    (parse "http://example.com")
    => [:html {}
              [:head {}
                     [:title {} "Example Web Page"]]
              [:body {}
                     [:p {} "You have reached this web page by typing \"example.com\",\n\"example.net\",\n  or \"example.org\" into your web browser."]
                     [:p {} "These domain names are reserved for use in documentation and are not available \n  for registration. See "
                         [:a {:shape "rect", :href "http://www.rfc-editor.org/rfc/rfc2606.txt"} "RFC \n  2606"]
                         ", Section 3."]]]

Author
------

cl-html-parse was written by [Daniel Janus].

 [cl-html-parse]: http://www.cliki.net/CL-HTML-Parse
 [reader]: http://richhickey.github.com/clojure-contrib/branch-1.1.x/duck-streams-api.html#clojure.contrib.duck-streams/reader
 [Daniel Janus]: http://danieljanus.pl
 [TagSoup]: http://home.ccil.org/~cowan/XML/tagsoup/
 [Leiningen]: http://github.com/technomancy/leiningen