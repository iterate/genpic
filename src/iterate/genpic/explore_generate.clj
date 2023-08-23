(ns iterate.genpic.explore-generate
  (:require 
   [iterate.genpic.create-image :as create-image]
   [nextjournal.clerk :as clerk]))

(def test-resp (create-image/gpt-generate "Picture of two developers sitting in a crowded co-working space" ( System/getenv "GENPIC_OPENAI_API_KEY")))
  
(->> test-resp
     (map (fn [resp-list]
            (:url resp-list)))
     (map clerk/image )
     )