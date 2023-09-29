(ns day12)


(def characters (seq "abcdefghijklmnopqrstuvwxyz"))


(defn translate-char [c]
  (case c
    \S (dec (translate-char \a))
    \E (inc (translate-char \z))
    (.indexOf characters c)))


(def grid (->> (slurp "resources/12.txt")
               (clojure.string/split-lines)
               (map seq)
               (mapv (fn [row] (mapv translate-char row)))))


(defn height? [pos grid]
  (-> (get grid (second pos))
      (get (first pos))))


(defn available-positions [paths grid]
  (for [path paths
        :let [pos (last path)
              x (first pos)
              y (second pos)
              height (height? pos grid)]
        pos' [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)]]
        :let [height' (height? pos' grid)]
        :when (and (some? height')
                   (> 2 (Math/abs (int (- height height'))))
                   (not ((set path) pos')))]
    (conj path pos')))

=

(loop [paths [[[0 0]]] paths-to-top []]
  (println (count paths) (count paths-to-top))
  (if (empty? paths)
    (dec (apply min (map count paths-to-top)))
    (recur (available-positions paths grid)
           (apply conj paths-to-top (filter #(= (translate-char \E) (height? (last %) grid)) paths)))))