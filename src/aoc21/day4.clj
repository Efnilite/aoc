(ns day4
  (:require [clojure.string :as str]))

; part 1

(def lines (->> (slurp "resources/04.txt")
                (str/split-lines)
                (remove (fn [line] (= "" line)))))

(def nums (as-> lines l
                (first l)
                (str/split l #",")
                (map (fn [num] (Integer/parseInt num)) l)))

(def boards (->> lines
                 (rest)
                 (partition 5)
                 (map
                   (fn [board]
                     (map
                       (fn [row]
                         (->> row
                              (re-seq #"\d+")
                              (map str/trim)
                              (map #(Integer/parseInt %))))
                       board)))))

(def boards-data (map
                   (fn [board]
                     {:rows board
                      :cols (map-indexed
                              (fn [index row]
                                (map
                                  #(nth (nth board %) index)
                                  (range (count row)))) board)
                      })
                   boards))

(defn finished-boards [data]
  (->> data
       (filter
         (fn [board-data]
           (some #{0}
                 (->> board-data
                      (vals)
                      (map
                        (fn [board]
                          (map
                            (fn [row]
                              (count row))
                            board)))
                      (flatten)))))))

(defn remove-same-values [map* key* value*]
  (map
    (fn [row]
      (remove (partial = value*) row))
    (get map* key*)))

(defn filter-same-value [value data]
  (zipmap
    [:rows :cols]
    (map
      #(remove-same-values data % value)
      (keys data)))
  )

(loop [remaining nums data boards-data previous-num (first nums)]
  (let [finished-boards (finished-boards data)]

    (if (not (empty? finished-boards))
      (*
        previous-num
        (->> finished-boards
             (first)
             (vals)
             (first)
             (flatten)
             (apply +)))

      (recur
        (rest remaining)
        (map (partial filter-same-value (first remaining)) data)
        (first remaining)))))

; part 2 (doesn't work)

(loop [remaining nums data boards-data]
  (if (= 1 (count data))
    (*
      (first remaining)
      (->> data
           (map (partial filter-same-value (first remaining)))
           (first)
           (vals)
           (first)
           (flatten)
           (apply +)))

    (recur
      (rest remaining)
      (->> data
           (map (partial filter-same-value (first remaining)))
           (remove
             (fn [board]
               (->> board
                    (vals)
                    (map (partial some empty?))
                    (some true?))))))))