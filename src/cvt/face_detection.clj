(ns cvt.face-detection
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str])
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
                (clojure.string/join
                 ","
                 [(.x rect) (.y rect)
                  (+ (.x rect) (.width rect))
                  (+ (.y rect) (.height rect))]))
              (.toArray detections))))

(defn detection-to-string
  [detections]
  (clojure.string/join " "
                       (detection-to-bbox detections)))

(defn face-detect
  [image-path]
  (let [image   (Highgui/imread image-path)
        face-classifier (CascadeClassifier. (.getPath (io/file (io/resource "cascades/lbpcascades/lbpcascade_frontalface.xml"))))
        face-detected   (atom [])]
    (reset! face-detected (MatOfRect.))
    (.detectMultiScale face-classifier image @face-detected)
    @face-detected))

(defn -main
  "I can do face detection: lein run -m cvt.face-detection ~/Pictures/lena.png"
  [image-path]
  (let [face-bbox (detection-to-string (face-detect image-path))]
    (if (not (str/blank? face-bbox))
      (println (str/join " " [image-path face-bbox])))))
