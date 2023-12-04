(ns aoc23.day4
  (:require [aoc23.inputs :as inputs]
            [clojure.string :as str]))

; part 1

(defn numbers [line]
  "Parses all the numbers from a line"
  (->> (str/split line #"[:|]")
       (rest)
       (map (fn [part]
              (->> (str/split part #" ")
                   (remove empty?)
                   (map #(Integer/parseInt %)))))))

(defn line-to-score [line]
  (let [[winning-nums nums-on-card] (numbers line)
        winning-nums-on-card (filter (set winning-nums) nums-on-card)
        winning-count (count winning-nums-on-card)]
    (if (zero? winning-count) 0 (Math/pow 2 (dec winning-count)))))

(->> (inputs/lines 4)
     (map line-to-score)
     (apply +)
     (int))

; part 2

(defn winning-numbers [[winning-nums nums-on-card]]
  "Returns the numbers on the card that are also in the winning numbers"
  (filter (set winning-nums) nums-on-card))


(defn line-to-winning-next [idx line]
  [(inc idx)
   (->> (range idx (+ idx (->> line (numbers) (winning-numbers) (count))))
        (map (partial + 2)))])

(def card->winning-next
  (->> (inputs/lines 4)
       (map-indexed line-to-winning-next)
       (into {})))

(loop [idx 1 nums []]
  (let [next (get card->winning-next idx)
        eq (->> nums (filter (partial = idx)) (count) (inc))
        repeated (conj (->> next (repeat eq) (flatten)) idx)]
    (if (nil? next)
      (count nums)
      (recur
        (inc idx)
        (reduce conj nums repeated)))))