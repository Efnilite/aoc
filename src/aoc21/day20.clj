(ns day20
  (:require [clojure.string :as str]))

(def lines (->> (slurp "resources/20.txt")
                (str/split-lines)))

(def enhancement (->> (first lines)
                      (seq)
                      (map-indexed vector)
                      (into {})))

(def initial-lighted-pixels (->> (drop 2 lines)
                                 (map-indexed (fn [y row] (map-indexed (fn [x char] (if (= char \#) [x y])) row)))
                                 (mapcat (partial filter some?))
                                 (into #{})))

(defn get-neighbouring [x y]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x) y]       [x y]       [(inc x) y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn neighbouring-to-int [row lighted-pixels]
  (->> (Integer/parseInt
         (->> row
              (map (fn [[x' y']] (if (lighted-pixels [x' y']) "1" "0")))
              (str/join)) 2)
       (enhancement)))

(defn enhance [lighted-pixels]
  (let [xs (map first lighted-pixels)
        ys (map second lighted-pixels)]
    (->> (for [x' (range (- (apply min xs) 3) (+ 3 (apply max xs)))
               y' (range (- (apply min ys) 3) (+ 3 (apply max ys)))]
           [[x' y'] (get-neighbouring x' y')])
         (map (fn [[pos neighbouring]] [pos (neighbouring-to-int neighbouring lighted-pixels)]))
         (filter (fn [[_ lighted]] (= lighted \#)))
         (map first)
         (into #{}))))

(->> initial-lighted-pixels
     (iterate enhance)
     (take 3)
     (last)
     (count))