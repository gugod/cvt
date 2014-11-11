(ns cvt.core
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]
   ))

(defn image-file-seq
  "file-seq + filter that gives only a sequence of image files."
  [path]
  (filter (fn [it] (and (.isFile it) (re-find #".((?i)jpg|png)$" (.getPath it)))) (file-seq (io/file path))))

(defn detection-to-bbox
  [detections]
  (doall (map (fn [rect]
                (clojure.string/join
                 ","
                 [(.x rect) (.y rect)
                  (+ (.x rect) (.width rect))
                  (+ (.y rect) (.height rect))]))
              (.toArray detections))))
