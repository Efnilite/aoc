(ns aoc23.day1
     (:require [clojure.string :as str]))

; part 1

(def lines (inputs/lines 1))

(defn line-to-first-last-numbers [line]
  (->> line
       (re-seq #"\d")
       ((juxt first last))
       (apply str)
       (Integer/parseInt)))

(->> lines
     (map line-to-first-last-numbers)
     (apply +))

; part 2

(def text->int {"one" 1 "two" 2 "three" 3 "four" 4 "five" 5 "six" 6 "seven" 7 "eight" 8 "nine" 9})

(def regex (re-pattern (format "(?=(\\d|%s))" (str/join "|" (keys text->int)))))

(defn line-to-first-last-numbers' [line]
  (->> line
       (re-seq regex)
       (map (comp (fn [x] (get text->int x x)) last))
       ((juxt first last))
       (apply str)
       (Integer/parseInt)))

(->> lines
     (map line-to-first-last-numbers')
     (apply +))