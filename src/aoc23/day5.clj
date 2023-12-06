(ns aoc23.day5
  (:require [aoc23.inputs :as inputs]
            [clojure.string :as str]))

; part 1

(def lines (->> (inputs/raw 5)
                (str/split-lines)))

(def seeds
  (->> (-> lines
           (first)
           (str/split #":")
           (last)
           (str/split #" "))
       (remove empty?)
       (map parse-long)))

(def from->to-vals
  (->> lines
       (partition-by empty?)
       (remove #(= 1 (count %)))
       (map (fn [[m & vs]]
              (let [map-parts (str/split m #"(-to-| )")
                    map-from (first map-parts)
                    map-to (second map-parts)
                    values (->> vs
                                (mapcat #(str/split % #" "))
                                (map parse-long)
                                (partition 3))]
                [map-from {:to map-to :vals values}])))
       (into {})))

(defn to-mapped-value [num vals]
  "Transforms num to the mapped value, based on vals. If there is no range which contains num, returns num."
  (let [[to-start from-start]
        (->> vals
             (filter (fn [[_ from-start start-range]]
                       (and (>= num from-start) (< num (+ from-start start-range)))))
             (first)
             (take 2))]
    (if (or (nil? to-start) (nil? from-start))
      num
      (+ to-start (- num from-start)))))

(->> (keys from->to-vals)
     (reduce (fn [nums type]
               (if-let [vals (:vals (get from->to-vals type))]
                 (map #(to-mapped-value % vals) nums)
                 nums))
             seeds)
     (apply min))

; part 2

; todo