(ns aoc20.day1
  (:require [clojure.string :as str]))

; part 1

(def nums (->> (slurp "resources/aoc20/day01.txt")
               (str/split-lines)
               (map #(Integer/parseInt %))))

(->> (for [x nums y nums
           :when (= 2020 (+ x y))]
       (* x y))
     (first))

; part 2

(->> (for [x nums y nums z nums
           :when (= 2020 (+ x y z))]
       (* x y z))
     (first))