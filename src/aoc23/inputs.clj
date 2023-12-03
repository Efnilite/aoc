(ns aoc23.inputs
  (:require [clojure.string :as str]))

(defn raw [day]
  (slurp (str "resources/aoc23/day" day)))

(defn lines [day]
  (->> (raw day)
       (str/split-lines)))

(defn char-grid [day]
  (->> (lines day)
       (map-indexed (fn [y line] (map-indexed (fn [x s] [[x y] s]) line)))
       (apply concat)
       (into {})))

(defn grid-adjacent-8 [[x y] grid]
  (->> (for [dx [-1 0 1]
             dy [-1 0 1]
             :when (not (and (= dx 0) (= dy 0)))
             :let [pos' [(+ x dx) (+ y dy)]]]
         [pos' (get grid pos')])
       (filter (fn [[_ v]] (some? v)))
       (into {})))