;; # Using the OpenAI API

(ns iterate.genpic.explore
  {:nextjournal.clerk/toc true}
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   [clojure.edn :as edn]
   [nextjournal.clerk :as clerk]
   [babashka.curl]))

(defn read-config []
  (-> (slurp "config.edn")
      edn/read-string))

^::clerk/no-cache
(def config (read-config))

(assert (:openapi-api-key config))
(assert (:openapi-org config))

;; ## Tokens and setup

;; This is Teodor's personal OpenAI key. Some usage for educational purposes is
;; fine.

(defn openapi-post
  [endpoint opts]
  (http/post endpoint {:headers (:headers opts)
                       :body (:body opts "")}))
(defn openapi-post-generate
  [ opts api-key]
  (def resp (http/post "https://api.openai.com/v1/images/generations"
                       {:headers {:content-type "application/json"
                                  :authorization (str "Bearer " api-key)}
                        :body (json/generate-string {:prompt opts 
                                            :n 2 
                                            :size "1024x1024"})})) 
  (:data (json/parse-string (:body resp) true)))
  
(defn resp->json [resp] (json/parse-string (:body resp) keyword))

(def test-resp {"created" 1692709920,
                "data"
                [{"url"
                  "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Oopwoel5KbsbxWjAIopGCFs9/user-XOwmgWwuxZnViZucx9P3EXdR/img-ayic01pWDuTqCFm569HmmmhQ.png?st=2023-08-22T12%3A12%3A00Z&se=2023-08-22T14%3A12%3A00Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-21T20%3A10%3A50Z&ske=2023-08-22T20%3A10%3A50Z&sks=b&skv=2021-08-06&sig=c8oWq%2BxO17paBmu5p%2BDZjyPXPI0dJFWEEjN5osF05JA%3D"}
                 {"url"
                  "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Oopwoel5KbsbxWjAIopGCFs9/user-XOwmgWwuxZnViZucx9P3EXdR/img-4cKnZoPTQEDVuEzO7ZPCmR5g.png?st=2023-08-22T12%3A12%3A00Z&se=2023-08-22T14%3A12%3A00Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-21T20%3A10%3A50Z&ske=2023-08-22T20%3A10%3A50Z&sks=b&skv=2021-08-06&sig=2RRHdBmyVY5YvYMu8bxMZQnDU7LeZLymhRBSmIfPcsc%3D"}]})

(keys test-resp)
(get test-resp "data")
(defn resp->url [resp] 
  (println (json/parse-string (get resp :body))))

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
                                               "temperature" 0.7})})
   resp->json
   :choices
   first
   :message
   :content))

(defn gpt-generate-new [p api-key]
  
   (openapi-post "https://api.openai.com/v1/images/generations"
                {:headers {:content-type "application/json"
                           :authorization (str "Bearer " api-key)}
                 :body (json/generate-string {:prompt p
                                              :n 2
                                              :size "1024x1024"})}) 
   )
  
(defn gpt-generate [p api-key]
  (->>
    (openapi-post-generate p api-key)
 ))

(openapi-post "https://api.openai.com/v1/chat/completions"
 {:headers {:content-type "application/json"
            :authorization (str "Bearer " ( System/getenv "GENPIC_OPENAI_API_KEY"))}
  :body (json/generate-string {:model "gpt-3.5-turbo"
                               :messages
                               [{"role" "user",
                                 "content" "Gi meg de 10 minste landene i Europa"}]
                               "temperature" 0.7})} )
(gpt-generate "Make a dog picture with 5 dogs" (System/getenv "GENPIC_OPENAI_API_KEY"))

(macroexpand '(->> (openapi-post "https://api.openai.com/v1/chat/completions"
                                 {:headers {:content-type "application/json"
                                            :authorization (str "Bearer " (System/getenv "GENPIC_OPENAI_API_KEY"))}
                                  :body (json/generate-string {:model "gpt-3.5-turbo"
                                                               :messages
                                                               [{"role" "user",
                                                                 "content" "Gi meg de 10 minste landene i Europa"}]
                                                               "temperature" 0.7})}) (resp->json) (first :message :content)))
(gpt-ask "What is 4+4" (System/getenv "GENPIC_OPENAI_API_KEY"))
(clerk/html [:p  
             (gpt-ask "What is the Norwegian company Iterate?" (System/getenv "GENPIC_OPENAI_API_KEY"))])
(clerk/html [:p (gpt-ask "Hva heter du?" (System/getenv "GENPIC_OPENAI_API_KEY"))]) 
(clerk/html [:p ( gpt-generate "White cat" (System/getenv "GENPIC_OPENAI_API_KEY"))])
