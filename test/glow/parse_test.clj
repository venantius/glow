(ns glow.parse-test
  (:require [clojure.test :refer :all]
            [glow.parse :as parse]))

;; what else is there?
(deftest keyword-edge-cases-parse
  (is (= (parse/parse ":nil") '(:file (:form (:literal (:keyword ":nil"))))))
  (is (= (parse/parse ":1") '(:file (:form (:literal (:keyword ":1"))))))
  (is (= (parse/parse ":a%/a") '(:file (:form (:literal (:keyword ":a%/a"))))))
  (is (= (parse/parse ":##a") '(:file (:form (:literal (:keyword ":##a"))))))
  (is (= (parse/parse ":true") '(:file (:form (:literal (:keyword ":true")))))))

(deftest gensym-parse
  (is (= (parse/parse "gensym#") '(:file (:form (:reader_macro (:gensym "gensym" "#")))))))

(deftest char-parse
  (is (= (parse/parse "(int \\;)")
         '(:file (:form (:list
                         "("
                         (:forms
                          (:form
                           (:literal (:symbol (:simple_sym "int"))))
                          (:form (:whitespace " "))
                          (:form (:literal (:character (:any_char "\\;")))))
                         ")"))))))

(deftest corpus
  (testing "Let's just pull in a large amount of source code and see what breaks"
    (parse/parse (slurp (.getResourceAsStream
                         (clojure.lang.RT/baseLoader)
                         "clojure/core.clj")))
    (parse/parse (slurp (.getResourceAsStream
                         (clojure.lang.RT/baseLoader)
                         "clojure/repl.clj")))
    (parse/parse (slurp (.getResourceAsStream
                         (clojure.lang.RT/baseLoader)
                         "clojure/main.clj")))
    (parse/parse (slurp (.getResourceAsStream
                         (clojure.lang.RT/baseLoader)
                         "clojure/core_print.clj")))
    (parse/parse (slurp (.getResourceAsStream
                         (clojure.lang.RT/baseLoader)
                         "clojure/data.clj")))
    (parse/parse (slurp (.getResourceAsStream
                         (clojure.lang.RT/baseLoader)
                         "clojure/test.clj")))))
