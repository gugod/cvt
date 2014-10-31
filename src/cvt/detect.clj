(ns cvt.detect
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io])
  (:import
   org.opencv.core.Core
   org.opencv.core.Mat
   org.opencv.core.MatOfRect
   org.opencv.core.Point
   org.opencv.core.Rect
   org.opencv.core.Scalar
   org.opencv.highgui.Highgui
   org.opencv.objdetect.CascadeClassifier))

(defn detection-to-bbox
  [detections]
  (doall (map (fn [rect]
                (str/join
                 ","
                 [(.x rect) (.y rect)
                  (+ (.x rect) (.width rect))
                  (+ (.y rect) (.height rect))]))
              (.toArray detections))))

(defn detection-to-string
  [detections]
  (str/join " " (detection-to-bbox detections)))

(defn all_cascades []
  (map (fn [it] (.getPath it)) (filter (fn [it] (.isFile it)) (file-seq (io/file (.getPath (io/resource "cascades")))))))

(defn detect-with-classfier [image classifier]
  (let [detected (atom (MatOfRect.))]
    (.detectMultiScale classifier image @detected)
    @detected))

(defn filename [path] (.getName (io/file path)))

(defn -main
  [image-path]
  (let [image (Highgui/imread image-path)]
    (doall
     (map (fn [it] (println (str/join " " (flatten [image-path it]))))
          (filter
           (fn [it] (not (str/blank? (last it))))
           (map (fn [cascade]
                  [(filename cascade) (detection-to-string (detect-with-classfier image (CascadeClassifier. cascade)))])
                (all_cascades)))))))
