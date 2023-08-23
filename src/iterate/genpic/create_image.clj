(ns iterate.genpic.create-image
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json] 
   [babashka.curl]))

(defn openapi-post-generate
  ( [opts api-key n]
   (println "det er blitt gitt et ønske om å lage et annet enn 2 bilder" n))
  ( [opts api-key]
   (let [ resp (http/post "https://api.openai.com/v1/images/generations"
                          {:headers {:content-type "application/json"
                                     :authorization (str "Bearer " api-key)}
                           :body (json/generate-string {:prompt opts
                                                        :n 2
                                                        :size "1024x1024"})})]
     (:data (json/parse-string (:body resp) true)))))

(defn openai-key-from-env []
  (System/getenv "GENPIC_OPENAI_API_KEY"))

(comment
  (openai-key-from-env)
  )
(defn generate 
  ([p api-key n]
   openapi-post-generate p api-key n)
  ([p api-key]
   (->>
    (openapi-post-generate p api-key))))
