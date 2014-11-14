(ns cvt.face-detection
  (:use cvt.core)
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str])
  (:import
   (org.opencv.core Mat MatOfRect)
   (org.opencv.imgproc Imgproc)
   (org.opencv.highgui Highgui)
   (org.opencv.objdetect CascadeClassifier)))

(defn detection-to-string
  [detections]
  (clojure.string/join " "
                       (detection-to-bbox detections)))

(defn face-detect
  [image-path]
  (let [image   (imread-reoriented image-path)
        image2  (Mat.)
        face-classifier (CascadeClassifier. (.getPath (io/file (io/resource "cascades/lbpcascades/lbpcascade_frontalface.xml"))))
        face-detected   (atom [])]

    (if (> (.channels image) 1)
      (do
        (Imgproc/cvtColor image image2 Imgproc/COLOR_RGB2GRAY)
        (Imgproc/equalizeHist image2 image)))

    (reset! face-detected (MatOfRect.))
    (.detectMultiScale face-classifier image @face-detected)
    @face-detected))

(defn -main
  "I can do face detection: lein run -m cvt.face-detection ~/Pictures/"
  [path]
  (doall (map (fn [image-io]
                (let [image-path (.getPath image-io)
                      face-bbox (detection-to-string (face-detect image-path))]
                  (if (not (str/blank? face-bbox))
                    (println (str/join " " [image-path face-bbox])))))
              (image-file-seq path))))
