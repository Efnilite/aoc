(ns aoc23.day8
  (:require [aoc23.inputs :as inputs]
            [clojure.string :as str]))

; part 1

(def lines (inputs/lines 8))

(def allowed-movements (->> (first lines) (map (fn [x] (if (= x \L) 0 1)))))

(def pos->next-options
  (->> lines
       (drop 2)
       (map (fn [x]
              (let [[a l r] (str/split x #"( = \(|, |\))")]
                [a [l r]])))
       (into {})))

(defn steps [start end]
  (loop [pos start allowed-movements (cycle allowed-movements) length 0]
    (let [direction (first allowed-movements)]
      (if (= (set pos) (set end))
        length
        (recur (map #(nth (pos->next-options %) direction) pos)
               (rest allowed-movements)
               (inc length))))))

(steps ["AAA"] ["ZZZ"])

; part 2

; todo

(defn nodes-ending-with [s]
  (->> pos->next-options
       (keys)
       (filterv #(str/ends-with? % s))))

(steps (nodes-ending-with "A") (nodes-ending-with "Z"))