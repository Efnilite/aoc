(ns day9
  (:require [clojure.string :as str]))

; part 1

(def numbers (->> (slurp "resources/09.txt")
                  (str/split-lines)
                  (map (fn [line]
                         (->> line
                              (re-seq #"\d")
                              (map #(Integer/parseInt %)))))))

(defn get-height [x y]
  (-> numbers (nth y '()) (nth x 9)))

(defn get-adjacents [x y]
  [[(dec x) y] [(inc x) y]
   [x (dec y)] [x (inc y)]])

(def troughs (filter some?
                     (for [x (range (count (first numbers)))
                           y (range (count numbers))
                           :let [current-height (get-height x y)
                                 adjacent (map (partial apply get-height) (get-adjacents x y))]]
                       (if (every? (partial < current-height) adjacent) [x y] nil))))

(->> troughs
     (map (fn [coords]
            (->> coords
                 (apply get-height)
                 (inc))))
     (apply +))

; part 2

(defn get-taller-adjacents [x y]
  (->> (get-adjacents x y)
       (filter #(< (get-height x y) (apply get-height %)))
       (filter (comp (partial not= 9) (partial apply get-height)))))

(->> troughs
     (map (fn [coords]
            (->> [coords]
                 (iterate (partial mapcat (partial apply get-taller-adjacents)))
                 (take-while not-empty)
                 (apply concat)
                 (distinct))))
     (map count)
     (sort)
     (take-last 3)
     (apply *))