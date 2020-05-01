(ns pl.danieljanus.tagsoup-test
  (:use
   pl.danieljanus.tagsoup
   clojure.test))

(deftest parse-string-test
  (is (= [:html {} [:body {} [:p {} "foo"]]]
         (parse-string "<p>foo</p>"))))
