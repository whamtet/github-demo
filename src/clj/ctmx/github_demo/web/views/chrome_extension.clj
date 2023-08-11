(ns ctmx.github-demo.web.views.chrome-extension
    (:require
      [ctmx.core :as ctmx :refer [defcomponent]]))

(defn uuid-input [uuid]
  [:input#uuid-input {:type "hidden" :value uuid}])

(def ari-grades ["Kindergarten"
                 "First Grade"
                 "Second Grade"
                 "Third Grade"
                 "Fourth Grade"
                 "Fifth Grade"
                 "Sixth Grade"
                 "Seventh Grade"
                 "Eighth Grade"
                 "Ninth Grade"
                 "Tenth Grade"
                 "Eleventh Grade"
                 "Twelfth Grade"
                 "College Student"])

(defn- ari-grade [score]
  (let [i (-> score dec (max 0) Math/ceil long)]
    (get ari-grades i (peek ari-grades))))

(defn flesch-rating [score]
  (condp <= score
    90.0	["5th grade"	"Very easy to read. Easily understood by an average 11-year-old student."]
    80.0	["6th grade"	"Easy to read. Conversational English for consumers."]
    70.0	["7th grade"	"Fairly easy to read."]
    60.0	["8th & 9th grade"	"Plain English. Easily understood by 13- to 15-year-old students."]
    50.0	["10th to 12th grade"	"Fairly difficult to read."]
    30.0	["College"	"Difficult to read."]
    10.0	["College graduate"	"Very difficult to read. Best understood by university graduates."]
    ["Professional"	"Extremely difficult to read. Best understood by university graduates."]))

(defn stats [{{:keys [characters
                      words
                      sentences
                      syllables
                      polysyllables]} :stats
              {:keys [ari
                      fk
                      smog
                      cl]} :readability}]
  [:div#stats
    [:h3 "Readability"]
    [:h5 "Automated readability index"]
    [:p "Score: " ari ". " (ari-grade ari )]])

(defcomponent ^:endpoint extension [req]
  [:div {:hx-ws "connect:/extension/sse"}
    (uuid-input nil)
    [:button.btn {:onclick "getStats()"} "Get Readability"]
    [:div#stats]])