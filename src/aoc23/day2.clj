(ns aoc23.day2
  (:require [aoc23.inputs :as inputs]
            [clojure.string :as str]))

; part 1

(def limits {"red" 12 "green" 13 "blue" 14})

(defn impossible? [[_ cubes]]
  (some (fn [[n c]] (> n (limits c))) cubes))

(defn line-to-cubes [line]
  (let [parts (str/split line #":")

        game (->> (first parts)
                  (re-seq #"\d+")
                  (first)
                  (Integer/parseInt))

        reveals (as-> (second parts) p
                      (str/split p #"[,;]")
                      (mapcat (comp (partial map (fn [[_ n c]] [(Integer/parseInt n) c]))
                                    (partial re-seq #"(\d+) (red|blue|green)")) p))]

    [game reveals]))

(->> (inputs/lines 2)
     (map line-to-cubes)
     (remove impossible?)
     (map first)
     (apply +))

; part 2

(defn line-to-power [line]
  (->> line
       (line-to-cubes)
       (last)
       (map (comp vec reverse))
       (sort-by second)
       (into {})
       (vals)
       (apply *)))

(->> (inputs/lines 2)
     (map line-to-power)
     (apply +))