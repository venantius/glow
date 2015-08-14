(ns glow.regex-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [glow.regex :as regex]))

(deftest match-special-works
  (is (true? (regex/match-special "monitor-exit")))
  (is (true? (regex/match-special "set!"))))

(deftest match-definition-works
  (is (true? (regex/match-definition "clojure.core/defn-")))
  (is (true? (regex/match-definition "defonce")))
  (is (false? (regex/match-definition "def"))))

(deftest match-macro-works
  (is (true? (regex/match-macro "->")))
  (is (true? (regex/match-macro "io!")))
  (is (true? (regex/match-macro ".."))))

(deftest match-core-fn-works
  (is (true? (regex/match-core-fn "*")))
  (is (true? (regex/match-core-fn "+")))
  (is (true? (regex/match-core-fn "neg?")))
  (is (true? (regex/match-core-fn "empty?")))
  (is (true? (regex/match-core-fn "empty")))
  (is (false? (regex/match-core-fn "run-tests")))
  (is (false? (regex/match-core-fn "clojure.test"))))

(deftest match-variable-works
  (is (true? (regex/match-variable "*1")))
  (is (true? (regex/match-variable "clojure.core/*print-length*"))))

(deftest match-conditional-works
  (is (true? (regex/match-conditional "case")))
  (is (false? (regex/match-conditional "(case ...")))
  (is (true? (regex/match-conditional "clojure.core/cond->")))
  (is (true? (regex/match-conditional "cond->>")))
  (is (true? (regex/match-conditional "clojure.core/when-some"))))

(deftest match-repeat-works
  (is (true? (regex/match-repeat "doseq")))
  (is (true? (regex/match-repeat "clojure.core/dotimes")))
  (is (true? (regex/match-repeat "while"))))

(deftest match-exception-works
  (is (true? (regex/match-exception "catch")))
  (is (true? (regex/match-exception "finally")))
  (is (true? (regex/match-exception "throw")))
  (is (true? (regex/match-exception "try"))))
