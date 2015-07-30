(defn split-in-two
  "Given two strings, split the former on the first occurrence of the latter."
  [s split]
  (println "asdf")
  (try
  (clojure.string/split s (re-pattern split) 2)
    (catch Exception e
      (println split))))

