;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "The core Clojure language."
      :author "Rich Hickey"}
 clojure.core)

(def unquote)
(def unquote-splicing)

(def
  ^{:arglists '([& items])
    :doc "Creates a new list containing the items."
    :added "1.0"}
  list (. clojure.lang.PersistentList creator))

(def
  ^{:arglists '([x seq])
    :doc "Returns a new seq where x is the first element and seq is
    the rest."
    :added "1.0"
    :static true}

  cons (fn* ^:static cons [x seq] (. clojure.lang.RT (cons x seq))))

;during bootstrap we don't have destructuring let, loop or fn, will redefine later
(def
  ^{:macro true
    :added "1.0"}
  let (fn* let [&form &env & decl] (cons 'let* decl)))

(def
  ^{:macro true
    :added "1.0"}
  loop (fn* loop [&form &env & decl] (cons 'loop* decl)))

(def
  ^{:macro true
    :added "1.0"}
  fn (fn* fn [&form &env & decl]
          (.withMeta ^clojure.lang.IObj (cons 'fn* decl)
                     (.meta ^clojure.lang.IMeta &form))))

(def
  ^{:arglists '([coll])
    :doc "Returns the first item in the collection. Calls seq on its
    argument. If coll is nil, returns nil."
    :added "1.0"
    :static true}
  first (fn ^:static first [coll] (. clojure.lang.RT (first coll))))

(def
  ^{:arglists '([coll])
    :tag clojure.lang.ISeq
    :doc "Returns a seq of the items after the first. Calls seq on its
  argument.  If there are no more items, returns nil."
    :added "1.0"
    :static true}
  next (fn ^:static next [x] (. clojure.lang.RT (next x))))

(def
  ^{:arglists '([coll])
    :tag clojure.lang.ISeq
    :doc "Returns a possibly empty seq of the items after the first. Calls seq on its
  argument."
    :added "1.0"
    :static true}
  rest (fn ^:static rest [x] (. clojure.lang.RT (more x))))

(def
  ^{:arglists '([coll x] [coll x & xs])
    :doc "conj[oin]. Returns a new collection with the xs
    'added'. (conj nil item) returns (item).  The 'addition' may
    happen at different 'places' depending on the concrete type."
    :added "1.0"
    :static true}
  conj (fn ^:static conj
         ([coll x] (. clojure.lang.RT (conj coll x)))
         ([coll x & xs]
          (if xs
            (recur (conj coll x) (first xs) (next xs))
            (conj coll x)))))
