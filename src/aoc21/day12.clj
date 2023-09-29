(ns day12
  (:require [clojure.string :as str]))

; part 1

(def paths (->> (slurp "resources/12.txt")
                (str/split-lines)
                (map (partial re-seq #"[A-Za-z]+"))
                (mapcat #(vector [(first %) (second %)] [(second %) (first %)]))
                (reduce (fn [map next]
                          (update map (next 0) #(conj % (next 1))))
                        {})))

(defn lower? [ch] (Character/isLowerCase (char ch)))

(defn step [history]
  (mapcat (fn [next]
            (if (= next "end")
              [(conj history next)]
              (step (conj history next))))
          (->> history
               (last)
               (paths)
               (remove (->> history
                            (filter (comp lower? first))
                            (into #{}))))))

(->> "start"
     (vector)
     (step)
     (count))

; part 2

(defn visitable? [history next]
  (let [lowers (->> history
                    (filter (comp lower? first))
                    (frequencies))
        counts (->> lowers
                    (map last)
                    (apply max))]
    (cond (or (= next "start") (= next "end")) (nil? (lowers next))
          (lower? (first next)) (or (not= 2 counts) (nil? (lowers next)))
          :else true)))

(defn step' [history]
  (mapcat (fn [next]
            (if (= next "end")
              [(conj history next)]
              (step' (conj history next))))
          (->> history
               (last)
               (paths)
               (filter (partial visitable? history)))))

(->> "start"
     (vector)
     (step')
     (count))