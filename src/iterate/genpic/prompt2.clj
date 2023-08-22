
;; # Using the OpenAI API

(ns iterate.genpic.prompt2
  {:nextjournal.clerk/toc true}
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   ))
(defn openapi-post
  [endpoint opts]
  (http/post endpoint {:headers (:headers opts)
                       :body (:body opts "")}))

(defn resp->json [resp] (json/parse-string (:body resp) keyword))


;; ## What models are available?

(defn gpt-ask [q api-key]
  (->>
   (openapi-post "https://api.openai.com/v1/chat/completions"
                 {:headers {:content-type "application/json"
                            :authorization (str "Bearer " api-key)}
                  :body (json/generate-string {:model "gpt-3.5-turbo"
                                               :messages
                                               [{"role" "user",
                                                 "content" q}]
                                               "temperature" 0.7
                                               })})
   resp->json
   :choices
   first
   :message 
   :content))


(gpt-ask "What is 4+4" (System/getenv "GENPIC_OPENAI_API_KEY"))

