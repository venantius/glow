(ns searchlight.ansi
  (:require [io.aviso.ansi :as ansi]))

(defn orange
  [s]
  (str ansi/csi 91 ansi/sgr s ansi/reset-font))

(defn grey
  [s]
  (str ansi/csi 92 ansi/sgr s ansi/reset-font))

(def red ansi/red)
(def green ansi/green)
(def yellow ansi/yellow)
(def blue ansi/blue)
(def magenta ansi/magenta)
(def cyan ansi/cyan)
(def white ansi/white)
