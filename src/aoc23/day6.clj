(ns aoc23.day6
  (:require [aoc23.inputs :as inputs]
            [clojure.string :as str]))

; part 1

(defn valid-push-durations [time]
  "Returns all push durations for a given time"
  (->> (inc time)
       (range)
       (map (fn [push-duration]
              (let [move-duration (- time push-duration)]
                (* push-duration move-duration))))))

(defn line-to-numbers [line]
  "Parses all numbers in a line"
  (->> (str/split line #" ( )*")
       (rest)
       (map #(Integer/parseInt %))))

(defn valid-way-count [[time distance]]
  "Return the count of all valid ways to push the button to beat the record."
  (->> (valid-push-durations time)
       (filter (partial < distance))
       (count)))

(->> (inputs/lines 6)
     (map line-to-numbers)
     (apply map vector)
     (map valid-way-count)
     (apply *))

; part 2

(defn line-to-numbers' [line]
  "Parses all numbers in a line and concats them"
  (->> (str/split line #" ( )*")
       (rest)
       (apply str)
       (parse-long)))

(valid-way-count (->> (inputs/lines 6) (map line-to-numbers')))