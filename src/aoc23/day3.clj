(ns aoc23.day3
  (:require [aoc23.inputs :as inputs]))

; part 1

(def grid (inputs/char-grid 3))

(defn next-to-symbol? [grid pos]
  "Returns true if pos is next to a symbol"
  (->> (inputs/grid-adjacent-8 pos grid)
       (some (fn [[_ v]] (re-matches #"[^.0-9]" (str v))))
       (some?)))

(defn numbers [line y]
  "Extracts numbers from line and their associated positions."
  (loop [rem line nums [] curr "" curr-pos [] x 0]
    (if (empty? rem)
      (if (empty? curr) nums (conj nums [(Integer/parseInt curr) (conj curr-pos [x y])]))

      (let [c (first rem)]
        (if (Character/isDigit (char c))
          (recur (rest rem)
                 nums
                 (str curr c)
                 (conj curr-pos [x y])
                 (inc x))

          (recur (rest rem)
                 (if (empty? curr) nums (conj nums [(Integer/parseInt curr) curr-pos]))
                 ""
                 []
                 (inc x)))))))

(def number->position (->> (inputs/lines 3)
                           (map-indexed (fn [idx line] (numbers line idx)))
                           (apply concat)))

(->> number->position
     (filter (fn [[_ poses]] (some #(next-to-symbol? grid %) poses)))
     (map first)
     (apply +))

; part 2

(defn numbers-next-to-* [pos grid]
  (->> grid
       (inputs/grid-adjacent-8 pos)
       (filter (fn [[_ v]] (re-matches #"[0-9]" (str v))))
       (mapcat (fn [[pos _]] (filter (fn [[_ poses]] (some (partial = pos) poses)) number->position)))
       (distinct)
       (map first)))

(->> grid
     (filter (fn [[_ v]] (= v \*)))
     (map (fn [[pos _]] (numbers-next-to-* pos grid)))
     (filter #(= (count %) 2))
     (map (partial apply *))
     (apply +))