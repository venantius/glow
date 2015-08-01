(ns glow.regex-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [glow.regex :as regex]))

(deftest match-regex-works
  (let [a (slurp (io/resource "test/regex/regexes/example_1.txt"))
        b (slurp (io/resource "test/regex/regexes/example_2.txt"))
        c (slurp (io/resource "test/regex/regexes/example_3.clj"))]
    (is (= "#\"\\\"\"" (regex/match-regex a)))
    (is (= "#\"(regex)\"" (regex/match-regex b)))
    (is (= "#\"\"" (regex/match-regex c)))))

(deftest match-string-works
  (let [a (slurp (io/resource "test/regex/strings/example"))
        b (slurp (io/resource "test/regex/strings/example2"))
        c (slurp (io/resource "test/regex/strings/example_3.clj"))
        d (slurp (io/resource "test/regex/strings/example4"))
        e (slurp (io/resource "test/regex/strings/example_5.clj"))]
    (is (= "\"test\"" (regex/match-string a)))
    (is (nil? (regex/match-string b)))
    (is (= "\"test \\\" asdf \"" (regex/match-string c)))
    (is (= "\"will be an\nexample\"" (regex/match-string d)))
    (is (= "\"Given two strings, split the former on the first occurrence of the latter.\"" (regex/match-string e)))))

(deftest match-comment-works
  (let [a (slurp (io/resource "test/regex/comments/example_1.clj"))
        b (slurp (io/resource "test/regex/comments/example_2.clj"))]
    (is (= ";; some profound stuff goes here\n" (regex/match-comment a)))
    (is (= "; this is another example here\n" (regex/match-comment b)))))

(deftest match-keyword-works
  (let [a "(ns basic\n  (:require clojure.java.io))"
        b "(def b ::this:is:an:example)"
        c "(ns example-the-third)\n\n(def with-symbols :afwe!234$*-_+=<>:a)"
        d "this is a :b single letter keyword"]
    (is (= ":require" (regex/match-keyword a)))
    (is (= "::this:is:an:example" (regex/match-keyword b)))
    (is (= ":afwe!234$*-_+=<>:a" (regex/match-keyword c)))
    (is (= ":b" (regex/match-keyword d)))))

(deftest match-s-exp-works
  (let [a "(def x true)"
        b "[5]"
        c "{:a :b}"
        d "a)b"
        e "asd ] "
        f " :b} * 2"]
    (is (= "(" (regex/match-s-exp a)))
    (is (= "[" (regex/match-s-exp b)))
    (is (= "{" (regex/match-s-exp c)))
    (is (= ")" (regex/match-s-exp d)))
    (is (= "]" (regex/match-s-exp e)))
    (is (= "}" (regex/match-s-exp f)))))

(deftest match-nil-works
  (is (= "nil" (regex/match-nil "nil")))
  (is (= "nil" (regex/match-nil "(if nil false)"))))

(deftest match-bool-works
  (is (= "true" (regex/match-bool "true")))
  (is (= "true" (regex/match-bool "(if true true)")))
  (is (= "false" (regex/match-bool "false")))
  (is (= "false" (regex/match-bool "(if false false)"))))

(deftest match-number-works
  (is (= "1" (regex/match-number "asdf 1")))
  (is (= "1" (regex/match-number "a 1 b")))
  (is (= "2" (regex/match-number "a 2 3 b")))
  (is (= "3.2" (regex/match-number "a 3.2 x")))
  (is (= "0.4" (regex/match-number "a 0.4 y")))
  (is (= "0.8" (regex/match-number "0.8 08"))))

(deftest match-special-works
  (is (= "monitor-exit" (regex/match-special "(monitor-exit ...)")))
  (is (= "set!" (regex/match-special "set! foo bar"))))

(deftest match-reader-char-works
  (is (= "~@" (regex/match-reader-char " ~@ a")))
  (is (= "`" (regex/match-reader-char "`(def x 5)")))
  (is (= "@" (regex/match-reader-char " @(future 5)")))
  (is (= "~" (regex/match-reader-char " ~(conj [] a)")))
  (is (= "^" (regex/match-reader-char " (defn foo [^Throwable tr] tr)")))
  (is (= "@" (regex/match-reader-char " @ a")))
  (is (= "'" (regex/match-reader-char " 'asfd ")))
  (is (= "&" (regex/match-reader-char " (defn foo [x & y] x)"))))

(deftest match-definition-works
  (is (= "clojure.core/defn-" (regex/match-definition "(clojure.core/defn- bar [] true")))
  (is (= "defonce" (regex/match-definition "(defonce bar [])"))))

(deftest match-macro-works
  (is (= "->" (regex/match-macro "(-> 5 (* 2))")))
  (is (= "io!" (regex/match-macro "(io! ...)")))
  (is (= ".." (regex/match-macro "(.. System (getProperties) (get \"os.name\"))"))))

(deftest match-core-fn-works
  (is (= "*" (regex/match-core-fn "(* 1 1)")))
  (is (= "+" (regex/match-core-fn "(+ 1 1)")))
  (is (= "->ArrayChunk" (regex/match-core-fn "(->ArrayChunk [])")))
  (is (= "neg?" (regex/match-core-fn "(neg? -5")))
  (is (= "empty?" (regex/match-core-fn "(empty? {})")))
  (is (= "empty" (regex/match-core-fn "(empty [])"))))

(deftest match-variable-works
  (is (= "*1" (regex/match-variable "(def bar *1)")))
  (is (= "clojure.core/*print-length*" (regex/match-variable "clojure.core/*print-length*"))))

(deftest match-conditional-works
  (is (= "case" (regex/match-conditional "(case ...")))
  (is (= "clojure.core/cond->" (regex/match-conditional "(clojure.core/cond->")))
  (is (= "cond->>" (regex/match-conditional "cond->>")))
  (is (= "clojure.core/when-some" (regex/match-conditional "clojure.core/when-some"))))

(deftest match-repeat-works
  (is (= "doseq" (regex/match-repeat "(doseq ..)")))
  (is (= "clojure.core/dotimes" (regex/match-repeat "(clojure.core/dotimes ...)")))
  (is (= "while" (regex/match-repeat "while ..."))))

(deftest match-exception-works
  (is (= "catch" (regex/match-exception "(catch Exception e (println e))")))
  (is (= "finally" (regex/match-exception "(finally ...)")))
  (is (= "throw" (regex/match-exception "(throw ...)")))
  (is (= "try" (regex/match-exception "(try ..."))))
