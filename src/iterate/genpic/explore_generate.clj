(ns iterate.genpic.explore-generate
  (:require 
   [iterate.genpic.create-image :as create-image]
   [nextjournal.clerk :as clerk]))

(def test-resp (create-image/generate "Picture of two developers sitting in a crowded co-working space" ( System/getenv "GENPIC_OPENAI_API_KEY")))
  
(->> (create-image/generate "Picture of two developers sitting in a crowded co-working space" (System/getenv "GENPIC_OPENAI_API_KEY"))
     (map (fn [resp-list]
            (:url resp-list)))
     (map clerk/image )
     )  

(->> test-resp
     (map (fn [resp-list]
            (prn + (:url resp-list ) "\n")
            ))
)

(def all-the-numbers (range))

(def all-nums*10 (map (partial * 10) all-the-numbers))

(take 20 all-nums*10)

(println 123)