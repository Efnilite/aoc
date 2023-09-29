(ns aoc20.day6
  (:require [clojure.set :as set]
            [clojure.string :as str]))

; part 1

(def parts (-> (slurp "resources/aoc20/day06.txt") (str/split #"\r\n\r\n")))

(->> parts
     (map (comp count distinct str/join str/split-lines))
     (apply +))

; part 2

(->> parts
     (map str/split-lines)
     (map (fn [line] (map seq line)))
     (map #(reduce (fn [x y] (set/intersection (set x) (set y))) %))
     (map count)
     (apply +))