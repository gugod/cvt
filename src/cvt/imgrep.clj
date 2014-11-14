(ns cvt.imgrep
  (:use cvt.core)
  (:import org.opencv.highgui.Highgui
           org.opencv.core.Mat
           org.opencv.core.MatOfKeyPoint
           org.opencv.core.MatOfDMatch
           org.opencv.features2d.DescriptorExtractor
           org.opencv.features2d.DescriptorMatcher
           org.opencv.features2d.KeyPoint
           org.opencv.features2d.FeatureDetector))

(defn -main [image-small image-dir]
  (let [detective (FeatureDetector/create FeatureDetector/FAST)
        extractor (DescriptorExtractor/create DescriptorExtractor/ORB)
        matcher   (DescriptorMatcher/create DescriptorMatcher/BRUTEFORCE_HAMMING)
        im-small  (imread-reoriented image-small)
        kp-small  (new MatOfKeyPoint)
        de-small  (new Mat)]
    (.detect detective im-small kp-small)
    (.compute extractor im-small kp-small de-small)
    (.add matcher [de-small])

    (println (str "The small image has " (.total de-small) " features"))

    (doseq [image-big (image-file-seq image-dir)]
      (let [im-big  (imread-reoriented (.getPath image-big))
            kp-big  (new MatOfKeyPoint)
            de-big  (new Mat)
            matches (new MatOfDMatch)]
        (.detect  detective im-big kp-big)
        (.compute extractor im-big kp-big de-big)
        (.match matcher de-big matches)
        (if (< 2 (.total matches))
          (let [min-distance (reduce (fn [a b] (if (> a b) b a)) (map (fn [it] (.distance it)) (.toList matches)))
                total-points (.total matches)]
            (if (and (< min-distance 2) (> total-points (* 0.8 (.total de-small))))
              (println (str (.getPath image-big) " " total-points " " min-distance)))))))))
            

            
