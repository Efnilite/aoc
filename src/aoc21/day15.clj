(ns day15
  (:require [clojure.string :as str]))

; part 1

(def grid (->> (slurp "resources/15.txt")
               (str/split-lines)
               (map seq)
               (map-indexed (fn [y row] (map-indexed (fn [x chr] [[x y] (- (int chr) (int \0))]) row)))
               (apply concat)
               (map (fn [[xy risk]] [xy [risk false nil (if (= xy [0 0]) 0 Integer/MAX_VALUE)]]))
               (into {})))

(defn get-available [grid [x y]]
  (if (and x y) (->> [[(inc x) y] [x (inc y)]]
                     (map (juxt identity (partial get grid)))
                     (filter last)
                     (map first))
                '()))

(defn step [grid]
  (println)
  (println (->> grid
                (filter (fn [[_ [_ visited? _ _]]] (not visited?)))
                (map (fn [[xy' [_ _ _ distance-to-start]]] [xy' distance-to-start]))
                (sort-by last)
                (ffirst))
           (get-available grid (->> grid
                                    (filter (fn [[_ [_ visited? _ _]]] (not visited?)))
                                    (map (fn [[xy' [_ _ _ distance-to-start]]] [xy' distance-to-start]))
                                    (sort-by last)
                                    (ffirst))))
  (let [xy (->> grid
                (filter (fn [[_ [_ visited? _ _]]] (not visited?)))
                (map (fn [[xy' [_ _ _ distance-to-start]]] [xy' distance-to-start]))
                (sort-by last)
                (ffirst))
        shortest-available (get-available grid xy)
        grid' (reduce (fn [grid' xy']
                        (let [distance-to-previous (last (grid xy'))
                              distance-to-start (last (grid xy))]
                          (if (< distance-to-start distance-to-previous)
                            (update grid' xy' (fn [[risk visited? _ _]] [risk visited? xy (+ risk distance-to-start)]))
                            grid')))
                      (update grid xy (fn [[r _ p d]] [r true p d]))
                      shortest-available)]
    (if (empty? shortest-available)
      grid'
      (reduce (fn [grid'' _] (step grid'')) grid' shortest-available))))

(step grid)