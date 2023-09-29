(ns aoc20.day7
  (:require [clojure.string :as str]))

; part 1

(def parts (->> (slurp "resources/aoc20/day07.txt")
                (str/split-lines)
                (map #(str/split % #"( bags contain | bag(s)?, | bag(s)?.)"))
                (map (fn [x]
                       [(first x)
                        (->> (rest x)
                             (map (fn [bag]
                                    (let [bags (re-find #"(\d+) (.+)" bag)
                                          amount (if (nil? (second bags)) 0 (Integer/parseInt (second bags)))]
                                      [(last bags) amount])))
                             (into {}))]))
                (into {})))

;(filter (fn [x] ((->> (last x) (map first) (set)) "shiny gold")) parts)