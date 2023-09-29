(ns day13
  (:require [clojure.string :as str]))

; part 1

(def parts (as-> (slurp "resources/13.txt") txt
                 (str/split txt #"\r\n\r\n")
                 (map str/split-lines txt)))

(def grid (->> (first parts)
               (map (comp (partial mapv #(Integer/parseInt %))
                          (partial re-seq #"\d+")))
               (into #{})))

(def folds (->> (last parts)
                (map (comp (juxt first (comp #(Integer/parseInt %) second))
                           (partial apply rest)
                           (partial re-seq #"([xy])=(\d+)")))))

(defn fold [x-y x-y-value grid]
  (->> grid
       (map
         (fn [[x y]]
           (cond (= x-y "x") (if (> x x-y-value) [(- (* 2 x-y-value) x) y] [x y])
                 (= x-y "y") (if (> y x-y-value) [x (- (* 2 x-y-value) y)] [x y]))))
       (into #{})))

(defn perform-folds [grid folds]
  (reduce
    (fn [grid' fold'] (fold (first fold') (last fold') grid'))
    grid
    folds))

(->> folds
     (first)
     (vector)
     (perform-folds grid)
     (count))

; part 2

(defn display [grid]
  (let [max-x (->> grid (map first) (apply max))
        max-y (->> grid (map last) (apply max))]
    (map (fn [row-y]
           (mapcat (fn [row-x]
                     (if (grid [row-x row-y]) "#" "."))
                   (range (inc max-x))))
         (range (inc max-y)))))

(->> folds
     (perform-folds grid)
     (display))