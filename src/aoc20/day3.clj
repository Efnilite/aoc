(ns aoc20.day3
  (:require [clojure.string :as str]))

; part 1

(def lines (->> (slurp "resources/aoc20/day03.txt")
                (str/split-lines)))

(def grid (->> lines
               (map-indexed (fn [y line] (map-indexed (fn [x char] [[x y] char]) line)))
               (apply concat)
               (into {})))

(def width (count (first lines)))

(defn get-trees [[slopeX slopeY]]
  (->> lines
       (count)
       (range 1)
       (map (fn [t] [(* t slopeX) (* t slopeY)]))
       (map (fn [[x y]] (get grid [(mod x width) y])))
       (filter (partial = \#))
       (count)))

(get-trees [3 1])

; part 2

(->> [[1 1] [3 1] [5 1] [7 1] [1 2]]
     (map get-trees)
     (apply *))