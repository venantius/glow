(ns glow.html
  "Functions for generating styled HTML"
  (:require [garden.core :as garden]
            [hiccup.core :as hiccup]
            [instaparse.core :as insta]))

(defn span-generator
  [x]
  (fn [y]
    [:span {:class x} (apply str y)]))

(defn macro-span-generator
  [& args]
  (list [:span {:class "reader-char"} (first args)]
        (rest args)))

(defn reverse-macro-span-generator
  [& args]
  (list (first args)
        [:span {:class "reader-char"} (rest args)]))

(defn collection-span
  [& args]
  (list [:span {:class "s-exp"} (first args)]
        (rest (butlast args))
        [:span {:class "s-exp"} (last args)]))

(defn hiccup-transform
  [d]
  (insta/transform
   {:simple_sym str
    :simple_keyword str
    :macro_keyword str
    :ns_symbol str

    ;; literals
    :literal identity
    :string (span-generator "string")
    :regex (span-generator "regex")
    :number (span-generator "number")
    :character (span-generator "character")
    :nil (span-generator "nil")
    :boolean (span-generator "boolean")
    :keyword (span-generator "keyword")
    :symbol (span-generator "symbol") ;; move to special keywords
    :param_name (span-generator "keyword")

     ;; reader macro characters
    :reader_macro identity
    :quote macro-span-generator
    :backtick macro-span-generator
    :unquote macro-span-generator
    :unquote_splicing macro-span-generator
    :tag macro-span-generator
    :deref macro-span-generator
    :gensym reverse-macro-span-generator
    :lambda macro-span-generator
    :meta_data macro-span-generator
    :var_quote macro-span-generator
    :host_expr macro-span-generator
    :discard macro-span-generator
    :dispatch macro-span-generator

     ;; top level
    :file (fn [& args] (concat args))
    :forms (fn [& args] (identity args))
    :form (fn [& args] (concat args))

     ;; collections
    ; :map colorize-collection
    :list collection-span
    :vector collection-span
    ; :set colorize-collection

     ;; extras
    :comment (comp str)
    :whitespace str}
   (vec d)))

(defn generate-html
  [d]
  (hiccup/html [:div {:class "syntax"}
                [:pre (hiccup-transform d)]]))

(defn to-div
  "Convert a keyword like :body to :.body."
  [k]
  (->> k name (str ".") keyword))

(defn css-color
  "Generate a garden-style css element with color styles."
  [[k v]]
  [(to-div k) {:color v}])

(defn generate-css
  "Given a colorscheme, generate a CSS file for that colorscheme."
  [c]
  (garden/css [:.syntax [:pre
                         {:background (:background c)}
                         (into [] (map css-color c))]]))
