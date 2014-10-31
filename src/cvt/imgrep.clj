(ns cvt.imgrep
  (:import org.opencv.highgui.Highgui
           org.opencv.core.Mat
           org.opencv.core.MatOfKeyPoint
           org.opencv.core.MatOfDMatch
           org.opencv.features2d.DescriptorExtractor
           org.opencv.features2d.DescriptorMatcher
           org.opencv.features2d.KeyPoint
           org.opencv.features2d.FeatureDetector))

(defn -main [image-small image-dir]
  (let [detective (FeatureDetector/create FeatureDetector/SURF)
        extractor (DescriptorExtractor/create DescriptorExtractor/SURF)
        matcher   (DescriptorMatcher/create DescriptorMatcher/BRUTEFORCE)
        im-small  (Highgui/imread image-small)
        kp-small  (new MatOfKeyPoint)
        de-small  (new Mat)]
    (.detect detective im-small kp-small)
    (.compute extractor im-small kp-small de-small)
    (.add matcher [de-small])

    (doseq [image-big (filter (fn [it] (.isFile it))
                              (file-seq (clojure.java.io/file image-dir)))]
      (let [im-big  (Highgui/imread (.getPath image-big))
            kp-big  (new MatOfKeyPoint)
            de-big  (new Mat)
            matches (new MatOfDMatch)]
        (.detect  detective im-big kp-big)
        (.compute extractor im-big kp-big de-big)
        (.match matcher de-big matches)
        (if (< 2 (.total matches))
          (let [min-distance (reduce (fn [a b] (if (> a b) b a)) (map (fn [it] (.distance it)) (.toList matches)))
                total-points (.total matches)]
            (if (> 0.1 min-distance)
              (println (str (.getPath image-big) " " total-points " " min-distance)))))))))
