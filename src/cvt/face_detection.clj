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
        eye-classifier  (CascadeClassifier. (.getPath (io/file (io/resource "cascades/haarcascades/haarcascade_eye.xml"))))
        eye-detected    (MatOfRect.)
        face-classifier (CascadeClassifier. (.getPath (io/file (io/resource "cascades/lbpcascades/lbpcascade_frontalface.xml"))))
        face-detected   (MatOfRect.)]

    (if (> (.channels image) 1)
      (do
        (Imgproc/cvtColor image image2 Imgproc/COLOR_RGB2GRAY)
        (Imgproc/equalizeHist image2 image)))

    (.detectMultiScale face-classifier image face-detected)
    (.detectMultiScale eye-classifier  image eye-detected)
    (println (str "face: " (detection-to-string face-detected)))
    (println (str "eye: " (detection-to-string eye-detected)))

    (filter (fn [face-rect]
              (let [fx1 (.x face-rect)
                    fx2 (+ (.x face-rect) (.width face-rect))
                    fy1 (.y face-rect)
                    fy2 (+ (.y face-rect) (.height face-rect))]
                (> (.size (filter (fn [eye-rect]
                                    (let [ex1 (.x eye-rect)
                                          ex2 (+ (.x eye-rect) (.width eye-rect))
                                          ey1 (.y eye-rect)
                                          ey2 (+ (.y eye-rect) (.height eye-rect))]
                                      (> (.size (filter
                                                 (fn [p]
                                                   (and (< fx1 (first p)) (< (first p) fx2)
                                                        (< fy1 (second p)) (< (second p) fy2)))
                                                 [[ex1 ey1] [ex1 ey2] [ex2 ey1] [ex2 ey2]]))
                                         0)))
                                  (.toArray face-detected)))
                   0)))
            (.toArray face-detected))))


(defn -main
  "I can do face detection: lein run -m cvt.face-detection ~/Pictures/"
  [path]
  (doall (map (fn [image-io]
                (let [image-path (.getPath image-io)
                      face-bbox (detection-to-string (face-detect image-path))]
                  (if (not (str/blank? face-bbox))
                    (println (str/join " " [image-path face-bbox])))))
              (image-file-seq path))))
