(defproject clojure-discord-bot "0.0.1"
  :description "A Discord bot made with Clojure"
  :url "https://github.com/matievisthekat/clojure-discord-bot"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.suskalo/discljord "1.1.1"]
                 [com.github.johnnyjayjay/slash "0.5.0-SNAPSHOT"]]
  :repl-options {:init-ns clojure-discord-bot.core}
  :main clojure-discord-bot.core)
