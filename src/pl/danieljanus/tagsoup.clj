(ns pl.danieljanus.tagsoup
  (:use (clojure.contrib def duck-streams))
  (:require [clojure.zip :as zip])
  (:import (org.ccil.cowan.tagsoup Parser)
           (org.xml.sax InputSource)))

(defn- attributes-map
  "Converts an Attributes object into a Clojure map,"
  [attrs]
  (into {}
        (map #(vector (keyword (.getQName attrs %)) (.getValue attrs %)) (range (.getLength attrs)))))

(defn tag
  "Returns the tag name of a given HTML tree node as a keyword."
  [node]
  (first node))

(defn attributes
  "Returns the attributes of a given HTML tree node as a Clojure map."
  [node]
  (second node))

(defn children
  "Returns a seq of children nodes of a given HTML tree node."
  [node]
  (rest (rest node)))

(defnk parse
  "Parses a file or HTTP URL.  file may be anything that can be fed
to clojure.contrib.duck-streams/reader.  If strip-whitespace is true
removes empty (whitespace-only) PCDATA from in between the tags, which
makes the resulting tree cleaner."
  [file :strip-whitespace true]
  (with-local-vars [tree (zip/vector-zip []) pcdata ""]
    (let [source (InputSource. (reader file))
          flush-pcdata #(let [data (var-get pcdata)]
                          (when-not (empty? data)
                            (when-not (and strip-whitespace (re-find #"^\s+$" data))
                              (var-set tree (-> tree var-get (zip/append-child data))))
                            (var-set pcdata "")))
          parser (proxy [Parser] []
                   (pcdata [buf offset length]
                     (var-set pcdata (str (var-get pcdata) (String. buf offset length))))
                   (startElement [uri localname qname attrs]
                     (flush-pcdata)
                     (var-set tree (-> tree var-get
                                       (zip/append-child [])
                                       (zip/down)
                                       (zip/rightmost)
                                       (zip/append-child (keyword localname))
                                       (zip/append-child (attributes-map attrs)))))
                   (endElement [uri localname qname]
                     (flush-pcdata)
                     (var-set tree (-> tree var-get zip/up))))]
    (.parse parser source)
    (first (remove string? (zip/root (var-get tree)))))))

(defn parse-string
  "Parses a given string as HTML, passing options to `parse'."
  [s & options]
  (apply parse (java.io.StringReader. s) options))