(ns day6)

; part 1

(def initial-states (->> (slurp "resources/06.txt")
                         (re-seq #"\d")
                         (map #(Integer/parseInt %))
                         (frequencies)))

(defn add [x y]
  (+ (if x x 0) (if y y 0)))

(defn step [states]
  (as-> states states
        (map (fn [[state amount]] [(dec state) amount]) states)

        (into {} states)

        (update states 8 (partial add (->> states (filter (comp (partial = -1) first)) (last) (last))))

        (update states 6 (partial add (get states -1)))

        (dissoc states -1)))

(defn lanternfish-count [day]
  (->> initial-states
       (iterate step)
       (take (inc day))
       (map vals)
       (map (partial apply +))
       (last)))

(lanternfish-count 80)

; part 2

(lanternfish-count 256)