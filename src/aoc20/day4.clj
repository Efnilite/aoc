(ns aoc20.day4
  (:require [clojure.set :as set]
            [clojure.string :as str]))

; part 1

(def passports
  (->> (-> (slurp "resources/aoc20/day04.txt")
           (str/split #"\r\n\r\n"))
       (map #(as-> % v
                   (str/split v #"(\r\n| |:)")
                   (partition 2 v)
                   (zipmap (map first v) (map last v))))))

(def fields #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"})

(->> passports
     (filter #(= 7 (->> %
                        (keys)
                        (set)
                        (set/intersection fields)
                        (count))))
     (count))

; part 2

(def eyecolours #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"})

(->> passports
     (filter #(= 7 (->> %
                        (keys)
                        (set)
                        (set/intersection fields)
                        (count))))
     (filter (fn [passport]
               (let [parse-int #(Integer/parseInt %)
                     get-int-field #(parse-int (get passport % "0"))
                     byr (get-int-field "byr")
                     iyr (get-int-field "iyr")
                     eyr (get-int-field "eyr")
                     hgt (get passport "hgt" "")
                     hcl (get passport "hcl" "")
                     ecl (get passport "ecl" "")
                     pid (get passport "pid" "")
                     in-range? (fn [v l u] (and (>= v l) (<= v u)))]
                 (and
                   (in-range? byr 1920 2002)
                   (in-range? iyr 2010 2020)
                   (in-range? eyr 2020 2030)
                   (let [[_ height measurement] (re-find (re-matcher #"(\d+)(in|cm)" hgt))]
                     (case measurement
                       "cm" (in-range? (parse-int height) 150 193)
                       "in" (in-range? (parse-int height) 59 76)
                       false)
                     )
                   (re-matches #"#[a-f0-9]{6}" hcl)
                   (eyecolours ecl)
                   (= 9 (.length pid))
                   ))))
     (count))