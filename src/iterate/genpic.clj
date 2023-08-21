(ns iterate.genpic 
  (:require [babashka.cli :as cli]))

(defn branch-create [opts]
  (println "branch create"))

(defn branch-delete [opts]
  (println "Branch delete!!!!"))

(defn complete [opts]
  (println "Complete!!!"))
(defn ask [opts]
  (println "otprs" (get opts :args)))

(defn help [opts]
  (println "Nyttig hjelpetekst her ..."))

(def subcommands [{:cmds ["branch" "create"] :fn branch-create}
                  {:cmds ["branch" "delete"] :fn branch-delete}
                  {:cmds ["complete"] :fn complete :args->opts [:prompt]}
                  {:cmds ["ask"] :fn ask}
                  {:cmds [] :fn help}])

(defn -main [& args]
  (cli/dispatch subcommands args))
