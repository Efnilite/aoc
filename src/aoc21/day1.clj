(ns day1
  (:require [clojure.string :as str]))

; part one

(->> (slurp "resources/01.txt")
     (str/split-lines)
     (map #(Integer/parseInt %))
     (partition 2 1)
     (filter #(> (second %) (first %)))
     (count))

; part 2

(->> (slurp "resources/01.txt")
     (str/split-lines)
     (map #(Integer/parseInt %))
     (partition 3 1)
     (map (partial apply +))
     (partition 2 1)
     (filter #(> (second %) (first %)))
     (count))