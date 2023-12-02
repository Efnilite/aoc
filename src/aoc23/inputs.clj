(ns aoc23.inputs
  (:require [clojure.string :as str]))

(defn raw [day]
  (slurp (str "resources/aoc23/day" day)))

(defn lines [day]
  (->> (raw day)
       (str/split-lines)))