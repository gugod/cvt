(ns cvt.people-detect
  (:use cvt.core)
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io])
  (:import
   org.opencv.core.Core
   org.opencv.core.Mat
   org.opencv.core.MatOfRect
   org.opencv.core.MatOfDouble
   org.opencv.core.Point
   org.opencv.core.Rect
   org.opencv.core.Scalar
   org.opencv.highgui.Highgui
   org.opencv.objdetect.HOGDescriptor
   org.opencv.objdetect.CascadeClassifier))

(defn detection-to-string
  [detections]
  (str/join " " (detection-to-bbox detections)))

(defn detect-people
  ""
  [image]
  (let [hog (HOGDescriptor.)
        foundLocations (MatOfRect.)
        foundWeights   (MatOfDouble.)]
    (.setSVMDetector hog (HOGDescriptor/getDefaultPeopleDetector))
    (.detectMultiScale hog image foundLocations foundWeights)
    foundLocations))


(defn -main
  [image-path]
  (let [image (imread-reoriented image-path)]
    (let [it (detection-to-string (detect-people image))]
      (if (not (str/blank? it)) (println (str/join " " [image-path it]))))))
