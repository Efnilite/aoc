(ns day13
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]))


(def parts (as-> (slurp "resources/13.txt") src
                 (str/split src #"\r\n")
                 (remove #{""} src)
                 (partition 2 src)
                 (map #(map edn/read-string %) src)))


(defn is-ordered? [x y]
  (cond (= x y) nil
        (and (int? x) (int? y)) (< x y)
        (and (coll? x) (coll? y)) (first (filter some? (map #(apply is-ordered? %) (map vector x y))))
        (int? x) (is-ordered? [x] y)
        (int? y) (is-ordered? x [y]))
  )


(defn deep-count [coll]
  (if (seqable? coll)
    (apply + (map deep-count coll))
    1))


(defn compare-parts [parts]
  (let [left (first parts)
        right (last parts)
        ordered? (is-ordered? left right)]
    (if (nil? ordered?) (< (deep-count left) (deep-count right)) ordered?)))


(->> parts
     (map compare-parts)
     (map-indexed vector)
     (filter #(true? (last %)))
     (map first)
     (map inc)
     (apply +))