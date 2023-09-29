(ns day14
  (:require [clojure.string :as str]))

; part 1

(def lines (->> (slurp "resources/14.txt")
                (str/split-lines)))

(def pairs (->> lines
                (drop 2)
                (map (comp (partial apply vector)
                           (partial re-seq #"[A-Z]+")))
                (into {})))

(def template (->> lines
                   (first)
                   (partition 2 1)
                   (map str/join)
                   (frequencies)))

(defn step [pairs template]
  (->> template
       (mapcat (fn [[pair amount]]
                 (let [next (pairs pair)]
                   (as-> [[(first pair) next] [next (last pair)]] v
                         (map (partial apply str) v)
                         (zipmap v (repeat amount))))))
       (merge-with + {})))

(defn get-difference [template pairs steps]
  (->> template
       (iterate (partial step pairs))
       (take (inc steps))
       (last)
       (map (fn [[pair amount]] (merge-with + {(first pair) amount} {(last pair) amount})))
       (apply merge-with +)
       (vals)
       ((juxt (partial apply max) (partial apply min)))
       (apply -)
       (* 0.5)
       (Math/round)))                                       ; it works! :)

(get-difference template pairs 10)

; part 2

(get-difference template pairs 40)

; note: in the last method apparently all values get doubled for some reason and this took me 3 days to realize
; todo optimize!