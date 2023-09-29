(ns day7)

(def positions (->> (slurp "resources/07.txt")
                    (re-seq #"\d+")
                    (map #(Integer/parseInt %))))

; part 1

(->> (range (apply min positions) (inc (apply max positions)))
     (map (fn [new-pos] (map #(Math/abs (- % new-pos)) positions)))
     (map (partial reduce +))
     (apply min))

; part 2

(->> (range (apply min positions) (inc (apply max positions)))
     (map (fn [new-pos]
            (->> positions
                 (map (fn [current-pos] (Math/abs (- current-pos new-pos))))
                 (map (fn [distance] (/ (* distance (inc distance)) 2)))
                 (reduce +))))
     (apply min))