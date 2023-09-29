(ns aoc20.day8
  (:require [clojure.string :as str]))

(def parts (->> (slurp "resources/aoc20/day08.txt")
                (str/split-lines)
                (map-indexed (fn [idx item]
                               (let [parts (->> item (re-find #"([a-z]+) \+?(-?\d+)") (rest))]
                                 [idx [(first parts) (Integer/parseInt (last parts))]])))
                (into {})))