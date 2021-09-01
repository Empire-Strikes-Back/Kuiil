#!/bin/bash

repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:repl ripley.core/process \
    :main-ns get-to-the-ship.main
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m get-to-the-ship.main
}

uberjar(){

  clojure \
    -X:identicon zazu.core/process \
    :word '"get-to-the-ship"' \
    :filename '"out/idenicon/icon.png"' \
    :size 256

  clojure \
    -X:uberjar genie.core/process \
    :main-ns get-to-the-ship.main \
    :filename '"out/get-to-the-ship.jar"' \
    :paths '["src" "out/identicon"]'
}

release(){
  uberjar
}

"$@"