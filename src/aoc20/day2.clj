(ns aoc20.day2
  (:require [clojure.string :as str]))

; part 1

(def parts
  (->> (slurp "resources/aoc20/day02.txt")
       (str/split-lines)
       (map #(->> %
                  (re-matcher #"(\d+)-(\d+) (\w): (\w+)")
                  (re-find)
                  (rest)))))

(->> parts
     (map (fn [[lowerBound upperBound letter password]]
            [(Integer/parseInt lowerBound) (Integer/parseInt upperBound)
             (first (.toCharArray letter)) (frequencies password)]))
     (filter (fn [[lowerBound upperBound char charFrequencies]]
               (let [freq (get charFrequencies char 0)]
                 (and (<= lowerBound freq)
                      (<= freq upperBound)))))
     (count))

; part 2

(->> parts
     (filter (fn [[lowerBound upperBound letter password]]
               (let [char (first (.toCharArray letter))
                     equals (fn [idx] (= char (.charAt password (dec (Integer/parseInt idx)))))
                     eqLower (equals lowerBound)
                     eqUpper (equals upperBound)]
                 (or (and eqLower (not eqUpper))
                     (and (not eqLower) eqUpper)))))
     (count))
