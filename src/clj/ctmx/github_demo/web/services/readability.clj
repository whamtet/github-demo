(ns ctmx.github-demo.web.services.readability)

(def WORDS_DELIMITER #"\PL+")
(def SENTENCES_DELIMITER #"[!?.]+")
(def PATTERN_SYLLABLE #"([aiouy]|e(?!$))+")

(defn- count-syllables [^String s]
  (-> PATTERN_SYLLABLE
      (.matcher s)
      .results
      .count
      (max 1)))
(defn- polysyllable? [^String s]
  (> (count-syllables s) 2))

(defn stats [^String s]
  (let [words (re-seq WORDS_DELIMITER s)]
    {:characters (-> s (.replaceAll "\\s" "") .length)
     :words (count words)
     :sentences (-> SENTENCES_DELIMITER (.splitAsStream s) .count)
     :syllables (->> words (map count-syllables) (apply +))
     :polysyllables (->> words (filter polysyllable?) count)}))

(defn- safe-div [a b]
  (if (zero? b) a (/ a b)))

(defn readability [{:keys [characters
                           words
                           sentences
                           syllables
                           polysyllables]}]
  {:ari (+
         (* 4.71 (safe-div characters words))
         (* 0.5 (safe-div words sentences))
         -21.43)
   :fre (-
          206.835
          (* 1.015 (safe-div words sentences))
          (* 84.6 (safe-div syllables words)))
   :smog (+
          (* 1.043
             (Math/sqrt (* polysyllables (safe-div 30 sentences))))
          3.1291)
   :cl (let [l (* 100 (safe-div characters words))
             s (* 100 (safe-div sentences words))]
         (- (* 0.0588 l)
            (* 0.296 s)
            15.8))})

(defn analysis [^String s]
  (let [stats (stats s)]
    {:stats stats
     :readability (readability stats)}))
