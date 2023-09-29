(ns day5
  (:require [clojure.string :as str]))

(def nums (->> (slurp "resources/05.txt")
               (str/split-lines)
               (map (partial re-seq #"\d+"))
               (map (partial map #(Integer/parseInt %)))))

; part 1

(->> nums
     (filter (fn [coords]
               (or (= (nth coords 0) (nth coords 2))
                   (= (nth coords 1) (nth coords 3)))))
     (map (fn [coords]
            (let [dx [(nth coords 0) (nth coords 2)]
                  dy [(nth coords 1) (nth coords 3)]
                  xs-between (range (apply min dx) (inc (apply max dx)))
                  ys-between (range (apply min dy) (inc (apply max dy)))
                  biggest (max (count xs-between) (count ys-between))]
              (map vector
                   (take biggest (cycle xs-between))
                   (take biggest (cycle ys-between))))))
     (apply concat)
     (frequencies)
     (sort-by val)
     (filter #(< 1 (second %)))
     (count))

; part 2

(->> nums
     (filter (fn [coords]
               (or (= (nth coords 0) (nth coords 2))
                   (= (nth coords 1) (nth coords 3))
                   (= (Math/abs (- (nth coords 0) (nth coords 2)))
                      (Math/abs (- (nth coords 1) (nth coords 3)))))))
     (map (fn [coords]
            (let [dx [(nth coords 0) (nth coords 2)]
                  dy [(nth coords 1) (nth coords 3)]
                  x-step (compare (last dx) (first dx))
                  y-step (compare (last dy) (first dy))
                  x-step (if (zero? x-step) 1 x-step)
                  y-step (if (zero? y-step) 1 y-step)
                  xs-between (range (first dx) (+ x-step (last dx)) x-step)
                  ys-between (range (first dy) (+ y-step (last dy)) y-step)
                  biggest-between (max (count xs-between) (count ys-between))]
              (map vector
                   (take biggest-between (cycle xs-between))
                   (take biggest-between (cycle ys-between))))))
     (apply concat)
     (frequencies)
     (sort-by val)
     (filter #(< 1 (second %)))
     (count))