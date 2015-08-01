(ns glow.core-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [glow.ansi :as ansi]
            [glow.core :as core]))

(deftest highlight-works
  (is (= (str (ansi/red "(")
              (ansi/orange "ns")
              " sample"
              (ansi/red ")")
              "\n\n"
              (ansi/red "(")
              (ansi/orange "defn")
              " func\n "
              (ansi/red "[")
              (ansi/red "^")
              "Throwable x "
              (ansi/red "&")
              " y"
              (ansi/red "]")
              "\n "
              (ansi/red "(")
              (ansi/blue "conj")
              " "
              (ansi/red "{")
              (ansi/red "}")
              " "
              (ansi/red "[")
              (ansi/green ":a")
              " "
              (ansi/red "(")
              (ansi/blue "+")
              " "
              (ansi/cyan "1.1")
              " x"
              (ansi/red ")")
              (ansi/red "]")
              (ansi/red ")")
              (ansi/red ")")
              "\n\n"
              (ansi/red "(")
              (ansi/red "def")
              " variable\n  "
              (ansi/red "@")
              (ansi/red "(")
              (ansi/orange "future")
              "\n     "
              (ansi/red "(")
              (ansi/green "if-let")
              " "
              (ansi/red "[")
              "x "
              (ansi/cyan "5")
              (ansi/red "]")
              "\n       "
              (ansi/cyan "true")
              "\n       "
              (ansi/cyan "\"false\"")
              (ansi/red ")")
              (ansi/red ")")
              (ansi/red ")")
              "\n")
         (core/highlight (slurp (io/resource "test/core/sample.clj"))))))
