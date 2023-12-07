(ns aoc23.day7
  (:require [aoc23.inputs :as inputs]
            [clojure.string :as str]))

; part

(def char->value (zipmap "AKQJT98765432" (range)))

(defn card-type [card]
  "Returns the card type for a given card"
  (let [freqs (map last (frequencies card))]
    (cond (= 1 (count freqs)) 6                             ; five
          (->> freqs (apply max) (= 4)) 5                   ; four
          (->> freqs (every? #(or (= 3 %) (= 2 %)))) 4      ; full house
          (->> freqs (apply max) (= 3)) 3                   ; three
          (->> freqs (filter (partial = 2)) (count) (= 2)) 2 ; two-
          (->> freqs (apply max) (= 2)) 1                   ; one
          :else 0)))                                        ; high

(defn sort-card-chars [x y char->value]
  "Sorts the cards by their chars based on the char->value map."
  (loop [rem (apply map vector [x y])]
    (let [[x-c y-c] (first rem)]
      (if (= x-c y-c)
        (recur (rest rem))
        (compare (char->value x-c) (char->value y-c))))))

(defn parse [card-type sort-card-chars char->value]
  "Returns the result."
  (->> (inputs/lines 7)
       (map (fn [line]
              (let [parts (str/split line #" ")
                    card (first parts)]
                [card (card-type card) (Integer/parseInt (last parts))])))
       (sort-by second)
       (partition-by second)
       (mapcat (partial sort (fn [x y] (sort-card-chars (first y) (first x) char->value))))
       (map-indexed (fn [idx card-data] (* (inc idx) (last card-data))))
       (apply +)))

(parse card-type sort-card-chars char->value)

; part 2

; todo

(def char->value' (zipmap "AKQT98765432J" (range)))

(defn joker-replacement [card]
  "Returns the replacement card for joker letters"
  (->> card
       (distinct)
       (map (fn [v] [v (char->value' v)]))
       (sort-by second)
       (ffirst)))

(parse (fn [card]
         (->> card
              (joker-replacement)
              (str)
              (str/replace card #"J")
              (card-type)))
       sort-card-chars
       char->value')