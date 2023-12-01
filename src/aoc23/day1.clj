(ns aoc23.day1
  (:require [clojure.string :as str]))

; part 1

(def lines (->> (slurp "resources/aoc23/day1")
                (str/split-lines)))

(->> lines
     (map (fn [line]
            (let [nums (re-seq #"\d" line)]
              (Integer/parseInt (str (first nums) (last nums)))
              )))
     (apply +))

; part 2

(def text->int {"one" 1 "two" 2 "three" 3 "four" 4 "five" 5 "six" 6 "seven" 7 "eight" 8 "nine" 9})

(def regex (re-pattern (format "(?=(\\d|%s))" (str/join "|" (keys text->int)))))

(->> lines
     (map (fn [line]
            (let [nums (->> line
                            (re-seq regex)
                            (map last)
                            (map #(get text->int % %)))]
              (Integer/parseInt (str (first nums) (last nums))))))
     (apply +))