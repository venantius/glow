(ns glow.html-test
  (:require [clojure.java.io :as io]
            [glow.core :as core]
            [glow.html :as html]))

(defn generate-demo-page
  []
  (spit "demo.css"
        (core/generate-css
          {:s-exp "#dc322f"
           :background "#002b36"
           :symbol "#93a1a1"}))
  (spit "demo.html"
    (str "<html>
         <link rel=\"stylesheet\"
         href=\"demo.css\">"
         (core/highlight-html (slurp (io/resource "test/core/sample.clj")))
         "</html>")))
