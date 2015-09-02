(ns glow.html-test
  (:require [clojure.java.io :as io]
            [glow.core :as core]
            [glow.html :as html]))

(defn generate-demo-page
  []
  (spit "demo.css"
        (core/generate-css))
  (spit "demo.html"
        (str "<html>
         <link rel=\"stylesheet\"
         href=\"demo.css\">"
             (core/highlight-html (slurp
                                   (.getResourceAsStream
                                    (clojure.lang.RT/baseLoader)
                                    "clojure/core.clj")))
             "</html>")))
