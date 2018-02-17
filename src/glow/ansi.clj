(ns glow.ansi)

(def csi "\u001b[")

(def suffix "m")

(def reset-font
  (str csi suffix))

(defn default
  [s]
  (str reset-font s))

(defn black
  [s]
  (str csi 30 suffix s reset-font))

(defn red
  [s]
  (str csi 31 suffix s reset-font))

(defn green
  [s]
  (str csi 32 suffix s reset-font))

(defn yellow
  [s]
  (str csi 33 suffix s reset-font))

(defn blue
  [s]
  (str csi 34 suffix s reset-font))

(defn magenta
  [s]
  (str csi 35 suffix s reset-font))

(defn cyan
  [s]
  (str csi 36 suffix s reset-font))

(defn white
  [s]
  (str csi 37 suffix s reset-font))

(defn bright-red
  [s]
  (str csi 91 suffix s reset-font))

(defn bright-green
  [s]
  (str csi 92 suffix s reset-font))

(defn bright-yellow
  [s]
  (str csi 93 suffix s reset-font))

(defn bright-blue
  [s]
  (str csi 94 suffix s reset-font))

(defn bright-magenta
  [s]
  (str csi 95 suffix s reset-font))

(defn bright-cyan
  [s]
  (str csi 96 suffix s reset-font))

(defn bright-white
  [s]
  (str csi 97 suffix s reset-font))
