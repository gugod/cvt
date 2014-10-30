(ns cvt.core
  (:require
   [clojure.string :as str]
   [cvt.face-detection :as face-detection]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn face-detect
  "I can do face detection: lein run -m cvt.core/face-detect ~/Pictures/lena.png"
  [image-path]
  (let [face-bbox (face-detection/detection-to-string (face-detection/face-detect image-path))]
    (if (not (str/blank? face-bbox))
      (println (str/join " " [image-path face-bbox])))))
