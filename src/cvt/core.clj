(ns cvt.core
  (:import
   (org.opencv.highgui Highgui))
  (:require
   [clj-exif-orientation.core :as ceo]
   [clojure.string :as str]
   [clojure.java.io :as io]))

(defn imread-reoriented
  ""
  [image-path]
  (let [temp-file "/tmp/cvt-reoriented.jpg"]
    (ceo/without-exif (io/file image-path) (io/file temp-file))
    (Highgui/imread temp-file)))

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
