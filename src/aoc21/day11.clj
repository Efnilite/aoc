(ns day11
  (:require [clojure.string :as str]))

; part 1

(def energy-grid (->> (slurp "resources/11.txt")
                      (str/split-lines)
                      (map (comp (partial map #(Integer/parseInt %)) (partial re-seq #"\d")))
                      (map-indexed (fn [y row] (map-indexed (fn [x value] [[x y] value]) row)))
                      (apply concat)
                      (into {})))

(def width (->> (keys energy-grid)
                (map first)
                (apply max)
                (inc)))

(defn get-neighbours [x y]
  (->> (for [dx [-1 0 1] dy [-1 0 1]] [(+ x dx) (+ y dy)])
       (filter (partial every? #(and (<= 0 %) (> width %))))))

(defn inc-energy [grid]
  (->> grid
       (map (partial (juxt first (comp inc second))))
       (into {})))

(defn inc* [x]
  (if (nil? x) x (inc x)))

(defn excited? [energy]
  (if (nil? energy) false (< 9 energy)))

(defn step [grid]
  (loop [grid (inc-energy grid) excited-history #{}]
    (let [current-excited (->> grid (filter (comp excited? last)) (map first) (remove excited-history))
          neighbours-to-update (mapcat (partial apply get-neighbours) current-excited)]
      (if (empty? current-excited)
        (->> grid
             (map (fn [[xy energy]] [xy (if (or (nil? energy) (excited? energy)) 0 energy)]))
             (into {}))
        (recur (->> neighbours-to-update
                    (reduce (fn [grid xy->energy] (update grid xy->energy inc*)) grid)
                    (map (fn [[xy energy]] [xy (if (and (excited? energy) (excited-history xy)) nil energy)]))
                    (into {}))
               (->> current-excited
                    (apply conj (into [] excited-history))
                    (distinct)
                    (into #{})))))))

(->> [energy-grid 0]
     (iterate
       (fn [[grid _]]
         (let [grid' (step grid)]
           [grid' (->> grid'
                       (filter (comp zero? val))
                       (count))])))
     (take (inc 100))
     (map last)
     (apply +))

; part 2

(->> [energy-grid 0]
     (iterate
       (fn [[grid _]]
         (let [grid' (step grid)]
           [grid' (->> grid'
                       (filter (comp zero? val))
                       (count))])))
     (take-while #(not= (* width width) (last %)))
     (count))