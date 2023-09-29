(ns day10
  (:require [clojure.string :as str]))

; part 1

(def missing-char-points {\) 3 \] 57 \} 1197 \> 25137})
(def pairs {\( \) \[ \] \{ \} \< \>})

(defn tread [string]
  (let [match (re-seq #"[\[\(\{\<][\>\}\)\]]" string)
        first-chunk (first match)]
    (if (empty? match)
      [string nil]
      [(str/replace-first string first-chunk "") first-chunk])))

(def final-states
  (->> (slurp "resources/10.txt")
       (str/split-lines)
       (map (fn [line]
              (take-while (fn [[_ invalid-chunk]]
                            (and invalid-chunk
                                 (= (last invalid-chunk) (get pairs (first invalid-chunk)))))
                          (iterate (comp tread first) [line "()"]))))
       (map (comp tread first last))))

(->> final-states
     (filter second)
     (map (comp (partial get missing-char-points) last last))
     (apply +))

; part 2

(def add-char-points {\( 1 \[ 2 \{ 3 \< 4})

(def scores (->> final-states
                 (filter (comp not (partial second)))
                 (map (fn [[incomplete-string _]]
                        (->> incomplete-string
                             (reverse)
                             (reduce (fn [current-score char]
                                       (+ (* current-score 5) (get add-char-points char)))
                                     0))))
                 (sort)))

(nth scores (/ (count scores) 2))