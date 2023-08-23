(ns iterate.genpic.explore-generate
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   [clojure.edn :as edn]
   [nextjournal.clerk :as clerk]
   [babashka.curl]
   [iterate.genpic.create-image :as create-image]))

(create-image/gpt-generate "Picture of two developers sitting in a crowded co-working space" ( System/getenv "GENPIC_OPENAI_API_KEY"))
