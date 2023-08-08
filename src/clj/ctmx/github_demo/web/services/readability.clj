(ns ctmx.github-demo.web.services.readability)

(def WORDS_DELIMITER #"\\PL+")
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

(defn readability [{:keys [characters
                           words
                           sentences
                           syllables
                           polysyllables]}]
  {:ari (+
         (* 4.71 (/ characters words))
         (* 0.5 (/ words sentences))
         -21.43)
   :fk (+
        (* 0.39 (/ words sentences))
        (* 11.8 (/ syllables words))
        -15.59)
   :smog (+
          (* 1.043
             (Math/sqrt (* polysyllables (/ 30 sentences))))
          3.1291)
   :cl (let [l (* 100 (/ characters words))
             s (* 100 (/ sentences words))]
         (- (* 0.0588 l)
            (* 0.296 s)
            15.8))})

(defn analysis [^String s]
  (let [stats (stats s)]
    {:stats stats
     :readability (readability stats)}))
