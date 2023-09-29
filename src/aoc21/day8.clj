(ns day8
  (:require [clojure.string :as str]))

; part 1

(->> (slurp "resources/08.txt")
     (re-seq #"([\w ]+)\|([\w ]+)")
     (map (fn [lines]
            (->> lines
                 (drop 1)
                 (map str/trim)
                 (map #(str/split % #" "))
                 (last)
                 (map count)
                 (filter #{2 3 4 7})
                 (count))))
     (apply +))

; part 2
; aaaa
;b    c
;b    c
; dddd
;e    f
;e    f
; gggg

(def number-parts '{0 (:a :b :c :e :f :g) 1 (:c :f) 2 (:a :c :d :e :g) 3 (:a :c :d :f :g) 4 (:b :c :d :f)
                    5 (:a :b :d :f :g) 6 (:a :b :d :e :f :g) 7 (:a :c :f) 8 (:a :b :c :d :e :f :g) 9 (:a :b :c :d :f :g)})

(def number-sizes '{2 (1) 3 (7) 4 (4) 5 (2 3 5) 6 (0 6 9) 7 (8)})

(->> (slurp "resources/08.txt")
     (re-seq #"([\w ]+)\|([\w ]+)")
     (map (fn [lines]
            (->> lines
                 (drop 1)
                 (map str/trim)
                 (map #(str/split % #" "))
                 (first)
                 (map (fn [number]
                        (let [possible-numbers (get number-sizes (count number))]
                          (->> number
                               (seq)
                               (map #(assoc {} % possible-numbers))
                               (into {})))))
                 (apply merge-with concat)
                 (map (fn [letter-occurrences]
                        {(first letter-occurrences)
                         (->> (last letter-occurrences)
                              (frequencies)
                              (sort-by val >))}))))))
