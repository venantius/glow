(ns sample)

(defn func
 [^Throwable x & y]
 (conj {} [:a (+ 1.1 x)]))

(def variable
  @(future
     (if-let [x 5]
       true
       "false")))
