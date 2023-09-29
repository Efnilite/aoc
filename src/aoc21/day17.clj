(ns day17)

; part 1

(def dimensions (->> (slurp "resources/17.txt")
                     (re-seq #"x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)")
                     (apply rest)
                     (mapv #(Integer/parseInt %))))

(defn step [x y [dx dy] history]
  (cond
    (and (>= x (dimensions 0))
         (<= x (dimensions 1))
         (>= y (dimensions 2))
         (<= y (dimensions 3))) history
    (or (> x (dimensions 1))
        (< y (dimensions 2))) nil
    :else (step (+ x dx) (+ y dy)
                [(cond (zero? dx) dx
                       (> dx 0) (dec dx)
                       :else (inc dx))
                 (dec dy)]
                (conj history [x y]))
    ))

; gotta think of a better way than just brute forcing it

(->> (for [dx (range 1 (dimensions 1))
           dy (range (dimensions 3) 1000)]
       (step 0 0 [dx dy] []))
     (filter some?)
     (map (partial map second))
     (flatten)
     (apply max))


; part 2

(->> (for [dx (range 1 (inc (dimensions 1)))
           dy (range (dec (dimensions 2)) 1000)]
       [[dx dy] (step 0 0 [dx dy] [])])
     (filter (fn [[_ history]] (some? history)))
     (mapv first)
     (count))